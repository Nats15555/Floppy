package com.vk.itmo.floppy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private long tgId;
    private long clicksSinceJackpot;
    private long clicksSinceWinning;
    private long clicksSinceGiveaway;
    private long balance;
    private LocalDateTime lastGiveaway;
}
