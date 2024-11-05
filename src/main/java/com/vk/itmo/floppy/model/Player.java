package com.vk.itmo.floppy.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private long tgId;
    private long clicksSinceJackpot;
    private long clicksSinceWinning;
    private long clicksSinceGiveaway;
    private BigDecimal balance;
    private LocalDateTime lastGiveaway;
}
