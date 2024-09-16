package com.junlog.comment.application

import com.junlog.article.ArticleFixture
import com.junlog.article.domain.model.ArticlePort
import com.junlog.comment.CommentFixture
import com.junlog.comment.domain.model.Comment
import com.junlog.comment.domain.model.CommentPort
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.UserFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class CommentServiceTest : BehaviorSpec({
    val commentPort = mockk<CommentPort>()
    val articlePort = mockk<ArticlePort>()
    val commentService = CommentService(commentPort, articlePort)

    Given("Comment 관련 Command 요청이 들어올 때") {
        val user = UserFixture.getById(1L)
        val article = ArticleFixture.get(user.id)
        val comment = CommentFixture.get(user.id, article.id)

        val createCommand = CommentFixture.getCommand()

        val invalidArticleId = 0L

        every { articlePort.loadByIdAndUserId(article.id, user.id) } returns article
        every { commentPort.persist(any(Comment::class)) } returns comment
        every { commentPort.loadByIdAndUserId(comment.id, article.id) } returns comment
        every { articlePort.loadByIdAndUserId(invalidArticleId, user.id) } returns null

        When("생성 요청 시") {
            val result = commentService.create(user, article.id, createCommand)
            Then("저장된 댓글이 반환된다.") {
                result.content shouldBe createCommand.content
            }
        }
        When("존재하지 않는 게시물에 댓글을 생성하려 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    commentService.create(user, invalidArticleId, createCommand)
                }
            }
        }
    }
})