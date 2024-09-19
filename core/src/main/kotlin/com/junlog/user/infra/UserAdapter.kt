package com.junlog.user.infra

import com.junlog.user.domain.entity.UserEntity
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
        return userRepository.findByIdAndDeleted(id, false)
            ?.run { toDomain() }
    }

    override fun persist(user: User): User {
        return userRepository.save(UserEntity.of(user))
            .toDomain()
    }

    override fun loadByEmail(email: String): User? {
        return userRepository.findByEmailAndDeleted(email, false)
            ?.run { toDomain() }
    }

    override fun existsByUsername(username: String) =
        userRepository.existsByUsernameAndDeleted(username, false)

    override fun delete(user: User) {
        userRepository.findByIdOrNull(user.id)
            ?.delete()
    }

    override fun existsByEmail(email: String) =
        userRepository.existsByEmailAndDeleted(email, false)
}