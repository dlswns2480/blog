package com.junlog.article.domain.model

import com.junlog.article.ArticleFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ArticleTest : BehaviorSpec({
    Given("도메인 모델을 수정할 때") {
        val article = ArticleFixture.get(userId = 1L)
        val command = ArticleFixture.getUpdateCommand()
        When("제목과 내용을 입력 받으면") {
            article.edit(command.title, command.content)
            Then("모델이 수정된다.") {
                article.title shouldBe command.title
                article.content shouldBe command.content
            }
        }
    }
})