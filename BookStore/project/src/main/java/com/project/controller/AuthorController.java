package com.project.controller;

import com.project.model.Author;
import com.project.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Author Management", description = "APIs for managing authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "Get all authors")
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new author")
    public Author createAuthor(@RequestBody Author author) {
        return authorService.save(author);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}