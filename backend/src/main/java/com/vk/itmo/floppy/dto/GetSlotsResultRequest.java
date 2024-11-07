package com.vk.itmo.floppy.dto;

public record GetSlotsResultRequest(Long userId, Bet bet) {
    public enum Bet {
        ODD,
        EVEN,
        NUMBER,
        RED,
        BLACK,
        ZERO
    }
}
