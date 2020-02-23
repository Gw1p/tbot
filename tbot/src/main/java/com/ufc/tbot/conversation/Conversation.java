package com.ufc.tbot.conversation;

import com.pengrad.telegrambot.model.Message;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;

import java.util.List;

/**
 * Базовый класс для всех Комманд между ботом и чатом
 */
public abstract class Conversation implements Cloneable {

    // Количество шагов в серии комманд
    protected int maxSteps = 0;

    // Текущий шаг; начинается с -1
    protected int currentStep = -1;

    // Минимальные Права, необходимые для использования комманды
    protected PermissionType minimumPermissions;

    public Conversation() { this.reset(); }

    public abstract void reset();

    /**
     * Может ли user начать этот Conversation с message?
     *
     * @param message сообщение от пользователя
     * @param user от которого пришло сообщение
     * @return true/false может ли пользователь начать серию комманд?
     */
    public abstract boolean canStart(String message, User user);

    /**
     * Закончил ли пользователь серию комманд?
     *
     * @return true/false
     */
    public boolean finished() { return this.currentStep >= this.maxSteps - 1; }

    /**
     * Решает, какой Response отправить пользователю, в зависимости от текущего шага
     *
     * @param message которое отправил пользователь
     * @param user от кого сообщение
     * @param users все пользователи
     * @return Response объект, который даст BotService понять, что делать дальше
     */
    public abstract Response step(Message message, User user, List<User> users);

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
