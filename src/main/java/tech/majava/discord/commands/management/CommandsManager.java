/*
 *  discord - tech.majava.discord.commands.management.CommandsManager
 *  Copyright (C) 2021  Majksa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package tech.majava.discord.commands.management;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import tech.majava.context.ApplicationContext;
import tech.majava.discord.commands.io.CommandException;
import tech.majava.discord.commands.io.Response;
import tech.majava.discord.commands.io.ResponseBuilder;
import tech.majava.discord.commands.registry.RegistrableCommand;
import tech.majava.discord.commands.structure.CommandStructure;
import tech.majava.discord.responses.MessageTemplate;
import tech.majava.discord.responses.MessageTemplateImpl;
import tech.majava.logging.LoggingModule;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link tech.majava.discord.commands.management.CommandsManager}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class CommandsManager {

    public static final @NotNull MessageTemplate INTERNAL_ERROR_TEMPLATE = new MessageTemplateImpl().append(":exclamation:").append("Internal error occurred");

    private final Map<Long, RegistrableCommand> commands = new ConcurrentHashMap<>();

    @Nonnull
    private ApplicationContext context;

    public CompletableFuture<Message> run(@Nonnull SlashCommandEvent event) {
        final Response response = runCommand(event, getCommand(event));
        return event.deferReply(response.getEphemeral().join())
                .submit()
                .thenCompose(response::send);
    }

    public boolean exists(@Nonnull SlashCommandEvent event) {
        return commands.containsKey(event.getCommandIdLong());
    }

    @Nonnull
    public CommandStructure getCommand(@Nonnull SlashCommandEvent event) {
        return commands.get(event.getCommandIdLong()).getStructure();
    }

    @Nonnull
    public CompletableFuture<List<Command>> clear(@Nonnull JDA jda) {
        return jda.updateCommands().submit();
    }

    @Nonnull
    public CompletableFuture<List<Command>> clear(@Nonnull Guild guild) {
        return guild.updateCommands().submit();
    }

    @Nonnull
    public CompletableFuture<List<Command>> register(@Nonnull RegistrableCommand... command) {
        return CompletableFuture.supplyAsync(() -> Arrays.stream(command)
                .map(this::register)
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }

    @Nonnull
    public CompletableFuture<Command> register(@Nonnull RegistrableCommand command) {
        return command.register()
                .thenApply(command1 -> {
                    commands.put(command1.getIdLong(), command);
                    return command1;
                });
    }

    @Nonnull
    private Response runCommand(@Nonnull SlashCommandEvent event, @Nonnull CommandStructure command) {
        final ResponseBuilder responseBuilder = new ResponseBuilder();
        try {
            return command.run(event);
        } catch (CommandException e) {
            responseBuilder.setTemplate(() -> new MessageTemplateImpl().append(":exclamation:").append(e.getMessage()));
            responseBuilder.setEphemeral(e.isEphemeral());
        } catch (Throwable throwable) {
            context.getModules().get(LoggingModule.class).log(throwable);
            responseBuilder.setTemplate(INTERNAL_ERROR_TEMPLATE);
            responseBuilder.setEphemeral(true);
        }
        return responseBuilder.build();
    }

}
