package com.project.service;

import com.project.model.Book;
import com.project.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Page<Book> findAll(String authorName, Pageable pageable) {
        if (authorName != null && !authorName.isEmpty()) {
            return bookRepository.findByAuthorName(authorName, pageable);
        } else {
            return bookRepository.findAll(pageable);
        }
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}