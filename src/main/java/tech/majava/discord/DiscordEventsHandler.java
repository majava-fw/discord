/*
 *  discord - tech.majava.discord.DiscordEventsHandler
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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import tech.majava.listeners.eventhandlers.AbstractEventsHandler;
import tech.majava.logging.LoggingModule;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link DiscordEventsHandler}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public final class DiscordEventsHandler extends AbstractEventsHandler<GenericEvent> implements EventListener {

    private final LoggingModule logging;
    private final JDA jda;

    public DiscordEventsHandler(@Nonnull LoggingModule logging, @Nonnull JDA jda) {
        super(GenericEvent.class, logging.getLogFunction());
        this.logging = logging;
        this.jda = jda;
    }

    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> jda.addEventListener(this));
    }

    @Override
    protected CompletableFuture<Void> onStop() {
        return CompletableFuture.runAsync(() -> jda.removeEventListener(this));
    }

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        runEvent(genericEvent)
                .thenAccept(unused -> logging.getLogger().atDebug().log("Finished handling event {}", genericEvent.getResponseNumber()));
    }

}
