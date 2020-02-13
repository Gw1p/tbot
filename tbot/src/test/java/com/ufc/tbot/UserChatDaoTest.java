package com.ufc.tbot;

import com.ufc.tbot.DAO.UserChatDAO;
import com.ufc.tbot.model.UserChat;
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
public class UserChatDaoTest {

    @Autowired
    private UserChatDAO userChatDAO;

    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = Logger.getLogger(UserChatDaoTest.class.getName());
        UserChat userChatA = new UserChat(
                99,
                34,
                12,
                "Private",
                new Date(2019, Calendar.NOVEMBER, 13)
        );
        UserChat userChatB = new UserChat(
                42,
                41,
                40,
                "Private",
                new Date(2019, Calendar.OCTOBER, 15)
        );
        this.userChatDAO.save(userChatA);
        this.userChatDAO.save(userChatB);
        assertNotNull(userChatA.getChatType());
        assertNotNull(userChatB.getChatType());
        logger.info("Test SetUp Complete");
    }

    @AfterEach
    public void tearDown() {
        UserChat userChatA = userChatDAO.findById(99);
        UserChat userChatB = userChatDAO.findById(42);

        if (userChatA != null) {
            userChatDAO.delete(userChatA);
        }
        if (userChatB != null) {
            userChatDAO.delete(userChatB);
        }

        logger.info("Test TearDown Complete");
    }

    @Test
    public void testFetchData() {
        UserChat userChatA = this.userChatDAO.findById(99);
        assertNotNull(userChatA);

        assertEquals(99, userChatA.getId());
        assertEquals(34, userChatA.getUserId());
        assertEquals(12, userChatA.getChatId());
        assertEquals("Private", userChatA.getChatType());
        assertEquals(2019, userChatA.getDiscoveredDate().getYear());
        assertEquals(Calendar.NOVEMBER, userChatA.getDiscoveredDate().getMonth());
        assertEquals(13, userChatA.getDiscoveredDate().getDate());

        UserChat userChatB = this.userChatDAO.findById(42);
        assertNotNull(userChatB);

        assertEquals(42, userChatB.getId());
        assertEquals(41, userChatB.getUserId());
        assertEquals(40, userChatB.getChatId());
        assertEquals("Private", userChatB.getChatType());
        assertEquals(2019, userChatB.getDiscoveredDate().getYear());
        assertEquals(Calendar.OCTOBER, userChatB.getDiscoveredDate().getMonth());
        assertEquals(15, userChatB.getDiscoveredDate().getDate());

        Iterable userChats = userChatDAO.findAll();
        int count = 0;
        for(Object p : userChats){
            // Только считаем заранее добавленные сообщения
            if (((UserChat) p).getId() == 99 || ((UserChat) p).getId() == 42){
                count++;
            }
        }
        assertEquals(count, 2);
    }

    @Test
    public void testUpdateData() {
        UserChat userChatA = userChatDAO.findById(99);
        assertEquals("Private", userChatA.getChatType());

        userChatA.setChatType("Updated");
        userChatDAO.update(userChatA);

        userChatA = userChatDAO.findById(99);
        assertEquals("Updated", userChatA.getChatType());
    }

    @Test
    public void testDeleteData() {
        UserChat userChatA = userChatDAO.findById(99);
        UserChat userChatB = userChatDAO.findById(42);

        assertNotNull(userChatA);
        userChatDAO.delete(userChatA);
        userChatA = userChatDAO.findById(99);
        assertNull(userChatA);

        assertNotNull(userChatB);
        userChatDAO.deleteById(userChatB.getId());
        userChatB = userChatDAO.findById(42);
        assertNull(userChatB);
    }
}

