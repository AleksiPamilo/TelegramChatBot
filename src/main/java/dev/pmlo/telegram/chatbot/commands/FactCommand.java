package dev.pmlo.telegram.chatbot.commands;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FactCommand implements BotCommand {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "/fact";
    }

    @Override
    public String getDescription() {
        return "Palauttaa satunnaisen faktan.";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://uselessfacts.jsph.pl/api/v2/facts/random?language=en"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            JSONObject json = new JSONObject(response.body());
            String fact = json.optString("text", "No fact found!");

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(fact)
                    .build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe faktan hakemisessa.")
                    .build();
        }
    }
}
