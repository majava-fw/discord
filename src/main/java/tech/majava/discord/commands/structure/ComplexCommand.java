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
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import tech.majava.discord.commands.Command;
import tech.majava.discord.commands.io.CommandException;
import tech.majava.discord.commands.io.Response;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * <p><b>Class {@link tech.majava.discord.commands.structure.ComplexCommand}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ComplexCommand extends CommandStructure {

    private final Group<CommandData> rootGroup;
    private final Map<String, Group<SubcommandGroupData>> subcommandGroups = new ConcurrentHashMap<>();

    public ComplexCommand(@Nonnull String name, @Nonnull String description, boolean defaultEnabled) {
        super(name, description, defaultEnabled);
        rootGroup = new Group<>(getData());
    }

    @Nonnull
    public ExecutableCommand addSubcommand(@Nonnull Command<?> command, @Nonnull String name, @Nonnull String description) {
        return rootGroup.addSubcommand(command, name, description);
    }

    @Nonnull
    public ComplexCommand addSubcommand(@Nonnull Command<?> command, @Nonnull String name, @Nonnull String description, @Nonnull Consumer<ExecutableCommand> callback) {
        callback.accept(addSubcommand(command, name, description));
        return this;
    }

    @Nonnull
    public Group<SubcommandGroupData> addSubcommandGroup(@Nonnull String name, @Nonnull String description) {
        final SubcommandGroupData groupData = new SubcommandGroupData(name, description);
        final Group<SubcommandGroupData> group = new Group<>(groupData);
        subcommandGroups.put(name, group);
        data.addSubcommandGroups(groupData);
        return group;
    }

    @Nonnull
    public ComplexCommand addSubcommandGroup(@Nonnull String name, @Nonnull String description, @Nonnull Consumer<Group<SubcommandGroupData>> callback) {
        callback.accept(addSubcommandGroup(name, description));
        return this;
    }

    @Nonnull
    @Override
    public Response run(@Nonnull SlashCommandEvent event) throws CommandException {
        final Group<?> group;
        if (event.getSubcommandGroup() == null) {
            group = rootGroup;
        } else {
            group = subcommandGroups.get(event.getSubcommandGroup());
        }
        final ExecutableCommand command = group.getSubcommand(Objects.requireNonNull(event.getSubcommandName()));
        return command.run(event);
    }

}
