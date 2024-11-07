package com.vk.itmo.floppy.service;

import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final static int GIVEAWAY_DURATION = 7200;
    private final static int GIVEAWAY_PRICE = 100;

    public boolean isUserExists(long userId) {
        return playerRepository.existsByTgId(userId);
    }

    public void addUser(long userId) {
        Player player = Player.builder()
                .tgId(userId)
                .clicksSinceGiveaway(0)
                .clicksSinceJackpot(0)
                .clicksSinceWinning(0)
                .balance(new BigDecimal(0))
                .build();

        log.info("Adding user {}", userId);

        playerRepository.save(player);
    }

    public Player getUser(long userId) {
        return playerRepository.findByTgId(userId)
                .orElseThrow();
    }

    public boolean giveaway(long userId) {
        var player = getUser(userId);
        var lastGiveawayTime = player.getLastGiveaway();
        if (Objects.isNull(lastGiveawayTime)) {
            takeGiveaway(player);
            return true;
        }
        var duration = Duration.between(LocalDateTime.now(), lastGiveawayTime).abs().getSeconds();
        if (duration > GIVEAWAY_DURATION) {
            takeGiveaway(player);
            return true;
        }
        return false;
    }

    private void takeGiveaway(Player player) {
        player.setLastGiveaway(LocalDateTime.now());
        player.setBalance(player.getBalance().add(new BigDecimal(GIVEAWAY_PRICE)));
        playerRepository.save(player);
    }
}
