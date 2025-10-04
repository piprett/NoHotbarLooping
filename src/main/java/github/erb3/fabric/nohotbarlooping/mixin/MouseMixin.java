package github.erb3.fabric.nohotbarlooping.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import github.erb3.fabric.nohotbarlooping.NoHotbarLooping;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Debug(export = true)
@Mixin(Mouse.class)
public class MouseMixin {
    @ModifyArg(
        method = "onMouseScroll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"
        ),
        index = 0
    )
    public int disableLooping(int scrollUpdatesTo, @Local PlayerInventory inv) {
        if (NoHotbarLooping.shouldLoopHotbar) {
            return scrollUpdatesTo;
        }

        int originalSlot = inv.getSelectedSlot();
        if (Math.abs(originalSlot - scrollUpdatesTo) > 1) {
            return originalSlot;
        }

        return scrollUpdatesTo;
    }
}
