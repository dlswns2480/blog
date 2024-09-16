package com.junlog.article

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.junlog.article.domain.model.ArticlePort
import com.junlog.article.dto.request.CreateArticleRequest
import com.junlog.article.dto.request.UpdateArticleRequest
import com.junlog.auth.application.JwtTokenProvider
import com.junlog.support.TestContainerSupport
import com.junlog.user.UserFixture
import com.junlog.user.domain.model.UserPort
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [TestContainerSupport::class])
class ArticleControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val tokenProvider: JwtTokenProvider,
    @Autowired private val userPort: UserPort,
    @Autowired private val articlePort: ArticlePort
) : DescribeSpec({
    val logger = KotlinLogging.logger { }
    describe("Article API") {
        val user = UserFixture.get()
        val savedUser = userPort.persist(user)

        val accessToken = "Bearer ${tokenProvider.createToken(savedUser.id).accessToken}"
        logger.info { "access token : $accessToken" }

        val article = ArticleFixture.get(savedUser.id)
        val savedArticle = articlePort.persist(article)

        val createRequest = CreateArticleRequest("title2", "content2")

        context("생성 요청 시") {
            val performRequest = mockMvc.perform(post("/api/v1/articles")
                    .header(AUTHORIZATION, accessToken)
                    .contentType(APPLICATION_JSON)
                    .content(toJson(createRequest))
            )
            it("요청이 성공한다.") {
                performRequest.andExpect(
                    status().isOk
                )
            }
            it("저장된 Article이 반환된다.") {
                performRequest.andExpectAll(
                    jsonPath("$.email").value(user.email),
                    jsonPath("$.title").value(createRequest.title),
                    jsonPath("$.content").value(createRequest.content)
                )
            }
        }

        val updateRequest = UpdateArticleRequest("title3", "title3")
        context("수정 요청 시") {
            val performRequest = mockMvc.perform(
                put("/api/v1/articles/{articleId}", savedArticle.id)
                    .contentType(APPLICATION_JSON)
                    .content(toJson(updateRequest))
                    .header(AUTHORIZATION, accessToken)
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("수정된 결과가 반환된다.") {
                performRequest.andExpectAll(
                    jsonPath("$.articleId").value(savedArticle.id),
                    jsonPath("$.title").value(updateRequest.title),
                    jsonPath("$.content").value(updateRequest.content)
                )
            }

        }
        context("삭제 요청 시") {
            mockMvc.perform(
                patch("/api/v1/articles/{articleId}", savedArticle.id)
                    .header(AUTHORIZATION, accessToken)
            )
            it("요청이 성공한다.") {
                status().isOk
            }
            it("해당 id의 article이 삭제된다.") {
                articlePort.loadById(savedArticle.id) shouldBe null
            }
        }
    }
})

private fun toJson(json: Any): String {
    return jacksonObjectMapper().writeValueAsString(json)
}