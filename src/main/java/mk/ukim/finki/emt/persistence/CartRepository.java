package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}
