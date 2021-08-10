/*
 *  discord - tech.majava.discord.templating.WebhookResponse
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

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link tech.majava.discord.responses.WebhookResponse}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class WebhookResponse extends Response<WebhookMessageUpdateAction<Message>> {

    public WebhookResponse(@Nonnull WebhookMessageUpdateAction<Message> action) {
        super(action);
    }

    @Nonnull
    public static WebhookResponse from(@Nonnull GenericInteractionCreateEvent event, @Nonnull MessageTemplate template) {
        return from(event, template, false);
    }

    @Nonnull
    public static WebhookResponse from(@Nonnull GenericInteractionCreateEvent event, @Nonnull MessageTemplate template, boolean ephemeral) {
        final InteractionHook interactionHook = event.isAcknowledged() ? event.getHook() : event.deferReply(ephemeral).submit().join();
        return new WebhookResponse(interactionHook.editOriginal(template.build()));
    }

}