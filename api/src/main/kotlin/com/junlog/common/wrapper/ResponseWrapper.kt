package com.junlog.common.wrapper

import org.springframework.http.ResponseEntity

object ResponseWrapper {
    fun <T> T.wrapOk(): ResponseEntity<T> = ResponseEntity.ok(this)

    fun Unit.wrapUnit(): ResponseEntity<Unit> = ResponseEntity.noContent().build()
}
