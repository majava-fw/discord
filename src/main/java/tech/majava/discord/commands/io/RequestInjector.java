/*
 *  discord - tech.majava.discord.commands.request.RequestInjector
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

import com.google.common.base.CaseFormat;
import lombok.Data;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link tech.majava.discord.commands.io.RequestInjector}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public final class RequestInjector<R extends Request> {

    private final Class<R> clazz;

    @SneakyThrows
    public R inject(@Nonnull SlashCommandEvent event) {
        final R request = init(event);
        for (OptionMapping option : event.getOptions()) {
            final Method setter = getOptionSetter(option);
            setter.invoke(request, getValue(option, setter.getParameterTypes()[0]));
        }
        return request;
    }

    @Nonnull
    private R init(@Nonnull SlashCommandEvent event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getConstructor(SlashCommandEvent.class).newInstance(event);
    }

    @Nonnull
    private Method getOptionSetter(@Nonnull OptionMapping option) throws NoSuchMethodException {
        final String name = "set" + convertOptionName(option.getName());
        for (Method method : clazz.getMethods()) {
            if (!method.getName().equals(name)) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                continue;
            }
            return method;
        }
        throw new NoSuchMethodException(name);
    }

    public String convertOptionName(@Nonnull String text) {
        final String convert = CaseFormat.LOWER_UNDERSCORE
                .converterTo(CaseFormat.LOWER_HYPHEN)
                .convert(text);
        return CaseFormat.LOWER_HYPHEN
                .converterTo(CaseFormat.UPPER_CAMEL)
                .convert(convert);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(@Nonnull OptionMapping option, @Nonnull Class<T> clazz) {
        switch (option.getType()) {
            case ROLE:
                check(clazz, Role.class);
                return (T) option.getAsRole();
            case USER:
                check(clazz, Member.class, User.class);
                if (clazz.isAssignableFrom(Member.class)) {
                    return (T) option.getAsMember();
                }
                if (clazz.isAssignableFrom(User.class)) {
                    return (T) option.getAsUser();
                }
                break;
            case STRING:
                check(clazz, String.class);
                return (T) option.getAsString();
            case BOOLEAN:
                check(clazz, Boolean.class);
                return (T) Boolean.valueOf(option.getAsBoolean());
            case CHANNEL:
                check(clazz, MessageChannel.class, GuildChannel.class);
                if (clazz.isAssignableFrom(MessageChannel.class)) {
                    return (T) option.getAsMessageChannel();
                }
                if (clazz.isAssignableFrom(GuildChannel.class)) {
                    return (T) option.getAsGuildChannel();
                }
                break;
            case INTEGER:
                check(clazz, Integer.class, Long.class);
                if (clazz.isAssignableFrom(Integer.class)) {
                    return (T) Integer.valueOf((int) option.getAsLong());
                }
                if (clazz.isAssignableFrom(Long.class)) {
                    return (T) Long.valueOf(option.getAsLong());
                }
                break;
            case MENTIONABLE:
                check(clazz, IMentionable.class);
                return (T) option.getAsMentionable();
        }
        throw new NullPointerException();
    }

    private void check(@Nonnull Class<?> clazz, @Nonnull Class<?>... allowed) {
        for (Class<?> aClass : allowed) {
            if (clazz.isAssignableFrom(aClass)) {
                return;
            }
        }
        throw new IllegalArgumentException(clazz + " must implement one of the following: " + Arrays.stream(allowed).map(Class::getName).collect(Collectors.joining(", ")));
    }

}
