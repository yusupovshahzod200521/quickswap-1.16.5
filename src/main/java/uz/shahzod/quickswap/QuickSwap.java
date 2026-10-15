package uz.shahzod.quickswap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("quickswap")
@Mod.EventBusSubscriber(modid = "quickswap")
public class QuickSwap {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide) return;

        ItemStack held = event.getItemStack();
        if (held.isEmpty()) return;

        EquipmentSlotType slot = MobEntity.getEquipmentSlotForItem(held);
        if (slot.getType() != EquipmentSlotType.Group.ARMOR) return;

        ItemStack worn = player.getItemBySlot(slot);
        if (worn.isEmpty()) return;

        if (EnchantmentHelper.hasBindingCurse(worn) && !player.isCreative()) return;

        ItemStack newWorn = held.copy();
        ItemStack newHeld = worn.copy();
        player.setItemSlot(slot, newWorn);
        player.setItemInHand(event.getHand(), newHeld);

        player.level.playSound(null, player.blockPosition(),
                SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, 1.0F);

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
    }
}
