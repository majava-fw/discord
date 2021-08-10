/*
 *  discord - tech.majava.discord.templating.DefaultResponse
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
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import javax.annotation.Nonnull;

/**
 * <p><b>Class {@link tech.majava.discord.responses.DefaultResponse}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */

public class DefaultResponse extends Response<MessageAction> {

    public DefaultResponse(@Nonnull MessageAction action) {
        super(action);
    }

    @Nonnull
    public static DefaultResponse send(@Nonnull MessageChannel channel, @Nonnull MessageTemplate template) {
        return new DefaultResponse(channel.sendMessage(template.build()));
    }

    @Nonnull
    public static DefaultResponse edit(@Nonnull Message original, @Nonnull MessageTemplate template) {
        return new DefaultResponse(original.editMessage(template.build()));
    }

    @Nonnull
    public static DefaultResponse reply(@Nonnull Message original, @Nonnull MessageTemplate template) {
        return new DefaultResponse(original.reply(template.build()));
    }

}
