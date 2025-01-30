package com.poc.authserver.infrastructure.api.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/svc/auth/users")
class UserController(
    private val userAdapter: UserAdapter
) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{
        return ResponseEntity.ok(userAdapter.getAllUsers())
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserView?>{
        return ResponseEntity.ok(userAdapter.getUserById(id))
    }

    @PostMapping
    fun createUser(@RequestBody request: UserCreationRequest): ResponseEntity<UserView>{
        return ResponseEntity.ok(userAdapter.create(request))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody request: UserEditionRequest): ResponseEntity<UserView?>{
        return ResponseEntity.ok(userAdapter.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<String>{
        userAdapter.delete(id)
        return ResponseEntity.ok("User with id: $id has been deleted")
    }
}