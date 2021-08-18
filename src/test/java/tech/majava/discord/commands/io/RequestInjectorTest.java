/*
 *  discord - tech.majava.discord.commands.request.RequestInjectorTest
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

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestInjectorTest {

    @Test
    void testOptionConvertName() {
        final RequestInjector<Request> injector = new RequestInjector<>(Request.class);
        assertEquals("HalloWorld", injector.convertOptionName("hallo-world"));
        assertEquals("HalloWorld", injector.convertOptionName("hallo_world"));
        assertEquals("HalloWorld", injector.convertOptionName("hallo_World"));
        assertEquals("HalloWorld", injector.convertOptionName("Hallo_World"));
        assertEquals("HalloWorld", injector.convertOptionName("HALLO_World"));
    }

    @Test
    void assignability() {
        assertTrue(IMentionable.class.isAssignableFrom(Role.class));
    }

}