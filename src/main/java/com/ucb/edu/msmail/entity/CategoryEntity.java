package com.ucb.edu.msmail.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "email_format", length = 1000, nullable = false)
    private String emailFormat;

    @Column(name = "orden", nullable = false)
    private Integer orden;

}
