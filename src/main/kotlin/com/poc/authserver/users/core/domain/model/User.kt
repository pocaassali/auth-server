package com.poc.authserver.users.core.domain.model

import com.poc.authserver.users.core.domain.valueobject.Mail
import com.poc.authserver.users.core.domain.valueobject.Password
import com.poc.authserver.users.core.domain.valueobject.UserRole
import java.util.UUID

data class User(
    val identifier : UUID,
    val mail : Mail,
    val password : Password,
    val role : UserRole,
)
