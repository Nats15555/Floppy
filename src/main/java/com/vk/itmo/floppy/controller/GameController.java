package com.vk.itmo.floppy.controller;

import com.vk.itmo.floppy.api.command.GameCommand;
import com.vk.itmo.floppy.dto.*;
import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {
    private final PlayerService playerService;
    private final Map<String, GameCommand> gameCommands;

    @PostMapping("/spin")
    public GetSpinResultResponse getSpinResult(@RequestBody GetSlotsResultRequest request) {
        return new GetSpinResultResponse(request.userId(), 0L, 0L);
    }

    @PostMapping("/roulette")
    public GetRouletteResultResponse getRouletteResult(@RequestBody GetRouletteResultRequest request) {
        return new GetRouletteResultResponse(request.userId(), 0L);
    }

    @PostMapping("/slots")
    public GetSlotsResultResponse getSlotsResult(@RequestBody GetSlotsResultRequest request) {
        return new GetSlotsResultResponse(request.userId(), 0L);
    }
}
