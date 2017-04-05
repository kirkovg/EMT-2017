package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Riste Stojanov
 */
public interface BookRepository extends CrudRepository<Book, Long>,
  JpaSpecificationExecutor<Book> {

    List<Book> findBooksByCategoryId(Long categoryId);
}

