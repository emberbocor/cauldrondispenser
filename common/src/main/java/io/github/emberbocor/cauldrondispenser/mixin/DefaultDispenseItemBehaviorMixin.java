package io.github.emberbocor.cauldrondispenser.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.emberbocor.cauldrondispenser.cauldron.CauldronDispensing;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

@Mixin(DefaultDispenseItemBehavior.class)
public abstract class DefaultDispenseItemBehaviorMixin {
    @Shadow
    protected abstract void playSound(BlockSource blockSource);

    @Shadow
    protected abstract void playAnimation(BlockSource blockSource, Direction direction);

    // dispense runs before execute, so the cauldron comes ahead of the vanilla behavior and every execute handler
    // (e.g. Ranching Dispensers milking with an empty bucket).
    @Inject(method = "dispense", at = @At("HEAD"), cancellable = true)
    private void cauldrondispenser$useCauldron(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        DefaultDispenseItemBehavior behavior = (DefaultDispenseItemBehavior) (Object) this;
        ItemStack result = CauldronDispensing.interact(source, stack, behavior);
        if (result != null) {
            ItemStack remaining = CauldronDispensing.consume(source, stack, result);
            if (behavior instanceof OptionalDispenseItemBehavior optional) {
                optional.setSuccess(true);
            }
            playSound(source);
            playAnimation(source, source.getBlockState().getValue(DispenserBlock.FACING));
            cir.setReturnValue(remaining);
        }
    }
}
