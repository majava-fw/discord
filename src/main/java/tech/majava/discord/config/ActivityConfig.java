/*
 *  discord - tech.majava.discord.config.ActivityConfig
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Activity;
import tech.majava.context.config.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p><b>Class {@link tech.majava.discord.config.ActivityConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityConfig implements Config {

    private static final long serialVersionUID = -8534799885704610978L;

    @Nonnull
    private Activity.ActivityType type;

    @Nonnull
    private String name;

    @Nullable
    private String url = null;

    public void setType(@Nonnull String rawType) {
        type = Activity.ActivityType.valueOf(rawType.toUpperCase());
    }

    @Nonnull
    public Activity toActivity() {
        return Activity.of(type, name, url);
    }

}
