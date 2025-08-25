package dev.pmlo.telegram.chatbot.commands;

import dev.pmlo.telegram.chatbot.Config;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherCommand implements BotCommand {
    private static final String API_KEY = Config.getWeatherApiKey();
    private static final String ENDPOINT =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s&lang=fi";

    @Override
    public String getCommand() {
        return "/weather";
    }

    @Override
    public String getDescription() {
        return "Hakee sään annetusta kaupungista, esim: /weather Helsinki";
    }

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        String[] parts = text.split(" ", 2);

        if (parts.length < 2) {
            return new SendMessage(message.getChatId().toString(),
                    "Anna kaupungin nimi, esim: /weather Helsinki");
        }

        String city = parts[1];

        try {
            String urlString = String.format(ENDPOINT, city, API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            double temp = json.getJSONObject("main").getDouble("temp");
            String desc = json.getJSONArray("weather").getJSONObject(0).getString("description");

            return new SendMessage(message.getChatId().toString(),
                    "Sää kohteessa " + city + ": " + temp + "°C, " + desc);

        } catch (Exception e) {
            e.printStackTrace();
            return new SendMessage(message.getChatId().toString(),
                    "Virhe haettaessa säätä kaupungille " + city);
        }
    }
}
