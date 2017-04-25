package mk.ukim.finki.emt.web;

import ch.qos.logback.classic.Logger;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.Category;
import mk.ukim.finki.emt.service.CategoryServiceHelper;
import mk.ukim.finki.emt.service.QueryService;
import mk.ukim.finki.emt.service.StoreClientService;
import mk.ukim.finki.emt.service.StoreManagementService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Riste Stojanov
 */
@Controller
public class AdminController {

    static Logger logger = (Logger) LoggerFactory.getLogger(CategoryServiceHelper.class);

    StoreManagementService storeManagementService;

    StoreClientService storeClientService;

    QueryService queryService;

    @Autowired
    public AdminController(
            StoreManagementService storeManagementService,
            QueryService queryService,
            StoreClientService storeClientService
    ) {
        this.storeManagementService = storeManagementService;
        this.queryService = queryService;
        this.storeClientService = storeClientService;
    }

    @RequestMapping(value = {"/admin/category"}, method = RequestMethod.GET)
    public String addCategory(Model model) {
        model.addAttribute("pageFragment", "addCategory");
        return "index";
    }

    @RequestMapping(value = {"/admin/manageProducts/{categoryId}"}, method = RequestMethod.GET)
    public String manageProductCategory(
            Model model,
            @PathVariable Long categoryId
    ) {
        Page<Book> page = queryService.getBooksInCategory(
                categoryId, 0, 50
        );
        model.addAttribute("pageFragment", "manageProducts");
        model.addAttribute("products", page);
        return "index";
    }

    @RequestMapping(
            value = {"/admin/manageProducts/{categoryId}/deleteProduct/{bookId}"},
            method = RequestMethod.POST
    )
    public String deleteProduct(
            @PathVariable Long categoryId,
            @PathVariable Long bookId
    ) throws SQLException {
        storeManagementService.removeBookDetails(bookId);
        storeManagementService.removeBookPicture(bookId);
        storeManagementService.removeBook(bookId);

        return "redirect:/admin/manageProducts/{categoryId}";
    }

    @RequestMapping(value = {"/admin/manageBookDetails/{bookId}"}, method = RequestMethod.GET)
    public String manageBookDetails(
            Model model,
            @PathVariable Long bookId
    ) {
        model.addAttribute("product", storeManagementService.getBook(bookId));
        model.addAttribute("details", storeClientService.getBookDetails(bookId));
        model.addAttribute("existingAuthors", storeManagementService.getAllAuthors());
        model.addAttribute("pageFragment", "manageBookDetails");
        return "index";
    }

    @RequestMapping(value = {"/admin/manageBookDetails/{bookId}"}, method = RequestMethod.POST)
    public String updateBookDetails(
            @PathVariable Long bookId,
            Model model,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam String[] existingAuthors,
            @RequestParam String authors,
            @RequestParam String isbn,
            @RequestParam Double price,
            @RequestParam String description,
            @RequestParam Boolean promoted,
            MultipartFile bookFile
    ) throws IOException, SQLException {

        Book book = storeManagementService.getBook(bookId);
        if (authors.length() == 0) {
            book = storeManagementService.updateBook(
                    bookId,
                    name,
                    null,
                    existingAuthors,
                    isbn,
                    promoted
            );
        } else if (existingAuthors.length == 0) {
            book = storeManagementService.updateBook(
                    bookId,
                    name,
                    authors.split(";"),
                    null,
                    isbn,
                    promoted
            );
        } else {
            book = storeManagementService.updateBook(
                    bookId,
                    name,
                    authors.split(";"),
                    existingAuthors,
                    isbn,
                    promoted
            );
        }

        book = storeManagementService.updateBookCategory(bookId, categoryId);

        book = storeManagementService.updateBookPrice(bookId, price);

        BookDetails details = null;
        try {
            if (bookFile.getBytes().length == 0) {
                throw new IOException();
            }
            details = storeManagementService.updateBookDetails(
                    bookId,
                    bookFile.getBytes(),
                    bookFile.getContentType(),
                    description
            );
        } catch (IOException ex) {
            details = storeClientService.getBookDetails(bookId);
        }


        model.addAttribute("product", book);
        model.addAttribute("details", details);
        return "redirect:/admin/manageBookDetails/{bookId}";
    }

    @RequestMapping(value = {"/admin/manageStock/{bookId}"}, method = RequestMethod.GET)
    public String manageStock(
            Model model,
            @PathVariable Long bookId
    ) {
        model.addAttribute("pageFragment", "manageStock");
        model.addAttribute("product", storeManagementService.getBook(bookId));
        return "index";
    }

    @RequestMapping(value = {"admin/manageStock/{bookId}"}, method = RequestMethod.POST)
    public String updateStock(
            Model model,
            @PathVariable Long bookId,
            @RequestParam Integer addedStock,
            @RequestParam Integer donatedStock
    ) throws NotEnoughStockException {
        storeManagementService.addBooksInStock(bookId, addedStock);
        storeManagementService.donateBooks(bookId, donatedStock);
        model.addAttribute("product", storeManagementService.getBook(bookId));
        model.addAttribute("pageFragment", "manageStock");
        return "redirect:/admin/manageStock/{bookId}";
    }

    @RequestMapping(value = {"/admin/managePicture/{bookId}"}, method = RequestMethod.GET)
    public String managePicture(
            Model model,
            @PathVariable Long bookId
    ) {
        model.addAttribute("product", storeManagementService.getBook(bookId));
        model.addAttribute("pageFragment", "managePicture");
        return "index";
    }

    @RequestMapping(value = {"/admin/managePicture/{bookId}"}, method = RequestMethod.POST)
    public String updatePicture(
            Model model,
            MultipartFile pictureForUpdate,
            @PathVariable Long bookId
    ) throws IOException, SQLException {
        storeManagementService.updateBookPicture(bookId, pictureForUpdate.getBytes(), pictureForUpdate.getContentType());
        model.addAttribute("pageFragment", "managePicture");
        return "redirect:/admin/managePicture/{bookId}";
    }

    @RequestMapping(value = {"/admin/book"}, method = RequestMethod.GET)
    public String addProduct(Model model) {
        model.addAttribute("existingAuthors", storeManagementService.getAllAuthors());
        model.addAttribute("pageFragment", "addBook");
        return "index";
    }

    @RequestMapping(value = {"/admin/category"}, method = RequestMethod.POST)
    public String createCategory(Model model,
                                 @RequestParam String categoryName) {
        Category category = storeManagementService.createTopLevelCategory(categoryName);
        return "redirect:/admin/category";
    }


    @RequestMapping(value = {"/admin/book"}, method = RequestMethod.POST)
    public String createProduct(
            Model model,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam(required = false) String authors,
            @RequestParam(required = false) Long[] authorIds,
            @RequestParam String isbn,
            @RequestParam Double price,
            @RequestParam String description,
            MultipartFile picture,
            MultipartFile bookFile
    ) throws IOException, SQLException {
        Book book = null;
        if (authorIds == null) {
            book = storeManagementService.createBook(
                    name,
                    categoryId,
                    authors.split(";"),
                    isbn,
                    price
            );
        } else if (authors == null || authors.length() == 0) {
            book = storeManagementService.createBook(
                    name,
                    categoryId,
                    authorIds,
                    isbn,
                    price
            );
        } else {
            book = storeManagementService.createBook(
                    name,
                    categoryId,
                    authors.split(";"),
                    authorIds,
                    isbn,
                    price
            );
        }
        storeManagementService.addBookDetails(book.id, bookFile.getBytes(), bookFile.getContentType(), description);
        storeManagementService.addBookPicture(book.id, picture.getBytes(), picture.getContentType());

        model.addAttribute("product", book);
        return "index";
    }

}
