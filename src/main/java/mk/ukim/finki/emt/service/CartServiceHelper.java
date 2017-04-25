package mk.ukim.finki.emt.service;


import mk.ukim.finki.emt.model.exceptions.NotEnoughItemQuantityException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;

import java.util.List;

public interface CartServiceHelper {
    Cart takeCart();

    CartItem addToCart(Long cartId, Long bookId, int quantity) throws NotEnoughStockException;

    void removeFromCart(Long cartId, Long bookId, int quantity) throws NotEnoughItemQuantityException, NotEnoughStockException;

    List<CartItem> getAllFromCart(Long cartId);

    void clearCart(Long cartId);

    Double getTotalPriceFromCart(Long cartId);
}
