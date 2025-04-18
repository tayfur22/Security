package com.example.securitydemo.controller;

import com.example.securitydemo.entity.User;
import com.example.securitydemo.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return ResponseEntity.ok(newUser);
    }


    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyAuthority('WRITE') or hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/findByUsername")
    public ResponseEntity<User> findByUsername(@RequestParam String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


//    //    @PreAuthorize("hasRole('ADMIN') or hasAuthority('READ')")
//    @GetMapping("/getAll")
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAll")
    public List<User> getCurrentUserOnly(@AuthenticationPrincipal org.springframework.security.core.userdetails.User currentUser) {
        String username = currentUser.getUsername();
        Optional<User> user = userService.findByUsername(username);
        return user.map(List::of).orElse(List.of()); // Əgər user tapılsa, list-də bir element, tapılmazsa boş list
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUserInfo(@AuthenticationPrincipal org.springframework.security.core.userdetails.User currentUser) {
        String username = currentUser.getUsername(); // Auth olunmuş user-in username-i
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
//        }
//
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof org.springframework.security.core.userdetails.User) {
//            org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) principal;
//            return ResponseEntity.ok(currentUser.getUsername());
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user details");
//        }
//    }



    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}
