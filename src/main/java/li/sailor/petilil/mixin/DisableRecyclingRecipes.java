package li.sailor.petilil.mixin;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

/// Disables all recycling recipes.
///
/// For some reason, KJS can't remove these anymore. I don't know if I should blame GTCEu, KJS, or
/// both, but here is my fix!
@Mixin(RecyclingRecipes.class)
abstract class DisableRecyclingRecipes {
    private DisableRecyclingRecipes() {
    }

    @Shadow(remap = false)
    public static void registerRecyclingRecipes(Consumer<FinishedRecipe> provider, ItemStack input, List<MaterialStack> components, boolean ignoreArcSmelting, @Nullable TagPrefix prefix) {
        throw new Error("unreachable");
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true, remap = false)
    private static void pt$disableRecycling(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        LogManager.getLogger().info("Petilil: Nuking recycling recipes!");
        // ci.cancel();
    }

    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/gregtechceu/gtceu/data/recipe/misc/RecyclingRecipes;registerRecyclingRecipes(Ljava/util/function/Consumer;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;ZLcom/gregtechceu/gtceu/api/data/tag/TagPrefix;)V"
            ),
            remap = false
    )
    private static void pt$checkItems(Consumer<FinishedRecipe> provider, ItemStack stack, List<MaterialStack> components, boolean ignoreArcSmelting, @Nullable TagPrefix prefix) {
        var item = stack.getItem();
        if (item instanceof BlockItem i) {
            var block = i.getBlock();
            if (block instanceof MetaMachineBlock) {
                return;
            }
        }
        registerRecyclingRecipes(provider, stack, components, ignoreArcSmelting, prefix);
    }
}
