package net.azilcoff.mbdiff.mixin;

import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    @Inject(method = "burnsInDaylight", at = @At("RETURN"), cancellable = true)
    private void burnsInDaylight(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }
}
