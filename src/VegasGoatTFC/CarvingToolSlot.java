package VegasGoatTFC;

import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;

public class CarvingToolSlot extends Slot
{
	int[] validItems;

	public CarvingToolSlot(IInventory parentInv, int index, int x, int y, int[] validItems)
	{
		super(parentInv, index, x, y);
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
}
