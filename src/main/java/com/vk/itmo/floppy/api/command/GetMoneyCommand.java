package com.vk.itmo.floppy.api.command;

import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Component(GetMoneyCommand.name)
@RequiredArgsConstructor
public class GetMoneyCommand implements Command {
    public final static String name = "Получить монеты";
    public final static String description = "Получить бесплатные монеты";

    private final PlayerService playerService;

    @Override
    public void execute(Long tgUserId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {

        var player = playerService.getUser(tgUserId);
        var playerInitBalance = player.getBalance();
        var wasGiveaway = playerService.giveaway(tgUserId);

        var message = sendMessageBuilder.replyMarkup(keyboardMarkup);
        if (wasGiveaway) {
            var playerAfterGiveawayBalance = playerService.getUser(tgUserId).getBalance();
            message.text("""
                    Поздравляю\\!
                    Вы получили %s монет
                    Теперь ваш баланс: ||%s||
                    """.formatted(playerAfterGiveawayBalance - playerInitBalance, playerAfterGiveawayBalance));
        } else {
            var timeLeft = Duration.between(LocalDateTime.now(), player.getLastGiveaway().plusHours(2)).abs().toSeconds();
            message.text("""
                    Еще рано забирать подарок, ты сможешь забрать его не раньше чем через %s секунд
                    """.formatted(timeLeft));
        }
        sendMessage.accept(message.build());
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
