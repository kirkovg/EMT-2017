package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.exceptions.NotEnoughItemQuantityException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CartItemRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CartHelperTest {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartServiceHelper cartServiceHelper;

    @Autowired
    BookServiceHelper bookServiceHelper;

    @Autowired
    StockServiceHelper stockServiceHelper;

    @Autowired
    BookRepository bookRepository;

    public static final String AUTHOR_NAME = "Gjorgji Kirkov";


    @After
    public void clearTestEntities() {

    }

    @Test
    public void takeCart() {
        Cart cart = cartServiceHelper.takeCart();
        Assert.assertNotNull(cart.id);
        Assert.assertNotNull(cart.expiryDate);
    }

    @Test
    public void addToCart() {
        Book book = bookServiceHelper.createBook(
                "book1",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );
        stockServiceHelper.addBooksInStock(book.id, 10);
        book = bookRepository.findOne(book.id);

        Cart cart = cartServiceHelper.takeCart();
        CartItem cartItem = cartServiceHelper.addToCart(cart.id, book.id, 4);
        book = bookRepository.findOne(book.id);

        Assert.assertNotNull(cartItem);
        Assert.assertNotNull(book);

        Assert.assertEquals("Not the same quantity in CartItem", 4, cartItem.quantity);
        Assert.assertEquals("Not the same quantity in stock", (Integer) 6, book.quantityInStock);

    }

    @Test
    public void removeFromCartWithEnoughStockAndQuantity() throws NotEnoughStockException, NotEnoughItemQuantityException {
        Cart cart = cartServiceHelper.takeCart();
        Book book = bookServiceHelper.createBook(
                "book1",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );
        stockServiceHelper.addBooksInStock(book.id, 10);
        book = bookRepository.findOne(book.id);

        CartItem cartItem = new CartItem();
        cartItem.book = book;
        cartItem.cart = cart;
        cartItem.quantity = 5;
        cartItemRepository.save(cartItem);

        cartItem = cartServiceHelper.removeFromCart(cart.id, book.id, 3);
        book = bookRepository.findOne(book.id);

        Assert.assertNotNull(cartItem);
        Assert.assertNotNull(book);

        Assert.assertEquals(2, cartItem.quantity);
        Assert.assertEquals((Integer) 13, book.quantityInStock);
    }

    @Test
    public void removeFromCartWithoutEnoughStock() throws NotEnoughStockException, NotEnoughItemQuantityException {
        Cart cart = cartServiceHelper.takeCart();
        Book book = bookServiceHelper.createBook(
                "book1",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );
        stockServiceHelper.addBooksInStock(book.id, 2);
        book = bookRepository.findOne(book.id);

        CartItem cartItem = new CartItem();
        cartItem.book = book;
        cartItem.cart = cart;
        cartItem.quantity = 5;
        cartItemRepository.save(cartItem);

        NotEnoughStockException expectedException = null;
        try {
            cartItem = cartServiceHelper.removeFromCart(cart.id, book.id, 3);
        } catch (NotEnoughStockException ex) {
            expectedException = ex;
        }

        Assert.assertNotNull("NotEnoughStockException not thrown", expectedException);
    }


    @Test
    public void removeFromCartWithoutEnoughItemQuantity() throws NotEnoughStockException, NotEnoughItemQuantityException {
        Cart cart = cartServiceHelper.takeCart();
        Book book = bookServiceHelper.createBook(
                "book1",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );
        stockServiceHelper.addBooksInStock(book.id, 10);
        book = bookRepository.findOne(book.id);

        CartItem cartItem = new CartItem();
        cartItem.book = book;
        cartItem.cart = cart;
        cartItem.quantity = 5;
        cartItemRepository.save(cartItem);

        NotEnoughItemQuantityException expectedException = null;
        try {
            cartItem = cartServiceHelper.removeFromCart(cart.id, book.id, 7);
        } catch (NotEnoughItemQuantityException ex) {
            expectedException = ex;
        }

        Assert.assertNotNull("NotEnoughItemQuantityException not thrown", expectedException);
    }


    @Test
    public void clearCart() {
        Book book = bookServiceHelper.createBook(
                "book1",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );
        Cart cart = cartServiceHelper.takeCart();

        for (int i = 0; i < 5; i++) {
            CartItem ct = new CartItem();
            ct.cart = cart;
            ct.book = book;
            ct.quantity = 4;
            cartItemRepository.save(ct);
        }

        cartServiceHelper.clearCart(cart.id);
        List<CartItem> items = cartItemRepository.findCartItemsByCartId(cart.id);
        Assert.assertEquals("There are still items in the cart!",0,items.size());
    }


    @Test
    public void getAllFromCart() {
        Cart cart = cartServiceHelper.takeCart();
        Book book1 = bookServiceHelper.createBook(
                "book1",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "123",
                300d
        );

        Book book2 = bookServiceHelper.createBook(
                "book2",
                0L,
                new String[]{AUTHOR_NAME},
                null,
                "1234",
                550d
        );

        CartItem cartItem = new CartItem();
        cartItem.book = book1;
        cartItem.cart = cart;
        cartItem.quantity = 5;
        cartItemRepository.save(cartItem);

        CartItem cartItem2 = new CartItem();
        cartItem2.book = book2;
        cartItem2.cart = cart;
        cartItem2.quantity = 8;
        cartItemRepository.save(cartItem2);

        List<CartItem> items = cartServiceHelper.getAllFromCart(cart.id);
        System.out.println(items);
        Assert.assertNotNull(items);
    }
}
