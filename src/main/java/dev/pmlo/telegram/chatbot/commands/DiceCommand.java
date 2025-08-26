package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Random;

public class DiceCommand implements BotCommand {
    private final Random random = new Random();

    @Override
    public String getCommand() {
        return "/dice";
    }

    @Override
    public String getDescription() {
        return "Heittää nopan (1-6).";
    }

    @Override
    public SendMessage handle(Message message) {
        int roll = random.nextInt(6) + 1;
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("🎲 Heitit noppaa: " + roll)
                .build();
    }
}
