package com.ufc.tbot.conversation.commands;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.ufc.tbot.conversation.ActionType;
import com.ufc.tbot.conversation.Conversation;
import com.ufc.tbot.conversation.Response;
import com.ufc.tbot.conversation.ResponseType;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;
import com.ufc.tbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Определяет серию комманд, с помощью которых пользователи могут изменять права других пользователей
 */
@Component
public class EditNewUserCommand extends Conversation implements Cloneable {

    private User newUser;

    @Autowired
    private UserService userService;

    @Override
    public void reset() {
        this.currentStep = -1;
        this.maxSteps = 2;
        this.minimumPermissions = PermissionType.ADMIN;
    }

    @Override
    public boolean canStart(String message, User user) {
        return message.startsWith("/пользовател") && user.hasPermission(PermissionType.ADMIN);
    }

    @Override
    public Response step(String message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {
            this.newUser = users.get(0);
            this.currentStep += 1;
        } else if (this.currentStep == 0) {

            if (message.equals("Дать " + newUser.getFirstName() + " Права Админа (+ пользователя)")) {

                try {
                    User updatedUser = (User) newUser.clone();
                    userService.addPermission(updatedUser, PermissionType.USER);
                    userService.addPermission(updatedUser, PermissionType.ADMIN);

                    Response[] extraResponses = {
                            new Response(ResponseType.NONE, ActionType.UPDATE_USER, updatedUser),
                            new Response(ResponseType.NONE,
                                    "Вы получили статус админа. " +
                                            "Чтобы увидеть доступные функции бота, просто напишите /помощь.",
                                    ActionType.MESSAGE_USER,
                                    newUser),
                    };

                    response = new Response(ResponseType.TEXT,
                            "Дал пользователю " + updatedUser.getFirstName() + " админку",
                            ActionType.EXTRA_ACTION,
                            extraResponses);
                } catch (CloneNotSupportedException ex) {}

            } else if (message.equals("Дать " + newUser.getFirstName() + " Права Пользователя")) {

                try {
                    User updatedUser = (User) newUser.clone();
                    userService.addPermission(updatedUser, PermissionType.USER);
                    Response[] extraResponses = {
                            new Response(ResponseType.NONE, ActionType.UPDATE_USER, updatedUser),
                            new Response(ResponseType.NONE,
                                    "Ваша учетная запись была подтверждена. " +
                                            "Чтобы увидеть доступные функции бота, просто напишите /помощь.",
                                    ActionType.MESSAGE_USER,
                                    newUser),
                    };

                    response = new Response(ResponseType.TEXT,
                            "Дал пользователю " + updatedUser.getFirstName() + " статус пользователя",
                            ActionType.EXTRA_ACTION,
                            extraResponses);
                } catch (CloneNotSupportedException ex) {}

            } else if (message.equals("Ничего")) {

                response = new Response(ResponseType.TEXT, "У пользователя " +
                        newUser.getFirstName() + " нет прав для Бота.");
                this.currentStep = this.maxSteps - 1;

            } else {

                response = new Response(
                        ResponseType.KEYBOARD,
                        "Не понял. Попробуйте еще раз.",
                        new ReplyKeyboardMarkup(
                        new String[]{ "Дать " + user.getFirstName() + " Права Пользователя" },
                        new String[]{ "Дать " + user.getFirstName() + " Права Админа (+ пользователя)" },
                        new String[]{ "Ничего" }
                ).oneTimeKeyboard(true)
                        .resizeKeyboard(true)
                        .selective(true)
                );
                this.currentStep -= 1;

            }

            this.currentStep += 1;
        }
        return response;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EditNewUserCommand editUserCommand2 = (EditNewUserCommand) super.clone();
        editUserCommand2.newUser = (User) this.newUser.clone();
        return editUserCommand2;
    }
}
