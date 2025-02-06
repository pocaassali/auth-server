package com.poc.authserver.infrastructure.api.user

import com.poc.authserver.infrastructure.api.auth.LoginRequest
import com.poc.authserver.utils.AuthServerSecurityGuard
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userAdapter: UserAdapter) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserView>>{
        return ResponseEntity.ok(userAdapter.getAllUsers())
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserView?>{
        return ResponseEntity.ok(userAdapter.getUserById(id))
    }

    //TODO : move this controller as register AuthController
    @PostMapping
    fun createUser(/*@RequestBody request: UserCreationRequest*/@ModelAttribute request: UserCreationRequest): ResponseEntity<UserView>{
        println(request)
        //TODO : Probably should return template register-confirmation
        return ResponseEntity.ok(userAdapter.create(request))
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authServerSecurityGuard.isSelfOrAdmin(#id)")
    fun updateUser(@PathVariable id: String, @RequestBody request: UserEditionRequest): ResponseEntity<UserView?>{
        return ResponseEntity.ok(userAdapter.update(id, request))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authServerSecurityGuard.hasAdminRole()")
    fun deleteUser(@PathVariable id: String): ResponseEntity<String>{
        userAdapter.delete(id)
        return ResponseEntity.ok("User with id: $id has been deleted")
    }
}