package com.ufc.tbot.conversation.commands;

import com.pengrad.telegrambot.model.Message;
import com.ufc.tbot.conversation.Conversation;
import com.ufc.tbot.conversation.Response;
import com.ufc.tbot.conversation.ResponseType;
import com.ufc.tbot.model.Chat;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;

import java.util.List;

/**
 * Выводит список всех комманд, доступных пользователю от Бота
 */
public class ListCommandsCommand extends Conversation {

    @Override
    public void reset() {
        this.maxSteps = 1;
        this.currentStep = -1;
    }

    @Override
    public boolean canStart(String message, User user, Chat chat) {
        if (user.hasPermission(PermissionType.USER) &&
                (message.toLowerCase().equals("/комманды") || message.toLowerCase().equals("/бот")
                || message.toLowerCase().equals("/помощь"))) {
            return true;
        }
        return false;
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            response = new Response(ResponseType.TEXT, "Список команд:\n/помощь - выдает список команд." +
                    "\n/статус - работает ли бот?");

            if (user.hasPermission(PermissionType.ADMIN)) {
                response.setResponseText(response.getResponseText() + "\n\nКоманды админа:" +
                        "\n/стоп - останавливает Бота\n" +
                        "/пользователи - посмотреть список пользователей и изменить права\n" +
                        "/админы - просмотреть список админов и изменить права");
            }

            this.currentStep += 1;
        }
        return response;
    }
}
