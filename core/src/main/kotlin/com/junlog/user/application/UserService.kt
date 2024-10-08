package com.junlog.user.application

import com.junlog.article.domain.model.ArticlePort
import com.junlog.auth.application.JwtTokenProvider
import com.junlog.auth.domain.model.Token
import com.junlog.comment.domain.model.CommentPort
import com.junlog.common.exception.ClientValidationException
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import com.junlog.user.dto.request.SignInCommand
import com.junlog.user.dto.request.UserCommand
import com.junlog.user.exception.UserErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userPort: UserPort,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider,
    private val commentPort: CommentPort,
    private val articlePort: ArticlePort
) {
    @Transactional
    fun signUp(command: UserCommand): User {
        if(userPort.existsByEmail(command.email)) {
            throw ClientValidationException(UserErrorCode.ALREADY_EXISTS_EMAIL)
        }

        if(userPort.existsByUsername(command.username)) {
            throw ClientValidationException(UserErrorCode.ALREADY_EXISTS_NAME)
        }

        val encryptedPassword = passwordEncoder.encode(command.password)

        val user = User(
            email = command.email,
            password = encryptedPassword,
            username = command.username
        )
        val savedUser = userPort.persist(user)
        return savedUser
    }

    @Transactional
    fun signIn(command: SignInCommand): Token {
        val user = userPort.loadByEmail(command.email)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)

        if(!passwordEncoder.matches(command.password, user.password)) {
            throw ClientValidationException(UserErrorCode.WRONG_PASSWORD)
        }

        return tokenProvider.createToken(user.id)
    }

    @Transactional
    fun revoke(user: User) {
        commentPort.deleteAllByUserId(user.id)
        articlePort.deleteAllByUserId(user.id)
        userPort.delete(user)
    }
}

fun UserPort.validate(userId: Long) = loadById(userId)
        ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)