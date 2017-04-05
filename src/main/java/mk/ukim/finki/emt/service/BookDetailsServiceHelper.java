package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.BookPicture;

import java.sql.SQLException;

/**
 * Created by Aleksandar on 05.04.2017.
 */
public interface BookDetailsServiceHelper {

    BookDetails getBookDetails(Long bookId);

    BookPicture addBookPicture(
            Long bookId,
            byte[] bytes,
            String contentType) throws SQLException;
}
