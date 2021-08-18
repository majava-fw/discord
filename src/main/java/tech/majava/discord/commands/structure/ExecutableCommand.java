/*
 *  discord - tech.majava.discord.commands.structure.ExecutableCommand
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.BaseCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import tech.majava.discord.commands.Command;
import tech.majava.discord.commands.io.CommandException;
import tech.majava.discord.commands.io.Response;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link tech.majava.discord.commands.structure.ExecutableCommand}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public class ExecutableCommand {

    @Nonnull
    private final Command<?> command;
    @Nonnull
    private final BaseCommand<?> data;

    @Nonnull
    public ExecutableCommand addOptions(@Nonnull OptionData... options) {
        if (data instanceof CommandData) {
            ((CommandData) data).addOptions(options);
        }
        if (data instanceof SubcommandData) {
            ((SubcommandData) data).addOptions(options);
        }
        return this;
    }

    @Nonnull
    public Response run(@Nonnull SlashCommandEvent event) throws CommandException {
        return command.run(event);
    }

}
