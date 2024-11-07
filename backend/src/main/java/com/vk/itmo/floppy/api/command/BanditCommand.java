package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

@Component(BanditCommand.name)
@RequiredArgsConstructor
public class BanditCommand implements Command {
    public final static String name = "Однорукий бандит";
    public final static String description = "Начать играть в игру Бандит";

    private final PlayerService playerService;

    @Value("${floppy.bot.games.bandit.url}")
    private String url;

    @Override
    public void execute(Long tgUserId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var playButton = getPlayGameButton(url + "/" + tgUserId + "/" + playerService.getBalance(tgUserId));
        var message = sendMessageBuilder
                .text("Игра Однорукий Бандит")
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

    public Integer[] calculateResult(Long tgUserId) {
        Player player = playerService.getUser(tgUserId);
        Integer[] result = new Integer[3];
        Random random = new Random();

        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(0, 6);
        }

        if (Arrays.stream(result).distinct().count() == 1) {
            long newBalance = player.getBalance() + 50;
            playerService.addBalance(tgUserId, newBalance);

        }

        return result;
    }
}
