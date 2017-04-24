package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookPicture;
import mk.ukim.finki.emt.model.jpa.Category;
import mk.ukim.finki.emt.model.jpa.FileEmbeddable;
import mk.ukim.finki.emt.persistence.BookPictureRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BookHelperTest {

    @Autowired
    CategoryServiceHelper categoryServiceHelper;

    @Autowired
    BookServiceHelper bookServiceHelper;

    @Autowired
    BookPictureRepository bookPictureRepository;

    public static final String AUTHOR_NAME = "Gjorgji Kirkov";

    private static final byte[] pictureDataBytes = new byte[]{(byte) 0x80, 0x53, 0x1c,
            (byte) 0x87, (byte) 0xa0, 0x42, 0x69, 0x10, (byte) 0xa2, (byte) 0xea, 0x08,
            0x00, 0x2b, 0x30, 0x30, (byte) 0x9d};

    private static final byte[] updatedPictureDataBytes = new byte[]{(byte) 0xe0, 0x4f, (byte) 0xd0,
            0x20, (byte) 0xea, 0x3a, 0x69, 0x10, (byte) 0xa2, (byte) 0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte) 0x9d};

    @After
    public void clearTestEntities() {

    }

    @Test
    public void getBooksInCategory() {
        Category category = categoryServiceHelper.createTopLevelCategory("kategorija");

        Book book1 = bookServiceHelper.createBook("book1", category.id, new String[]{AUTHOR_NAME},null, "123", 300d);
        Book book2 = bookServiceHelper.createBook("book2", category.id, new String[]{AUTHOR_NAME},null, "1234", 300d);

        List<Book> books = bookServiceHelper.getBooksInCategory(category.id);
        Assert.assertNotNull(books);
        Assert.assertEquals(2, books.size());
        Assert.assertEquals(true, books.contains(book1));
        Assert.assertEquals(true, books.contains(book2));
    }

    @Test
    public void createBook() {
        Book book = bookServiceHelper.createBook("name", 1l, new String[]{AUTHOR_NAME},null, "123", 300d);
        Assert.assertNotNull(book);
        Assert.assertNotNull(book.id);
        Assert.assertEquals(1, book.authors.size());
        Assert.assertEquals(AUTHOR_NAME, book.authors.get(0).nameAndLastName);
    }


    /**
     * TODO: Reimplement this test
     */
    @Test
    public void updateBook() {
        Book bookFirst = bookServiceHelper.createBook("C#", 1L, new String[]{"Riste Stojanov"},null, "1234", 300d);

        Book book = bookServiceHelper.updateBook(bookFirst.id, "Java", new String[]{AUTHOR_NAME},null, "123",false);
        Assert.assertEquals("Book name is not equal!", book.name, "Java");
        Assert.assertEquals("Authors not equal!", book.authors.get(0).nameAndLastName, AUTHOR_NAME);
        Assert.assertEquals("Isbn not equal!", book.isbn, "123");
    }

    @Test
    public void updateBookPrice() {
        Book book = bookServiceHelper.createBook("C#", 1L, new String[]{AUTHOR_NAME},null, "1234", 300d);
        Book bookUpdated = bookServiceHelper.updateBookPrice(book.id, 500d);

        Assert.assertEquals(bookUpdated.price, 500d, 0);
    }

    @Test
    public void updateBookCategory() {
        Category category1 = categoryServiceHelper.createTopLevelCategory("thriller");
        Book book = bookServiceHelper.createBook("C#", category1.id, new String[]{AUTHOR_NAME},null, "1234", 300d);

        Category category2 = categoryServiceHelper.createTopLevelCategory("fantasy");
        Book bookUpdated = bookServiceHelper.updateBookCategory(book.id, category2.id);

        Assert.assertEquals(bookUpdated.category.id, category2.id);
    }


    @Test
    public void addBookPicture() throws SQLException {
        Book book = bookServiceHelper.createBook(
                "name",
                1l,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );

        BookPicture bookPicture = new BookPicture();
        bookPicture.book = book;
        FileEmbeddable pic = new FileEmbeddable();
        pic.fileName = book.name;
        pic.contentType = "png";
        pic.data = new SerialBlob(pictureDataBytes);
        pic.size = pictureDataBytes.length;
        bookPicture.picture = pic;

        bookPictureRepository.save(bookPicture);

        BookPicture foundBookPicture = bookPictureRepository.findByBookId(book.id);

        Assert.assertNotNull(foundBookPicture.book);
        Assert.assertNotNull(foundBookPicture.picture);

        Assert.assertEquals("Picture name is not the same!",
                pic.fileName,
                foundBookPicture.picture.fileName
        );

        Assert.assertEquals("ContentType is not the same!",
                pic.contentType,
                foundBookPicture.picture.contentType
        );

        Assert.assertEquals("Picture size is not the same!",
                pic.size,
                foundBookPicture.picture.size
        );

        /**
         * TODO: Find better solution instead casting to SerialBlob
         */
        Assert.assertEquals("Picture data is not the same!",
                pic.data,
                new SerialBlob(foundBookPicture.picture.data)
        );
    }

    @Test
    public void updateBookPicture() throws SQLException {
        Book book = bookServiceHelper.createBook(
                "name",
                1l,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );

        bookServiceHelper.addBookPicture(
                book.id,
                pictureDataBytes,
                "png"
        );


        BookPicture updatedPicture = bookServiceHelper.updateBookPicture(
                book.id,
                updatedPictureDataBytes,
                "bnp"
        );

        Assert.assertNotNull(updatedPicture);
        // casting both to SerialBlob because of incompatible types
        // TODO: find better solution for this
        Assert.assertEquals(new SerialBlob(updatedPictureDataBytes),new SerialBlob(updatedPicture.picture.data));
        Assert.assertEquals("bnp",updatedPicture.picture.contentType);
    }

    @Test
    public void removeBookPicture() throws SQLException {
        Book book = bookServiceHelper.createBook(
                "name",
                1l,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );

        bookServiceHelper.addBookPicture(
                book.id,
                pictureDataBytes,
                "png"
        );

        bookServiceHelper.removeBookPicture(book.id);
        BookPicture deleted = bookPictureRepository.findByBookId(book.id);
        Assert.assertNull("Picture not deleted",deleted);
    }
}
