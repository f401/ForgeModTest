package net.qpowei.tpitem.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.qpowei.tpitem.TeleportItemGroup;

public class TeleportItem extends Item {

	public TeleportItem() {
		super(new Properties().durability(200).tab(TeleportItemGroup.GROUP));
	}

	@Override
	public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
		if (!pLevel.isClientSide && pPlayer instanceof ServerPlayerEntity) {
		} 
		return super.use(pLevel, pPlayer, pHand);
	}

}
