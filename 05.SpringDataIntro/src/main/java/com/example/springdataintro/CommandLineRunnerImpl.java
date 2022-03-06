package com.example.springdataintro;

import com.example.springdataintro.model.entity.Book;
import com.example.springdataintro.service.AuthorService;
import com.example.springdataintro.service.BookService;
import com.example.springdataintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService){
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDatabase();
        //printAllBooksAfter2000(2000);
        //printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //printAllAuthorsAndNumberOfTheirBooks();
        //printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");

    }

    private void printAllBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService.printAllBooksByAuthorFirstNameAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService.getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService.findAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfter2000(int year) {
        bookService.findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedDatabase() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
