package com.example.springdataintro.repository;

import com.example.springdataintro.model.entity.AgeRestriction;
import com.example.springdataintro.model.entity.Book;
import com.example.springdataintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction authorRepository);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate lower, LocalDate upper);

    List<Book> findAllByTitleContaining(String containStr);

    List<Book> findAllByAuthor_LastNameStartsWith(String startStr);

    @Query("SELECT count(b) FROM Book b WHERE length(b.title) > :param")
    int countOfBooksWithTitleLengthMoreThan(@Param(value = "param") int titleLength);

    @Query("SELECT b.title, b.editionType, b.ageRestriction, b.price FROM Book b WHERE b.title = :param")
    String bookTitleEditionTypeAgeRestrictionAndPriceByTitle(@Param(value = "param") String bookTitle);

}
