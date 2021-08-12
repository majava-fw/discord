/*
 *  discord - tech.majava.discord.components.MessageComponents
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

package tech.majava.discord.components;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
import tech.majava.discord.responses.MessageModifier;
import tech.majava.discord.responses.MessageTemplate;
import tech.majava.discord.responses.Response;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p><b>Class {@link MessageComponents}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MessageComponents<R extends RestAction<Message>> implements MessageModifier {

    @Nonnull
    private final ComponentsModule module;
    @Nonnull
    private final Response<R> response;
    @Nonnull
    private final List<ActionRow> rows = new ArrayList<>();

    @Nonnull
    public MessageComponents<R> addRow(@Nonnull Consumer<ComponentsRow> rowModifier) {
        final ComponentsRow row = new ComponentsRow(module);
        rowModifier.accept(row);
        rows.add(row.toRow());
        return this;
    }

    @Nonnull
    @Override
    public R modify() {
        final R action = response.getAction();
        if (action instanceof MessageAction) {
            ((MessageAction) action).setActionRows(rows);
        } else if (action instanceof WebhookMessageUpdateAction) {
            ((WebhookMessageUpdateAction<?>) action).setActionRows(rows);
        }
        return action;
    }

}
