package mk.ukim.finki.emt.web;

import mk.ukim.finki.emt.model.exceptions.NotEnoughItemQuantityException;
import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookPicture;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Controller
public class BasicController {


    private QueryService queryService;

    private StoreClientService storeClientService;

    private StoreManagementService storeManagementService;

    @Autowired
    public BasicController(QueryService queryService,
                           StoreClientService storeClientService,
                           StoreManagementService storeManagementService) {
        this.queryService = queryService;
        this.storeClientService = storeClientService;
        this.storeManagementService = storeManagementService;
    }


    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(
            Model model,
            HttpSession session
    ) {
        session.setMaxInactiveInterval(15 * 60);  // 15 mins
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", storeClientService.takeCart());
        }
        model.addAttribute("products", queryService.getPromotedBooks(1, 20));
        return "index";
    }

    @RequestMapping(value = {"/cart"}, method = RequestMethod.GET)
    public String cart(
            Model model,
            HttpSession session
    ) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            model.addAttribute("cartItems", storeClientService.getAllFromCart(cart.id));
            model.addAttribute("totalPrice", storeClientService.getTotalPriceFromCart(cart.id));
        }

        model.addAttribute("pageFragment", "cart");
        return "index";
    }


    @RequestMapping(value = {"/addToCart/category/{categoryId}/book/{bookId}"}, method = RequestMethod.POST)
    public String addToCart(
            Model model,
            HttpSession session,
            HttpServletResponse httpServletResponse,
            @PathVariable Long categoryId,
            @PathVariable Long bookId,
            @RequestParam Integer quantity
    ) throws NotEnoughStockException, IOException {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
           try {
               storeClientService.addToCart(cart.id, bookId, quantity);
           } catch (NotEnoughStockException ex) {
               httpServletResponse.sendRedirect("/category/"+categoryId+"?error=Not enough stock");
               return null;
           }
        }

        return "redirect:/category/{categoryId}";
    }

    @RequestMapping(value = {"/cart/removeFromCart/{bookId}"}, method = RequestMethod.POST)
    public String removeFromCart(
            Model model,
            HttpSession session,
            @PathVariable Long bookId,
            @RequestParam Integer quantityToRemove
    ) throws NotEnoughStockException, NotEnoughItemQuantityException {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            try {
                storeClientService.removeFromCart(cart.id, bookId, quantityToRemove);
            } catch (NotEnoughItemQuantityException ex) {
                model.addAttribute("exception","You cannot substract that much quantity");
            }
        }
        model.addAttribute("cartItems", storeClientService.getAllFromCart(cart.id));
        model.addAttribute("pageFragment","cart");
        return "index";
    }

    @RequestMapping(value = {"/cart/clearCart"}, method = RequestMethod.POST)
    public String clearCart(
            Model model,
            HttpSession session
    ){
        Cart cart = (Cart)session.getAttribute("cart");
        if (cart != null) {
            storeManagementService.clearCart(cart.id);
        }
        model.addAttribute("pageFragment","cart");
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


    @RequestMapping(value = {"/category/{categoryId}"}, method = RequestMethod.GET)
    public String categoryProducts(
            @PathVariable Long categoryId,
            @RequestParam(required = false) String error,
            Model model
    ) {
        Page<Book> page = queryService.getBooksInCategory(
                categoryId, 0, 20
        );

        if (error != null) {
            model.addAttribute("error",error);
        }

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
