/*
 *  discord - tech.majava.discord.commands.management.CommandsListener
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

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import tech.majava.listeners.AbstractListener;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link CommandsListener}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommandsListener extends AbstractListener<SlashCommandEvent> {

    @Nonnull
    private final CommandsManager manager;

    public CommandsListener(@Nonnull CommandsManager manager) {
        super(SlashCommandEvent.class);
        this.manager = manager;
    }

    @Nonnull
    @Override
    public CompletableFuture<Void> run(@Nonnull SlashCommandEvent event) {
        return manager.run(event).thenAccept(message -> {});
    }

    @Override
    public boolean check(@Nonnull SlashCommandEvent event) {
        return manager.exists(event);
    }

}
