/*
 *  discord - tech.majava.discord.commands.structure.Group
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.data.SerializableData;
import tech.majava.discord.commands.Command;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * <p><b>Class {@link tech.majava.discord.commands.structure.Group}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Group<T extends SerializableData> {

    @Nonnull
    private final T data;
    @Nonnull
    private final Map<String, ExecutableCommand> subcommands = new ConcurrentHashMap<>();

    @Nonnull
    public ExecutableCommand addSubcommand(@Nonnull Command<?> command, @Nonnull String name, @Nonnull String description) {
        final SubcommandData data = new SubcommandData(name, description);
        final ExecutableCommand executableCommand = new ExecutableCommand(command, data);
        subcommands.put(name, executableCommand);
        useData(commandData -> commandData.addSubcommands(data), groupData -> groupData.addSubcommands(data));
        return executableCommand;
    }

    @Nonnull
    public Group<T> addSubcommand(@Nonnull Command<?> command, @Nonnull String name, @Nonnull String description, @Nonnull Consumer<ExecutableCommand> callback) {
        callback.accept(addSubcommand(command, name, description));
        return this;
    }

    public ExecutableCommand getSubcommand(@Nonnull String name) {
        return subcommands.get(name);
    }

    private void useData(@Nonnull Consumer<CommandData> ifCommand, @Nonnull Consumer<SubcommandGroupData> ifGroup) {
        if (data instanceof CommandData) {
            ifCommand.accept((CommandData) data);
        } else if (data instanceof SubcommandGroupData) {
            ifGroup.accept((SubcommandGroupData) data);
        } else {
            throw new ClassCastException("Data must be either " + CommandData.class + " or " + SubcommandGroupData.class);
        }
    }

}
