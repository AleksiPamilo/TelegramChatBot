package dev.pmlo.telegram.chatbot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            String token = Config.getBotToken();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot(token));
            System.out.println("Botti käynnistetty!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
