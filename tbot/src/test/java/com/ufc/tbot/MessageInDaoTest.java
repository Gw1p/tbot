package com.ufc.tbot;

import com.ufc.tbot.DAO.MessageInDAO;
import com.ufc.tbot.model.MessageIn;
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
public class MessageInDaoTest {

    @Autowired
    private MessageInDAO messageInDAO;

    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = Logger.getLogger(MessageInDaoTest.class.getName());
        MessageIn messageInA = new MessageIn(
                12,
                "Test message",
                new Date(2018, Calendar.FEBRUARY, 23),
                123,
                345
        );
        MessageIn messageInB = new MessageIn(
                44,
                "Another msg",
                new Date(2018, Calendar.FEBRUARY, 24),
                456,
                123
        );
        this.messageInDAO.save(messageInA);
        this.messageInDAO.save(messageInB);
        assertNotNull(messageInA.getMessage());
        assertNotNull(messageInB.getMessage());
        logger.info("Test SetUp Complete");
    }

    @AfterEach
    public void tearDown() {
        MessageIn messageInA = messageInDAO.findById(12);
        MessageIn messageInB = messageInDAO.findById(44);

        if (messageInA != null) {
            messageInDAO.delete(messageInA);
        }
        if (messageInB != null) {
            messageInDAO.delete(messageInB);
        }

        logger.info("Test TearDown Complete");
    }

    @Test
    public void testFetchData() {
        MessageIn messageInA = this.messageInDAO.findById(12);
        assertNotNull(messageInA);

        assertEquals(12, messageInA.getId());
        assertEquals("Test message", messageInA.getMessage());
        assertEquals(2018, messageInA.getMessageDate().getYear());
        assertEquals(Calendar.FEBRUARY, messageInA.getMessageDate().getMonth());
        assertEquals(23, messageInA.getMessageDate().getDate());
        assertEquals(123, messageInA.getChatId());
        assertEquals(345, messageInA.getUserId());

        MessageIn messageInB = this.messageInDAO.findById(44);
        assertNotNull(messageInB);

        assertEquals(44, messageInB.getId());
        assertEquals("Another msg", messageInB.getMessage());
        assertEquals(2018, messageInB.getMessageDate().getYear());
        assertEquals(Calendar.FEBRUARY, messageInB.getMessageDate().getMonth());
        assertEquals(24, messageInB.getMessageDate().getDate());
        assertEquals(456, messageInB.getChatId());
        assertEquals(123, messageInB.getUserId());

        Iterable messagesIn = messageInDAO.findAll();
        int count = 0;
        for(Object p : messagesIn){
            // Только считаем заранее добавленные сообщения
            if (((MessageIn) p).getId() == 12 || ((MessageIn) p).getId() == 44){
                count++;
            }
        }
        assertEquals(count, 2);
    }

    @Test
    public void testUpdateData() {
        MessageIn messageInA = messageInDAO.findById(44);
        assertEquals("Another msg", messageInA.getMessage());

        messageInA.setMessage("Another message");
        messageInDAO.update(messageInA);

        messageInA = messageInDAO.findById(44);
        assertEquals("Another message", messageInA.getMessage());
    }

    @Test
    public void testDeleteData() {
        MessageIn messageInA = messageInDAO.findById(12);
        MessageIn messageInB = messageInDAO.findById(44);

        assertNotNull(messageInA);
        messageInDAO.delete(messageInA);
        messageInA = messageInDAO.findById(12);
        assertNull(messageInA);

        assertNotNull(messageInB);
        messageInDAO.deleteById(messageInB.getId());
        messageInB = messageInDAO.findById(44);
        assertNull(messageInB);
    }
}

