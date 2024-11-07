package com.vk.itmo.floppy.dto;

public record GetRouletteResultStringRequest(long tgUserId, Bet bet) {
    public enum Bet {
        ODD,
        EVEN,
        RED,
        BLACK,
        ZERO
    }
}
