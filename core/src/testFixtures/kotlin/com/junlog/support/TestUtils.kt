package com.junlog.support

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class TestUtils {
    companion object {
        fun toJson(json: Any): String {
            return jacksonObjectMapper().writeValueAsString(json)
        }
    }
}