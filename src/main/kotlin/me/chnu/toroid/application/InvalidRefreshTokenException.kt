package me.chnu.toroid.application

import me.chnu.toroid.ServerException

class InvalidRefreshTokenException(
    override val message: String,
    override val statusCode: Int = 401,
) : ServerException()
