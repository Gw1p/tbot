package com.ufc.tbot.conversation.commands;

import com.pengrad.telegrambot.model.Message;
import com.ufc.tbot.conversation.Conversation;
import com.ufc.tbot.conversation.Response;
import com.ufc.tbot.conversation.ResponseType;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;

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
    public boolean canStart(String message, User user) {
        return user.hasPermission(PermissionType.USER) && message.toLowerCase().equals("/статус");
    }

    @Override
    public Response step(Message message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            response = new Response(ResponseType.TEXT, "Статус: \u2615 TBot работает \u2705");
            this.currentStep += 1;
        }
        return response;
    }
}
