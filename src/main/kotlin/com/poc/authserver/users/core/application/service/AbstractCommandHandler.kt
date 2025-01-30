package com.poc.authserver.users.core.application.service

abstract class AbstractCommandHandler<C,R> {
    protected abstract fun execute(command: C): R

    fun handle(command: C): R {
        return execute(command)
    }
}