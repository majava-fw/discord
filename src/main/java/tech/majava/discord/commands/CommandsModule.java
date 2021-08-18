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
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import tech.majava.context.ApplicationContext;
import tech.majava.discord.DiscordModule;
import tech.majava.discord.commands.management.CommandsListener;
import tech.majava.discord.commands.management.CommandsManager;
import tech.majava.discord.components.ComponentsConfig;
import tech.majava.discord.components.ComponentsModule;
import tech.majava.listeners.EntryPoint;
import tech.majava.modules.Module;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link tech.majava.discord.commands.CommandsModule}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public final class CommandsModule extends Module<ComponentsConfig> {

    private DiscordModule discordModule;
    private EntryPoint<SlashCommandEvent> listener;
    private CommandsManager manager;

    public CommandsModule(@Nonnull ComponentsConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "discord-commands", "discord commands module for handling discord components in majava");
        dependencies.add(DiscordModule.class);
        dependencies.add(ComponentsModule.class);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> {
            discordModule = context.getModules().get(DiscordModule.class);
            manager = new CommandsManager();
            manager.clear(discordModule.getJda());
            listener = discordModule.getListeners().loadListener(new CommandsListener(manager));
            listener.register();
        });
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> listener.unregister());
    }

}
