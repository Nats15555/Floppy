package com.vk.itmo.floppy.api.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.function.Consumer;

@Component(RouletteCommand.name)
public class RouletteCommand implements Command {
    public final static String name = "Рулетка";
    public final static String description = "Начать играть в игру Рулетка";

    @Value("${floppy.bot.games.roulette.url}")
    private String url;

    @Override
    public void execute(Long userId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var playButton = getPlayGameButton(url);
        var message = sendMessageBuilder
                .text("Игра Рулетка")
                .replyMarkup(playButton)
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
