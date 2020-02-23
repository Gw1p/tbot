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

    private List<Integer> selectedUserIndexes;

    private String adminNotification = "Вы получили статус админа. " +
            "Чтобы увидеть доступные функции бота, просто напишите /помощь.";

    private String userNotification = "Ваша учетная запись была подтверждена. " +
            "Чтобы увидеть доступные функции бота, просто напишите /помощь.";

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
        return message.startsWith("/пользовател") && user.hasPermission(PermissionType.ADMIN);
    }

    @Override
    public Response step(String message, User user, List<User> users) {
        Response response = new Response(ResponseType.TEXT, "");

        if (this.currentStep == -1) {

            String output = userSummaries(users);
            output += "\nНапишите индекс пользователя, которого вы хотите изменить (например, 1).\n" +
                    "Для изменения нескольких пользователей, напишите идексы через запятую.";
            response = new Response(ResponseType.TEXT, output);

            this.userList = users;
            this.currentStep += 1;

        } else if (this.currentStep == 0) {

            response = getUserOptions(message);

        } else if (this.currentStep == 1) {

            response = changeUsersPermissions(message);

        }
        return response;
    }

    /**
     * Создает Response объект с возможными опциями для админа
     *
     * @param message которое отправил админ с индексами пользователей
     * @return Response объект с опциями
     */
    private Response getUserOptions(String message) {
        Response response = new Response(ResponseType.NONE, "");

        try {
            selectedUserIndexes = getIndexes(message);
            System.out.println("Indexes: " + selectedUserIndexes);
            if (selectedUserIndexes.size() == 1) {

                String userOption = userList.get(selectedUserIndexes.get(0) - 1).hasPermission(PermissionType.USER) ?
                        "Убрать Права Пользователя" : "Дать Права Пользователя";
                String adminOption = userList.get(selectedUserIndexes.get(0) - 1).hasPermission(PermissionType.ADMIN) ?
                        "Убрать Админку" : "Назначить Админом";

                response = new Response(ResponseType.KEYBOARD,
                        "Что сделать с пользователем " + (selectedUserIndexes.get(0)) + ". " +
                                this.userList.get(selectedUserIndexes.get(0) - 1).getFirstName() + "?",
                        new ReplyKeyboardMarkup(
                                new String[]{ userOption },
                                new String[]{ adminOption },
                                new String[]{ "Отменить" })
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .selective(true)
                );

            } else if (selectedUserIndexes.size() > 1) {

                StringBuilder output = new StringBuilder();
                output.append("Вы выбрали пользователей:\n");
                for (Integer userIndex : selectedUserIndexes) {
                    output.append(userIndex);
                    output.append(". ");
                    output.append(userList.get(userIndex - 1).getFirstName());
                    output.append(" ");
                    output.append(userList.get(userIndex - 1).getLastName());
                    output.append("\n");
                }
                output.append("\nЧто с ними делать?");

                response = new Response(ResponseType.KEYBOARD,
                        output.toString(),
                        new ReplyKeyboardMarkup(
                                new String[]{ "- Права Пользователей", "+ Права Пользователей" },
                                new String[]{ "- Права Админов", "+ Права Админов" },
                                new String[]{ "Отменить" })
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .selective(true)
                );

            }

            this.currentStep += 1;
        } catch (Exception ex) {
            System.out.println("Exception when retrieving indexes from message: " + ex.getMessage());
            response = new Response(ResponseType.TEXT, "Неверный формат." +
                    "\nНапишите индекс пользователя, которого вы хотите изменить (например, 1)");
        }

        return response;
    }

    /**
     * Изменяет права пользователя/пользователей в зависимости от выбора пользователя
     *
     * @param message ответ пользователя
     * @return Response объект с изменениями
     */
    private Response changeUsersPermissions(String message) {
        Response response = new Response(ResponseType.NONE, "");

        if (message.equals("Отменить")) {

            response = new Response(ResponseType.TEXT, "Отменяю операцию.");
            this.currentStep = this.maxSteps - 1;

        } else {

            if (selectedUserIndexes.size() == 1) {

                if (message.equals("Убрать Админку")) {

                    try {
                        User updatedUser = (User) this.userList.get(selectedUserIndexes.get(0) - 1).clone();
                        userService.removePermission(updatedUser, PermissionType.ADMIN);
                        response = new Response(ResponseType.TEXT,
                                "Забрал у пользователя " + updatedUser.getFirstName() + " админку",
                                ActionType.UPDATE_USER, updatedUser);
                    } catch (CloneNotSupportedException ex) {}

                } else if (message.equals("Назначить Админом")) {

                    try {
                        User updatedUser = (User) this.userList.get(selectedUserIndexes.get(0) - 1).clone();
                        userService.addPermission(updatedUser, PermissionType.ADMIN);

                        Response[] extraResponses = {
                                new Response(ResponseType.NONE, ActionType.UPDATE_USER, updatedUser),
                                new Response(
                                        ResponseType.NONE,
                                        adminNotification,
                                        ActionType.MESSAGE_USER,
                                        this.userList.get(selectedUserIndexes.get(0) - 1)
                                ),
                        };

                        response = new Response(ResponseType.TEXT,
                                "Дал пользователю " + updatedUser.getFirstName() + " админку",
                                ActionType.EXTRA_ACTION,
                                extraResponses);
                    } catch (CloneNotSupportedException ex) {}

                } else if (message.equals("Убрать Права Пользователя")) {

                    try {
                        User updatedUser = (User) this.userList.get(selectedUserIndexes.get(0) - 1).clone();
                        userService.removePermission(updatedUser, PermissionType.USER);
                        response = new Response(ResponseType.TEXT,
                                "Забрал у пользователя " + updatedUser.getFirstName() + " статус пользователя",
                                ActionType.UPDATE_USER,
                                updatedUser);
                    } catch (CloneNotSupportedException ex) {}

                } else if (message.equals("Дать Права Пользователя")) {

                    try {
                        User updatedUser = (User) this.userList.get(selectedUserIndexes.get(0) - 1).clone();
                        userService.addPermission(updatedUser, PermissionType.USER);
                        Response[] extraResponses = {
                                new Response(ResponseType.NONE, ActionType.UPDATE_USER, updatedUser),
                                new Response(
                                        ResponseType.NONE,
                                        userNotification,
                                        ActionType.MESSAGE_USER,
                                        this.userList.get(selectedUserIndexes.get(0) - 1)
                                ),
                        };

                        response = new Response(ResponseType.TEXT,
                                "Дал пользователю " + updatedUser.getFirstName() + " статус пользователя",
                                ActionType.EXTRA_ACTION,
                                extraResponses);
                    } catch (CloneNotSupportedException ex) {}

                }

            } else if (selectedUserIndexes.size() > 1) {

                if (message.equals("- Права Пользователей")) {

                    try {
                        List<Response> extraResponses = new ArrayList<>();
                        for (Integer index : selectedUserIndexes) {
                            User updatedUser = (User) this.userList.get(index - 1).clone();
                            if (updatedUser.hasPermission(PermissionType.USER)) {
                                userService.removePermission(updatedUser, PermissionType.USER);
                                extraResponses.add(
                                        new Response(ResponseType.NONE,
                                                ActionType.UPDATE_USER,
                                                updatedUser)
                                );
                            }
                        }

                        response = new Response(
                                ResponseType.TEXT,
                                "Забрал у пользователей статус пользователя",
                                ActionType.EXTRA_ACTION,
                                extraResponses
                        );

                    } catch (CloneNotSupportedException ex) {}

                } else if (message.equals("+ Права Пользователей")) {

                    try {
                        List<Response> extraResponses = new ArrayList<>();
                        for (Integer index : selectedUserIndexes) {
                            User updatedUser = (User) this.userList.get(index - 1).clone();
                            if (!updatedUser.hasPermission(PermissionType.USER)) {
                                userService.addPermission(updatedUser, PermissionType.USER);

                                extraResponses.add(
                                        new Response(ResponseType.NONE,
                                                ActionType.UPDATE_USER,
                                                updatedUser)
                                );

                                extraResponses.add(new Response(
                                        ResponseType.NONE,
                                        "Ваша учетная запись была подтверждена. " +
                                                "Чтобы увидеть доступные функции бота, просто напишите /помощь.",
                                        ActionType.MESSAGE_USER,
                                        this.userList.get(index - 1))
                                );
                            }
                        }

                        response = new Response(
                                ResponseType.TEXT,
                                "Дал пользователям права пользователя",
                                ActionType.EXTRA_ACTION,
                                extraResponses
                        );

                    } catch (CloneNotSupportedException ex) {}

                } else if (message.equals("- Права Админов")) {

                    try {
                        List<Response> extraResponses = new ArrayList<>();
                        for (Integer index : selectedUserIndexes) {
                            User updatedUser = (User) this.userList.get(index - 1).clone();
                            if (updatedUser.hasPermission(PermissionType.ADMIN)) {
                                userService.removePermission(updatedUser, PermissionType.ADMIN);
                                extraResponses.add(
                                        new Response(ResponseType.NONE,
                                                ActionType.UPDATE_USER,
                                                updatedUser)
                                );
                            }
                        }

                        response = new Response(
                                ResponseType.TEXT,
                                "Забрал у пользователей статус админа",
                                ActionType.EXTRA_ACTION,
                                (Response[]) extraResponses.toArray()
                        );

                    } catch (CloneNotSupportedException ex) {}

                } else if (message.equals("+ Права Админов")) {

                    try {
                        List<Response> extraResponses = new ArrayList<>();
                        for (Integer index : selectedUserIndexes) {
                            User updatedUser = (User) this.userList.get(index - 1).clone();
                            if (!updatedUser.hasPermission(PermissionType.ADMIN)) {
                                userService.addPermission(updatedUser, PermissionType.ADMIN);

                                extraResponses.add(
                                        new Response(ResponseType.NONE,
                                                ActionType.UPDATE_USER,
                                                updatedUser)
                                );

                                extraResponses.add(new Response(
                                        ResponseType.NONE,
                                        adminNotification,
                                        ActionType.MESSAGE_USER,
                                        this.userList.get(index - 1))
                                );
                            }
                        }

                        response = new Response(
                                ResponseType.TEXT,
                                "Дал пользователям права админа",
                                ActionType.EXTRA_ACTION,
                                extraResponses
                        );

                    } catch (CloneNotSupportedException ex) {}

                }
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
     * Возвращает все integers из String
     *
     * @param message из которого надо получить цифры
     * @return List<Integer> челые числа, находящиеся в message
     */
    private List<Integer> getIndexes(String message) {
        List<Integer> indexes = new ArrayList<>();

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(message);

        while (m.find()) {
            indexes.add(Integer.parseInt(m.group()));
            System.out.println(m.group());
        }

        System.out.println("Indexes: " + indexes);
        return indexes;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EditUserCommand editUserCommand2 = (EditUserCommand) super.clone();
        editUserCommand2.userList = new ArrayList<>(this.userList);
        return editUserCommand2;
    }
}
