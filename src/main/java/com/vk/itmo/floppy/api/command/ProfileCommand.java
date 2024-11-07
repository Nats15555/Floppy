package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.function.Consumer;

@Component(ProfileCommand.name)
@RequiredArgsConstructor
public class ProfileCommand extends Command {
    public final static String name = "Профиль";
    public final static String description = "Посмотреть профиль";

    private final PlayerService playerService;

    @Override
    public void execute(Long userId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var player = playerService.getUser(userId);
        var message = sendMessageBuilder
                .replyMarkup(keyboardMarkup)
                .text("""
                        *Ваш профиль*
                        • Telegram ID : %d
                        • Баланс : ||%s||
                        """.formatted(player.getTgId(), player.getBalance()))
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
