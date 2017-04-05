package mk.ukim.finki.emt.service.impl;

import com.sun.corba.se.pept.transport.*;
import com.sun.corba.se.pept.transport.ContactInfo;
import mk.ukim.finki.emt.model.jpa.*;
import mk.ukim.finki.emt.model.search.BookSearchCriteria;
import mk.ukim.finki.emt.service.BookDetailsServiceHelper;
import mk.ukim.finki.emt.service.BookServiceHelper;
import mk.ukim.finki.emt.service.CategoryServiceHelper;
import mk.ukim.finki.emt.service.StoreClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aleksandar on 06.04.2017.
 */
@Service
public class StoreClientServiceImpl implements StoreClientService {
    @Autowired
    private BookServiceHelper bookServiceHelper;

    @Autowired
    private CategoryServiceHelper categoryServiceHelper;

    @Autowired
    private BookDetailsServiceHelper bookDetailsServiceHelper;

    @Override
    public Cart takeCart() {
        return null;
    }

    @Override
    public List<Book> findBooks(BookSearchCriteria criteria) {
        return null;
    }

    @Override
    public List<Category> getTopLevelCategories() {
        return categoryServiceHelper.getTopLevelCategories();
    }

    @Override
    public List<Category> getSubCategories(Long categoryId) {
        return categoryServiceHelper.getSubCategories(categoryId);
    }

    @Override
    public List<Book> getBooksInCategory(Long categoryId) {
        return bookServiceHelper.getBooksInCategory(categoryId);
    }

    @Override
    public BookDetails getBookDetails(Long bookId) {
        return bookDetailsServiceHelper.getBookDetails(bookId);
    }

    @Override
    public CartItem addToCart(Long cartId, Long bookId, int quantity) {
        return null;
    }

    @Override
    public void removeFromCart(Long cartId, Long bookId, int quantity) {

    }

    @Override
    public Checkout startCheckout(Long cartId) {
        return null;
    }

    @Override
    public DeliveryInfo provideDeliveryInfo(Long checkoutId, String country, String city, String postalCode, String address) {
        return null;
    }

    @Override
    public ContactInfo provideContactInfo(Long checkoutId, String firstName, String lastName, String phone) {
        return null;
    }

    @Override
    public void provideDeliveryAndContactInfoFromCustomerCard(Long checkoutId, Long customerId) {

    }

    @Override
    public void provideCoupon(Long checkoutId, String coupon) {

    }

    @Override
    public Invoice providePaymentInfo(Long checkoutId, String cardNumber, String cardHolder, String cardType, String cvs, String expiryDate) {
        return null;
    }

    @Override
    public void confirmDelivery(Long invoiceId, Integer rating, String comment) {

    }
}
