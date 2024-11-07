package com.vk.itmo.floppy.controller;

import com.vk.itmo.floppy.api.command.BanditCommand;
import com.vk.itmo.floppy.api.command.RouletteCommand;
import com.vk.itmo.floppy.api.command.SlotsCommand;
import com.vk.itmo.floppy.dto.*;
import com.vk.itmo.floppy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {
    private final PlayerService playerService;
    private final BanditCommand banditCommand;
    private final RouletteCommand rouletteCommand;
    private final SlotsCommand slotsCommand;

    @PostMapping("/bandit")
    public GetBanditResultResponse getBanditResult(@RequestBody GetBanditResultRequest request) {
        Integer[] result = banditCommand.calculateResult(request.tgUserId());
        long balance = playerService.getBalance(request.tgUserId());

        return new GetBanditResultResponse(balance, result);
    }

    @PostMapping("/roulette/string")
    public GetRouletteResultResponse getRouletteResult(@RequestBody GetRouletteResultStringRequest request) {
        Pair<Integer, GetRouletteResultResponse.Color> result = rouletteCommand.calculateResult(request.tgUserId(), request.bet());
        long balance = playerService.getBalance(request.tgUserId());

        return new GetRouletteResultResponse(balance, result.getLeft(), result.getRight());
    }

    @PostMapping("/roulette/number")
    public GetRouletteResultResponse getRouletteResult(@RequestBody GetRouletteResultNumberRequest request) {
        Pair<Integer, GetRouletteResultResponse.Color> result = rouletteCommand.calculateResult(request.tgUserId(), request.bet());
        long balance = playerService.getBalance(request.tgUserId());

        return new GetRouletteResultResponse(balance, result.getLeft(), result.getRight());
    }

    @PostMapping("/slots")
    public GetSlotsResultResponse getSlotsResult(@RequestBody GetSlotsResultRequest request) {
        Pair<Integer[][], Integer[][]> result = slotsCommand.calculateResult(request.tgUserId());
        long balance = playerService.getBalance(request.tgUserId());

        return new GetSlotsResultResponse(balance, result.getLeft(), result.getRight());
    }
}
