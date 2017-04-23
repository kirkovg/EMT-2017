package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.jpa.*;
import mk.ukim.finki.emt.persistence.AuthorsRepository;
import mk.ukim.finki.emt.persistence.BookPictureRepository;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CategoryRepository;
import mk.ukim.finki.emt.service.BookServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

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
    private BookPictureRepository bookPictureRepository;

    @Autowired
    public BookHelperImpl(CategoryRepository categoryRepository,
                          BookRepository bookRepository,
                          AuthorsRepository authorsRepository,
                          BookPictureRepository bookPictureRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
        this.bookPictureRepository = bookPictureRepository;
    }

    @Override
    public List<Book> getBooksInCategory(Long categoryId) {
        return bookRepository.findBooksByCategoryId(categoryId);
    }


    @Override
    public Book getBook(Long bookId) {
        return bookRepository.findOne(bookId);
    }

    /**
     * TODO: Reimplement this with existing authors etc.
     * */
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
    public Book updateBook(
            Long bookId,
            String name,
            String[] authors,
            String[] existingAuthors,
            String isbn,
            Boolean promoted
    ) {
        Book book = bookRepository.findOne(bookId);
        book.name = name;
        book.promoted = promoted;
        ArrayList<Author> authorsList = new ArrayList<>();
        ArrayList<Author> existingAuthorsList = new ArrayList<>();
        ArrayList<Author> listToAdd = new ArrayList<>();
        if (existingAuthors != null) {
            for (String author : existingAuthors) {
                Author a = authorsRepository.findByNameAndLastName(author);
                existingAuthorsList.add(a);
            }
        }
        if (authors != null) {
            for (String author : authors) {
                Author a = new Author(author);
                authorsList.add(a);
                authorsRepository.save(a);
            }
        }

        listToAdd.addAll(existingAuthorsList);
        listToAdd.addAll(authorsList);

        book.authors = listToAdd;
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
        Category newCategory = categoryRepository.findOne(newCategoryId);
        book.category = newCategory;
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

    @Override
    public BookPicture addBookPicture(Long bookId, byte[] bytes, String contentType) throws SQLException {
        BookPicture bookPicture = new BookPicture();
        bookPicture.book = bookRepository.findOne(bookId);
        FileEmbeddable picture = new FileEmbeddable();
        picture.contentType = contentType;
        picture.data = new SerialBlob(bytes);
        picture.size = bytes.length;
        picture.fileName = bookPicture.book.name;
        bookPicture.picture = picture;
        return bookPictureRepository.save(bookPicture);
    }

    @Override
    public BookPicture updateBookPicture(Long bookId, byte[] bytes, String contentType) throws SQLException {
        BookPicture bookPicture = bookPictureRepository.findByBookId(bookId);
        // this line intentionally so you can update even if there is no picture
        bookPicture.book = bookRepository.findOne(bookId);
        FileEmbeddable picture = new FileEmbeddable();
        picture.contentType = contentType;
        picture.data = new SerialBlob(bytes);
        picture.size = bytes.length;
        picture.fileName = bookPicture.book.name;
        bookPicture.picture = picture;
        return bookPictureRepository.save(bookPicture);
    }

    @Override
    public void removeBookPicture(Long bookId) throws SQLException {
        BookPicture bookPicture = bookPictureRepository.findByBookId(bookId);
        bookPictureRepository.delete(bookPicture);
    }


}
