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

    private BookDetailsRepository bookDetailsRepository;

    @Autowired
    public BookDetailsHelperImpl(BookDetailsRepository bookDetailsRepository) {
        this.bookDetailsRepository = bookDetailsRepository;
    }


    @Override
    public BookDetails getBookDetails(Long bookId) {
        return bookDetailsRepository.findBookDetailsByBookId(bookId);
    }
}
