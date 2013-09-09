package VegasGoatTFC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;

public class CarvingResultSlot extends Slot
{
	ContainerCarvingBase container;
	int[] validItems;

	public CarvingResultSlot(ContainerCarvingBase container, IInventory parentInv, int index, int x, int y, int[] validItems)
	{
		super(parentInv, index, x, y);
		this.container = container;
		this.validItems = validItems;
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		for(int testID : this.validItems)
		{
			if(is.itemID == testID)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack is)
	{
		this.container.onResultPickedUp(player);
	}
}
