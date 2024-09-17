package com.junlog.user.infra

import com.junlog.user.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmailAndDeleted(email: String, deleted: Boolean): UserEntity?

    fun existsByUsernameAndDeleted(username: String, deleted: Boolean): Boolean

    fun findByIdAndDeleted(id: Long, deleted: Boolean): UserEntity?
}