package com.junlog.user.domain.model

interface UserPort {
    fun loadById(id: Long): User?
}