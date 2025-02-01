package com.poc.authserver.core.application.ports.output

import com.poc.authserver.core.domain.model.RefreshToken
import com.poc.authserver.core.domain.valueobject.Token

interface Tokens {
    fun findByToken(token: Token): RefreshToken?
    fun delete(token: Token)
}