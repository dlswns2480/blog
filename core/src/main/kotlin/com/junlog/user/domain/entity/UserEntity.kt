package com.junlog.user.domain.entity

import com.junlog.common.entity.BaseEntity
import com.junlog.user.domain.model.Role
import com.junlog.user.domain.model.User
import jakarta.persistence.*

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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role,

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
            username = user.username,
            role = user.role
        )
    }
}

internal fun UserEntity.toDomain() = User(
    id = this.id,
    email = this.email,
    password = this.password,
    username = this.username,
    role = this.role
)