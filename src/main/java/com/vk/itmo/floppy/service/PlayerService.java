package com.vk.itmo.floppy.service;

import com.vk.itmo.floppy.model.Player;
import com.vk.itmo.floppy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

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
}
