package com.mp.mp_time.data

data class Schedule(
    var date: String = "2020-01-01", // 일정 날짜
    var title: String = "", // 일정 제목
    var content: String = "", // 상세 이저
    var dDay: Boolean = false // D-Day 설정 여부
)