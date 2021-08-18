/*
 *  discord - tech.majava.discord.commands.registry.RegistrableCommand
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

import lombok.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import tech.majava.discord.commands.structure.CommandStructure;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Abstract class {@link RegistrableCommand}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public abstract class RegistrableCommand {

    @Nonnull
    protected final Function<CommandData, CommandCreateAction> createFunction;
    @Nonnull
    protected final CommandStructure structure;

    @Nonnull
    public CompletableFuture<Command> register() {
        return createFunction.apply(structure.getData())
                .submit()
                .thenApply(this::setPrivileges);
    }

    @Nonnull
    protected abstract Command setPrivileges(@Nonnull Command command);

}
