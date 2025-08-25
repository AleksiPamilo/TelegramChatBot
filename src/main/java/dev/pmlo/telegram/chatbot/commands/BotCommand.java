package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotCommand {
    String getCommand();
    String getDescription();
    SendMessage handle(Message message);
}
