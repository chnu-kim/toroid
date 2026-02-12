package me.chnu.toroid.domain.chzzk

import me.chnu.toroid.ServerException

class ChzzkApiException(
    override val message: String,
    override val statusCode: Int,
) : ServerException()
