package VegasGoatTFC;

import java.util.Arrays;
import java.util.TreeMap;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.Player;
import TFC.TFCItems;

public class ContainerItemCarving extends ContainerCarvingBase
{
	private InventoryBasic resultInventory;

	public ContainerItemCarving(EntityPlayer player)
	{
		super(player.entityId);
		this.resultInventory = new InventoryBasic("ItemCarving", false, 2);

		int[] resultItems = new int[] {
			TFCItems.Logs.itemID, WoodworkingTFC.woodenCarving.itemID
		};
		int[] toolItems = new int[] {
			WoodworkingTFC.stoneWoodworkingKnife.itemID, WoodworkingTFC.fingerPaint.itemID
		};

		// result slot
		addSlotToContainer(new CarvingResultSlot(this, this.resultInventory, 0, 142, 58, resultItems));

		// tool slot
		addSlotToContainer(new CarvingToolSlot(this.resultInventory, 1, 142, 20, toolItems));

		// player inventory slots
		int col, row;
		for(row = 0; row < 3; ++row)
		{
			for(col = 0; col < 9; ++col)
			{
				this.addSlotToContainer(new Slot(player.inventory, row * 9 + col + 9, 8 + (col * 18), 92 + (row * 18)));
			}
		}

		// player hotbar slots
		for(col = 0; col < 9; ++col)
		{
			this.addSlotToContainer(new Slot(player.inventory, col, 8 + (col * 18), 150));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack returnStack = null;
		Slot clickedSlot = getSlot(slotID);

		if(clickedSlot != null && clickedSlot.getHasStack())
		{
			ItemStack transferStack = clickedSlot.getStack();
			returnStack = transferStack.copy();

			if(slotID == 0)
			{
				// transfer from result slot to inventory
				if(!this.mergeItemStack(transferStack, 2, inventorySlots.size(), true))
				{
					return null;
				}
				onResultPickedUp(player);
			}
			else if(slotID == 1)
			{
				// transfer from tool slot to inventory
				if(!this.mergeItemStack(transferStack, 2, inventorySlots.size(), true))
				{
					return null;
				}
			}
			else
			{
				// transfer from inventory to result or tool slot
				Slot resultSlot = getSlot(0);
				Slot toolSlot = getSlot(1);
				if(toolSlot.isItemValid(transferStack))
				{
					if(!this.mergeItemStack(transferStack, 1, 2, false))
					{
						return null;
					}
				}
				else if((!resultSlot.getHasStack()) && resultSlot.isItemValid(transferStack))
				{
					ItemStack copy = transferStack.copy();
					copy.stackSize = 1;
					--transferStack.stackSize;
					resultSlot.putStack(copy);
				}
				else
				{
					return null;
				}
			}

			if(transferStack.stackSize == 0)
			{
				clickedSlot.putStack(null);
			}
			else
			{
				clickedSlot.onSlotChanged();
			}
		}

		return returnStack;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer player)
	{
		Slot resultSlot = getSlot(0);
		Slot toolSlot = getSlot(1);
		if(resultSlot.getHasStack())
		{
			player.dropPlayerItem(resultSlot.getStack());
		}
		if(toolSlot.getHasStack())
		{
			player.dropPlayerItem(toolSlot.getStack());
		}
		super.onCraftGuiClosed(player);
	}

	@Override
	public void onCarvingAction(int tileX, int tileY, Player player)
	{
		// check for tool
		Slot toolSlot = getSlot(1);
		if(!toolSlot.getHasStack())
		{
			// can't carve without tool in slot
			return;
		}
		ItemStack tool = toolSlot.getStack();
		if(!toolSlot.isItemValid(tool))
		{
			// not a valid carving tool (how did that get there?)
			return;
		}

		Slot resultSlot = getSlot(0);
		if(resultSlot.getHasStack())
		{
			ItemStack is = resultSlot.getStack();

			// convert log to carving
			boolean isNew = false;
			byte colorValue = 0;
			if(is.itemID == TFCItems.Logs.itemID)
			{
				colorValue = (byte) (is.getItemDamage() + RenderWoodenCarving.WOOD_BASE);
				is = new ItemStack(WoodworkingTFC.woodenCarving);
				isNew = true;
			}

			// must be a wooden carving item
			if(is.itemID != WoodworkingTFC.woodenCarving.itemID)
			{
				// not a valid carving item (how did that get there?)
				return;
			}

			// needs a tag compound for custom data
			if(!is.hasTagCompound())
			{
				is.setTagCompound(new NBTTagCompound("tag"));
			}
			NBTTagCompound tag = is.getTagCompound();

			// set image data
			byte[] imageData;
			if(tag.hasKey("carvingData"))
			{
				imageData = tag.getByteArray("carvingData");
			}
			else
			{
				imageData = new byte[256];
				Arrays.fill(imageData, colorValue);
				tag.setByteArray("carvingData", imageData);
			}

			// check tool type for damage flag and value to set
			int index = tileY * 16 + tileX;
			boolean doDamage = false;
			boolean doConsume = false;
			if(tool.itemID == WoodworkingTFC.stoneWoodworkingKnife.itemID)
			{
				colorValue = 0;
				doDamage = true;
			}
			else if(tool.itemID == WoodworkingTFC.fingerPaint.itemID)
			{
				colorValue = (byte) (tool.getItemDamage() + RenderWoodenCarving.DYE_BASE);
				doConsume = true;
			}

			// only update if not already carved and value changed
			if((imageData[index] != 0) && (imageData[index] != colorValue))
			{
				// damage tool
				if(doDamage)
				{
					int dmg = tool.getItemDamage();
					if(++dmg >= tool.getMaxDamage())
					{
						toolSlot.putStack(null);
					}
					else
					{
						tool.setItemDamage(dmg);
						toolSlot.onSlotChanged();
					}
				}

				// consume tool
				if(doConsume)
				{
					if(--tool.stackSize == 0)
					{
						toolSlot.putStack(null);
					}
					else
					{
						toolSlot.onSlotChanged();
					}
				}

				// update image data
				imageData[index] = colorValue;

				// send update
				if(isNew)
				{
					resultSlot.putStack(is);
				}
				else
				{
					resultSlot.onSlotChanged();
				}
			}
		}
	}
}
