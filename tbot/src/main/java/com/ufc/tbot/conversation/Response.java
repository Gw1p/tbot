package com.ufc.tbot.conversation;

import com.pengrad.telegrambot.model.request.Keyboard;

/**
 * Объект, определяющий действия в ответ на сообщение пользователя Боту
 */
public class Response {

    // Какой это тип ответа?
    private ResponseType responseType;

    // Сообщение, которое надо отправить пользователю
    private String responseText;

    // (Зависит от ResponseType) клавиатура, которую стоит представить пользователю
    private Keyboard responseKeyboard;

    // Id сообщения, на которое ответить
    private int messageId;

    // Дополнительное действие, сопутствующее этому ответу
    private ActionType action;

    // Дополнительный объект для соответствующего Action
    private Object actionObject;

    public Response(ResponseType responseType, String responseText) {
        this.responseType = responseType;
        this.responseText = responseText;
        this.action = ActionType.NONE;
    }

    public Response(ResponseType responseType, String responseText, int messageId) {
        this.responseType = responseType;
        this.responseText = responseText;
        this.messageId = messageId;
        this.action = ActionType.NONE;
    }

    public Response(ResponseType responseType, String responseText, ActionType action) {
        this.responseType = responseType;
        this.responseText = responseText;
        this.action = action;
    }

    public Response(ResponseType responseType, String responseText, ActionType action, Object actionObject) {
        this.responseType = responseType;
        this.responseText = responseText;
        this.action = action;
        this.actionObject = actionObject;
    }

    public Response(ResponseType responseType,
                    String responseText,
                    int messageId,
                    ActionType action,
                    Object actionObject) {
        this.responseType = responseType;
        this.responseText = responseText;
        this.messageId = messageId;
        this.action = action;
        this.actionObject = actionObject;
    }

    public Response(ResponseType responseType, String responseText, Keyboard responseKeyboard) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.responseText = responseText;
        this.action = ActionType.NONE;
    }

    public Response(ResponseType responseType, String responseText, Keyboard responseKeyboard, int messageId) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.responseText = responseText;
        this.messageId = messageId;
        this.action = ActionType.NONE;
    }

    public Response(ResponseType responseType,
                    String responseText,
                    Keyboard responseKeyboard,
                    ActionType action) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.responseText = responseText;
        this.action = action;
    }

    public Response(ResponseType responseType,
                    String responseText,
                    Keyboard responseKeyboard,
                    ActionType action,
                    Object actionObject) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.responseText = responseText;
        this.action = action;
        this.actionObject = actionObject;
    }

    public Response(ResponseType responseType,
                    String responseText,
                    Keyboard responseKeyboard,
                    int messageId,
                    ActionType action,
                    Object actionObject) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.responseText = responseText;
        this.messageId = messageId;
        this.action = action;
        this.actionObject = actionObject;
    }

    public Response(ResponseType responseType, ActionType action, Object actionObject) {
        this.responseType = responseType;
        this.action = action;
        this.actionObject = actionObject;
    }

    public Response(ResponseType responseType, Keyboard responseKeyboard) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.action = ActionType.NONE;
    }

    public Response(ResponseType responseType, Keyboard responseKeyboard, ActionType action) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.action = action;
    }

    public Response(ResponseType responseType, Keyboard responseKeyboard, ActionType action, Object actionObject) {
        this.responseType = responseType;
        this.responseKeyboard = responseKeyboard;
        this.action = action;
        this.actionObject = actionObject;
    }

    public ResponseType getResponseType() { return responseType; }

    public void setResponseText(String responseText) { this.responseText = responseText; }

    public String getResponseText() { return responseText; }

    public Keyboard getResponseKeyboard() { return responseKeyboard; }

    public int getMessageId() { return messageId; }

    public ActionType getAction() { return action; }

    public Object getActionObject() { return actionObject; }

    @Override
    public String toString() {
        return "Response (" + this.responseType.toString() + "): '" + this.responseText +
                "' | Action: " + this.action.toString();
    }
}
