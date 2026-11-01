package com.example.walknoclip.mixin;

import com.example.walknoclip.WalkNoclipClient;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replaces the collision step for the local player: horizontal movement is never
 * blocked (walk through walls), but falling still collides with the floor, so you
 * stand on the ground instead of flying or sinking.
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "collide", at = @At("HEAD"), cancellable = true)
    private void walknoclip$collide(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
        if (!WalkNoclipClient.enabled || !((Object) this instanceof LocalPlayer)) {
            return;
        }

        Entity self = (Entity) (Object) this;
        double dy = movement.y;

        if (dy < 0.0) {
            AABB box = self.getBoundingBox();
            Iterable<VoxelShape> shapes = self.level().getBlockCollisions(self, box.expandTowards(0.0, dy, 0.0));
            dy = Shapes.collide(Direction.Axis.Y, box, shapes, dy);
        }

        cir.setReturnValue(new Vec3(movement.x, dy, movement.z));
    }
}
