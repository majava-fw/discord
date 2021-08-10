/*
 *  discord - tech.majava.discord.templating.Response
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

package tech.majava.discord.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * <p><b>Abstract class {@link tech.majava.discord.responses.Response}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract class Response<T extends RestAction<Message>> {

    @Getter
    @Nonnull
    protected final T action;
    @Nonnull
    protected final List<Consumer<Message>> callbacks = new LinkedList<>();

    @Nonnull
    public Response<T> addCallback(@Nonnull Consumer<Message> callback) {
        callbacks.add(callback);
        return this;
    }

    @Nonnull
    public CompletableFuture<Message> send() {
        CompletableFuture<Message> future = getAction().submit();
        for (Consumer<Message> callback : callbacks) {
            future = future.thenApply(message -> {
                callback.accept(message);
                return message;
            });
        }
        return future;
    }

}
