package dev.pmlo.telegram.chatbot;

import dev.pmlo.telegram.chatbot.commands.BotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

public class MyBot extends TelegramLongPollingBot {
    private final Map<String, BotCommand> commands = new HashMap<>();

    public MyBot(String botToken) {
        super(botToken);
        ServiceLoader.load(BotCommand.class).forEach(cmd -> {
            commands.put(cmd.getCommand(), cmd);
            System.out.println("Rekisteröitiin komento: " + cmd.getCommand());
        });
    }

    @Override
    public String getBotUsername() {
        return "Paskabotti";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            BotCommand cmd = commands.get(text);
            if (cmd != null) {
                try {
                    execute(cmd.handle(update.getMessage()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
