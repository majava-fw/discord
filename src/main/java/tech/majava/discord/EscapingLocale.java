/*
 *  discord - tech.majava.discord.EscapingLocale
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

package tech.majava.discord;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p><b>Class {@link tech.majava.discord.EscapingLocale}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class EscapingLocale {

    /**
     * The text that replaces new lines
     */
    private String newLineReplacement;
    /**
     * The text that replaces apostrophes
     */
    private String apostropheReplacement;

}
