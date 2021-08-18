/*
 *  discord - tech.majava.discord.commands.registry.GuildCommand
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

package tech.majava.discord.commands.registry;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import tech.majava.discord.commands.structure.CommandStructure;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link GuildCommand}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class GuildCommand extends RegistrableCommand {

    @Nonnull
    protected final CommandPermissions permissions;

    public GuildCommand(@Nonnull Guild guild, @Nonnull CommandStructure structure) {
        super(guild::upsertCommand, structure);
        permissions = new CommandPermissions(guild);
    }

    @Nonnull
    @Override
    protected Command setPrivileges(@Nonnull Command command) {
        permissions.apply(command).join();
        return command;
    }

}
