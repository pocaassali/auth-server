package com.poc.authserver.users.infrastructure.api

import com.poc.authserver.users.core.domain.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/svc/auth/users")
class UserController(
    private val userAdapter: UserAdapter
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{
        return ResponseEntity.ok(userAdapter.getAllUsers())
    }

    /*@GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{

    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{

    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{

    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{

    }*/
}