package com.example.securitydemo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String identifier;
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
