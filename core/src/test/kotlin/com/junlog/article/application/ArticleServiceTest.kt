package com.junlog.article.application

import com.junlog.article.ArticleFixture
import com.junlog.article.domain.model.Article
import com.junlog.article.domain.model.ArticlePort
import com.junlog.comment.domain.model.CommentPort
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.UserFixture
import com.junlog.user.domain.model.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ArticleServiceTest : BehaviorSpec({
    val articlePort = mockk<ArticlePort>()
    val userPort = mockk<UserPort>()
    val commentPort = mockk<CommentPort>()
    val articleService = ArticleService(userPort, articlePort, commentPort)



    Given("Article 관련 Command 요청이 들어올 때") {
        val user = UserFixture.getById(1L)
        val invalidUser = UserFixture.getById(0L)
        val article = ArticleFixture.get(user.id)
        val createCommand = ArticleFixture.getCommand()
        val updateCommand = ArticleFixture.getUpdateCommand()

        every { userPort.loadById(user.id) } returns user
        every { userPort.loadById(0L) } returns null
        every { articlePort.persist(any(Article::class)) } returns article
        every { articlePort.loadByIdAndUserId(0L, 0L) } returns null
        every { articlePort.loadByIdAndUserId(article.id, user.id) } returns article
        every { userPort.loadById(user.id) } returns user
        every { articlePort.delete(article) } returns Unit
        every { commentPort.deleteAllByArticleId(article.id) } returns Unit

        When("존재하지 않는 유저가 저장을 요청하면") {
            Then("예외가 발생한다"){
                shouldThrow<NotFoundCustomException> {
                    articleService.create(invalidUser, createCommand)
                }
            }
        }
        When("존재하는 유저가 저장 요청하면") {
            val savedArticle = articleService.create(user, createCommand)
            Then("저장 후 저장된 Article이 반환된다") {
                savedArticle.articleId shouldBe article.id
                savedArticle.title shouldBe article.title
                savedArticle.content shouldBe article.content
            }
        }
        When("본인의 것이 아니거나 존재하지 않는 Article에 대해 수정을 요청하면") {
            Then("예외가 발생한다") {
                shouldThrow<NotFoundCustomException> {
                    articleService.update(invalidUser, 0L, updateCommand)
                }
            }
        }
        When("본인의 Article에 대해 수정을 요청하면") {
            val updatedArticle = articleService.update(user, article.id, updateCommand)
            Then("수정된 article 결과가 반환된다.") {
                updatedArticle.articleId shouldBe article.id
                updatedArticle.title shouldBe updateCommand.title
                updatedArticle.content shouldBe updateCommand.content
            }
        }
        When("삭제 요청 시") {
            articleService.delete(user, article.id)
            Then("Article이 삭제되면서 댓글도 삭제된다.") {
                verify(exactly = 1) { articlePort.delete(article) }
                verify(exactly = 1) { commentPort.deleteAllByArticleId(article.id) }
            }
        }
    }

})