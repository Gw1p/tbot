package com.ufc.tbot.conversation.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.ufc.tbot.conversation.ActionType;
import com.ufc.tbot.conversation.Conversation;
import com.ufc.tbot.conversation.Response;
import com.ufc.tbot.conversation.ResponseType;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;
import com.ufc.tbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Запрашивает номер телефона у пользователей
 */
public class RequestPhoneCommand extends Conversation {

    @Autowired
    private UserService userService;

    @Override
    public void reset() {
        this.maxSteps = 2;
        this.currentStep = -1;
    }

    @Override
    public boolean canStart(String message, User user) {
        return user.hasPermission(PermissionType.USER) && user.getPhone() == null;
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {

            response = new Response(
                    ResponseType.KEYBOARD_REPLY,
                    "Пожалуйста, дайте свой номер телефона.",
                    new ReplyKeyboardMarkup(
                            new KeyboardButton[]{
                                    new KeyboardButton("\u1F4DE Отправить Контакт").requestContact(true),
                            }
                            ),
                    message.messageId()
            );
            this.currentStep += 1;

        } else if (this.currentStep == 0) {

            try {
                User updatedUser = (User) user.clone();
                updatedUser.setPhone(Long.parseLong(
                        message
                        .contact()
                        .phoneNumber()
                        .replaceAll("\\D+","")
                        )
                );
                response = new Response(
                        ResponseType.TEXT_REPLY,
                        "Спасибо!",
                        message.messageId(),
                        ActionType.UPDATE_USER,
                        updatedUser
                );
                this.currentStep += 1;
            } catch (CloneNotSupportedException ex) {}
        }
        return response;
    }
}
