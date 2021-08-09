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

package tech.majava.discord.components;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import tech.majava.context.ApplicationContext;
import tech.majava.discord.DiscordModule;
import tech.majava.listeners.EntryPoint;
import tech.majava.modules.Module;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p><b>Class {@link ComponentsModule}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public final class ComponentsModule extends Module<ComponentsConfig> {

    @Nonnull
    private final Map<String, Function<ButtonClickEvent, CompletableFuture<Void>>> buttons = new HashMap<>();
    private DiscordModule discordModule;
    private ComponentsListener componentsListener;
    private EntryPoint<GenericComponentInteractionCreateEvent> componentsEntryPoint;

    public ComponentsModule(@Nonnull ComponentsConfig config, @Nonnull ApplicationContext context) {
        super(config, context, "discord-components", "discord components module for handling discord components in majava");
        dependencies.add(DiscordModule.class);
    }

    public void registerButton(@Nonnull String id, @Nonnull Function<ButtonClickEvent, CompletableFuture<Void>> callback) {
        componentsListener.getButtons().put(id, callback);
    }

    public void unregisterButton(@Nonnull String id) {
        componentsListener.getButtons().remove(id);
    }

    public void registerSelection(@Nonnull String id, @Nonnull Function<SelectionMenuEvent, CompletableFuture<Void>> callback) {
        componentsListener.getSelections().put(id, callback);
    }

    public void unregisterSelection(@Nonnull String id) {
        componentsListener.getSelections().remove(id);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onStart() {
        return CompletableFuture.runAsync(() -> {
            discordModule = context.getModules().get(DiscordModule.class);
            componentsListener = new ComponentsListener();
            componentsEntryPoint = discordModule.getListeners().loadListener(componentsListener);
            componentsEntryPoint.register();
        });
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> onShutdown() {
        return CompletableFuture.runAsync(() -> componentsEntryPoint.unregister());
    }

}
