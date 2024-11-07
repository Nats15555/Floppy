package com.vk.itmo.floppy.dto;

public record GetRouletteResultStringRequest(long tgUserId, Bet bet, long betAmount) {
    public enum Bet {
        ODD,
        EVEN,
        RED,
        BLACK,
        ZERO
    }
}
