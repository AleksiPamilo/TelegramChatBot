package dev.pmlo.telegram.chatbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class MenuCommand implements BotCommand {
    @Override
    public String getCommand() {
        return "/menu";
    }

    @Override
    public String getDescription() {
        return "Näyttää tämän päivän lounaslistan";
    }

    @Override
    public SendMessage handle(Message message) {
        LocalDate today = LocalDate.now();
        String targetDate = today.toString();

        String url = "https://www.compass-group.fi/menuapi/week-menus?costCenter=0083"
                + "&date=" + targetDate + "&language=fi";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            JSONArray menus = json.getJSONArray("menus");

            JSONObject todaysMenu = null;
            for (int i = 0; i < menus.length(); i++) {
                JSONObject dayMenu = menus.getJSONObject(i);
                if (dayMenu.getString("date").startsWith(targetDate)) {
                    todaysMenu = dayMenu;
                    break;
                }
            }

            if (todaysMenu == null) {
                return new SendMessage(message.getChatId().toString(),
                        "Ruokalistaa ei löytynyt tälle päivälle.");
            }

            StringBuilder sb = new StringBuilder("*Lounas tänään:*\n");
            JSONArray menuPackages = todaysMenu.getJSONArray("menuPackages");
            for (int i = 0; i < menuPackages.length(); i++) {
                JSONObject menuPackage = menuPackages.getJSONObject(i);
                String pkgName = menuPackage.getString("name").trim();
                String pkgPrice = menuPackage.getString("price").trim();

                if (pkgName.isEmpty() && pkgPrice.isEmpty()) continue;

                sb.append("*").append(escapeMarkdown(pkgName)).append("* (")
                        .append(escapeMarkdown(pkgPrice)).append(")\n");

                JSONArray meals = menuPackage.getJSONArray("meals");
                for (int j = 0; j < meals.length(); j++) {
                    JSONObject meal = meals.getJSONObject(j);
                    sb.append("- ").append(escapeMarkdown(meal.getString("name")));
                    JSONArray diets = meal.getJSONArray("diets");
                    if (!diets.isEmpty()) {
                        sb.append(" (");
                        for (int k = 0; k < diets.length(); k++) {
                            sb.append(escapeMarkdown(diets.getString(k)));
                            if (k < diets.length() - 1) sb.append(", ");
                        }
                        sb.append(")");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }

            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), sb.toString());
            sendMessage.setParseMode("Markdown");
            return sendMessage;

        } catch (Exception e) {
            e.printStackTrace();
            return new SendMessage(message.getChatId().toString(),
                    "Virhe haettaessa ruokalistaa.");
        }
    }

    private String escapeMarkdown(String text) {
        if (text == null) return "";
        return text.replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("`", "\\`");
    }
}