package com.junlog.comment

import com.junlog.article.dto.request.CreateArticleRequest
import com.junlog.auth.model.PrincipalUser
import com.junlog.auth.model.toDomain
import com.junlog.comment.application.CommentService
import com.junlog.comment.dto.request.CreateCommentRequest
import com.junlog.comment.dto.request.UpdateCommentRequest
import com.junlog.comment.dto.request.toDto
import com.junlog.comment.dto.response.CommentInfo
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
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/{articleId}")
    fun createComment(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("articleId") articleId: Long,
        @Valid @RequestBody request: CreateCommentRequest
    ): ResponseEntity<CommentInfo> {
        return commentService.create(user.toDomain(), articleId, request.toDto())
            .wrapOk()
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("commentId") commentId: Long,
        @Valid @RequestBody request: UpdateCommentRequest
    ): ResponseEntity<CommentInfo> {
        return commentService.update(user.toDomain(), commentId, request.toDto())
            .wrapOk()
    }

    @PatchMapping("/{commentId}")
    fun deleteComment(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("commentId") commentId: Long,
    ): ResponseEntity<Unit> {
        return commentService.delete(user.toDomain(), commentId)
            .wrapUnit()
    }
}