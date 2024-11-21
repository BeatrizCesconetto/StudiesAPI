package br.com.beatriz.exceptions

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.RuntimeException

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidJwtAuthenticationException(exception: String?) : AuthenticationException(exception)
