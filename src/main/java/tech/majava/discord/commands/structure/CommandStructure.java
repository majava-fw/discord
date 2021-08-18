/*
 *  discord - tech.majava.discord.commands.structure.CommandStructure
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

import lombok.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import tech.majava.discord.commands.io.CommandException;
import tech.majava.discord.commands.io.Response;
import tech.majava.discord.commands.registry.GlobalCommand;
import tech.majava.discord.commands.registry.GuildCommand;

import javax.annotation.Nonnull;

/**
 * <p><b>Abstract class {@link tech.majava.discord.commands.structure.CommandStructure}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public abstract class CommandStructure {

    @Nonnull
    protected final CommandData data;

    public CommandStructure(@Nonnull String name, @Nonnull String description, boolean defaultEnabled) {
        data = new CommandData(name, description)
                .setDefaultEnabled(defaultEnabled);
    }

    public GlobalCommand toRegistrable(@Nonnull JDA jda) {
        return new GlobalCommand(jda, this);
    }

    public GuildCommand toRegistrable(@Nonnull Guild guild) {
        return new GuildCommand(guild, this);
    }

    public abstract Response run(@Nonnull SlashCommandEvent event) throws CommandException;

}
