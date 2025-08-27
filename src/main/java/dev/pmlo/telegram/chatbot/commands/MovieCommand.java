package dev.pmlo.telegram.chatbot.commands;

import dev.pmlo.telegram.chatbot.Config;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MovieCommand implements BotCommand {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_KEY = Config.getRapidApiKey();

    @Override
    public String getCommand() {
        return "movie";
    }

    @Override
    public String getDescription() {
        return "Palauttaa satunnaisen elokuvan.";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://random-movie-api2.p.rapidapi.com/api/random-movie"))
                    .header("x-rapidapi-key", API_KEY)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            System.out.println(json);
            String movie = json.optString("movie", "Elokuvaa ei löydetty!");

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Tässä satunnainen elokuva:\n" + movie)
                    .build();

        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe elokuvan hakemisessa.")
                    .build();
        }
    }
}
