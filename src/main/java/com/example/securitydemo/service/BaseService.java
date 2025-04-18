package com.example.securitydemo.service;


import java.util.List;
import java.util.Optional;

public interface BaseService<T,ID> {
    List<T> getAll();
    Optional<T> getById(ID id);
    T save(T entity);
    void deleteById(ID id);
}
