package com.librarymanagement.repository;

import com.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book>  findByAuthor(String author);
    List<Book> findByPublisher(String publisher);
    List<Book> findByName(String name);
    List<Book> findByCategory(String category);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE books b SET b.copies = b.copies+1 WHERE b.id = :id ")
    void IncreaseCopies(@Param("id") long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE books b SET b.copies = b.copies-1 WHERE b.id = :id ")
    void DecreaseCopies(@Param("id") long id);
}
