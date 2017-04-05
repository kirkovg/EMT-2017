package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.service.StockServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Aleksandar on 06.04.2017.
 */
@Service
public class StockServiceHelperImpl implements StockServiceHelper{
    private BookRepository bookRepository;

    @Autowired
    public StockServiceHelperImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBooksInStock(Long bookId, int quantity) {
        Book book = bookRepository.findOne(bookId);
        Integer previousQuantity = book.quantityInStock;
        book.quantityInStock = previousQuantity + quantity;
        bookRepository.save(book);
    }

    @Override
    public void donateBooks(Long bookId, int quantity) throws NotEnoughStockException {
        Book book = bookRepository.findOne(bookId);
        Integer previousQuantity = book.quantityInStock;
        if (previousQuantity < quantity) {
            throw new NotEnoughStockException();
        }
        book.quantityInStock = previousQuantity - quantity;
        bookRepository.save(book);
    }
}
