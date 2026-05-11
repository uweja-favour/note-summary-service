package com.xapps.note_summary.infrastructure.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class DomainJwtAuthenticationToken(
    private val jwt: Jwt,
    authorities: Collection<GrantedAuthority>,
    private val principal: DomainUserPrincipal
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any = jwt

    override fun getPrincipal(): Any = principal
}
