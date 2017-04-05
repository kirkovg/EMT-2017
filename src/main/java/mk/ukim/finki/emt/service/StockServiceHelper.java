package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;

/**
 * Created by Aleksandar on 06.04.2017.
 */
public interface StockServiceHelper {

    void addBooksInStock(
            Long bookId,
            int quantity
    );

    void donateBooks(
            Long bookId,
            int quantity
    ) throws NotEnoughStockException;
}
