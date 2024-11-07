package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Map;
import java.util.function.Consumer;

@Component(RouletteCommand.name)
@RequiredArgsConstructor
public class RouletteCommand implements GameCommand {
    public final static String name = "Рулетка";
    public final static String description = "Начать играть в игру Рулетка";

    private final PlayerService playerService;

    @Value("${floppy.bot.games.roulette.url}")
    private String url;

    @Override
    public void execute(Long tgUserId,
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

    @Override
    public Double calculateResult(Long tgUserId) {
        Player player = playerService.getUser(tgUserId);


        return 0.0;
    }
}
