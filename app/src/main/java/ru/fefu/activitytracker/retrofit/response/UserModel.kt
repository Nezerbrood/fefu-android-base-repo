package ru.fefu.activitytracker.retrofit.response

data class UserModel (
    val id: Long,
    val name: String,
    val login: String,
    val gender: GenderModel,
)