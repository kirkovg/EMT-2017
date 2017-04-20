package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.FileEmbeddable;
import mk.ukim.finki.emt.persistence.BookDetailsRepository;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.service.BookDetailsServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;


@Service
public class BookDetailsHelperImpl implements BookDetailsServiceHelper {

    private BookDetailsRepository bookDetailsRepository;

    private BookRepository bookRepository;

    @Autowired
    public BookDetailsHelperImpl(BookDetailsRepository bookDetailsRepository,
                                 BookRepository bookRepository) {
        this.bookDetailsRepository = bookDetailsRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDetails getBookDetails(Long bookId) {
        return bookDetailsRepository.findBookDetailsByBookId(bookId);
    }

    @Override
    public BookDetails addBookDetails(Long bookId, byte[] content, String contentType, String description) throws SQLException {
        BookDetails bookDetails = new BookDetails();
        bookDetails.book = bookRepository.findOne(bookId);
        FileEmbeddable bookFile = new FileEmbeddable();
        bookFile.data = new SerialBlob(content);
        bookFile.contentType = contentType;
        bookFile.fileName = bookDetails.book.name;
        bookFile.size = content.length;
        bookDetails.downloadFile = bookFile;
        bookDetails.description = description;
        return bookDetailsRepository.save(bookDetails);

    }

    @Override
    public BookDetails updateBookDetails(Long bookId, byte[] content, String contentType, String description) throws SQLException {
        BookDetails bookDetails = bookDetailsRepository.findBookDetailsByBookId(bookId);
        bookDetails.book = bookRepository.findOne(bookId);
        FileEmbeddable bookFile = new FileEmbeddable();
        bookFile.data = new SerialBlob(content);
        bookFile.contentType = contentType;
        bookFile.fileName = bookDetails.book.name;
        bookFile.size = content.length;
        bookDetails.downloadFile = bookFile;
        bookDetails.description = description;
        return bookDetailsRepository.save(bookDetails);
    }

    @Override
    public void removeBookDetails(Long bookId) {
        BookDetails bookDetails = bookDetailsRepository.findBookDetailsByBookId(bookId);
        bookDetailsRepository.delete(bookDetails);
    }

}
