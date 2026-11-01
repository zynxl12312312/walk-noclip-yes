package com.example.walknoclip.mixin;

import com.example.walknoclip.WalkNoclipClient;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Stops the game from pushing the player out of blocks they are standing inside. */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true)
    private void walknoclip$noPushOut(double x, double z, CallbackInfo ci) {
        if (WalkNoclipClient.enabled) {
            ci.cancel();
        }
    }
}
