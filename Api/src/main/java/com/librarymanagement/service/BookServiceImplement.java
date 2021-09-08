package com.librarymanagement.service;

import com.librarymanagement.entity.Book;
import com.librarymanagement.exceptions.ResourceNotFoundException;
import com.librarymanagement.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImplement implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        Optional<Book> bookDb = this.bookRepository.findById(book.getId());
        if(bookDb.isPresent()){
            Book bookUpdate= bookDb.get();
            bookUpdate.setId(book.getId());
            bookUpdate.setAuthor(book.getAuthor());
            bookUpdate.setCategory(book.getCategory());
            bookUpdate.setName(book.getName());
            bookUpdate.setDescription(book.getDescription());
            bookUpdate.setPrice(book.getPrice());
            bookUpdate.setPublisher(book.getPublisher());
            return bookUpdate;
        }
        else{
            throw new ResourceNotFoundException("Record Not Found with id:" + book.getId());

        }
    }

    @Override
    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    @Override
    public int addCopies(long id){
        Optional<Book> bookDb = this.bookRepository.findById(id);
        if(bookDb.isPresent()) {
            this.bookRepository.IncreaseCopies(id);
            int mybookcount = this.bookRepository.findById(id).get().getCopies();
            return mybookcount;
        }
        else{
            throw new ResourceNotFoundException("Record Not Found with id: "+ id);
        }

    }

    @Override
    public int removeCopies(long id){
        Optional<Book> bookDb = this.bookRepository.findById(id);
        if(bookDb.isPresent()) {
            this.bookRepository.DecreaseCopies(id);
            int mybookcount = this.bookRepository.findById(id).get().getCopies();
            if(mybookcount == 0) {
                this.bookRepository.delete(bookDb.get());
            }
            return mybookcount;
        }
        else{
            throw new ResourceNotFoundException("Record Not Found with id: "+ id);
        }

    }
    @Override
    public Book getBookById(long id) {
        Optional<Book> bookDb = this.bookRepository.findById(id);
        if(bookDb.isPresent()) {
            return bookDb.get();
        }
        else{
            throw new ResourceNotFoundException("Record Not Found with id: "+ id);
        }
    }

    @Override
    public List<Book> getBooksByName(String name) {
        List<Book> bookDb = this.bookRepository.findByName(name);
        if(bookDb.size()!=0) {
            return bookDb;
        }
        else{
            throw new ResourceNotFoundException("No Record Found with name: "+ name);
        }
    }

    @Override
    public List<Book> getBooksByPublisher(String publisher) {
        List<Book> bookDb = this.bookRepository.findByPublisher(publisher);
        if(bookDb.size()!=0) {
            return bookDb;
        }
        else{
            throw new ResourceNotFoundException("No Record Found with publisher: "+ publisher);
        }
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> bookDb = this.bookRepository.findByAuthor(author);
        if(bookDb.size()!=0) {
            return bookDb;
        }
        else{
            throw new ResourceNotFoundException("No Record Found with author: "+ author);
        }
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        List<Book> bookDb = this.bookRepository.findByCategory(category);
        if(bookDb.size()!=0) {
            return bookDb;
        }
        else{
            throw new ResourceNotFoundException("No Record Found with category: "+ category);
        }
    }


    @Override
    public void deleteBook(long id) {
        Optional<Book> bookDb = this.bookRepository.findById(id);
        if(bookDb.isPresent()) {
            this.bookRepository.delete(bookDb.get());
        }
        else{
            throw new ResourceNotFoundException("Record Not Found with id: " + id);
        }

    }
}
