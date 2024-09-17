package com.junlog.user

import com.junlog.auth.domain.model.Token
import com.junlog.auth.model.PrincipalUser
import com.junlog.auth.model.toDomain
import com.junlog.common.wrapper.ResponseWrapper.wrapOk
import com.junlog.common.wrapper.ResponseWrapper.wrapUnit
import com.junlog.user.application.UserService
import com.junlog.user.dto.request.SignInRequest
import com.junlog.user.dto.request.SignUpRequest
import com.junlog.user.dto.request.toDto
import com.junlog.user.dto.response.UserResponse
import com.junlog.user.dto.response.toResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody request: SignUpRequest
    ): ResponseEntity<UserResponse> {
        return userService.signUp(request.toDto())
            .toResponse()
            .wrapOk()
    }

    @PostMapping("/signin")
    fun signIn(
        @Valid @RequestBody request: SignInRequest
    ): ResponseEntity<Token> {
        return userService.signIn(request.toDto())
            .wrapOk()
    }

    @PatchMapping("/revoke")
    fun withDraw(
        @AuthenticationPrincipal user: PrincipalUser
    ): ResponseEntity<Unit> {
        return userService.revoke(user.toDomain())
            .wrapUnit()
    }

}