package com.gw1p.tbot;

import com.gw1p.tbot.model.Chat;
import com.gw1p.tbot.DAO.ChatDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChatDaoTest {

    @Autowired
    private ChatDAO chatDAO;

    private Logger logger;

    @BeforeEach
    public void setUp() {
        logger = Logger.getLogger(ChatDaoTest.class.getName());
        Chat chat1 = new Chat(
                123,
                "Private",
                "First",
                "Chat",
                "first_chat",
                "First Chat",
                "https://samplelink.com/"
        );
        Chat chat2 = new Chat(
                456,
                "Test",
                "Second",
                "Chatt",
                "second_chat",
                "Second Chat",
                "https://samplelink2.com/"
        );
        this.chatDAO.save(chat1);
        this.chatDAO.save(chat2);
        assertNotNull(chat1.getTitle());
        assertNotNull(chat2.getTitle());
        logger.info("Test SetUp Complete");
    }

    @AfterEach
    public void tearDown() {
        Chat chatA = chatDAO.findById(123);
        Chat chatB = chatDAO.findById(456);

        if (chatA != null) {
            chatDAO.delete(chatA);
        }
        if (chatB != null) {
            chatDAO.delete(chatB);
        }

        logger.info("Test TearDown Complete");
    }

    @Test
    public void testFetchData() {
        Chat chatA = this.chatDAO.findById(123);
        assertNotNull(chatA);

        assertEquals(123, chatA.getId());
        assertEquals("Private", chatA.getChatType());
        assertEquals("First", chatA.getFirstName());
        assertEquals("Chat", chatA.getLastName());
        assertEquals("first_chat", chatA.getUsername());
        assertEquals("First Chat", chatA.getTitle());
        assertEquals("https://samplelink.com/", chatA.getInviteLink());

        Chat chatB = this.chatDAO.findById(456);
        assertNotNull(chatB);

        assertEquals(456, chatB.getId());
        assertEquals("Test", chatB.getChatType());
        assertEquals("Second", chatB.getFirstName());
        assertEquals("Chatt", chatB.getLastName());
        assertEquals("second_chat", chatB.getUsername());
        assertEquals("Second Chat", chatB.getTitle());
        assertEquals("https://samplelink2.com/", chatB.getInviteLink());

        Iterable chats = chatDAO.findAll();
        int count = 0;
        for(Object p : chats){
            // Только считаем заранее добавленных пользователей
            if (((Chat) p).getId() == 123 || ((Chat) p).getId() == 456){
                count++;
            }
        }
        assertEquals(count, 2);
    }

    @Test
    public void testUpdateData() {
        Chat chatA = chatDAO.findById(456);
        assertEquals("Chatt", chatA.getLastName());

        chatA.setLastName("Chat");
        chatDAO.update(chatA);

        chatA = chatDAO.findById(456);
        assertEquals("Chat", chatA.getLastName());
    }

    @Test
    public void testDeleteData() {
        Chat chatA = chatDAO.findById(123);
        Chat chatB = chatDAO.findById(456);

        assertNotNull(chatA);
        chatDAO.delete(chatA);
        chatA = chatDAO.findById(123);
        assertNull(chatA);

        assertNotNull(chatB);
        chatDAO.deleteById(chatB.getId());
        chatB = chatDAO.findById(456);
        assertNull(chatB);
    }
}

