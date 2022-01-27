package ru.fefu.activitytracker.retrofit

sealed class Result<T> {
    class Success<T>(val result: T): Result<T>()
    class Error<T>(val e: Throwable): Result<T>()
}