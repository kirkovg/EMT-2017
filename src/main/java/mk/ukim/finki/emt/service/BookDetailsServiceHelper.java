package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.BookDetails;

public interface BookDetailsServiceHelper {
    BookDetails getBookDetails(Long bookId);

    BookDetails addBookDetails(Long bookId);

    BookDetails uploadBookFile(Long bookId);

    BookDetails removeBookFile(Long bookId);

    BookDetails changeBookFile(Long bookId, byte[] content);
}
