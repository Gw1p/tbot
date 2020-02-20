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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Определяет серию комманд, с помощью которых пользователи могут изменять права других пользователей
 */
@Component
public class EditUserCommand extends Conversation implements Cloneable {

    private List<User> userList;

    private int selectedUserIndex;

    @Autowired
    private UserService userService;

    @Override
    public void reset() {
        this.currentStep = -1;
        this.maxSteps = 3;
        this.minimumPermissions = PermissionType.ADMIN;
        this.userList = new ArrayList<>();
    }

    @Override
    public boolean canStart(String message, User user) {
        return message.startsWith("/пользователи") && user.hasPermission(PermissionType.ADMIN);
    }

    @Override
    public Response step(String message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {

            String output = userSummaries(users);
            output += "\nНапишите индекс пользователя, которого вы хотите изменить (например, 1)";
            response = new Response(ResponseType.TEXT, output);

            this.userList = users;
            this.currentStep += 1;

        } else if (this.currentStep == 0) {

            try {
                this.selectedUserIndex = getIndex(message) - 1;

                String userOption = userList.get(selectedUserIndex).hasPermission(PermissionType.USER) ?
                        "Убрать Права Пользователя" : "Дать Права Пользователя";
                String adminOption = userList.get(selectedUserIndex).hasPermission(PermissionType.ADMIN) ?
                        "Убрать Админку" : "Назначить Админом";

                response = new Response(ResponseType.KEYBOARD,
                        "Что сделать с пользователем " + (selectedUserIndex + 1) + ". " +
                                this.userList.get(this.selectedUserIndex).getFirstName() + "?",
                        new ReplyKeyboardMarkup(
                                new String[]{ userOption },
                                new String[]{ adminOption },
                                new String[]{ "Отменить" })
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .selective(true)
                );
                this.currentStep += 1;
            } catch (Exception ex) {
                response = new Response(ResponseType.TEXT, "Неверный формат." +
                        "\nНапишите индекс пользователя, которого вы хотите изменить (например, 1)");
            }

        } else if (this.currentStep == 1) {
            user = userList.get(selectedUserIndex);
            if (message.equals("Убрать Админку")) {
                try {
                    User updatedUser = (User) this.userList.get(this.selectedUserIndex).clone();
                    userService.removePermission(updatedUser, PermissionType.ADMIN);
                    response = new Response(ResponseType.TEXT,
                            "Забрал у пользователя " + updatedUser.getFirstName() + " админку",
                            ActionType.UPDATE_USER, updatedUser);
                } catch (CloneNotSupportedException ex) {}
            } else if (message.equals("Назначить Админом")) {
                try {
                    User updatedUser = (User) this.userList.get(this.selectedUserIndex).clone();
                    userService.addPermission(updatedUser, PermissionType.ADMIN);

                    Response[] extraResponses = {
                            new Response(ResponseType.NONE, ActionType.UPDATE_USER, updatedUser),
                            new Response(ResponseType.NONE,
                                    "Вы получили статус админа. " +
                                            "Чтобы увидеть доступные функции бота, просто напишите /помощь.",
                                    ActionType.MESSAGE_USER,
                                    this.userList.get(this.selectedUserIndex)),
                    };

                    response = new Response(ResponseType.TEXT,
                            "Дал пользователю " + updatedUser.getFirstName() + " админку",
                            ActionType.EXTRA_ACTION,
                            extraResponses);
                } catch (CloneNotSupportedException ex) {}
            } else if (message.equals("Убрать Права Пользователя")) {
                try {
                    User updatedUser = (User) this.userList.get(this.selectedUserIndex).clone();
                    userService.removePermission(updatedUser, PermissionType.USER);
                    response = new Response(ResponseType.TEXT,
                            "Забрал у пользователя " + updatedUser.getFirstName() + " статус пользователя",
                            ActionType.UPDATE_USER,
                            updatedUser);
                } catch (CloneNotSupportedException ex) {}
            } else if (message.equals("Дать Права Пользователя")) {
                try {
                    User updatedUser = (User) this.userList.get(this.selectedUserIndex).clone();
                    userService.addPermission(updatedUser, PermissionType.USER);
                    Response[] extraResponses = {
                            new Response(ResponseType.NONE, ActionType.UPDATE_USER, updatedUser),
                            new Response(ResponseType.NONE,
                                    "Ваша учетная запись была подтверждена. " +
                                            "Чтобы увидеть доступные функции бота, просто напишите /помощь.",
                                    ActionType.MESSAGE_USER,
                                    this.userList.get(this.selectedUserIndex)),
                    };

                    response = new Response(ResponseType.TEXT,
                            "Дал пользователю " + updatedUser.getFirstName() + " статус пользователя",
                            ActionType.EXTRA_ACTION,
                            extraResponses);
                } catch (CloneNotSupportedException ex) {}
            } else if (message.equals("Отменить")) {
                response = new Response(ResponseType.TEXT, "Отменяю операцию.");
                this.currentStep = this.maxSteps - 2;
            }

            this.currentStep += 1;
        }
        return response;
    }

    /**
     * Возвращает общую информацию о каждом пользователе
     *
     * @param users о которых создается обобщение
     * @return String с информацией о всех пользователях
     */
    private String userSummaries(List<User> users) {
        StringBuilder responseBuilder = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            // 1. Вася Пупкин (13245) (пользователь: true | админ: false)
            responseBuilder.append((i+1) + ". " + user.getFirstName() + " " + user.getLastName() +
                    " (" + user.getId() + ") " + "(пользователь: " + user.hasPermission(PermissionType.USER) +
                    " | админ: " + user.hasPermission(PermissionType.ADMIN) + ")\n");
        }
        return responseBuilder.toString();
    }

    /**
     * Возвращает первую цифру из String
     *
     * @param message из которого надо получить цифру
     * @return int челое число, находящееся в message
     */
    private int getIndex(String message) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(message);
        m.matches();
        return Integer.parseInt(m.group());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EditUserCommand editUserCommand2 = (EditUserCommand) super.clone();
        editUserCommand2.userList = new ArrayList<>(this.userList);
        return editUserCommand2;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setSelectedUserIndex(int selectedUserIndex) {
        this.selectedUserIndex = selectedUserIndex;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
}
