package com.example.springdataintro;

import com.example.springdataintro.model.entity.AgeRestriction;
import com.example.springdataintro.model.entity.Book;
import com.example.springdataintro.service.AuthorService;
import com.example.springdataintro.service.BookService;
import com.example.springdataintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader){
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDatabase();
        //printAllBooksAfter2000(2000);
        //printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //printAllAuthorsAndNumberOfTheirBooks();
        //printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Enter exercise number:");
        int exNumber = Integer.parseInt(bufferedReader.readLine());
        switch (exNumber){
            case 1 -> booksTitlesByAge();
            case 2 -> goldenBooks();
            case 3 -> booksByPrice();
            case 4 -> notReleasedBooks();
            case 5 -> booksReleasedBeforeDate();
            case 6 -> authorsSearch();
            case 7 -> booksSearch();
            case 8 -> bookTitleSearch();
            case 9 -> countBooks();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();

        }

    }

    private void reducedBook() throws IOException {
        System.out.println("Enter book title:");
        String bookTitle = bufferedReader.readLine();
        System.out.println(bookService.findBookTitleEditionTypeAgeRestrictionAndPriceByGivenTitle(bookTitle));
    }

    private void totalBookCopies() {
        authorService. findAllAuthorsAndTheirTotalCopies()
                .forEach(System.out::println);
    }

    private void countBooks() throws IOException {
        System.out.println("Enter title length:");
        int titleLength = Integer.parseInt(bufferedReader.readLine());
        System.out.println(bookService.findCountOfBooksWithTitleLengthLongerThen(titleLength));
    }

    private void bookTitleSearch() throws IOException {
        System.out.println("Enter author last name starts with str:");
        String startStr = bufferedReader.readLine();
        bookService.findAllBooksTitleWrittenByAuthorsWhichLastNameStartsWithStr(startStr)
                .forEach(System.out::println);
    }

    private void booksSearch() throws IOException {
        System.out.println("Enter containing string:");
        String containStr = bufferedReader.readLine();
        bookService.findAllBooksTitleWhereTitleContainsStr(containStr)
                .forEach(System.out::println);
    }

    private void authorsSearch() throws IOException {
        System.out.println("Enter first name ends with str:");
        String endStr = bufferedReader.readLine();
        authorService.findAuthorFullNameEndsWithStr(endStr)
                .forEach(System.out::println);

    }

    private void booksReleasedBeforeDate() throws IOException {
        System.out.println("Enter Date dd-MM-yyyy:");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        bookService.findAllBooksTitleEditionTypePriceReleasedBeforeGivenDate(localDate)
                .forEach(System.out::println);
    }

    private void notReleasedBooks() throws IOException {
        System.out.println("Enter released year:");
        int year = Integer.parseInt(bufferedReader.readLine());
        bookService.findAllTitlesWithAreNotReleasedInYear(year)
                .forEach(System.out::println);
    }

    private void booksByPrice() {
        bookService.findAllBooksTitlesWithPriceLessThan5OrGreaterThan40()
                .forEach(System.out::println);
    }

    private void goldenBooks() {
        bookService.findAllGoldBookTitlesWithCopiesLessThan5000()
                .forEach(System.out::println);
    }

    private void booksTitlesByAge() throws IOException {
        System.out.println("Enter Age Restriction:");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase(Locale.ROOT));

        bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);
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
