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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import tech.majava.discord.templating.MessageTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p><b>Class {@link MessageComponents}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MessageComponents {

    @Nonnull
    private final ComponentsModule module;
    @Getter
    @Nonnull
    private final MessageAction action;
    private final List<ActionRow> rows = new ArrayList<>();

    @Nonnull
    public static MessageComponents send(@Nonnull ComponentsModule module, @Nonnull MessageChannel channel, @Nonnull MessageTemplate template) {
        return new MessageComponents(module, channel.sendMessage(template.build()));
    }

    @Nonnull
    public static MessageComponents edit(@Nonnull ComponentsModule module, @Nonnull Message original, @Nonnull MessageTemplate template) {
        return new MessageComponents(module, original.editMessage(template.build()));
    }

    @Nonnull
    public static MessageComponents reply(@Nonnull ComponentsModule module, @Nonnull Message original, @Nonnull MessageTemplate template) {
        return new MessageComponents(module, original.reply(template.build()));
    }

    public MessageComponents addRow(@Nonnull Consumer<MessageRow> rowModifier) {
        final MessageRow row = new MessageRow(module);
        rowModifier.accept(row);
        rows.add(row.toRow());
        return this;
    }

    @RequiredArgsConstructor
    public static class MessageRow {

        @Nonnull
        private final ComponentsModule module;
        @Nonnull
        private final List<Component> components = new ArrayList<>();


        /**
         * Adds a button to message
         *
         * @param button   button itself
         * @param callback button callback
         */
        public void addButton(@Nonnull Button button, @Nonnull Function<ButtonClickEvent, CompletableFuture<Void>> callback) {
            components.add(button);
            module.registerButton(Objects.requireNonNull(button.getId()), callback);
        }

        /**
         * Adds a selection menu to message
         *
         * @param menu     menu itself
         * @param callback menu callback
         */
        public void addSelection(@Nonnull SelectionMenu menu, @Nonnull Function<SelectionMenuEvent, CompletableFuture<Void>> callback) {
            components.add(menu);
            module.registerSelection(Objects.requireNonNull(menu.getId()), callback);
        }

        public ActionRow toRow() {
            return ActionRow.of(components);
        }

    }

    @Nonnull
    public CompletableFuture<Message> send() {
        return action.setActionRows(rows).submit();
    }

}
