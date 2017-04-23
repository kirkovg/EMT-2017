package mk.ukim.finki.emt.web;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookPicture;
import mk.ukim.finki.emt.service.QueryService;
import mk.ukim.finki.emt.service.StoreClientService;
import mk.ukim.finki.emt.service.StoreManagementService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Controller
public class BasicController {


    QueryService queryService;

    StoreClientService storeClientService;

    StoreManagementService storeManagementService;

    @Autowired
    public BasicController(QueryService queryService,
                           StoreClientService storeClientService,
                           StoreManagementService storeManagementService) {
        this.queryService = queryService;
        this.storeClientService = storeClientService;
        this.storeManagementService = storeManagementService;
    }

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("products", queryService.getPromotedBooks(1, 20));
        //model.addAttribute("theCart", storeClientService.takeCart());
        return "index";
    }

    @RequestMapping(value = {"/bookDetails/{bookId}"}, method = RequestMethod.GET)
    public String getBookDetails(
            Model model,
            @PathVariable Long bookId
    ) {
        model.addAttribute("product", storeManagementService.getBook(bookId));
        model.addAttribute("details", storeClientService.getBookDetails(bookId));
        model.addAttribute("pageFragment", "bookDetails");
        return "index";
    }

    @RequestMapping(value = {"/cart"}, method = RequestMethod.GET)
    public String cart(
            //@PathVariable Long cartId,
            Model model
    ) {
        //model.addAttribute("productsInCart", storeClientService.getAllFromCart(1L));
        model.addAttribute("pageFragment", "cart");
        return "index";
    }

    @RequestMapping(value = {"/category/{categoryId}"}, method = RequestMethod.GET)
    public String categoryProducts(
            @PathVariable Long categoryId,
            Model model
    ) {
        Page<Book> page = queryService.getBooksInCategory(
                categoryId, 0, 20
        );

        model.addAttribute("products", page);

        return "index";
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public String search(
            @RequestParam String query,
            Model model
    ) {
        List<Book> page = queryService.searchBook(query);

        model.addAttribute("products", page);

        return "index";
    }

    @RequestMapping(value = {"/book/{id}/picture"}, method = RequestMethod.GET)
    @ResponseBody
    public void index(@PathVariable Long id, HttpServletResponse response) throws IOException, SQLException {
        OutputStream out = response.getOutputStream();
        BookPicture bookPicture = queryService.getByBookId(id);

        String contentDisposition = String.format("inline;filename=\"%s\"",
                bookPicture.picture.fileName + ".png?productId=" + id);
        response.setHeader("Content-Disposition", contentDisposition);
        response.setContentType("image/png");
        response.setContentLength((int) bookPicture.picture.size);
        IOUtils.copy(bookPicture.picture.data.getBinaryStream(), out);
        out.flush();
        out.close();
    }


}
