package com.gw1p.tbot.conversation;

/**
 * Определяет дополнительное действие, которое BotService предпримет, получив Response из Conversation
 */
public enum ActionType {
    NONE, // Ничего не делать
    UPDATE_USER, // Обновить пользователя (actionObject должен быть User)
    STOP_BOT, // Останавливает Бота
    MESSAGE_USER, // Отправляет сообщение пользователю (если надо 2+ сообщений, а так используйте основной Response)
    EXTRA_ACTION // actionObject принимает array Response, каждый из которых BotService использует
}
