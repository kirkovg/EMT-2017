package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.persistence.BookRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StockHelperTest {

    @Autowired
    StockServiceHelper stockServiceHelper;

    @Autowired
    BookRepository bookRepository;

    private static final String AUTHOR_NAME = "Gjorgji Kirkov";


    @After
    public void clearTestEntities() {

    }

    @Test
    public void addBooksInStock() {
        Book book = new Book();
        book.quantityInStock = 3;
        bookRepository.save(book);
        stockServiceHelper.addBooksInStock(book.id, 3);
        Book book2 = bookRepository.findOne(book.id);

        Assert.assertNotNull(book2.id);
        Assert.assertEquals("Not the same book",
                book.id,
                book2.id
        );

        Assert.assertEquals("Not the same quantity!",
                6L,
                (long) book2.quantityInStock
        );
    }

    @Test
    public void donateBooksWithEnoughStock() throws NotEnoughStockException {
        Book book = new Book();
        book.quantityInStock = 3;
        bookRepository.save(book);

        stockServiceHelper.donateBooks(book.id, 3);
        Book book2 = bookRepository.findOne(book.id);

        Assert.assertNotNull(book2.id);
        Assert.assertEquals("Not the same book",
                book.id,
                book2.id
        );

        Assert.assertEquals("Not the same quantity!",
                0L,
                (long) book2.quantityInStock
        );
    }

    @Test
    public void donateBooksWithoutEnoughStock() throws NotEnoughStockException {
        Book book = new Book();
        book.quantityInStock = 3;
        bookRepository.save(book);

        NotEnoughStockException expectedException = null;
        try {
            stockServiceHelper.donateBooks(book.id, 5);
        } catch (NotEnoughStockException ex) {
            expectedException = ex;
        }
        Assert.assertNotNull("NotEnoughStockException not thrown", expectedException);
    }

}
