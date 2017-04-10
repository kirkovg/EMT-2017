package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.BookDetails;

public interface BookDetailsServiceHelper {
    BookDetails getBookDetails(Long bookId);
}
