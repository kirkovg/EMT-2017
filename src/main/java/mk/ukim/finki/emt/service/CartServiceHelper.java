package mk.ukim.finki.emt.service;


import mk.ukim.finki.emt.model.exceptions.NotEnoughItemQuantityException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;

public interface CartServiceHelper {
    Cart takeCart();

    CartItem addToCart(Long cartId, Long bookId, int quantity);

    CartItem removeFromCart(Long cartId, Long bookId, int quantity) throws NotEnoughItemQuantityException, NotEnoughStockException;

    void clearCart(Long cartId);
}
