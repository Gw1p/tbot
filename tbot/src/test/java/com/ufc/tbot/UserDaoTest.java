package com.ufc.tbot;

import com.ufc.tbot.DAO.UserDAO;
import com.ufc.tbot.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDAO userDAO;

    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = Logger.getLogger(UserDaoTest.class.getName());
        User user1 = new User(123, 
                "Anton", 
                "Bendrikov", 
                "anton", 
                new Date(1, Calendar.JANUARY, 2017));
        User user2 = new User(
                345,
                "Temirlan",
                "Tolibaev",
                "temir",
                new Date(3, Calendar.APRIL, 2018));
        this.userDAO.save(user1);
        this.userDAO.save(user2);
        assertNotNull(user1.getFirstName());
        assertNotNull(user2.getFirstName());
        logger.info("Test SetUp Complete");
    }

    @AfterEach
    public void tearDown() {
        User userA = userDAO.findByUsername("temir");
        User userB = userDAO.findByUsername("anton");

        if (userA != null) {
            userDAO.delete(userA);
        }
        if (userB != null) {
            userDAO.delete(userB);
        }

        logger.info("Test TearDown Complete");
    }

    @Test
    public void testFetchData() {
        User userA = this.userDAO.findByUsername("temir");
        assertNotNull(userA);

        assertEquals("Temirlan", userA.getFirstName());
        assertEquals("Tolibaev", userA.getLastName());
        assertEquals("temir", userA.getUsername());
        assertEquals(new Date(3, Calendar.APRIL, 2018), userA.getFirstMessage());

        User userB = this.userDAO.findById(123);
        assertNotNull(userB);

        assertEquals("Anton", userB.getFirstName());
        assertEquals("Bendrikov", userB.getLastName());
        assertEquals("anton", userB.getUsername());
        assertEquals(new Date(1, Calendar.JANUARY, 2017), userB.getFirstMessage());

        Iterable users = userDAO.findAll();
        int count = 0;
        for(Object p : users){
            // Только считаем заранее добавленных пользователей
            if (((User) p).getId() == 123 || ((User) p).getId() == 345){
                count++;
            }
        }
        assertEquals(count, 2);
    }

    @Test
    public void testUpdateData() {
        User userA = userDAO.findByUsername("temir");
        assertEquals("Tolibaev", userA.getLastName());

        userA.setLastName("Toilibaev");
        userDAO.update(userA);

        userA = userDAO.findByUsername("temir");
        assertEquals("Toilibaev", userA.getLastName());

    }

    @Test
    public void testDeleteData() {
        User userA = userDAO.findByUsername("temir");
        User userB = userDAO.findByUsername("anton");

        assertNotNull(userA);
        userDAO.delete(userA);
        userA = userDAO.findByUsername("temir");
        assertNull(userA);

        assertNotNull(userB);
        userDAO.deleteById(userB.getId());
        userB = userDAO.findByUsername("anton");
        assertNull(userB);
    }
}

