package li.sailor.petilil.mixin;

import com.gregtechceu.gtceu.common.data.GTItems;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Disables custom GT cauldron interactions.
 */
@Mixin(GTItems.class)
abstract class DisableCauldron {
    private DisableCauldron() {}

    @Inject(method = "cauldronInteraction", at = @At("HEAD"), cancellable = true, remap = false)
    private static <T extends Item> void pt$disableCauldronRecipes(T item, CallbackInfo ci) {
        ci.cancel();
    }
}
