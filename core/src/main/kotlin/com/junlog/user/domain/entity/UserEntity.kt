package com.junlog.user.domain.entity

import com.junlog.common.entity.BaseEntity
import com.junlog.user.domain.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "USER")
@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "username")
    val username: String,

    @Column(name = "is_deleted")
    var deleted: Boolean = false
) : BaseEntity() {

    fun delete() {
        this.deleted = true
    }

    companion object {
        fun of(user: User) = UserEntity(
            email = user.email,
            password = user.password,
            username = user.username
        )
    }
}

internal fun UserEntity.toDomain() = User(
    id = this.id,
    email = this.email,
    password = this.password,
    username = this.username
)