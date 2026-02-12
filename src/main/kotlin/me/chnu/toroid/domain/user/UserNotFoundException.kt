package me.chnu.toroid.domain.user

import me.chnu.toroid.ServerException

class UserNotFoundException(
    override val message: String,
    override val statusCode: Int = 404,
) : ServerException()
