package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class NewsCommand implements BotCommand {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "news";
    }

    @Override
    public String getDescription() {
        return "Näyttää ajankohtaisia uutisia";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            String url = "https://yle.fi/rss/uutiset/paauutiset";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/rss+xml")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String[] items = response.body().split("<item>");
            Random random = new Random();
            String item = items[random.nextInt(items.length - 1) + 1];
            String title = item.split("<title>")[1].split("</title>")[0];
            String link = item.split("<link>")[1].split("</link>")[0];

            String newsMessage = "Ajankohtainen uutinen:\n" + title + "\n" + link;

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(newsMessage)
                    .build();

        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe uutisten hakemisessa.")
                    .build();
        }
    }
}
