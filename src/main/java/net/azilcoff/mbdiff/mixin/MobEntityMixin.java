package net.azilcoff.mbdiff.mixin;

import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(method = "isAffectedByDaylight", at = @At("RETURN"), cancellable = true)
    private void isAffectedByDaylight(CallbackInfoReturnable<Boolean> cir){
        if ((Object)this instanceof HostileEntity){
            cir.setReturnValue(false);
        }
    }
}
