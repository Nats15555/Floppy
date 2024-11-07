package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.dto.GetRouletteResultResponse;
import com.vk.itmo.floppy.dto.GetRouletteResultStringRequest;
import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

@Component(RouletteCommand.name)
@RequiredArgsConstructor
public class RouletteCommand implements Command {
    public final static String name = "Рулетка";
    public final static String description = "Начать играть в игру Рулетка";

    private final static Map<Integer, GetRouletteResultResponse.Color> NUMBER_COLOR_MAP = Map.ofEntries(
            Map.entry(0, GetRouletteResultResponse.Color.GREEN),
            Map.entry(1, GetRouletteResultResponse.Color.RED),
            Map.entry(2, GetRouletteResultResponse.Color.BLACK),
            Map.entry(3, GetRouletteResultResponse.Color.RED),
            Map.entry(4, GetRouletteResultResponse.Color.BLACK),
            Map.entry(5, GetRouletteResultResponse.Color.RED),
            Map.entry(6, GetRouletteResultResponse.Color.BLACK),
            Map.entry(7, GetRouletteResultResponse.Color.RED),
            Map.entry(8, GetRouletteResultResponse.Color.BLACK),
            Map.entry(9, GetRouletteResultResponse.Color.RED),
            Map.entry(10, GetRouletteResultResponse.Color.BLACK),
            Map.entry(11, GetRouletteResultResponse.Color.BLACK),
            Map.entry(12, GetRouletteResultResponse.Color.RED),
            Map.entry(13, GetRouletteResultResponse.Color.BLACK),
            Map.entry(14, GetRouletteResultResponse.Color.RED),
            Map.entry(15, GetRouletteResultResponse.Color.BLACK),
            Map.entry(16, GetRouletteResultResponse.Color.RED),
            Map.entry(17, GetRouletteResultResponse.Color.BLACK),
            Map.entry(18, GetRouletteResultResponse.Color.RED),
            Map.entry(19, GetRouletteResultResponse.Color.BLACK),
            Map.entry(20, GetRouletteResultResponse.Color.RED),
            Map.entry(21, GetRouletteResultResponse.Color.BLACK),
            Map.entry(22, GetRouletteResultResponse.Color.RED),
            Map.entry(23, GetRouletteResultResponse.Color.BLACK),
            Map.entry(24, GetRouletteResultResponse.Color.RED),
            Map.entry(25, GetRouletteResultResponse.Color.BLACK),
            Map.entry(26, GetRouletteResultResponse.Color.RED),
            Map.entry(27, GetRouletteResultResponse.Color.BLACK),
            Map.entry(28, GetRouletteResultResponse.Color.RED),
            Map.entry(29, GetRouletteResultResponse.Color.BLACK),
            Map.entry(30, GetRouletteResultResponse.Color.RED),
            Map.entry(31, GetRouletteResultResponse.Color.BLACK),
            Map.entry(32, GetRouletteResultResponse.Color.RED),
            Map.entry(33, GetRouletteResultResponse.Color.BLACK),
            Map.entry(34, GetRouletteResultResponse.Color.RED),
            Map.entry(35, GetRouletteResultResponse.Color.BLACK),
            Map.entry(36, GetRouletteResultResponse.Color.RED)
    );

    private final PlayerService playerService;

    @Value("${floppy.bot.games.roulette.url}")
    private String url;

    @Override
    public void execute(Long tgUserId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var playButton = getPlayGameButton(url + "/" + tgUserId + "/" + playerService.getBalance(tgUserId));
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

    public Pair<Integer, GetRouletteResultResponse.Color> calculateResult(Long tgUserId, GetRouletteResultStringRequest.Bet bet, long betAmount) {
        Player player = playerService.getUser(tgUserId);
        Random random = new Random();

        int resultNumber = random.nextInt(0, NUMBER_COLOR_MAP.size());
        GetRouletteResultResponse.Color resultColor = NUMBER_COLOR_MAP.get(resultNumber);

        switch (bet) {
            case BLACK -> {
                if (resultColor.equals(GetRouletteResultResponse.Color.BLACK)) {
                    addBalance(player, 2, betAmount);
                } else {
                    subtractBalance(player, betAmount);
                }
            }
            case RED -> {
                if (resultColor.equals(GetRouletteResultResponse.Color.RED)) {
                    addBalance(player, 2, betAmount);
                } else {
                    subtractBalance(player, betAmount);
                }
            }
            case ODD -> {
                if (resultNumber % 2 != 0) {
                    addBalance(player, 10, betAmount);
                } else {
                    subtractBalance(player, betAmount);
                }
            }
            case EVEN -> {
                if (resultNumber % 2 == 0) {
                    addBalance(player, 10, betAmount);
                } else {
                    subtractBalance(player, betAmount);
                }
            }
            case ZERO -> {
                if (resultNumber == 0) {
                    addBalance(player, 100, betAmount);
                } else {
                    subtractBalance(player, betAmount);
                }
            }
        }

        return Pair.of(resultNumber, resultColor);
    }

    public Pair<Integer, GetRouletteResultResponse.Color> calculateResult(Long tgUserId, int bet, long betAmount) {
        Player player = playerService.getUser(tgUserId);
        Random random = new Random();

        int resultNumber = random.nextInt(0, NUMBER_COLOR_MAP.size());
        GetRouletteResultResponse.Color resultColor = NUMBER_COLOR_MAP.get(resultNumber);

        if (resultNumber == bet) {
            addBalance(player, 100, betAmount);
        } else {
            subtractBalance(player, betAmount);
        }

        return Pair.of(resultNumber, resultColor);
    }

    private void addBalance(Player player, int multiplier, long betAmount) {
        long newBalance = player.getBalance() + multiplier * betAmount;
        playerService.setBalance(player.getTgId(), newBalance);
    }

    private void subtractBalance(Player player, long betAmount) {
        long newBalance = player.getBalance() - betAmount;
        playerService.setBalance(player.getTgId(), newBalance);
    }
}
