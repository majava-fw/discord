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

import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tech.majava.context.config.Config;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * <p><b>Class {@link tech.majava.discord.commands.config.OptionConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class OptionConfig implements Config {

    private static final long serialVersionUID = -5125170783140956500L;

    @Nonnull
    private String name;

    @Nonnull
    private String description;

    private boolean required = true;

    @Nonnull
    private OptionType type;

    public void setType(@Nonnull String raw) {
        this.type = OptionType.valueOf(raw.toUpperCase(Locale.ROOT));
    }

    @Nonnull
    public OptionData toData() {
        return new OptionData(type, name, description, required);
    }

}
