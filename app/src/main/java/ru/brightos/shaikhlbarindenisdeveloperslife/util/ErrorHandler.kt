package ru.brightos.shaikhlbarindenisdeveloperslife.util

enum class ErrorHandler {
    SUCCESS, LOAD_ERROR;

    companion object {
        var currentError = SUCCESS
            get() = field
    }

}