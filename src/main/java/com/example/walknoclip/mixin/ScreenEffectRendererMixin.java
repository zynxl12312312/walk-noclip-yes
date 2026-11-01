package com.example.walknoclip.mixin;

import com.example.walknoclip.WalkNoclipClient;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Removes the dark "block texture" overlay that normally covers the screen when your
 * head is inside a block, so you can see through walls while inside them.
 * require = 0: if this method ever changes, the mod still loads (you'd just see the overlay).
 */
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Inject(method = "getViewBlockingState", at = @At("HEAD"), cancellable = true, require = 0)
    private static void walknoclip$noOverlay(Player player, CallbackInfoReturnable<BlockState> cir) {
        if (WalkNoclipClient.enabled) {
            cir.setReturnValue(null);
        }
    }
}
