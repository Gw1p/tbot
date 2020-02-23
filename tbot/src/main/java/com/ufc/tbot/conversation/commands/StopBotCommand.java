package com.ufc.tbot.conversation.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.ufc.tbot.conversation.ActionType;
import com.ufc.tbot.conversation.Conversation;
import com.ufc.tbot.conversation.Response;
import com.ufc.tbot.conversation.ResponseType;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;

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
    public boolean canStart(String message, User user) {
        if (user.hasPermission(this.minimumPermissions) &&
                (message.toLowerCase().equals("/stop") || message.toLowerCase().equals("/стоп"))) {
            return true;
        }

        return false;
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            response = new Response(ResponseType.KEYBOARD,
                    "Не понял, попробуйте еще раз. Вы хотите остановить Бота? ( Да/Нет )",
                    new ReplyKeyboardMarkup(
                            new String[]{ "Да" },
                            new String[]{ "Нет" })
                            .oneTimeKeyboard(true)
                            .resizeKeyboard(true)
                            .selective(true));
            this.currentStep += 1;
        } else if (this.currentStep == 0) {
            if (message.text().toLowerCase().equals("да")) {
                response = new Response(ResponseType.TEXT, "Останавливаю Бота.", ActionType.STOP_BOT);
                this.currentStep += 1;
            } else if (message.text().toLowerCase().equals("нет")) {
                response = new Response(ResponseType.TEXT, "Отменяю.");
                this.currentStep += 1;
            } else {
                response = new Response(ResponseType.KEYBOARD,
                        "Не понял, попробуйте еще раз. Вы хотите остановить Бота? ( Да/Нет )",
                        new ReplyKeyboardMarkup(
                                new String[]{ "Да" },
                                new String[]{ "Нет" })
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .selective(true));
            }
        }
        return response;
    }
}
