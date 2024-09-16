package com.junlog.comment

import com.junlog.article.ArticleFixture
import com.junlog.article.domain.model.ArticlePort
import com.junlog.auth.application.JwtTokenProvider
import com.junlog.comment.domain.model.CommentPort
import com.junlog.comment.dto.request.CreateCommentRequest
import com.junlog.comment.dto.request.UpdateCommentRequest
import com.junlog.support.TestContainerSupport
import com.junlog.support.TestUtils
import com.junlog.user.UserFixture
import com.junlog.user.domain.model.UserPort
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [TestContainerSupport::class])
class CommentControllerTest (
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val userPort: UserPort,
    @Autowired private val commentPort: CommentPort,
    @Autowired private val articlePort: ArticlePort,
    @Autowired private val tokenProvider: JwtTokenProvider
) : DescribeSpec({
    describe("Comment API") {
        val user = UserFixture.get()
        val savedUser = userPort.persist(user)

        val accessToken = "Bearer ${tokenProvider.createToken(savedUser.id).accessToken}"

        val article = ArticleFixture.get(savedUser.id)
        val savedArticle = articlePort.persist(article)

        val comment = CommentFixture.get(savedUser.id, savedArticle.id)
        val savedComment = commentPort.persist(comment)

        val createRequest = CreateCommentRequest(
            savedArticle.id,
            "comment1"
        )
        val updateRequest = UpdateCommentRequest(
            content = "updatedContent1"
        )

        context("댓글 생성 요청 시") {
            val performRequest = mockMvc.perform(
                post("/api/v1/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.toJson(createRequest))
                    .header(AUTHORIZATION, accessToken)
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("댓글이 생성된다.") {
                jsonPath("$.content").value(createRequest.content)
            }
        }
        context("수정 요청 시") {
            val performRequest = mockMvc.perform(
                put("/api/v1/comments/{commentId}", savedComment.id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.toJson(updateRequest))
                    .header(AUTHORIZATION, accessToken)
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("수정된 댓글이 반환된다.") {
                performRequest.andExpectAll(
                    jsonPath("$.content").value(updateRequest.content)
                )
            }
        }
        context("삭제 요청 시") {
            val performRequest = mockMvc.perform(
                patch("/api/v1/comments/{commentId}", savedComment.id)
                    .header(AUTHORIZATION, accessToken)
            )
            it("요청이 성공한다") {
                status().isOk
            }
            it("댓글이 삭제된다.") {
                commentPort.loadByIdAndUserId(savedComment.id, savedUser.id) shouldBe null
            }
        }
    }
})