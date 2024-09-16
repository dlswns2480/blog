package com.junlog.user

import com.junlog.user.domain.model.User

class UserFixture {
    companion object {
        fun get() = User(
            email = "email@usuu.com",
            password = "qwdsa",
            username = "injun"
        )

        fun getById(id: Long) = User(
            id = id,
            email = "email@urssu.com",
            password = "adqwdas",
            username = "ssu",
        )
    }
}