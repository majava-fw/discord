/*
 *  discord - tech.majava.discord.commands.request.Response
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import tech.majava.discord.responses.MessageTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Class {@link tech.majava.discord.commands.io.Response}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public class Response {

    @Nonnull
    protected final CompletableFuture<Boolean> ephemeral;
    @Nonnull
    protected final CompletableFuture<MessageTemplate> template;
    @Nonnull
    protected List<Function<Message, CompletableFuture<?>>> callbacks;

    @Nonnull
    public CompletableFuture<Message> send(@Nonnull InteractionHook hook) {
        CompletableFuture<Message> future = template.thenCompose(template -> hook.editOriginal(template.build()).submit());
        for (Function<Message, CompletableFuture<?>> callback : callbacks) {
            future = future.thenCompose(message -> callback.apply(message).thenApply(unused -> message));
        }
        return future;
    }

}
