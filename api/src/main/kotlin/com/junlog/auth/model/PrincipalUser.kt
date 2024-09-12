package com.junlog.auth.model

import com.junlog.user.domain.model.Role
import com.junlog.user.domain.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class PrincipalUser(
    val id: Long,
    val email: String,
    val code: String,
    val name: String,
    val role: Role
) : UserDetails {
    companion object {
        fun of(user: User) =
            PrincipalUser(
                id = user.id,
                email = user.email,
                code = user.password,
                name = user.username,
                role = user.role
            )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.description))
    }

    override fun getPassword(): String? {
        return this.code
    }

    override fun getUsername(): String {
        return this.email
    }
}

fun PrincipalUser.toDomain() = User(
    id = this.id,
    email = this.email,
    password = this.code,
    username = this.name,
    role = this.role
)
