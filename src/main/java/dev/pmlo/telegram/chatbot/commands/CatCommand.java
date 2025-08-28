package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CatCommand implements BotCommand {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "cat";
    }

    @Override
    public String getDescription() {
        return "Näyttää satunnaisen kissakuvan";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            String url = "https://api.thecatapi.com/v1/images/search";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String imageUrl = response.body().split("\"url\":\"")[1].split("\"")[0];

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Kissakuva:\n" + imageUrl)
                    .build();

        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe kissakuvan hakemisessa.")
                    .build();
        }
    }
}
