package com.example.springdataintro.repository;

import com.example.springdataintro.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY size(a.books)DESC")
    List<Author> findAllBooksSizeDESC();

    List<Author> findAllByFirstNameEndsWith(String endStr);
}
