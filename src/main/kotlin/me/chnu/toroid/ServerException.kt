package me.chnu.toroid

abstract class ServerException(
    override val message: String? = null,
) : RuntimeException() {
    abstract val statusCode: Int
}
