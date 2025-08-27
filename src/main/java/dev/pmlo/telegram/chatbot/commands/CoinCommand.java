package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Random;

public class CoinCommand implements BotCommand {
    private final Random random = new Random();

    @Override
    public String getCommand() {
        return "coin";
    }

    @Override
    public String getDescription() {
        return "Heittää kolikon (Kruuna/Klaava).";
    }

    @Override
    public SendMessage handle(Message message) {
        String result = random.nextBoolean() ? "Kruuna" : "Klaava";
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("🪙 Heitit kolikkoa: " + result)
                .build();
    }
}
