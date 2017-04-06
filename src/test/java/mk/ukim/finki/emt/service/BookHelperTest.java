package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Category;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Riste Stojanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BookHelperTest {

    public static final String AUTHOR_NAME = "Gjorgji Kirkov";

    @Autowired
    CategoryServiceHelper categoryServiceHelper;

    @Autowired
    BookServiceHelper bookServiceHelper;


    @After
    public void clearTestEntities() {

    }

    @Test
    public void getBooksInCategory() {
        Category category = categoryServiceHelper.createTopLevelCategory("kategorija");

        Book book1 = bookServiceHelper.createBook("book1", category.id, new String[]{AUTHOR_NAME}, "123", 300d);
        Book book2 = bookServiceHelper.createBook("book2", category.id, new String[]{AUTHOR_NAME}, "1234", 300d);

        List<Book> books = bookServiceHelper.getBooksInCategory(category.id);
        Assert.assertNotNull(books);
        Assert.assertEquals(2, books.size());
        Assert.assertEquals(true, books.contains(book1));
        Assert.assertEquals(true, books.contains(book2));
    }

    @Test
    public void createBook() {
        Book book = bookServiceHelper.createBook("name", 1l, new String[]{AUTHOR_NAME}, "123", 300d);
        Assert.assertNotNull(book);
        Assert.assertNotNull(book.id);
        Assert.assertEquals(1, book.authors.size());
        Assert.assertEquals(AUTHOR_NAME, book.authors.get(0).nameAndLastName);
    }


    @Test
    public void updateBook() {
        Book bookFirst = bookServiceHelper.createBook("C#", 1L, new String[]{"Riste Stojanov"}, "1234", 300d);

        Book book = bookServiceHelper.updateBook(bookFirst.id, "Java", new String[]{AUTHOR_NAME}, "123");
        Assert.assertEquals("Book name is not equal!", book.name, "Java");
        Assert.assertEquals("Authors not equal!", book.authors.get(0).nameAndLastName, AUTHOR_NAME);
        Assert.assertEquals("Isbn not equal!", book.isbn, "123");
    }

    @Test
    public void updateBookPrice() {
        Book book = bookServiceHelper.createBook("C#", 1L, new String[]{AUTHOR_NAME}, "1234", 300d);
        Book bookUpdated = bookServiceHelper.updateBookPrice(book.id, 500d);

        Assert.assertEquals(bookUpdated.price, 500d, 0);
    }

    @Test
    public void updateBookCategory() {
        Category category1 = categoryServiceHelper.createTopLevelCategory("thriller");
        Book book = bookServiceHelper.createBook("C#", category1.id, new String[]{AUTHOR_NAME}, "1234", 300d);

        Category category2 = categoryServiceHelper.createTopLevelCategory("fantasy");
        Book bookUpdated = bookServiceHelper.updateBookCategory(book.id, category2.id);

        Assert.assertEquals(bookUpdated.category.id, category2.id);
    }

}
