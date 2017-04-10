package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    List<CartItem> findCartItemsByCartId(Long cartId);

    CartItem findByCartIdAndBookId(Long cartId, Long bookId);
}