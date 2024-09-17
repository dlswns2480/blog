package com.junlog.user

import com.junlog.article.ArticleFixture
import com.junlog.article.domain.model.ArticlePort
import com.junlog.auth.application.JwtTokenProvider
import com.junlog.comment.CommentFixture
import com.junlog.comment.domain.model.CommentPort
import com.junlog.support.TestContainerSupport
import com.junlog.support.TestUtils
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import com.junlog.user.dto.request.SignInRequest
import com.junlog.user.dto.request.SignUpRequest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.*
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
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
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val tokenProvider: JwtTokenProvider,
    @Autowired private val articlePort: ArticlePort,
    @Autowired private val commentPort: CommentPort,
) : DescribeSpec({
    describe("User API") {
        val password = passwordEncoder.encode("password")
        val user = User(
            email = "email@ursuu.com",
            password = password,
            username = "user1"
        )
        val savedUser = userPort.persist(user)
        val accessToken = "Bearer ${tokenProvider.createToken(savedUser.id).accessToken}"

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
        val article = articlePort.persist(ArticleFixture.get(savedUser.id))
        val comment = commentPort.persist(CommentFixture.get(savedUser.id, article.id))
        context("탈퇴 요청 시") {
            val performRequest = mockMvc.perform(
                patch("/api/v1/users/revoke")
                    .header(AUTHORIZATION, accessToken)
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("유저와 유저의 게시물, 댓글이 모두 삭제된다.") {
                userPort.loadById(savedUser.id) shouldBe null
                commentPort.loadByIdAndUserId(comment.id, savedUser.id) shouldBe null
                articlePort.loadById(article.id) shouldBe null
            }
        }
    }
})