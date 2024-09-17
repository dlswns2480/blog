package com.junlog.user.application

import com.junlog.auth.application.JwtTokenProvider
import com.junlog.auth.domain.model.Token
import com.junlog.common.exception.ClientValidationException
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.UserFixture
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest : BehaviorSpec({
    val userPort = mockk<UserPort>()
    val tokenProvider = mockk<JwtTokenProvider>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userService = UserService(userPort, passwordEncoder, tokenProvider)

    Given("회원가입 요청 시") {
        val user = UserFixture.getById(1L)
        val command = UserFixture.getCommand()
        val invalidCommand = UserFixture.getDuplicateCommand()


        every { userPort.existsByUsername(command.username) } returns false
        every { userPort.existsByUsername(invalidCommand.username) } returns true
        every { userPort.persist(any(User::class)) } returns user
        every { passwordEncoder.encode(command.password) } returns "encryptedPassword"

        When("중복 닉네임의 유저가 있으면") {
            Then("예외가 발생한다.") {
                shouldThrow<ClientValidationException> {
                    userService.signUp(invalidCommand)
                }
            }
        }
        When("중복 닉네임의 유저가 없으면") {
            val result = userService.signUp(command)
            Then("저장된 유저가 반환된다.") {
                result.id shouldBe user.id
                result.email shouldBe user.email
            }
        }
    }

    Given("로그인 요청 시 ") {
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"
        val user = UserFixture.getById(1L)
        val command = UserFixture.getSignInCommand()
        val invalidEmailCommand = UserFixture.getInvalidEmailSignInCommand()
        val invalidPasswordCommand = UserFixture.getInvalidPasswordCommand()

        every {
            tokenProvider.createToken(user.id)
        } returns Token(accessToken, refreshToken)
        every { userPort.loadByEmail(command.email) } returns user
        every { userPort.loadByEmail(invalidEmailCommand.email) } returns null
        every { userPort.loadByEmail(invalidPasswordCommand.email) } returns user
        every { passwordEncoder.matches(command.password, user.password) } returns true
        every { passwordEncoder.matches(invalidPasswordCommand.password, user.password) } returns false

        When("존재하지 않는 이메일이면") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    userService.signIn(invalidEmailCommand)
                }
            }
        }
        When("비밀번호가 일치하지 않으면") {
            Then("예외가 발생한다.") {
                shouldThrow<ClientValidationException> {
                    userService.signIn(invalidPasswordCommand)
                }
            }
        }
        When("비밀번호가 일치하면") {
            val result = userService.signIn(command)
            Then("로그인에 성공하고 토큰을 발급받는다.") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe refreshToken
            }
        }
    }
})