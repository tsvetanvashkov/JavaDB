package com.example.springdataintro.service;

import com.example.springdataintro.model.entity.AgeRestriction;
import com.example.springdataintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year);

    List<String> printAllBooksByAuthorFirstNameAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldBookTitlesWithCopiesLessThan5000();

    List<String> findAllBooksTitlesWithPriceLessThan5OrGreaterThan40();

    List<String> findAllTitlesWithAreNotReleasedInYear(int year);

    List<String> findAllBooksTitleEditionTypePriceReleasedBeforeGivenDate(LocalDate localDate);

    List<String> findAllBooksTitleWhereTitleContainsStr(String containStr);

    List<String> findAllBooksTitleWrittenByAuthorsWhichLastNameStartsWithStr(String startStr);

    int findCountOfBooksWithTitleLengthLongerThen(int titleLength);

    String findBookTitleEditionTypeAgeRestrictionAndPriceByGivenTitle(String bookTitle);
}
