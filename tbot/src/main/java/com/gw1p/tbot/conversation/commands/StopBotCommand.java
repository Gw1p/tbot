package com.gw1p.tbot.conversation.commands;

import com.gw1p.tbot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.gw1p.tbot.conversation.ActionType;
import com.gw1p.tbot.conversation.Conversation;
import com.gw1p.tbot.conversation.Response;
import com.gw1p.tbot.conversation.ResponseType;
import com.gw1p.tbot.model.PermissionType;
import com.gw1p.tbot.model.User;

import java.util.List;

/**
 * Позволяет пользователю остановить Бота через чат
 */
public class StopBotCommand extends Conversation {

    @Override
    public void reset() {
        this.currentStep = -1;
        this.maxSteps = 2;
        this.minimumPermissions = PermissionType.ADMIN;
    }

    @Override
    public boolean canStart(String message, User user, Chat chat) {
        if (user.hasPermission(this.minimumPermissions) &&
                (message.toLowerCase().equals("/stop"))) {
            return true;
        }

        return false;
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            response = new Response(ResponseType.KEYBOARD,
                    "Sorry, I did not get that. Do you want to stop TBot? ( Yes/No )",
                    new ReplyKeyboardMarkup(
                            new String[]{ "Yes" },
                            new String[]{ "No" })
                            .oneTimeKeyboard(true)
                            .resizeKeyboard(true)
                            .selective(true));
            this.currentStep += 1;
        } else if (this.currentStep == 0) {
            if (message.text().toLowerCase().equals("yes")) {
                response = new Response(ResponseType.TEXT, "Stopping bot.", ActionType.STOP_BOT);
                this.currentStep += 1;
            } else if (message.text().toLowerCase().equals("No")) {
                response = new Response(ResponseType.TEXT, "Aborting.");
                this.currentStep += 1;
            } else {
                response = new Response(ResponseType.KEYBOARD,
                        "Sorry, I did not get that. Do you want to stop TBot? ( Yes/No )",
                        new ReplyKeyboardMarkup(
                                new String[]{ "Yes" },
                                new String[]{ "No" })
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .selective(true));
            }
        }
        return response;
    }
}
