package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.jpa.*;
import mk.ukim.finki.emt.persistence.AuthorsRepository;
import mk.ukim.finki.emt.persistence.BookPictureRepository;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CategoryRepository;
import mk.ukim.finki.emt.service.BookServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Service
public class BookHelperImpl implements BookServiceHelper {

    private CategoryRepository categoryRepository;
    private BookRepository bookRepository;
    private AuthorsRepository authorsRepository;


    @Autowired
    public BookHelperImpl(CategoryRepository categoryRepository,
                          BookRepository bookRepository,
                          AuthorsRepository authorsRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
    }

    @Override
    public List<Book> getBooksInCategory(Long categoryId) {
        return bookRepository.findBooksByCategoryId(categoryId);
    }

    @Override
    public Book createBook(String name, Long categoryId, String[] authors, String isbn, Double price) {
        Book book = new Book();
        book.name = name;
        book.isbn = isbn;
        book.price = price;
        book.category = categoryRepository.findOne(categoryId);
        for (String authorName : authors) {
            Author author = getOrCreateAuthor(authorName);
            book.authors.add(author);
        }
        return bookRepository.save(book);
    }


    @Override
    public Book updateBook(Long bookId, String name, String[] authors, String isbn) {
        Book book = bookRepository.findOne(bookId);
        book.name = name;
        ArrayList<Author> authorsList = new ArrayList<>();
        for (String author : authors) {
            Author a = new Author(author);
            authorsList.add(a);
        }
        authorsRepository.save(authorsList);
        book.authors = authorsList;
        book.isbn = isbn;

        return bookRepository.save(book);
    }

    @Override
    public Book updateBookPrice(Long bookId, Double price) {
        Book book = bookRepository.findOne(bookId);
        book.price = price;
        return bookRepository.save(book);
    }

    @Override
    public Book updateBookCategory(Long bookId, Long newCategoryId) {
        Book book = bookRepository.findOne(bookId);
        book.category.id = newCategoryId;
        return bookRepository.save(book);
    }

    private Author getOrCreateAuthor(String authorName) {
        Author author = authorsRepository.findByNameAndLastName(authorName);
        if (author == null) {
            author = new Author();
            author.nameAndLastName = authorName;
            author = authorsRepository.save(author);
        }
        return author;
    }
}
