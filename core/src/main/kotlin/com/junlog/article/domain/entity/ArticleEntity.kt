package com.junlog.article.domain.entity

import com.junlog.article.domain.model.Article
import com.junlog.common.entity.BaseEntity
import jakarta.persistence.*

@Table(name = "ARTICLE")
@Entity
class ArticleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "title")
    val title: String,

    @Column(name = "content")
    val content: String,

    @Column(name = "is_deleted")
    var deleted: Boolean = false
) : BaseEntity() {

    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(article: Article) = ArticleEntity(
            userId = article.userId,
            title = article.title,
            content = article.content
        )
    }
}

internal fun ArticleEntity.toDomain() = Article(
    id = this.id,
    userId = this.userId,
    title = this.title,
    content = this.content
)