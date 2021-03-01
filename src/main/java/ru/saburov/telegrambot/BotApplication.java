package ru.saburov.telegrambot;

import com.fasterxml.jackson.core.type.TypeReference;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BotApplication {

    private static final Map<String, String> getenv = System.getenv();

    public static void main(String[] args) {
        Bot bot = new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN"));
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(bot);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//        SendMessage message = new SendMessage();
////        message.setChatId();
//        message.setText("Привет");
//
////        bot.execute();

    }

}
