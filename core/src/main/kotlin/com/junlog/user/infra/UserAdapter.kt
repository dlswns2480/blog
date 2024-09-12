package com.junlog.user.infra

import com.junlog.user.domain.entity.toDomain
import com.junlog.user.domain.model.User
import com.junlog.user.domain.model.UserPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserAdapter(
    private val userRepository: UserRepository
) : UserPort {
    override fun loadById(id: Long): User? {
        return userRepository.findByIdOrNull(id)?.toDomain()
    }
}