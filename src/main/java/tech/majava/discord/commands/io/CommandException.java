/*
 *  discord - tech.majava.discord.commands.request.CommandException
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

package tech.majava.discord.commands.io;

import lombok.Getter;

import javax.annotation.Nonnull;

/**
 * <p><b>Exception {@link tech.majava.discord.commands.io.CommandException}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class CommandException extends Exception {

    private static final long serialVersionUID = 7702892504816479375L;

    private final boolean ephemeral;

    public CommandException(@Nonnull String message, boolean ephemeral) {
        super(message);
        this.ephemeral = ephemeral;
    }

}
