package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.exceptions.NotEnoughStockException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Aleksandar on 06.04.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StockHelperTest {

    @Autowired
    StockServiceHelper stockServiceHelper;

    @After
    public void clearTestEntities() {

    }

    @Test
    public void addBooksInStock() {
        //Book book =
    }

    @Test
    public void donateBooks() throws NotEnoughStockException {

    }

}
