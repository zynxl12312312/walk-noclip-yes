package com.example.walknoclip.mixin;

import com.example.walknoclip.WalkNoclipClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * In singleplayer / LAN the integrated server runs in the same game. While noclip is on,
 * stop it from teleporting the local player out of blocks.
 * Has no effect on remote servers.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "handleMovePlayer", at = @At("HEAD"), require = 0)
    private void walknoclip$allowMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (walknoclip$isLocalNoclipPlayer()) {
            this.player.noPhysics = true;
        }
    }

    @Inject(method = "handleMovePlayer", at = @At("RETURN"), require = 0)
    private void walknoclip$restore(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (walknoclip$isLocalNoclipPlayer()) {
            this.player.noPhysics = false;
        }
    }

    private boolean walknoclip$isLocalNoclipPlayer() {
        if (!WalkNoclipClient.enabled) return false;
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getUUID().equals(this.player.getUUID());
    }
}
