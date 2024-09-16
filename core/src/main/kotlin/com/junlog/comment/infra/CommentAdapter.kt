package com.junlog.comment.infra

import com.junlog.comment.domain.model.CommentPort
import org.springframework.stereotype.Repository

@Repository
class CommentAdapter(
    private val commentRepository: CommentRepository
) : CommentPort{
    override fun deleteAllByArticleId(articleId: Long) {
        commentRepository.deleteAllByArticleId(articleId)
    }
}