package com.vk.itmo.floppy.api.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component(HelpCommand.name)
@RequiredArgsConstructor
public class HelpCommand extends Command {
    public final static String name = "/help";
    public final static String description = "Получить список доступных команд";
    private final List<Command> commands;

    @Override
    public void execute(Long userId,
                        SendMessage.SendMessageBuilder sendMessageBuilder,
                        ReplyKeyboardMarkup keyboardMarkup,
                        Consumer<SendMessage> sendMessage) {
        var text = getCommands();
        var message = sendMessageBuilder
                .replyMarkup(keyboardMarkup)
                .text(text)
                .build();
        sendMessage.accept(message);
    }

    private String getCommands() {
        commands.add(this);
        return "*Доступные команды:*\n" + commands.stream()
                .map(this::getPrettyLine)
                .collect(Collectors.joining("\n\n"));
    }

    private String getPrettyLine(Command command) {
        return String.format("• `%s` : %s", command.getName(), command.getDescription());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
