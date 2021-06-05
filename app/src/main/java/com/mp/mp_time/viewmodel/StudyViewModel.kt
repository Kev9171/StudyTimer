package com.mp.mp_time.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mp.mp_time.data.Schedule
import com.mp.mp_time.data.Subject
import com.mp.mp_time.database.SubjectDBHelper
import com.mp.mp_time.database.ScheduleDBHelper

enum class FragmentRequest {
    REQUEST_SUBJECT, REQUEST_TIMER, REQUEST_MODIFY, REQUEST_STUDY
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    // user subject list
    var subjectList = mutableListOf<Subject>()
    //val testList = mutableListOf<Test>()

    var timerSubjectNow : Subject? = null   // Timer 시작하는 과목 정보
    var modifySubjectNow : Subject? = null   // 수정/삭제하려는 과목 정보
    //var newTest: MutableLiveData<Test> = MutableLiveData()

    var scheduleList = mutableListOf<Schedule>()

    // fragment translation
    val fragmentRequest: MutableLiveData<FragmentRequest> = MutableLiveData<FragmentRequest>()

    // [SQLite] DB Helper
    private val scheduleDBHelper = ScheduleDBHelper(application)
    private val subjectDBHelper = SubjectDBHelper(application)


    //타이머 상태
    var time = 0
    var maxtime = 0
    var recreate = false
    var isauto = false
    var progress = 0
    var backpressact = false
    //////////////////////////////


    init {
        // DB 에서 값 가져오기
        subjectList = subjectDBHelper.findAll() ?: mutableListOf()


        // 테스트를 위한 임시 값들
        scheduleList = scheduleDBHelper.findAll() ?: mutableListOf()


        //insertSchedule(Schedule("2020-07-01", dDay = true, title = "모프 강의"))
        //insertSchedule(Schedule("2020-07-02", dDay = true, title = "운체 강의"))
        //insertSchedule(Schedule("2020-07-01", dDay = false, title = "동아리 과제"))
    }

    // Schedule DB 관련
    // 일정 추가
    fun insertSchedule(data: Schedule){
        scheduleList.add(data)
        scheduleDBHelper.insertSchedule(data)
    }

    // 날짜로 일정 찾기
    fun getScheduleByDate(date: String): MutableList<Schedule>? {
        return scheduleDBHelper.findScheduleByDate(date)
    }

    // D-Day 설정된 일정 찾기
    fun findDdaySchedule(): MutableList<Schedule>? {
        return scheduleDBHelper.findDdaySchedule()
    }

    // 날짜와 일정 제목으로 일정 지우기
    fun deleteScheduleByDateAndTitle(date: String, title: String): Boolean {
        return scheduleDBHelper.deleteScheduleByDateAndTitle(date, title)
    }

    // 해당 날짜의 일정 모두 지우기
    fun deleteScheduleByDate(date: String): Boolean {
        return scheduleDBHelper.deleteScheduleByDate(date)
    }

    // 저장되어 있는 일정 모두 찾기기
    fun findAllSchedule(): MutableList<Schedule>? {
        return scheduleDBHelper.findAll()
    }

    // Subject DB 관련

    // 과목 등록
    fun insertSubject(data: Subject){
        subjectList.add(data)
        subjectDBHelper.insertSubject(data)
    }

    // 과목 이름으로 과목 삭제하기
    fun deleteSubjectByName(subName: String) {
        subjectList.removeIf {
            it.subName == subName
        }
        subjectDBHelper.deleteSubjectByName(subName)
    }

    // 해당 날짜의 과목 삭제하기
    fun deleteSubjectByDate(date: String) {
        subjectList.removeIf {
            it.date == date
        }
        subjectDBHelper.deleteSubjectByName(date)
    }
    
    // 과목 정보 수정
    fun updateSubject(subjectName: String, date: String, updateData: HashMap<String, Any>) {
        val newSubject = subjectList.find {
            it.date == date
        }!!

        updateData.forEach {
            val key = it.key
            if(it.key == "ispage"){
                val value = it.value as Int
                newSubject.isPage = (value==1)
            }else if(it.key == "goal"){
                val value = it.value as Int
                newSubject.goalInt = value
            }else if(it.key == "studytime"){
                val value = it.value as Float
                newSubject.studyTime = value
            }else if(it.key == "breaktime"){
                val value = it.value as Float
                newSubject.breakTime = value
            }
        }

        val idx: Int = subjectList.indexOf(newSubject)
        subjectList[idx] = newSubject

        // DB 업데이트
        subjectDBHelper.updateSubject(subjectName, date, updateData)
    }

    fun insertNewDate(date: String) {
        if(subjectDBHelper.findSubjectByDate(date) == null){
            // 해당 date 에 대해 데이터 없으니, 생성
            val results = subjectDBHelper.insertNewDate(date)
            results?.forEach {
                subjectList.add(it)
            }
        }
    }

    /*fun addTest(test: Test){
        newTest.value = test
        testList.add(test)
    }*/

    fun fragmentTranslationRequest(target: FragmentRequest){
        fragmentRequest.value = target
    }
}