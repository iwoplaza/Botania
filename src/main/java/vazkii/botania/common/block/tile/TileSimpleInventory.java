/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 9:56:24 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileSimpleInventory extends TileMod {

	protected SimpleItemStackHandler itemHandler = createItemHandler();

	@Override
	public void readCustomNBT(NBTTagCompound par1NBTTagCompound) {
		itemHandler = createItemHandler();
		itemHandler.deserializeNBT(par1NBTTagCompound);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.merge(itemHandler.serializeNBT());
	}

	public abstract int getSizeInventory();

	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true);
	}

	public IItemHandlerModifiable getItemHandler() {
		return itemHandler;
	}

	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> cap, EnumFacing side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) itemHandler;
		return super.getCapability(cap, side);
	}

	/* Extension of ItemStackHandler that uses our own slot array, allows for control of writing,
	   allows control over stack limits, and allows for itemstack-slot validation */
	protected static class SimpleItemStackHandler extends ItemStackHandler {

		private final boolean allowWrite;
		private final TileSimpleInventory tile;

		public SimpleItemStackHandler(TileSimpleInventory inv, boolean allowWrite) {
			super(inv.getSizeInventory());
			this.allowWrite = allowWrite;
			this.tile = inv;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(allowWrite) {
				return super.insertItem(slot, stack, simulate);
			} else return stack;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(allowWrite) {
				return super.extractItem(slot, amount, simulate);
			} else return null;
		}

		@Override
		public void onContentsChanged(int slot) {
			tile.markDirty();
		}
	}
}
