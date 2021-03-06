package ru.saburov.telegrambot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BotApplication {

    private static final Map<String, String> getenv = System.getenv();
    private static final DAO dao = new DAO();
    private static final Parser parser = new Parser(dao);

    public static void main(String[] args) {
        Bot bot = new Bot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN"), dao);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


        Timer myTimer = new Timer();
        TimerTask checkChapters = new TimerTask() {
            @SneakyThrows
            @Override
            public void run () {
            if (dao.getCon().isClosed()) {
                dao.newConnection();
            }

            List<String> chapterList = parser.checkNewChapters();

            if (chapterList != null && !chapterList.isEmpty()) {
                String msg = "Вышли новые главы: " + String.join(", ", chapterList);
                bot.notifyAboutNewChapters(msg);
            }
            }
        };

        myTimer.scheduleAtFixedRate(checkChapters , 0L, 10 * (60*1000));

    }

}
