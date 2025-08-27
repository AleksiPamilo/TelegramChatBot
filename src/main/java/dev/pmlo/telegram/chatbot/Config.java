package dev.pmlo.telegram.chatbot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }

    public static String getWeatherApiKey() {
        return dotenv.get("WEATHER_API_KEY");
    }

    public static String getRapidApiKey() {
        return dotenv.get("RAPID_API_KEY");
    }
}
