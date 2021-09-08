package com.librarymanagement.controller;

import com.librarymanagement.entity.Book;
import com.librarymanagement.exceptions.ResourceNotFoundException;
import com.librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok().body(bookService.getAllBooks());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable long id) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(bookService.getBookById(id));
    }

    @GetMapping("/books/author")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author){
        return ResponseEntity.ok().body(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/books/name")
    public ResponseEntity<List<Book>> getBooksByName(@RequestParam String name){
        return ResponseEntity.ok().body(bookService.getBooksByName(name));
    }

    @GetMapping("/books/publisher")
    public ResponseEntity<List<Book>> getBooksByPublisher(@RequestParam String publisher){
        return ResponseEntity.ok().body(bookService.getBooksByPublisher(publisher));
    }

    @GetMapping("/books/category")
    public ResponseEntity<List<Book>> getBooksByCategory(@RequestParam String category){
        return ResponseEntity.ok().body(bookService.getBooksByCategory(category));
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        return ResponseEntity.ok().body(this.bookService.addBook(book));
    }
    @PutMapping("/books/{id}")
    public  ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book book){
        book.setId(id);
        return ResponseEntity.ok().body(this.bookService.updateBook(book));
    }

    @PatchMapping("/books/addcopies/{id}")
    public  int AddCopies(@PathVariable long id){
        return this.bookService.addCopies(id);
    }

    @PatchMapping("/books/removecopies/{id}")
    public  int RemoveCopies(@PathVariable long id){
        return this.bookService.removeCopies(id);
    }

    @DeleteMapping("/books/{id}")
    public HttpStatus deleteBook(@PathVariable long id){
        this.bookService.deleteBook(id);
        return HttpStatus.OK;
    }
}
