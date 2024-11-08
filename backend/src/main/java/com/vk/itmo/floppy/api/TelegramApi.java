package com.vk.itmo.floppy.api;

import com.vk.itmo.floppy.api.command.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TelegramApi implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String token;
    private final ReplyKeyboardMarkup menuKeyBoard;
    private final Map<String, Command> commandMap;

    public TelegramApi(@Value("${floppy.bot.token.telegram}") String token, Map<String, Command> commandMap) {
        this.token = token;
        this.telegramClient = new OkHttpTelegramClient(token);
        this.menuKeyBoard = initMenuKeyboard();
        this.commandMap = commandMap;
    }

    private ReplyKeyboardMarkup initMenuKeyboard() {
        return new ReplyKeyboardMarkup(List.of(
                new KeyboardRow(new KeyboardButton(BanditCommand.name), new KeyboardButton(SlotsCommand.name)),
                new KeyboardRow(new KeyboardButton(RouletteCommand.name)),
                new KeyboardRow(new KeyboardButton(ProfileCommand.name), new KeyboardButton(GetMoneyCommand.name))
        ));
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var userInput = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            long userId = update.getMessage().getFrom().getId();

            processCommand(userInput, chatId, userId);
        }
    }

    private void processCommand(String userInput, Long chatId, Long userId) {
        commandMap.getOrDefault(userInput, new WrongCommand())
                .execute(userId, sendMessageBuilder(chatId), menuKeyBoard, this::sendMessage);
    }

    private void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to consume", e);
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }

    private SendMessageBuilder sendMessageBuilder(Long chatId) {
        return SendMessage.builder()
                .parseMode("MARKDOWNV2")
                .chatId(chatId);
    }

}
