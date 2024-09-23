package hu.bme.aut.userapi.dto.exception

class UserException : Exception {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}