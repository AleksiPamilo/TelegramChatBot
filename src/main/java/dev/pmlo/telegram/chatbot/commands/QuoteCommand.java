package dev.pmlo.telegram.chatbot.commands;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuoteCommand implements BotCommand {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "quote";
    }

    @Override
    public String getDescription() {
        return "Palauttaa satunnaisen motivaatiolauseen";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://zenquotes.io/api/random"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray arr = new JSONArray(response.body());
            JSONObject quoteJson = arr.getJSONObject(0);
            String quote = quoteJson.getString("q") + "\n- " + quoteJson.getString("a");

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(quote)
                    .build();
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe lainauksen hakemisessa: " + e.getMessage())
                    .build();
        }
    }
}