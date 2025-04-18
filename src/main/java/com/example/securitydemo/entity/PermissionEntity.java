package com.example.securitydemo.entity;

import com.example.securitydemo.Enum.Permission;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Permission permission;
}
