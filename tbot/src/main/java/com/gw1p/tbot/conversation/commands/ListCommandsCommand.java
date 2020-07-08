package com.gw1p.tbot.conversation.commands;

import com.gw1p.tbot.conversation.ResponseType;
import com.gw1p.tbot.model.Chat;
import com.gw1p.tbot.model.PermissionType;
import com.gw1p.tbot.model.User;
import com.pengrad.telegrambot.model.Message;
import com.gw1p.tbot.conversation.Conversation;
import com.gw1p.tbot.conversation.Response;

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
                (message.toLowerCase().equals("/commands") || message.toLowerCase().equals("/bot")
                || message.toLowerCase().equals("/help"))) {
            return true;
        }
        return false;
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            response = new Response(ResponseType.TEXT, "Available commands:\n/help - lists all available commands." +
                    "\n/status - is the bot working?");

            if (user.hasPermission(PermissionType.ADMIN)) {
                response.setResponseText(response.getResponseText() + "\n\nAdmin Commands:" +
                        "\n/stop - stops TBot");
            }

            this.currentStep += 1;
        }
        return response;
    }
}
