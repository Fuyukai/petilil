package li.sailor.petilil;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("petilil")
@Mod.EventBusSubscriber(modid = "petilil", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class Petilil {
    public Petilil() {
        // https://www.youtube.com/watch?v=qVP_nCK0tXU

    }

    /// Removes incorrect tooltips and removes some of the cringier machine descriptions.
    @SubscribeEvent
    public static void deCringifyTooltips(ItemTooltipEvent evt) {
        var item = evt.getItemStack().getItem();
        if (item instanceof TagPrefixItem i) {
            if (i.tagPrefix == TagPrefix.crushed || i.tagPrefix == TagPrefix.dustImpure || i.tagPrefix == TagPrefix.dust) {
                evt.getToolTip().removeIf((it) -> it.getContents() instanceof TranslatableContents tr && tr.getKey().startsWith("metaitem"));
            }
        }

        if (item instanceof BlockItem i && i.getBlock() instanceof MetaMachineBlock) {
            evt.getToolTip().removeIf((it) -> {
                if (it.getContents() instanceof TranslatableContents tr) {
                    var key = tr.getKey();
                    if (key.equals("gtceu.machine.electric_blast_furnace.tooltip")) {
                        return true;
                    }

                    return key.matches("gtceu\\.machine\\.(?:(?:u?[lmheiu]|lu)v|zpm)_.*");
                }

                return false;
            });
        }
    }
}
