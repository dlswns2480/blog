package com.junlog.comment.infra

import com.junlog.comment.domain.entity.CommentEntity
import com.junlog.comment.domain.entity.toDomain
import com.junlog.comment.domain.model.Comment
import com.junlog.comment.domain.model.CommentPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CommentAdapter(
    private val commentRepository: CommentRepository
) : CommentPort{
    override fun deleteAllByArticleId(articleId: Long) =
        commentRepository.deleteAllByArticleId(articleId)

    override fun persist(comment: Comment) =
        commentRepository.save(CommentEntity.of(comment))
            .toDomain()

    override fun loadByIdAndUserId(id: Long, userId: Long): Comment? {
        return commentRepository.findByIdAndUserIdAndDeleted(id, userId, false)
            ?.run { toDomain() }
    }

    override fun delete(comment: Comment) {
        commentRepository.findByIdOrNull(comment.id)
            ?.delete()
    }

    override fun deleteAllByUserId(userId: Long) {
        commentRepository.deleteAllByUserId(userId)
    }
}