package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.BookPicture;
import mk.ukim.finki.emt.model.jpa.FileEmbeddable;
import mk.ukim.finki.emt.persistence.BookDetailsRepository;
import mk.ukim.finki.emt.persistence.BookPictureRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BookDetailsHelperTest {
    @Autowired
    BookDetailsServiceHelper bookDetailsServiceHelper;

    @Autowired
    BookServiceHelper bookServiceHelper;

    @Autowired
    BookDetailsRepository bookDetailsRepository;


    private static final String AUTHOR_NAME = "Gjorgji Kirkov";

    private static final byte[] bookDataBytes = new byte[]{(byte) 0xe0, 0x4f, (byte) 0xd0,
            0x20, (byte) 0xea, 0x3a, 0x69, 0x10, (byte) 0xa2, (byte) 0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte) 0x9d};

    @Test
    public void getBookDetails() throws SQLException {
        Book book = bookServiceHelper.createBook(
                "name",
                1l,
                new String[]{AUTHOR_NAME},
                "123",
                300d
        );
        BookDetails bookDetails = new BookDetails();
        bookDetails.description = "some description";
        bookDetails.book = book;

        FileEmbeddable downloadBook = new FileEmbeddable();
        downloadBook.fileName = book.name;
        downloadBook.contentType = "pdf";
        downloadBook.data = new SerialBlob(bookDataBytes);
        downloadBook.size = bookDataBytes.length;
        bookDetails.downloadFile = downloadBook;
        bookDetailsRepository.save(bookDetails);

        BookDetails newBookDetails = bookDetailsServiceHelper.getBookDetails(book.id);
        Assert.assertNotNull(newBookDetails);
        Assert.assertNotNull(newBookDetails.id);
        Assert.assertNotNull(newBookDetails.description);
        Assert.assertNotNull(newBookDetails.book);
        Assert.assertNotNull(newBookDetails.downloadFile);

        Assert.assertEquals("Book is not the same!",
                book,
                bookDetails.book
        );

        Assert.assertEquals("Description not equal!",
                bookDetails.description,
                newBookDetails.description
        );

        Assert.assertEquals("FileName is not the same!",
                downloadBook.fileName,
                newBookDetails.downloadFile.fileName
        );

        Assert.assertEquals("ContentType is not the same!",
                downloadBook.contentType,
                newBookDetails.downloadFile.contentType
        );

        Assert.assertEquals("File size is not the same!",
                downloadBook.size,
                newBookDetails.downloadFile.size
        );

        /**
         * TODO: Find better solution instead casting to SerialBlob
         */
        Assert.assertEquals("File data is not the same!",
                downloadBook.data,
                new SerialBlob(newBookDetails.downloadFile.data)
        );
    }
}
