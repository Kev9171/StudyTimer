package com.mp.mp_time.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mp.mp_time.data.Subject

enum class FragmentRequest {
    REQUEST_SUBJECT, REQUEST_TIMER
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    // user subject list
    val subjectList = mutableListOf<Subject>()

    var newSubject: MutableLiveData<Subject> = MutableLiveData()

    // fragment translation
    val fragmentRequest: MutableLiveData<FragmentRequest> = MutableLiveData<FragmentRequest>()

    init {
        // 테스트를 위한 임시 값들
        subjectList.add(Subject("모프", "1:00:00", "12주차 강의", "30%"))
        subjectList.add(Subject("운체", "3:04:10", "11주차 강의", "50%"))
        subjectList.add(Subject("c프", "1:00:00", "12주차 강의", "30%"))
        subjectList.add(Subject("java", "1:00:00", "12주차 강의", "30%"))
        subjectList.add(Subject("동아리", "1:00:00", "1주차 강의", "80%"))
        subjectList.add(Subject("리액트", "1:00:00", "0주차 강의", "30%"))
        subjectList.add(Subject("알고리즘", "1:00:00", "강의", "10%"))
        subjectList.add(Subject("자료구조", "1:00:00", "강의", "10%"))
        subjectList.add(Subject("팀플", "1:00:00", "12주차 강의", "10%"))
    }

    fun addSubject(subject: Subject){
        newSubject.value = subject
        subjectList.add(subject)
    }

    fun fragmentTranslationRequest(target: FragmentRequest){
        fragmentRequest.value = target
    }
}