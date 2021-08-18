/*
 *  discord - tech.majava.discord.commands.config.CommandsConfig
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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonMerge;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import tech.majava.context.config.Config;
import tech.majava.discord.commands.Command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * <p><b>Class {@link tech.majava.discord.commands.config.SubcommandConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class SubcommandConfig implements Config {

    private static final long serialVersionUID = -5325869960337124183L;

    @Nonnull
    @JsonMerge
    @JsonAlias("class")
    private Class<Command<?>> command;

    @Nonnull
    @JsonMerge
    private String name;

    @Nonnull
    @JsonMerge
    private String description;

    @Nonnull
    private List<OptionConfig> options = new LinkedList<>();

}
