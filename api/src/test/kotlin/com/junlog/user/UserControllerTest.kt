package com.junlog.user

import com.junlog.support.TestContainerSupport
import com.junlog.support.TestUtils
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import com.junlog.user.dto.request.SignInRequest
import com.junlog.user.dto.request.SignUpRequest
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [TestContainerSupport::class])
class UserControllerTest(
    @Autowired private val userPort: UserPort,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val passwordEncoder: PasswordEncoder
) : DescribeSpec({
    describe("User API") {
        val password = passwordEncoder.encode("password")
        val user = User(
            email = "email@ursuu.com",
            password = password,
            username = "user1"
        )
        val savedUser = userPort.persist(user)

        val createRequest = SignUpRequest(
            email = "dlswns@gmail.com",
            password = "tomato",
            username = "injun"
        )

        val signInRequest = SignInRequest(savedUser.email, "password")

        context("회원가입 요청 시") {
            val performRequest = mockMvc.perform(
                post("/api/v1/users/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.toJson(createRequest))
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("저장된 회원 정보가 반환된다.") {
                performRequest.andExpectAll(
                    jsonPath("$.email").value(createRequest.email),
                    jsonPath("$.username").value(createRequest.username)
                )
            }
        }
        context("로그인 요청 시") {
            val performRequest = mockMvc.perform(
                post("/api/v1/users/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.toJson(signInRequest))
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("로그인 성공 후 토큰이 반환된다.") {
                performRequest.andExpectAll(
                    jsonPath("$.accessToken").exists(),
                    jsonPath("$.refreshToken").exists()
                )
            }
        }
    }
})