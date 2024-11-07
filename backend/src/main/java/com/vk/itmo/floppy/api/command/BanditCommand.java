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
    private final double additionalWinProbability = 0.01;
    private final double additionalJackpotProbability = 0.001;
    private final double playerWinProbability = 0.45;
    private final double playerJackpotProbability = 0.01;



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
        BanditExceedType exceed = calculate(player);
        int bet =50;
        switch (exceed){
            case JACKPOT:
                result[0]=result[1]=result[2]=7;
                playerService.addBalance(tgUserId, player.getBalance()+bet*2);
                break;
            case WIN:
                Random r = new Random();
                result[0]=result[1]=result[2]=r.nextInt(0,6);
                playerService.addBalance(tgUserId, player.getBalance()+bet);
                break;
            case LOSE:
                Random r1 = new Random();
                result[0] = r1.nextInt(0,6);
                result[1] = r1.nextInt(0,6);
                result[2] = r1.nextInt(0,6);
                playerService.addBalance(tgUserId, player.getBalance()-bet);
                break;
        }

        return result;
    }

    private BanditExceedType calculate(Player player){
        var winAttempt = player.getClicksSinceWinning();
        var jackpotAttempt = player.getClicksSinceJackpot();
        Random random = new Random();
        double x = random.nextDouble(0,1);
        double y = random.nextDouble(0,0.5);
        int win = CharacteristicFunction(playerWinProbability, 1.0,
            (x+CharacteristicFunction(4,100, winAttempt)*winAttempt*additionalWinProbability));
        int jackpot = CharacteristicFunction(-100.0, playerJackpotProbability,
            (y-CharacteristicFunction(10,1000,jackpotAttempt)*jackpotAttempt*additionalJackpotProbability));
        if (jackpot == 1){
            return BanditExceedType.JACKPOT;
        }
        if (win == 1){
            return BanditExceedType.WIN;
        }
        return BanditExceedType.LOSE;
    }

    public static int CharacteristicFunction(double lower_bound ,double upper_bound, double value){
        if(lower_bound <= value && value <= upper_bound){
            return 1;
        }
        return 0;
    }

    public enum BanditExceedType{
        LOSE, WIN, JACKPOT;
    }
}
