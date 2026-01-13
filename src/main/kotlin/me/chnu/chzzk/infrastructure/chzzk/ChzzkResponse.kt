package me.chnu.chzzk.infrastructure.chzzk

data class ChzzkResponse<T>(
    val code: Int,
    val message: String?,
    val content: T?,
)
