package mk.ukim.finki.emt.service;


import mk.ukim.finki.emt.model.exceptions.CategoryInUseException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public interface StoreManagementService {


    Category createTopLevelCategory(String name);

    Category createCategory(
            String name,
            Long parentId
    );

    Category updateCategoryName(
            Long id,
            String newName
    );

    Category changeCategoryParent(
            Long id,
            Long parentId
    );

    void removeCategory(Long id)
            throws CategoryInUseException;

    Book createBook(
            String name,
            Long categoryId,
            String[] authors,
            String isbn,
            Double price
    );

    Book getBook(Long bookId);

    Book updateBook(
            Long bookId,
            String name,
            String[] authors,
            String[] existingAuthors,
            String isbn,
            Boolean promoted
    );

    Book updateBookPrice(
            Long bookId,
            Double price
    );

    Book updateBookCategory(
            Long bookId,
            Long newCategoryId
    );


    void addBooksInStock(
            Long bookId,
            int quantity
    );

    void donateBooks(
            Long bookId,
            int quantity
    ) throws NotEnoughStockException;

    void clearCart(
            Long cartId
    );

    void markInvoiceAsExpired(
            Long invoiceId
    );

    DeliveryPackage markInvoiceAsPayed(
            Long invoiceId
    );

    void preparedDelivery(
            Long deliverId
    );

    void shippedDelivery(
            Long deliveryId
    );

    void closeDeliveryWithoutConfirmation(
            Long deliveryId
    );


    BookPicture addBookPicture(
            Long bookId,
            byte[] bytes,
            String contentType
    ) throws SQLException;

    BookPicture updateBookPicture(
            Long bookId,
            byte[] bytes,
            String contentType
    ) throws SQLException;

    void removeBookPicture(Long bookId) throws SQLException;

    BookDetails addBookDetails(
            Long bookId,
            byte[] content,
            String contentType,
            String description
    ) throws SQLException;

    BookDetails updateBookDetails(
            Long bookId,
            byte[] content,
            String contentType,
            String description
    ) throws SQLException;

    void removeBookDetails(Long bookId);

    List<Author> getAllAuthors();
}
