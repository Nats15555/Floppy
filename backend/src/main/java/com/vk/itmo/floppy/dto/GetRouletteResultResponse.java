package com.vk.itmo.floppy.dto;

public record GetRouletteResultResponse(long balance, int winNumber, Color winColor) {
    public enum Color {
        RED,
        BLACK,
        GREEN
    }
}
