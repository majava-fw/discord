/*
 *  discord - tech.majava.discord.commands.Command
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

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import tech.majava.discord.commands.io.CommandException;
import tech.majava.discord.commands.io.Request;
import tech.majava.discord.commands.io.RequestInjector;
import tech.majava.discord.commands.io.Response;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Interface {@link tech.majava.discord.commands.Command}</b></p>
 *
 * @author majksa
 * @version 1.0.1
 * @since 1.0.0
 */
public interface Command<R extends Request> {

    @Nonnull
    Response run(@Nonnull R request) throws CommandException;

    @Nonnull
    default Response run(@Nonnull SlashCommandEvent event) throws CommandException {
        final RequestInjector<R> injector = new RequestInjector<>(getRequestClass());
        return run(injector.inject(event));
    }

    @Nonnull
    Class<R> getRequestClass();

}
