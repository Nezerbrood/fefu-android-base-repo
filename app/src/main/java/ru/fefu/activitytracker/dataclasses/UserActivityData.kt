package ru.fefu.activitytracker.dataclasses

import java.time.LocalDateTime

data class UserActivityData(
    val id: Int,
    val distance: String,
    val activityType: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val user: String,
)