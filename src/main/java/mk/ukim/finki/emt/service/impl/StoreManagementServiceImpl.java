package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.exceptions.CategoryInUseException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.*;
import mk.ukim.finki.emt.persistence.AuthorsRepository;
import mk.ukim.finki.emt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Service
public class StoreManagementServiceImpl implements StoreManagementService {

    @Autowired
    private CategoryServiceHelper categoryServiceHelper;

    @Autowired
    private BookServiceHelper bookServiceHelper;

    @Autowired
    private StockServiceHelper stockServiceHelper;

    @Autowired
    private CartServiceHelper cartServiceHelper;

    @Autowired
    private BookDetailsServiceHelper bookDetailsServiceHelper;

    @Autowired
    private AuthorsRepository authorsRepository;

    @Override
    public Category createTopLevelCategory(String name) {
        return categoryServiceHelper.createTopLevelCategory(name);
    }

    @Override
    public Category createCategory(String name, Long parentId) {
        return categoryServiceHelper.createCategory(name, parentId);
    }

    @Override
    public Category updateCategoryName(Long id, String newName) {
        return categoryServiceHelper.updateCategoryName(id, newName);
    }

    @Override
    public Category changeCategoryParent(Long id, Long parentId) {
        return categoryServiceHelper.changeCategoryParent(id, parentId);
    }

    @Override
    public void removeCategory(Long id) throws CategoryInUseException {
        categoryServiceHelper.removeCategory(id);
    }

    @Override
    public Book createBook(String name, Long categoryId, String[] authors, String isbn, Double price) {
        return bookServiceHelper.createBook(name, categoryId, authors, isbn, price);
    }

    @Override
    public Book getBook(Long bookId) {
        return bookServiceHelper.getBook(bookId);
    }


    @Override
    public Book updateBook(Long bookId, String name, String[] authors,String[] existingAuthors, String isbn, Boolean promoted) {
        return bookServiceHelper.updateBook(bookId, name, authors,existingAuthors, isbn, promoted);
    }

    @Override
    public Book updateBookPrice(Long bookId, Double price) {
        return bookServiceHelper.updateBookPrice(bookId, price);
    }

    @Override
    public Book updateBookCategory(Long bookId, Long newCategoryId) {
        return bookServiceHelper.updateBookCategory(bookId, newCategoryId);
    }

    @Override
    public void addBooksInStock(Long bookId, int quantity) {
        stockServiceHelper.addBooksInStock(bookId, quantity);
    }

    @Override
    public void donateBooks(Long bookId, int quantity) throws NotEnoughStockException {
        stockServiceHelper.donateBooks(bookId, quantity);
    }

    @Override
    public void clearCart(Long cartId) {
        cartServiceHelper.clearCart(cartId);
    }

    @Override
    public void markInvoiceAsExpired(Long invoiceId) {

    }

    @Override
    public DeliveryPackage markInvoiceAsPayed(Long invoiceId) {
        return null;
    }

    @Override
    public void preparedDelivery(Long deliverId) {

    }

    @Override
    public void shippedDelivery(Long deliveryId) {

    }

    @Override
    public void closeDeliveryWithoutConfirmation(Long deliveryId) {

    }

    @Override
    public BookPicture addBookPicture(Long bookId, byte[] bytes, String contentType) throws SQLException {
        return bookServiceHelper.addBookPicture(bookId, bytes, contentType);
    }

    @Override
    public BookPicture updateBookPicture(Long bookId, byte[] bytes, String contentType) throws SQLException {
        return bookServiceHelper.updateBookPicture(bookId,bytes,contentType);
    }

    @Override
    public void removeBookPicture(Long bookId) throws SQLException {
        bookServiceHelper.removeBookPicture(bookId);
    }

    @Override
    public BookDetails addBookDetails(Long bookId, byte[] content, String contentType, String description) throws SQLException {
        return bookDetailsServiceHelper.addBookDetails(bookId,content,contentType,description);
    }

    @Override
    public BookDetails updateBookDetails(Long bookId, byte[] content, String contentType, String description) throws SQLException {
        return  bookDetailsServiceHelper.updateBookDetails(bookId,content,contentType,description);
    }

    @Override
    public void removeBookDetails(Long bookId) {
        bookDetailsServiceHelper.removeBookDetails(bookId);
    }

    @Override
    public List<Author> getAllAuthors() {
        return (List)authorsRepository.findAll();
    }
}
