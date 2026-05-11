package com.xapps.note_summary.infrastructure.security.jwt

object JwtClaims {

    const val SUBJECT = "sub"
    const val EMAIL = "email"
    const val ROLE = "role"
    const val ROLES = "roles"

    const val DEFAULT_ROLE = "USER"
}
