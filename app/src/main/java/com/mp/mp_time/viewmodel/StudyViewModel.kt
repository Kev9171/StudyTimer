package com.mp.mp_time.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mp.mp_time.data.Schedule
import com.mp.mp_time.data.Subject
import com.mp.mp_time.data.Test
import com.mp.mp_time.database.SubjectDBHelper
import com.mp.mp_time.database.ScheduleDBHelper

enum class FragmentRequest {
    REQUEST_SUBJECT, REQUEST_TIMER
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    // user subject list
    var subjectList = mutableListOf<Subject>()
    val testList = mutableListOf<Test>()

    var newTest: MutableLiveData<Test> = MutableLiveData()

    // fragment translation
    val fragmentRequest: MutableLiveData<FragmentRequest> = MutableLiveData<FragmentRequest>()

    // [SQLite] DB Helper
    private val scheduleDBHelper = ScheduleDBHelper(application)
    private val subjectDBHelper = SubjectDBHelper(application)


    init {
        // DB 에서 값 가져오기
        subjectList = subjectDBHelper.findAll() ?: mutableListOf()


        // 테스트를 위한 임시 값들

        testList.add(Test("모프", 2021, 4,2))
        testList.add(Test("운체", 2021, 5,15))


        //insertSchedule(Schedule("2020-07-01", dDay = true, title = "모프 강의"))
        //insertSchedule(Schedule("2020-07-02", dDay = true, title = "운체 강의"))
        //insertSchedule(Schedule("2020-07-01", dDay = false, title = "동아리 과제"))
    }

    // Schedule DB 관련
    // 일정 추가
    fun insertSchedule(data: Schedule){
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

    fun addTest(test: Test){
        newTest.value = test
        testList.add(test)
    }

    fun fragmentTranslationRequest(target: FragmentRequest){
        fragmentRequest.value = target
    }
}