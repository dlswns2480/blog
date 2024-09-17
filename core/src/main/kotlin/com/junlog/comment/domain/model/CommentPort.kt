package com.junlog.comment.domain.model

import org.hibernate.type.descriptor.java.LongJavaType

interface CommentPort {
    fun deleteAllByArticleId(articleId: Long)

    fun persist(comment: Comment): Comment

    fun loadByIdAndUserId(id: Long, userId: Long): Comment?

    fun delete(comment: Comment)

    fun deleteAllByUserId(userId: Long)
}