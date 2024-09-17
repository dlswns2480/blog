package com.junlog.user.domain.model

interface UserPort {
    fun loadById(id: Long): User?

    fun persist(user: User): User

    fun loadByEmail(email: String): User?

    fun existsByUsername(username: String): Boolean

    fun delete(user: User)
}