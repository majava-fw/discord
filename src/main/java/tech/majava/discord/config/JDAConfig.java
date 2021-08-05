/*
 *  discord - tech.majava.discord.config.JDAConfig
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

import cz.majksa.commons.majava.context.config.Config;
import cz.majksa.commons.majava.context.config.Methods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.BiFunction;

/**
 * <p><b>Class {@link tech.majava.discord.config.JDAConfig}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JDAConfig implements Config {

    @Setter
    @Nonnull
    private String token;

    @Nonnull
    private Template template = Template.DEFAULT;

    @Nonnull
    private EnumSet<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.DEFAULT);

    @Setter
    @Nullable
    private ActivityConfig activity = null;

    @Nullable
    private Methods modifier = null;

    @Nullable
    public Method getModifier() {
        if (modifier == null) {
            return null;
        }
        return modifier.get(JDABuilder.class.getName());
    }

    public void setTemplate(@Nonnull String name) {
        template = Template.valueOf(name.toUpperCase());
    }

    public void setIntents(int intents) {
        this.intents = GatewayIntent.getIntents(intents);
    }

    @Nonnull
    public JDABuilder toBuilder() {
        final JDABuilder builder = template.createBuilder(token, intents);
        if (activity != null) {
            builder.setActivity(activity.toActivity());
        }
        final Method modifier = getModifier();
        if (modifier != null) {
            try {
                modifier.invoke(null, builder);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return builder;
    }

    @RequiredArgsConstructor
    public enum Template {
        NONE(JDABuilder::create),
        DEFAULT(JDABuilder::createDefault),
        LIGHT(JDABuilder::createLight);

        private final BiFunction<String, Collection<GatewayIntent>, JDABuilder> constuctor;

        public JDABuilder createBuilder(@Nonnull String token, @Nonnull EnumSet<GatewayIntent> intents) {
            return constuctor.apply(token, intents);
        }

    }

}
