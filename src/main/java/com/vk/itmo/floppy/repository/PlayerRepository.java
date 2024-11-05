package com.vk.itmo.floppy.repository;

import com.vk.itmo.floppy.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    boolean existsByTgId(long tgId);
}
