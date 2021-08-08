/*
 *  discord - tech.majava.discord.DiscordModule
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

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import tech.majava.context.ApplicationContext;
import tech.majava.discord.config.DiscordConfig;
import tech.majava.listeners.ListenersModule;
import tech.majava.logging.LoggingModule;
import tech.majava.modules.Module;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link tech.majava.discord.DiscordModule}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public final class DiscordModule extends Module<DiscordConfig> {

    private ListenersModule listeners;
    private LoggingModule logging;
    private JDA jda;
    private DiscordEventsHandler handler;

    public DiscordModule(@Nonnull DiscordConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "discord", "discord module for creating bots in majava");
        dependencies.add(ListenersModule.class);
        dependencies.add(LoggingModule.class);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> {
            listeners = context.getModules().get(ListenersModule.class);
            logging = context.getModules().get(LoggingModule.class);
            try {
                jda = config.getJda().toBuilder().build().awaitReady();
                handler = new DiscordEventsHandler(logging, jda);
                listeners.registerHandler(GenericEvent.class, handler);
            } catch (InterruptedException | LoginException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> {
            listeners.unregisterHandler(GenericEvent.class);
            jda.shutdown();
        });
    }

}
