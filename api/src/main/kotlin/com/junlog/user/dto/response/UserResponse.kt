package com.junlog.user.dto.response

import com.junlog.user.domain.model.User

data class UserResponse(
    val email: String,
    val username: String
)

internal fun User.toResponse() = UserResponse(
    email = this.email,
    username = this.username
)
