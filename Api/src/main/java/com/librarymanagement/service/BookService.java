package com.librarymanagement.service;

import com.librarymanagement.entity.Book;
import com.librarymanagement.exceptions.ResourceNotFoundException;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(long id);
    List<Book> getBooksByAuthor(String author);
    List<Book> getBooksByPublisher(String publisher);
    List<Book> getBooksByCategory(String category);
    List<Book> getBooksByName(String name);
    void deleteBook(long id);

    int addCopies(long id);

    int removeCopies(long id);
}
