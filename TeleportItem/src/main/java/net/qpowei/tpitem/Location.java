package net.qpowei.tpitem;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class Location implements INBTSerializable<CompoundNBT> {

	private BlockPos pos;
	private ResourceLocation dimension;

	public Location(BlockPos pos, RegistryKey<World> dimension) {
		this.pos = pos;
		this.dimension = dimension.getRegistryName();
	}

	public Location(CompoundNBT nbt) {
		deserializeNBT(nbt);
	}

	@Override
	public String toString() {
		return "Location [pos=" + pos + ", dimension=" + dimension + "]";
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT root = new CompoundNBT();
		root.putLong("pos", pos.asLong());

		CompoundNBT dimension = new CompoundNBT();
		dimension.putString("namespace", this.dimension.getNamespace());
		dimension.putString("path", this.dimension.getPath());
		root.put("dimension", dimension);

		return root;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		pos = BlockPos.of(nbt.getLong("pos"));
		CompoundNBT compoundTag = nbt.getCompound("dimension");
		dimension = new ResourceLocation(compoundTag.getString("namespace"), compoundTag.getString("path"));
	}

}
