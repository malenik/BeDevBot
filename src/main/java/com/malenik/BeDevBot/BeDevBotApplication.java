package com.malenik.BeDevBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class BeDevBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeDevBotApplication.class, args);

        ApiContextInitializer.init();

        TelegramBotsApi botapi = new TelegramBotsApi();

        ExecutorService executorService = Executors.newCachedThreadPool();
        WatsonAssistantService watsonAssistantService = new WatsonAssistantService();
        BeDevBot beDevBot = new BeDevBot(executorService, watsonAssistantService);

        try {
            botapi.registerBot(beDevBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
