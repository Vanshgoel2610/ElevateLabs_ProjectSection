package com.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long pages;
    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(nullable = false)
    private Author author;
}