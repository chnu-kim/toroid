package me.chnu.toroid.application

import me.chnu.toroid.ServerException

class InvalidOAuthParameterException(
    override val message: String,
    override val statusCode: Int = 400,
) : ServerException()
