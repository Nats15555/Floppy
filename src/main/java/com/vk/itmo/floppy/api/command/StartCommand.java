package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.function.Consumer;

@Component(StartCommand.name)
@RequiredArgsConstructor
public class StartCommand implements Command {
    public final static String name = "/start";
    public final static String description = "Запустить бота";
    private final PlayerService playerService;

    @Override
    public void execute(Long userId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        registerUserIfNotExist(userId);
        var message = sendMessageBuilder
                .replyMarkup(keyboardMarkup)
                .text("Привет\\. Это казино\\-бот Floppy\\. Со мной ты сможешь сыграть в разные игры\\.")
                .build();
        sendMessage.accept(message);
    }

    private void registerUserIfNotExist(Long userId) {
        if (!playerService.isUserExists(userId)) {
            playerService.addUser(userId);
        }
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
