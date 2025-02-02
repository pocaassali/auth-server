package com.poc.authserver.core.application.service

import com.poc.authserver.core.application.dto.query.GetRefreshTokenByTokenQuery
import com.poc.authserver.core.application.ports.output.Tokens
import com.poc.authserver.core.domain.model.RefreshToken

class GetRefreshTokenByToken(
    private val tokens: Tokens
) : AbstractQueryHandler<GetRefreshTokenByTokenQuery, RefreshToken?>() {
    override fun execute(query: GetRefreshTokenByTokenQuery) : RefreshToken? {
        return tokens.findByToken(query.toToken())
    }
}
