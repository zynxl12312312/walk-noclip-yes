package com.example.walknoclip;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class WalkNoclipClient implements ClientModInitializer {

    /** True while walk-noclip is active. Read by the mixins. */
    public static volatile boolean enabled = false;

    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.walknoclip.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(WalkNoclipClient::tick);
    }

    private static void tick(Minecraft mc) {
        LocalPlayer player = mc.player;

        // Safety: switch off when there is no player or creative mode is lost.
        if (player == null || mc.level == null) {
            enabled = false;
            return;
        }
        if (enabled && !player.isCreative()) {
            enabled = false;
            player.displayClientMessage(Component.literal("Noclip disabled (not in creative)"), false);
        }

        while (toggleKey.consumeClick()) {
            if (!player.isCreative()) {
                player.displayClientMessage(Component.literal("Noclip only works in creative mode"), false);
                continue;
            }
            enabled = !enabled;
            player.displayClientMessage(
                    Component.literal(enabled ? "Noclip enabled" : "Noclip disabled"), false);
        }
    }
}
