package com.example.walknoclip.mixin;

import com.example.walknoclip.WalkNoclipClient;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Singleplayer / LAN only: the integrated server's copy of the local player gets
 * noPhysics = true at the END of every server tick while noclip is on. Packets are handled
 * between ticks, so the server's movement check sees noPhysics and does not teleport you back.
 * (Player.tick() resets noPhysics at the start of the next tick.)
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("RETURN"), require = 0)
    private void walknoclip$keepNoPhysics(CallbackInfo ci) {
        if (!WalkNoclipClient.enabled) return;
        ServerPlayer self = (ServerPlayer) (Object) this;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getUUID().equals(self.getUUID())) {
            self.noPhysics = true;
        }
    }
}
