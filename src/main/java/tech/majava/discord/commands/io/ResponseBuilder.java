/*
 *  discord - tech.majava.discord.commands.io.ResponseBuilder
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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import tech.majava.discord.components.ComponentsModule;
import tech.majava.discord.components.MessageComponents;
import tech.majava.discord.responses.DefaultResponse;
import tech.majava.discord.responses.MessageTemplate;
import tech.majava.discord.responses.MessageTemplateImpl;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p><b>Class {@link tech.majava.discord.commands.io.ResponseBuilder}</b></p>
 *
 * @author majksa
 * @version 1.0.1
 * @since 1.0.0
 */
@Getter
public class ResponseBuilder {

    @Nonnull
    protected CompletableFuture<Boolean> ephemeral = CompletableFuture.completedFuture(false);
    @Nonnull
    protected CompletableFuture<MessageTemplate> template = CompletableFuture.supplyAsync(MessageTemplateImpl::new);
    @Nonnull
    protected List<Function<Message, CompletableFuture<?>>> callbacks = new LinkedList<>();

    @Nonnull
    public ResponseBuilder setEphemeral(boolean ephemeral) {
        this.ephemeral = CompletableFuture.completedFuture(ephemeral);
        return this;
    }

    @Nonnull
    public ResponseBuilder setEphemeral(@Nonnull Supplier<Boolean> ephemeral) {
        this.ephemeral = CompletableFuture.supplyAsync(ephemeral);
        return this;
    }

    @Nonnull
    public ResponseBuilder setEphemeral(@Nonnull CompletableFuture<Boolean> ephemeral) {
        this.ephemeral = ephemeral;
        return this;
    }

    @Nonnull
    public ResponseBuilder setTemplate(@Nonnull MessageTemplate template) {
        this.template = CompletableFuture.completedFuture(template);
        return this;
    }

    @Nonnull
    public ResponseBuilder setTemplate(@Nonnull Supplier<MessageTemplate> template) {
        this.template = CompletableFuture.supplyAsync(template);
        return this;
    }

    @Nonnull
    public ResponseBuilder setTemplate(@Nonnull CompletableFuture<MessageTemplate> template) {
        this.template = template;
        return this;
    }

    @Nonnull
    public ResponseBuilder setCallbacks(@Nonnull List<Function<Message, CompletableFuture<?>>> callbacks) {
        this.callbacks = callbacks;
        return this;
    }

    @Nonnull
    public ResponseBuilder addCallback(@Nonnull Function<Message, CompletableFuture<?>> callback) {
        this.callbacks.add(callback);
        return this;
    }

    public ResponseBuilder setComponents(@Nonnull ComponentsModule componentsModule, @Nonnull Consumer<MessageComponents<MessageAction>> componentsConsumer) {
        return addCallback(message -> {
            final MessageComponents<MessageAction> components = new MessageComponents<>(componentsModule, DefaultResponse.edit(message, getTemplate().join()));
            componentsConsumer.accept(components);
            return components.modify().submit().thenAccept(unused -> {});
        });
    }

    @Nonnull
    public Response build() {
        return new Response(ephemeral, template, callbacks);
    }

}
