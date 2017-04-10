package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.exceptions.NotEnoughItemQuantityException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CartItemRepository;
import mk.ukim.finki.emt.persistence.CartRepository;
import mk.ukim.finki.emt.service.CartServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceHelperImpl implements CartServiceHelper {

    private CartRepository cartRepository;

    private CartItemRepository cartItemRepository;

    private BookRepository bookRepository;

    @Autowired
    public CartServiceHelperImpl(CartRepository cartRepository,
                                 CartItemRepository cartItemRepository,
                                 BookRepository bookRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Cart takeCart() {
        Cart emptyCart = new Cart();
        emptyCart.expiryDate = LocalDateTime.now().plusMonths(1);
        return cartRepository.save(emptyCart);
    }

    @Override
    public CartItem addToCart(Long cartId, Long bookId, int quantity) {
        Book book = bookRepository.findOne(bookId);
        Cart cart = cartRepository.findOne(cartId);

        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.book = book;
        cartItem.quantity = quantity;

        Integer prevQuantityInStock = book.quantityInStock;
        if (prevQuantityInStock == null) {
            book.quantityInStock = quantity;
        } else {
            book.quantityInStock = prevQuantityInStock - quantity;
        }
        bookRepository.save(book);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem removeFromCart(Long cartId, Long bookId, int quantity) throws NotEnoughItemQuantityException, NotEnoughStockException {
        Book book = bookRepository.findOne(bookId);
        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(cartId, bookId);

        Integer prevItemQuantity = cartItem.quantity;
        if (prevItemQuantity < quantity) {
            throw new NotEnoughItemQuantityException();
        } else {
            cartItem.quantity = prevItemQuantity - quantity;
        }

        Integer prevQuantityInStock = book.quantityInStock;
        if (prevQuantityInStock < quantity) {
            throw new NotEnoughStockException();
        } else {
            book.quantityInStock = prevQuantityInStock + quantity;
        }
        bookRepository.save(book);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public void clearCart(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findCartItemsByCartId(cartId);
        cartItemRepository.delete(cartItems);
    }
}




