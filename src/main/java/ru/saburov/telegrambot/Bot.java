package ru.saburov.telegrambot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bot extends TelegramLongPollingBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String CHATS_PATH = "src/main/resources/chats.json";

    private ObjectMapper objectMapper;
    private File file;
    private HashSet<String> chats;

    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        this.objectMapper = new ObjectMapper();
        this.file = new File(CHATS_PATH);
        this.chats = readChatsFromJson();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();

        if (!chats.contains(String.valueOf(chatId))) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Привет, запомнил тебя.");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public void writeChatInJson(String chatId) {
        HashSet<String> chats = readChatsFromJson();
        chats.add(chatId);

        try {
            objectMapper.writeValue(file, new ArrayList<>(chats));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<String> readChatsFromJson() {
        try {
            List<String> li = objectMapper.readValue(file, new TypeReference<List<String>>(){});
            return new HashSet<>(li);
        } catch (IOException e) {
            System.out.println("Json пустой");
        }

        return new HashSet<>();
    }

}
