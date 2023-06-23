package net.qpowei.tpitem.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class MyBlock extends Block {

	public MyBlock() {
		super(Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).strength(3));
	}

} 
