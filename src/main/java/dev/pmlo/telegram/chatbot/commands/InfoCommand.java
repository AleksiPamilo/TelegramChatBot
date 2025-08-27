package dev.pmlo.telegram.chatbot.commands;

import dev.pmlo.telegram.chatbot.MyBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class InfoCommand implements BotCommand {

    @Override
    public String getCommand() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Näyttää tietoja botin komennoista";
    }

    @Override
    public SendMessage handle(Message message) {
        StringBuilder sb = new StringBuilder("*Komennot:*\n");
        MyBot.getCommands().values().stream()
                .filter(cmd -> !cmd.getCommand().equals("/info"))
                .forEach(cmd ->
                sb.append("`").append(cmd.getCommand()).append("`")
                        .append(" - ").append(cmd.getDescription())
                        .append("\n")
        );

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), sb.toString());
        sendMessage.setParseMode("Markdown");
        return sendMessage;
    }
}
