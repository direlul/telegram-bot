package ru.saburov.telegrambot;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Bot extends TelegramLongPollingBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final DAO dao;

    private List<String> chats;

    public Bot(String botName, String botToken, DAO dao) {
        super();
        this.dao = dao;
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        this.chats = dao.readChats();
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
        Long chatId = update.getMessage().getChatId();

        if (!chats.contains(String.valueOf(chatId))) {
            dao.saveChat(String.valueOf(chatId));
            this.chats = dao.readChats();
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

    public void notifyAboutNewChapters(String msg) {
        for (String chatId : chats) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(msg);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
