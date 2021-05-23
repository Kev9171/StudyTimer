package com.mp.mp_time.data

// 각 자료형 수정?
// 순서대로 과목명, 과목별 학습시간, 목표량, 달성도
data class Subject(var name: String, var time: String = "00:00:00", var goal: String  = "", var achieve: String = "")