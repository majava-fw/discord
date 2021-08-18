/*
 *  discord - tech.majava.discord.commands.config.ConfigStructureConvertor
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

package tech.majava.discord.commands.config;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tech.majava.di.Container;
import tech.majava.discord.commands.Command;
import tech.majava.discord.commands.structure.CommandStructure;
import tech.majava.discord.commands.structure.ComplexCommand;
import tech.majava.discord.commands.structure.SimpleCommand;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Class {@link tech.majava.discord.commands.config.ConfigStructureConvertor}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigStructureConvertor {

    private static final Map<Class<?>, OptionType> classes = new LinkedHashMap<>();

    static {
        classes.put(String.class, OptionType.STRING);
        classes.put(Integer.class, OptionType.INTEGER);
        classes.put(Long.class, OptionType.INTEGER);
        classes.put(Member.class, OptionType.USER);
        classes.put(User.class, OptionType.USER);
        classes.put(Role.class, OptionType.ROLE);
        classes.put(MessageChannel.class, OptionType.CHANNEL);
        classes.put(GuildChannel.class, OptionType.CHANNEL);
    }

    @Nonnull
    public CommandStructure convert(@Nonnull Container container, @Nonnull CommandConfig config) {
        if (config.getCommand() == null) {
            return convertComplex(container, config);
        }
        return convertSimple(container, config);
    }

    private SimpleCommand convertSimple(@Nonnull Container container, @Nonnull CommandConfig config) {
        assert config.getCommand() != null;
        final Command<?> command = container.get(config.getCommand());
        final SimpleCommand simpleCommand = new SimpleCommand(command, config.getName(), config.getDescription(), config.isDefaultEnabled());
        simpleCommand.addOptions(convertOptions(config.getOptions()));
        return simpleCommand;
    }

    private ComplexCommand convertComplex(@Nonnull Container container, @Nonnull CommandConfig config) {
        final ComplexCommand complexCommand = new ComplexCommand(config.getName(), config.getDescription(), config.isDefaultEnabled());
        config.getSubcommands().forEach(subcommandConfig -> complexCommand.addSubcommand(
                container.get(subcommandConfig.getCommand()),
                subcommandConfig.getName(),
                subcommandConfig.getDescription(),
                subcommand -> subcommand.addOptions(convertOptions(subcommandConfig.getOptions()))
        ));
        config.getGroups().forEach(groupConfig -> complexCommand.addSubcommandGroup(
                groupConfig.getName(),
                groupConfig.getDescription(),
                group -> groupConfig.getSubcommands().forEach(subcommandConfig -> group.addSubcommand(
                        container.get(subcommandConfig.getCommand()),
                        subcommandConfig.getName(),
                        subcommandConfig.getDescription(),
                        subcommand -> subcommand.addOptions(convertOptions(subcommandConfig.getOptions()))
                ))
        ));
        return complexCommand;
    }

    @Nonnull
    private OptionData[] convertOptions(@Nonnull List<OptionConfig> options) {
        return options.stream().map(OptionConfig::toData).toArray(OptionData[]::new);
    }

}
