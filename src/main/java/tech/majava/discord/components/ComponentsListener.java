/*
 *  discord - tech.majava.discord.buttons.ButtonsListener
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
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import tech.majava.listeners.AbstractListener;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Class {@link ComponentsListener}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class ComponentsListener extends AbstractListener<GenericComponentInteractionCreateEvent> {

    @Nonnull
    private final Map<String, Function<SlashCommandEvent, CompletableFuture<Void>>> commands = new HashMap<>();

    @Nonnull
    private final Map<String, Function<ButtonClickEvent, CompletableFuture<Void>>> buttons = new HashMap<>();
    @Nonnull
    private final Map<String, Function<SelectionMenuEvent, CompletableFuture<Void>>> selections = new HashMap<>();

    public ComponentsListener() {
        super(GenericComponentInteractionCreateEvent.class);
    }

    @Nonnull
    @Override
    public CompletableFuture<Void> run(@Nonnull GenericComponentInteractionCreateEvent event) {
        if (event instanceof ButtonClickEvent) {
            return buttons.get(event.getComponentId()).apply((ButtonClickEvent) event);
        }
        if (event instanceof SelectionMenuEvent) {
            return selections.get(event.getComponentId()).apply((SelectionMenuEvent) event);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean check(@Nonnull GenericComponentInteractionCreateEvent event) {
        if (event instanceof ButtonClickEvent) {
            return buttons.containsKey(event.getComponentId());
        }
        if (event instanceof SelectionMenuEvent) {
            return selections.containsKey(event.getComponentId());
        }
        return false;
    }

}
