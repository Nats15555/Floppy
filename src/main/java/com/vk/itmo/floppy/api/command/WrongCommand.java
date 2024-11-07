package com.vk.itmo.floppy.api.command;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.function.Consumer;

public class WrongCommand extends Command {
    public final static String name = StringUtils.EMPTY;
    public final static String description = StringUtils.EMPTY;

    @Override
    public void execute(Long userId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var message = sendMessageBuilder
                .replyMarkup(keyboardMarkup)
                .text("Такой команды нет. Попробуй еще")
                .build();
        sendMessage.accept(message);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
