package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.BookDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDetailsRepository extends CrudRepository<BookDetails,Long> {
    BookDetails findBookDetailsByBookId(Long bookId);
}
