package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Random;
import java.util.function.Consumer;

@Component(SlotsCommand.name)
@RequiredArgsConstructor
public class SlotsCommand implements Command {
    public final static String name = "Слоты";
    public final static String description = "Начать играть в игру Слоты";

    private final PlayerService playerService;

    @Value("${floppy.bot.games.slots.url}")
    private String url;

    @Override
    public void execute(Long tgUserId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var playButton = getPlayGameButton(url + "/" + tgUserId + "/" + playerService.getBalance(tgUserId));
        var message = sendMessageBuilder
                .text("Игра Слоты")
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

    public Pair<Integer[][], Integer[][]> calculateResult(Long tgUserId) {
        Player player = playerService.getUser(tgUserId);
        Random random = new Random();

        Integer[][] imageMatrix = new Integer[3][5];
        Integer[][] winMatrix = new Integer[3][5];

        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                int resultIndex = random.nextInt(0, 5);
                imageMatrix[i][j] = resultIndex;
            }
        }

        for (int i = 0; i < winMatrix.length; i++) {
            for (int j = 0; j < winMatrix[i].length; j++) {
                if (random.nextBoolean()) {
                    winMatrix[i][j] = 1;
                } else {
                    winMatrix[i][j] = 0;
                }
            }
        }

        // TODO handle winning

        return Pair.of(imageMatrix, winMatrix);
    }
}
