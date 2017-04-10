package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;


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
