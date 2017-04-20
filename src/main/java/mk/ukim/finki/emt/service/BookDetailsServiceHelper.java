package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.BookDetails;

import java.sql.SQLException;

public interface BookDetailsServiceHelper {
    BookDetails getBookDetails(Long bookId);

    BookDetails addBookDetails(Long bookId, byte[] content, String contentType, String description) throws SQLException;

    BookDetails updateBookDetails(Long bookId, byte[] content, String contentType, String description) throws SQLException;

    void removeBookDetails(Long bookId);
}
