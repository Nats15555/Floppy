package com.vk.itmo.floppy.api.command;

public interface GameCommand extends Command {
    Double calculateResult(Long tgUserId);
}
