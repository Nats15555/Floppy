package com.vk.itmo.floppy.dto;

public record GetRouletteResultRequest(Long userId, Bet bet) {
    public enum Bet {
        ODD,
        EVEN,
        NUMBER,
        RED,
        BLACK,
        ZERO
    }
}
