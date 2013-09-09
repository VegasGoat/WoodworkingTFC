package VegasGoatTFC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftingHandler implements ICraftingHandler
{
	@Override
	public void onCrafting(EntityPlayer player, ItemStack is, IInventory inventory) 
	{
		if(is.itemID == WoodworkingTFC.fingerPaint.itemID)
		{
			// don't consume the bowl
			undoConsume(inventory, Item.bowlEmpty.itemID);
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack is)
	{
	}

	private void undoConsume(IInventory inventory, int itemID)
	{
		int ii;
		for(ii=0; ii<inventory.getSizeInventory(); ++ii)
		{
			ItemStack is = inventory.getStackInSlot(ii);
			if(is != null && is.itemID == itemID)
			{
				inventory.setInventorySlotContents(ii, new ItemStack(is.itemID, is.stackSize+1, is.getItemDamage()));
			}
		}
	}
}
