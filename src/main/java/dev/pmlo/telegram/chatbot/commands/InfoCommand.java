package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class InfoCommand implements BotCommand {
    @Override
    public String getCommand() {
        return "/info";
    }

    @Override
    public String getDescription() {
        return "Näyttää tietoja botin komennoista";
    }

    @Override
    public SendMessage handle(Message message) {
        return new SendMessage(message.getChatId().toString(), "Komennot: /info");
    }
}
