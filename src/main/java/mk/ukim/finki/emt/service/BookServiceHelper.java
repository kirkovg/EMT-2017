package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookPicture;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public interface BookServiceHelper {

    List<Book> getBooksInCategory(Long categoryId);

    Book createBook(
            String name,
            Long categoryId,
            String[] authors,
            String isbn,
            Double price
    );

    Book createBook(
            String name,
            Long categoryId,
            Long[] existingAuthors,
            String isbn,
            Double price
    );

    Book createBook(
            String name,
            Long categoryId,
            String[] authors,
            Long[] existingAuthors, String isbn,
            Double price
    );

    Book getBook(Long bookId);

    Book updateBook(
            Long bookId,
            String name,
            String[] authors,
            String[] existingAuthors,
            String isbn,
            Boolean promoted
    );

    Book updateBookPrice(
            Long bookId,
            Double price
    );

    Book updateBookCategory(
            Long bookId,
            Long newCategoryId
    );

    void removeBook(Long bookId);

    BookPicture addBookPicture(
            Long bookId,
            byte[] bytes,
            String contentType
    ) throws SQLException;

    BookPicture updateBookPicture(
            Long bookId,
            byte[] bytes,
            String contentType
    ) throws SQLException;

    void removeBookPicture(Long bookId) throws SQLException;

}
