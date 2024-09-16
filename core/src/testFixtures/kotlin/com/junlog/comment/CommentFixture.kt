package com.junlog.comment

import com.junlog.comment.domain.model.Comment
import com.junlog.comment.dto.request.CommentCommand

class CommentFixture {
    companion object {
        fun get(userId: Long, articleId: Long) = Comment(
            id = 1L,
            userId = userId,
            articleId = articleId,
            content = "comment1"
        )
        fun getCommand() = CommentCommand(
            content = "comment1"
        )
        fun getUpdateCommand() = CommentCommand(
            content = "updatedComment"
        )
        fun getUpdateComment() = Comment(
            id = 1L,
            userId = 1L,
            articleId = 1L,
            content = "updatedComment"
        )
    }
}