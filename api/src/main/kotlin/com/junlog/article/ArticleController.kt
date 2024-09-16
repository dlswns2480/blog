package com.junlog.article

import com.junlog.article.application.ArticleService
import com.junlog.article.dto.response.ArticleInfo
import com.junlog.article.dto.request.CreateArticleRequest
import com.junlog.article.dto.request.UpdateArticleRequest
import com.junlog.article.dto.request.toDto
import com.junlog.auth.model.PrincipalUser
import com.junlog.auth.model.toDomain
import com.junlog.common.wrapper.ResponseWrapper.wrapOk
import com.junlog.common.wrapper.ResponseWrapper.wrapUnit
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/articles")
class ArticleController(
    private val articleService: ArticleService
) {
    @PostMapping
    fun createArticle(
        @AuthenticationPrincipal user: PrincipalUser,
        @Valid @RequestBody request: CreateArticleRequest
    ): ResponseEntity<ArticleInfo> {
        return articleService.create(user.toDomain(), request.toDto())
            .wrapOk()
    }

    @PutMapping("/{articleId}")
    fun updateArticle(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("articleId") articleId: Long,
        @Valid @RequestBody request: UpdateArticleRequest
    ): ResponseEntity<ArticleInfo> {
        return articleService.update(user.toDomain(), articleId, request.toDto())
            .wrapOk()
    }

    @PatchMapping("/{articleId}")
    fun deleteArticle(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("articleId") articleId: Long,
    ): ResponseEntity<Unit> {
        return articleService.delete(user.toDomain(), articleId)
            .wrapUnit()
    }
}