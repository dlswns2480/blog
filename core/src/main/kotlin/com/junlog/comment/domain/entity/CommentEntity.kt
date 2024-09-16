package com.junlog.comment.domain.entity

import com.junlog.comment.domain.model.Comment
import com.junlog.common.entity.BaseEntity
import jakarta.persistence.*

@Table(name = "COMMENT")
@Entity
class CommentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "article_id")
    val articleId: Long,

    @Column(name = "content")
    val content: String,

    @Column(name = "is_deleted")
    var deleted: Boolean = false
) : BaseEntity() {

    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(comment: Comment) = CommentEntity(
            id = comment.id,
            userId = comment.userId,
            articleId = comment.articleId,
            content = comment.content
        )
    }
}

internal fun CommentEntity.toDomain() = Comment(
    id = this.id,
    userId = this.userId,
    articleId = this.articleId,
    content = this.content
)