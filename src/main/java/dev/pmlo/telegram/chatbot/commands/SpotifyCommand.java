package dev.pmlo.telegram.chatbot.commands;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyCommand implements BotCommand {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "spotify";
    }

    @Override
    public String getDescription() {
        return "Näyttää tällä hetkellä kuunnellun kappaleen Spotifysta.";
    }

    @Override
    public SendMessage handle(Message message) {
        String apiUrl = "https://api.pmlo.dev/spotify";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body().trim();

            if (body.isEmpty()) {
                return createMessage(message, "Tällä hetkellä ei kuunnella mitään.");
            }

            JSONObject json;
            try {
                json = new JSONObject(body);
            } catch (Exception e) {
                return createMessage(message, "Tällä hetkellä ei kuunnella mitään.");
            }

            JSONObject track = json.optJSONObject("currentlyPlaying");
            if (track == null || !track.optBoolean("isPlaying", false) || json.optInt("code", 0) != 200) {
                return createMessage(message, "Tällä hetkellä ei kuunnella mitään.");
            }

            String name = track.optString("name", "Tuntematon kappale");
            String artists = track.optString("artists", "Tuntematon artisti");
            String url = track.optString("url", "");

            String reply = "🎵 Nyt soitetaan: " + name + " — " + artists;
            if (!url.isEmpty()) {
                reply += "\n" + url;
            }

            return createMessage(message, reply);

        } catch (Exception e) {
            return createMessage(message, "Tällä hetkellä ei kuunnella mitään.");
        }
    }

    private SendMessage createMessage(Message message, String text) {
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(text)
                .build();
    }
}
