package com.project.controller;

import com.project.model.Author;
import com.project.model.Book;
import com.project.service.AuthorService;
import com.project.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "Get all books with filtering, pagination, and sorting")
    public Page<Book> getAllBooks(
            @Parameter(description = "Filter by author's name") @RequestParam(required = false) String authorName,
            Pageable pageable) {
        return bookService.findAll(authorName, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by its ID")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public Book createBook(@RequestBody Book book) {
        // Ensure author exists or is created
        Author author = authorService.findByName(book.getAuthor().getName())
                .orElse(book.getAuthor());
        book.setAuthor(author);
        return bookService.save(book);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookService.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookDetails.getTitle());
                    existingBook.setPages(bookDetails.getPages());

                    // Handle author update correctly
                    Author author = authorService.findByName(bookDetails.getAuthor().getName())
                            .orElse(bookDetails.getAuthor());
                    existingBook.setAuthor(author);

                    Book updatedBook = bookService.save(existingBook);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by its ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}