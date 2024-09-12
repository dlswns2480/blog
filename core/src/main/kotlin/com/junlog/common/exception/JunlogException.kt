package com.junlog.common.exception

open class JunlogException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)

class ClientValidationException(errorCode: ErrorCode) : JunlogException(errorCode)

class InvalidRequestException(errorCode: ErrorCode) : JunlogException(errorCode)

class NotFoundCustomException(errorCode: ErrorCode) : JunlogException(errorCode)

class AlreadyExistsException(errorCode: ErrorCode) : JunlogException(errorCode)

class ExternalApiException(errorCode: ErrorCode) : JunlogException(errorCode)
