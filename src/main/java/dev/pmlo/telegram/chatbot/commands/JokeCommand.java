package dev.pmlo.telegram.chatbot.commands;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JokeCommand implements BotCommand {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "/joke";
    }

    @Override
    public String getDescription() {
        return "Kertoo huisin hauskoja vitsejä";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v2.jokeapi.dev/joke/Any?type=single"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            String joke = json.getString("joke");

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(joke)
                    .build();
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe vitsin hakemisessa: " + e.getMessage())
                    .build();
        }
    }
}

