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

    public Pair<Integer[][], Integer[][]> calculateResult(Long tgUserId, long betAmount) {
        Player player = playerService.getUser(tgUserId);
        Random random = new Random();

        SlotsWinType winType = calculateWin(player);

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

    private SlotsWinType calculateWin(Player player) {
        Random r = new Random();
        double x = r.nextDouble(0, 0.5);
        double y = r.nextDouble(0, 0.45);
        double z = r.nextDouble(0, 0.5);

        double additionalWinProbability = 0.01;
        double additionalJackpotProbability = 0.01;

        double l = x + y +
                BanditCommand.CharacteristicFunction(5, 1000, player.getClicksSinceWinning()) * player.getClicksSinceWinning() * additionalWinProbability
                + BanditCommand.CharacteristicFunction(7, 1000, player.getClicksSinceJackpot()) * player.getClicksSinceJackpot() * additionalJackpotProbability;

        if (l <= 0.65) {
            if (x <= 0.25) {
                return SlotsWinType.ZERO;
            }
            if (y <= 0.25) {
                return SlotsWinType.QUARTER;
            }
            if (x + y <= 0.5) {
                return SlotsWinType.THREE_HALVES;
            }

            return SlotsWinType.BET;
        }

        if (l <= 0.9) {
            if (z <= 0.5) {
                return SlotsWinType.BET_AND_HALF;
            }

            return SlotsWinType.TWO_BET;
        }

        if (l <= 0.98) {
            return SlotsWinType.TEN_BET;
        }

        return SlotsWinType.HUNDRED_BET;
    }

    public enum SlotsWinType {
        ZERO,
        QUARTER,
        THREE_HALVES,
        BET,
        BET_AND_HALF,
        TWO_BET,
        TEN_BET,
        HUNDRED_BET
    }
}
