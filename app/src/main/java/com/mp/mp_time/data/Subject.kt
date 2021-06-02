package com.mp.mp_time.data

// 과목
data class Subject(
    var subName: String = "", // 과목명
    var isPage: Boolean = true, // goalInt 가 의미하는 것이 페이지 수 인지, 시간 단위인지 구별
    var goalInt: Int = 0, // 목표 페이지 수 or 시간
    var studyTime: Float = 1.0f, // 공부할 시간. 1.0 = 1시간 1.35 = 1시간 35분
    var breakTime: Float = 1.0f, // 쉴 시간
    var date: String = "2020-01-01",   // 날짜
    var achievedTime: String = "00:00:00",  // 공부한 시간
)