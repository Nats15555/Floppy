package com.vk.itmo.floppy.dto;

public record GetSlotsResultResponse(long balance, Integer[][] imageMatrix, Integer[][] winMatrix) {
}
