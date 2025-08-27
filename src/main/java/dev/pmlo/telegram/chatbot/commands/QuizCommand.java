package dev.pmlo.telegram.chatbot.commands;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizCommand implements BotCommand {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getCommand() {
        return "quiz";
    }

    @Override
    public String getDescription() {
        return "Palauttaa satunnaisen trivia-kysymyksen ja vastausvaihtoehdot, oikea vastaus spoilerina.";
    }

    @Override
    public SendMessage handle(Message message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://opentdb.com/api.php?amount=1&type=multiple"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject result = new JSONObject(response.body())
                    .getJSONArray("results")
                    .getJSONObject(0);

            String question = StringEscapeUtils.unescapeHtml4(result.getString("question"));
            String correctAnswer = StringEscapeUtils.unescapeHtml4(result.getString("correct_answer"));

            List<String> options = new ArrayList<>();
            options.add(correctAnswer);
            JSONArray incorrect = result.getJSONArray("incorrect_answers");
            for (int i = 0; i < incorrect.length(); i++) {
                options.add(StringEscapeUtils.unescapeHtml4(incorrect.getString(i)));
            }
            Collections.shuffle(options);

            StringBuilder textBuilder = new StringBuilder();
            textBuilder.append(escapeMarkdownV2(question)).append("\nVastausvaihtoehdot:\n");
            for (int i = 0; i < options.size(); i++) {
                textBuilder.append(i + 1).append("\\. ").append(escapeMarkdownV2(options.get(i))).append("\n");
            }
            textBuilder.append("Oikea vastaus: ||").append(escapeMarkdownV2(correctAnswer)).append("||");

            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(textBuilder.toString())
                    .parseMode("MarkdownV2")
                    .build();

        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Virhe kysymyksen hakemisessa.")
                    .build();
        }
    }

    private String escapeMarkdownV2(String text) {
        return text.replaceAll("([_\\*\\[\\]\\(\\)~`>#+\\-=|{}!])", "\\\\$1");
    }
}
