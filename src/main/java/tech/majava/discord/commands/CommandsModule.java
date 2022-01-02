/*
 *  discord - tech.majava.discord.DiscordModule
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

package tech.majava.discord.commands;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import tech.majava.context.ApplicationContext;
import tech.majava.discord.DiscordModule;
import tech.majava.discord.commands.config.CommandConfig;
import tech.majava.discord.commands.config.CommandsConfig;
import tech.majava.discord.commands.config.ConfigStructureConvertor;
import tech.majava.discord.commands.management.CommandsListener;
import tech.majava.discord.commands.management.CommandsManager;
import tech.majava.discord.components.ComponentsModule;
import tech.majava.listeners.EntryPoint;
import tech.majava.logging.LoggingModule;
import tech.majava.modules.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link tech.majava.discord.commands.CommandsModule}</b></p>
 *
 * @author majksa
 * @version 1.0.1
 * @since 1.0.0
 */
@Getter
public final class CommandsModule extends Module<CommandsConfig> {

    private final CommandsManager manager;
    private final ConfigStructureConvertor convertor;
    private DiscordModule discordModule;
    private EntryPoint<SlashCommandEvent> listener;

    public CommandsModule(@Nonnull CommandsConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "discord-commands", "discord commands module for handling discord components in majava");
        dependencies.add(LoggingModule.class);
        dependencies.add(DiscordModule.class);
        dependencies.add(ComponentsModule.class);
        convertor = new ConfigStructureConvertor();
        context.getContainer().register(manager = new CommandsManager(context));
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> {
            discordModule = context.getModules().get(DiscordModule.class);
            if (config.isTruncate()) {
                manager.clear(discordModule.getJda()).join();
                discordModule.getJda().getGuilds()
                        .stream()
                        .filter(guild -> config.getGuild().containsKey(guild.getIdLong()))
                        .map(manager::clear)
                        .forEach(CompletableFuture::join);
            }
            listener = discordModule.getListeners().loadListener(new CommandsListener(manager));
            listener.register();
            register(config.getGlobal(), null).join();
            config.getGuild().entrySet().stream()
                    .map(entry -> {
                        final Guild guild = discordModule.getJda().getGuildById(entry.getKey());
                        Objects.requireNonNull(guild);
                        return register(entry.getValue(), guild);
                    })
                    .forEach(CompletableFuture::join);
        });
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> listener.unregister());
    }

    private CompletableFuture<List<Command>> register(@Nonnull List<CommandConfig> commands, @Nullable Guild guild) {
        return CompletableFuture.supplyAsync(() -> commands.stream()
                .map(command -> convertor.convert(context.getContainer(), command))
                .map(structure -> {
                    if (guild == null) {
                        return structure.toRegistrable(discordModule.getJda());
                    }
                    return structure.toRegistrable(guild);
                })
                .map(manager::register)
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }

}
