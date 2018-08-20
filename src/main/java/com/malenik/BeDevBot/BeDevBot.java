package com.malenik.BeDevBot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ExecutorService;


public class BeDevBot extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = "646496633:AAHBQt_2Qq74m9UDrU6WqbNGIi4zkxsCW6M";
    private static final String BOT_USERNAME = "BeDevBot";
    private static Collection<String> collection = Arrays.asList("привет", "здарова", "салют", "алоха");

    private ExecutorService executorService;
    private WatsonAssistantService watson;

    public BeDevBot(ExecutorService executorService, WatsonAssistantService watson) {
        this.executorService = executorService;
        this.watson = watson;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        executorService.execute(() -> {
            Message message = update.getMessage();
            String str = message.getText();

            if (str.equals("/start")) {
                sendMsg(message, "Hi! This is BeDevBot which will help you choose your developer path!");
            } else if (collection.stream().anyMatch(str::equalsIgnoreCase)) {
                sendMsg(message, "Ну привет. Чо хотел?");
            } else if (str.equalsIgnoreCase("botname")) {
                sendMsg(message, getBotUsername());
            } else {
                String res = watson.ask(str);
                sendMsg(message, res);
            }
        });
    }

    private void setButtons(SendMessage sendMessage) {

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Eat"));
        keyboardFirstRow.add(new KeyboardButton("Sleep"));
        keyboardFirstRow.add(new KeyboardButton("Rave"));
        keyboardFirstRow.add(new KeyboardButton("Repeat"));

        List<KeyboardRow> keyboardRowList = Collections.singletonList(keyboardFirstRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup()
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false)
                .setKeyboard(keyboardRowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    @SuppressWarnings("deprecation")
    private void sendMsg(Message msg, String text) {
        executorService.execute(() -> {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(msg.getChatId());
            sendMessage.setText(text);
            sendMessage.enableMarkdown(true);
            try {
                setButtons(sendMessage);
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }
}
