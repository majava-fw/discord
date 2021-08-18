/*
 *  discord - tech.majava.discord.commands.structure.SingleCommand
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

package tech.majava.discord.commands.structure;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tech.majava.discord.commands.Command;
import tech.majava.discord.commands.io.CommandException;
import tech.majava.discord.commands.io.Response;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link SimpleCommand}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleCommand extends CommandStructure {

    @Nonnull
    private final ExecutableCommand command;

    public SimpleCommand(@Nonnull Command<?> command, @Nonnull String name, @Nonnull String description, boolean defaultEnabled) {
        super(name, description, defaultEnabled);
        this.command = new ExecutableCommand(command, getData());
    }

    @Nonnull
    public SimpleCommand addOptions(@Nonnull OptionData... options) {
        data.addOptions(options);
        return this;
    }

    @Nonnull
    @Override
    public Response run(@Nonnull SlashCommandEvent event) throws CommandException {
        return command.run(event);
    }

}
