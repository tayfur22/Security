package com.example.securitydemo.service;

import com.example.securitydemo.entity.User;

import java.util.Optional;

public interface UserService extends BaseService<User,Long>{
    Optional<User> findByUsername(String username);

}
