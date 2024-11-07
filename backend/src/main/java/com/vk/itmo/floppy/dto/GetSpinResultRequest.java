package com.vk.itmo.floppy.dto;

public record GetSpinResultRequest(Long userId, Bet bet) {
    public enum Bet {
        ODD,
        EVEN,
        NUMBER,
        RED,
        BLACK,
        ZERO
    }
}
