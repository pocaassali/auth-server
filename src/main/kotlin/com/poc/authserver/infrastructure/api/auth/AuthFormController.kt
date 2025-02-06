package com.poc.authserver.infrastructure.api.auth

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

const val LOGIN_TEMPLATE = "login"
const val SIGNUP_TEMPLATE = "signup"

@Controller
class AuthFormController {

    @GetMapping("/login")
    fun showLoginPage(@RequestParam(required = false) params: String?,model: Model): String {
        model.addAttribute("params", params)
        return LOGIN_TEMPLATE
    }

    @GetMapping("/signup")
    fun showSignupPage(): String {
        return SIGNUP_TEMPLATE
    }

}