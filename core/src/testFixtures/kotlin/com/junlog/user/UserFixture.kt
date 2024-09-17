package com.junlog.user

import com.junlog.user.domain.model.User
import com.junlog.user.dto.request.SignInCommand
import com.junlog.user.dto.request.UserCommand

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

        fun getCommand() = UserCommand(
            email = "asd@email.com",
            password = "asdasd",
            username = "injun"
        )

        fun getDuplicateCommand() = UserCommand(
            email = "asd@email.com",
            password = "asdasd",
            username = "duplicateName"
        )

        fun getSignInCommand() = SignInCommand(
            email = "email@email.com",
            password = "password"
        )

        fun getInvalidEmailSignInCommand() = SignInCommand(
            email = "wrong@wrong.com",
            password = "rightPassword"
        )

        fun getInvalidPasswordCommand() = SignInCommand(
            email = "rightEmail@email.com",
            password = "wrongPassword"
        )
    }
}