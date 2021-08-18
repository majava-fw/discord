/*
 *  discord - tech.majava.discord.commands.registry.CommandPermissions
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

package tech.majava.discord.commands.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p><b>Class {@link CommandPermissions}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class CommandPermissions {

    @Getter
    @Nonnull
    protected final Guild guild;
    @Nonnull
    protected final Map<Long, CommandPrivilege> allowed = new ConcurrentHashMap<>();
    protected final Map<Long, CommandPrivilege> disallowed = new ConcurrentHashMap<>();

    public Collection<CommandPrivilege> getPrivileges() {
        final Collection<CommandPrivilege> privileges = allowed.values();
        privileges.addAll(disallowed.values());
        return privileges;
    }

    @Nonnull
    public CompletableFuture<List<CommandPrivilege>> apply(@Nonnull Command command) {
        return command.updatePrivileges(guild, getPrivileges()).submit();
    }

    public void set(@Nonnull CommandPrivilege.Type type, boolean enabled, long id) {
        final CommandPrivilege privilege = new CommandPrivilege(type, enabled, id);
        if (enabled) {
            allowed.put(id, privilege);
            disallowed.remove(id);
        } else {
            disallowed.put(id, privilege);
            allowed.remove(id);
        }
    }

    public void allow(@Nonnull CommandPrivilege.Type type, long id) {
        set(type, true, id);
    }

    public void allow(@Nonnull Role role) {
        allow(CommandPrivilege.Type.ROLE, role.getIdLong());
    }

    public void allow(@Nonnull User user) {
        allow(CommandPrivilege.Type.USER, user.getIdLong());
    }

    public void deny(@Nonnull CommandPrivilege.Type type, long id) {
        set(type, false, id);
    }

    public void deny(@Nonnull Role role) {
        deny(CommandPrivilege.Type.ROLE, role.getIdLong());
    }

    public void deny(@Nonnull User user) {
        deny(CommandPrivilege.Type.USER, user.getIdLong());
    }

}
