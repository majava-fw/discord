/*
 *  discord - tech.majava.discord.config.DiscordConfig
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

package tech.majava.discord.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.majksa.commons.majava.context.config.ConfigNode;
import cz.majksa.commons.majava.modules.ModuleConfig;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * <p><b>Class {@link tech.majava.discord.config.DiscordConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class DiscordConfig extends ModuleConfig {

    private final JDAConfig jda;

    public DiscordConfig(@Nonnull ConfigNode node) {
        super(node);
        final Map<?, ?> map = node.get("jda");
        this.jda = new ObjectMapper().convertValue(map, JDAConfig.class);
    }

}
