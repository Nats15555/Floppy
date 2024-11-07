package com.vk.itmo.floppy.service;

import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final static int GIVEAWAY_DURATION = 7200;
    private final static int GIVEAWAY_PRICE = 100;
    private final PlayerRepository playerRepository;

    public boolean isUserExists(long tgUserId) {
        return playerRepository.existsByTgId(tgUserId);
    }

    public void addUser(long tgUserId) {
        Player player = Player.builder()
                .tgId(tgUserId)
                .clicksSinceGiveaway(0)
                .clicksSinceJackpot(0)
                .clicksSinceWinning(0)
                .balance(0)
                .build();

        log.info("Adding user {}", tgUserId);

        playerRepository.save(player);
    }

    public Player getUser(long tgUserId) {
        return playerRepository.findByTgId(tgUserId)
                .orElseThrow();
    }

    public long getBalance(long tgUserId) {
        return playerRepository.findPlayerBalanceByTgId(tgUserId);
    }

    public boolean giveaway(long tgUserId) {
        var player = getUser(tgUserId);
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
        player.setBalance(player.getBalance() + GIVEAWAY_PRICE);
        playerRepository.save(player);
    }
}
