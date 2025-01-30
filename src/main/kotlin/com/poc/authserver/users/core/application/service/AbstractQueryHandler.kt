package com.poc.authserver.users.core.application.service

abstract class AbstractQueryHandler<Q,R> {
    protected abstract fun execute(query: Q): R

    fun handle(query: Q): R {
        return execute(query)
    }
}