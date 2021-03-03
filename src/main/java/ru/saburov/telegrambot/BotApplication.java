package ru.saburov.telegrambot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BotApplication {

    private static final Map<String, String> getenv = System.getenv();
    private static final Parser parser = new Parser();


    public static void main(String[] args) {
        Bot bot = new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN"));
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


        Timer myTimer = new Timer();
        TimerTask checkChapters = new TimerTask() {
            @Override
            public void run () {
                List<String> chapterList = parser.checkNewChapters();

                if (!chapterList.isEmpty()) {
                    String msg = "Вышли новые главы: " + String.join(", ", chapterList);
                    bot.notifyAboutNewChapters(msg);
                    bot.readChatsFromJson();
                }
            }
        };

        myTimer.scheduleAtFixedRate(checkChapters , 0L, 20 * (60*1000));

    }

}
