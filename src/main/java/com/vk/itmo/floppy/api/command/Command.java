package com.vk.itmo.floppy.api.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.List;
import java.util.function.Consumer;

public interface Command {
    void execute(Long userId,
                 SendMessage.SendMessageBuilder sendMessageBuilder,
                 ReplyKeyboardMarkup keyboardMarkup,
                 Consumer<SendMessage> sendMessage);

    String getName();

    String getDescription();

    default InlineKeyboardMarkup getPlayGameButton(String gameUrl) {
        return new InlineKeyboardMarkup(List.of(new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text("Играть")
                        .webApp(new WebAppInfo(gameUrl))
                        .build()))
        );
    }
}
