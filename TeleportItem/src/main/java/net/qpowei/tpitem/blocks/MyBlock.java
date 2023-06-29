package net.qpowei.tpitem.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

public class MyBlock extends Block {

	public MyBlock() {
		super(Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).strength(3));
	}

	@Override
	public List<ItemStack> getDrops(BlockState pState, Builder pBuilder) {
		ResourceLocation loot = getLootTable();
		if (loot.equals(LootTables.EMPTY)) {
			return Collections.emptyList();
		}
		LootContext ctx = pBuilder.withParameter(LootParameters.BLOCK_STATE, pState)
				.create(LootParameterSets.BLOCK);
		return pBuilder.getLevel().getServer().getLootTables().get(loot).getRandomItems(ctx);
	}

}
