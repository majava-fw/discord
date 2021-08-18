/*
 *  discord - tech.majava.discord.commands.registry.GlobalCommand
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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import tech.majava.discord.commands.structure.CommandStructure;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p><b>Class {@link GlobalCommand}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class GlobalCommand extends RegistrableCommand {

    @Nonnull
    protected final Map<Guild, CommandPermissions> permissionsMap = new ConcurrentHashMap<>();

    public GlobalCommand(@Nonnull JDA jda, @Nonnull CommandStructure structure) {
        super(jda::upsertCommand, structure);
    }

    @Nonnull
    @Override
    protected Command setPrivileges(@Nonnull Command command) {
        permissionsMap.values()
                .stream()
                .map(commandPermissions -> commandPermissions.apply(command))
                .forEach(CompletableFuture::join);
        return command;
    }

}
