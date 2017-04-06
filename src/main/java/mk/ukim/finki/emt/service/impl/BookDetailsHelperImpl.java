package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.BookPicture;
import mk.ukim.finki.emt.model.jpa.FileEmbeddable;
import mk.ukim.finki.emt.persistence.BookDetailsRepository;
import mk.ukim.finki.emt.persistence.BookPictureRepository;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.service.BookDetailsServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;


@Service
public class BookDetailsHelperImpl implements BookDetailsServiceHelper {

    private BookRepository bookRepository;
    private BookPictureRepository bookPictureRepository;
    private BookDetailsRepository bookDetailsRepository;


    @Autowired
    public BookDetailsHelperImpl(BookRepository bookRepository,
                                 BookPictureRepository bookPictureRepository,
                                 BookDetailsRepository bookDetailsRepository) {
        this.bookRepository = bookRepository;
        this.bookPictureRepository = bookPictureRepository;
        this.bookDetailsRepository = bookDetailsRepository;
    }



    @Override
    public BookDetails getBookDetails(Long bookId) {
        return bookDetailsRepository.findBookDetailsByBookId(bookId);
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



}
