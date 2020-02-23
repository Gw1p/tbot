package com.ufc.tbot.conversation;

/**
 * Тип ответа для пользователя
 */
public enum ResponseType {
    NONE, // Никакого ответа в чате
    TEXT, // Только отправить сообщение
    TEXT_REPLY, // Отправить сообщение как ответ на другое сообщение
    KEYBOARD, // Показать custom клавиатуру в чате
    KEYBOARD_REPLY // Показать custom клавиатуру в чате как ответ на другое сообщение
}
