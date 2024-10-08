package com.junlog.auth.filter

import com.junlog.auth.application.JwtTokenProvider
import com.junlog.auth.exception.AuthErrorCode
import com.junlog.auth.model.PrincipalUser
import com.junlog.common.exception.ClientValidationException
import com.junlog.common.exception.NotFoundCustomException
import com.junlog.user.application.validate
import com.junlog.user.domain.model.UserPort
import com.junlog.user.exception.UserErrorCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val userPort: UserPort,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val excludePath = arrayOf(
            "/api/v1/users/signup",
            "/api/v1/users/signin",
            "/swagger-ui/index.html#/",
            "/swagger", "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs",
            "/api-docs/**",
            "/v3/api-docs/**",
        )
        val path = request.requestURI
        val shouldNotFilter =
            excludePath
                .any { it == path }

        return shouldNotFilter
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authentication = getAuthentication(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            request.setAttribute("exception", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (!StringUtils.hasText(header)) {
            throw ClientValidationException(AuthErrorCode.TOKEN_REQUIRED)
        }

        val token = header.split(" ")[1]
        val userId = tokenProvider.getUserId(token)
        val user = userPort.validate(userId)

        val principalUser = PrincipalUser.of(user)
        val authorities = listOf(SimpleGrantedAuthority(principalUser.role.description))

        val authentication =
            UsernamePasswordAuthenticationToken(
                principalUser,
                null,
                authorities,
            )
        authentication.details = WebAuthenticationDetails(request)
        return authentication
    }
}
