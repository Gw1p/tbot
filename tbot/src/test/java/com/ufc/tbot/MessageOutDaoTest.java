package com.ufc.tbot;

import com.ufc.tbot.DAO.MessageOutDAO;
import com.ufc.tbot.model.MessageOut;
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
public class MessageOutDaoTest {

    @Autowired
    private MessageOutDAO messageOutDAO;

    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = Logger.getLogger(MessageOutDaoTest.class.getName());
        MessageOut messageOutA = new MessageOut(
                12,
                34,
                "Test message out",
                new Date(2018, Calendar.FEBRUARY, 23),
                false
        );
        MessageOut messageOutB = new MessageOut(
                55,
                66,
                "Test msg out",
                new Date(2018, Calendar.MARCH, 24),
                true
        );
        this.messageOutDAO.save(messageOutA);
        this.messageOutDAO.save(messageOutB);
        assertNotNull(messageOutA.getMessage());
        assertNotNull(messageOutB.getMessage());
        logger.info("Test SetUp Complete");
    }

    @AfterEach
    public void tearDown() {
        MessageOut messageOutA = messageOutDAO.findById(12);
        MessageOut messageOutB = messageOutDAO.findById(55);

        if (messageOutA != null) {
            messageOutDAO.delete(messageOutA);
        }
        if (messageOutB != null) {
            messageOutDAO.delete(messageOutB);
        }

        logger.info("Test TearDown Complete");
    }

    @Test
    public void testFetchData() {
        MessageOut messageOutA = this.messageOutDAO.findById(12);
        assertNotNull(messageOutA);

        assertEquals(12, messageOutA.getId());
        assertEquals(34, messageOutA.getChatId());
        assertEquals("Test message out", messageOutA.getMessage());
        assertEquals(2018, messageOutA.getMessageDate().getYear());
        assertEquals(Calendar.FEBRUARY, messageOutA.getMessageDate().getMonth());
        assertEquals(23, messageOutA.getMessageDate().getDate());
        assertFalse(messageOutA.isSent());

        MessageOut messageOutB = this.messageOutDAO.findById(55);
        assertNotNull(messageOutB);

        assertEquals(55, messageOutB.getId());
        assertEquals(66, messageOutB.getChatId());
        assertEquals("Test msg out", messageOutB.getMessage());
        assertEquals(2018, messageOutB.getMessageDate().getYear());
        assertEquals(Calendar.MARCH, messageOutB.getMessageDate().getMonth());
        assertEquals(24, messageOutB.getMessageDate().getDate());
        assertTrue(messageOutA.isSent());

        Iterable messagesOut = messageOutDAO.findAll();
        int count = 0;
        for(Object p : messagesOut){
            // Только считаем заранее добавленные сообщения
            if (((MessageOut) p).getId() == 12 || ((MessageOut) p).getId() == 55){
                count++;
            }
        }
        assertEquals(count, 2);
    }

    @Test
    public void testUpdateData() {
        MessageOut messageOutA = messageOutDAO.findById(55);
        assertEquals("Test msg out", messageOutA.getMessage());

        messageOutA.setMessage("Test message out 2");
        messageOutDAO.update(messageOutA);

        messageOutA = messageOutDAO.findById(55);
        assertEquals("Test message out 2", messageOutA.getMessage());
    }

    @Test
    public void testDeleteData() {
        MessageOut messageOutA = messageOutDAO.findById(12);
        MessageOut messageOutB = messageOutDAO.findById(55);

        assertNotNull(messageOutA);
        messageOutDAO.delete(messageOutA);
        messageOutA = messageOutDAO.findById(12);
        assertNull(messageOutA);

        assertNotNull(messageOutB);
        messageOutDAO.deleteById(messageOutB.getId());
        messageOutB = messageOutDAO.findById(44);
        assertNull(messageOutB);
    }
}

