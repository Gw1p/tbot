package com.gw1p.tbot.conversation.commands;

import com.gw1p.tbot.model.Chat;
import com.gw1p.tbot.model.PermissionType;
import com.pengrad.telegrambot.model.Message;
import com.gw1p.tbot.conversation.Conversation;
import com.gw1p.tbot.conversation.Response;
import com.gw1p.tbot.conversation.ResponseType;
import com.gw1p.tbot.model.User;

import java.util.List;

/**
 * Дает знать, работает ли бот
 */
public class StatusCommand extends Conversation {

    @Override
    public void reset() {
        this.maxSteps = 1;
        this.currentStep = -1;
    }

    @Override
    public boolean canStart(String message, User user, Chat chat) {
        return user.hasPermission(PermissionType.USER) && message.toLowerCase().equals("/status");
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            response = new Response(ResponseType.TEXT, "Status: \u2615 TBot is working \u2705");
            this.currentStep += 1;
        }
        return response;
    }
}
