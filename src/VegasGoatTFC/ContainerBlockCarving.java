package VegasGoatTFC;

import java.util.Arrays;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.Player;
import TFC.TFCItems;

public class ContainerBlockCarving extends ContainerCarvingBase
{
	private InventoryBasic carvingInventory;
	private int woodType;
	public byte[] carvingData;

	public ContainerBlockCarving(EntityPlayer player, int woodType)
	{
		super(player.entityId);
		this.carvingInventory = new InventoryBasic("BlockCarving", false, 2);
		this.woodType = woodType;
		this.carvingData = new byte[25];
		Arrays.fill(this.carvingData, (byte) 1);

		int[] resultItems = new int[0];
		int[] toolItems = new int[] {
			WoodworkingTFC.stoneWoodworkingKnife.itemID
		};

		// result slot
		addSlotToContainer(new CarvingResultSlot(this, this.carvingInventory, 0, 142, 58, resultItems));

		// tool slot
		addSlotToContainer(new CarvingToolSlot(this.carvingInventory, 1, 142, 20, toolItems));

		// start with a log in the slot so it can be taken back before carving
		getSlot(0).putStack(new ItemStack(TFCItems.Logs, 1, woodType));

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
				// transfer tool slot to inventory
				if(!this.mergeItemStack(transferStack, 2, inventorySlots.size(), true))
				{
					return null;
				}
			}
			else
			{
				// transfer inventory to tool slot
				Slot toolSlot = getSlot(1);
				if(toolSlot.isItemValid(transferStack))
				{
					if(!this.mergeItemStack(transferStack, 1, 2, true))
					{
						return null;
					}
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

		// only update if it changes
		int index = tileY * 5 + tileX;
		if(carvingData[index] != 0)
		{
			// damage tool
			int dmg = tool.getItemDamage();
			if(++dmg == tool.getMaxDamage())
			{
				toolSlot.putStack(null);
			}
			else
			{
				tool.setItemDamage(dmg);
				toolSlot.onSlotChanged();
			}

			// update carving data
			carvingData[index] = 0;

			// send data to client
			sendCarvingData(carvingData, player);

			// update slot with match (or none)
			Slot resultSlot = getSlot(0);
			CarvingRecipe recipe = CarvingRecipe.findMatch(carvingData, 5);
			if(recipe != null)
			{
				resultSlot.putStack(recipe.generateResult(this.woodType));
			}
			else
			{
				if(resultSlot.getHasStack())
				{
					resultSlot.putStack(null);
				}
			}
		}
	}

	@Override
	public void onResultPickedUp(EntityPlayer player)
	{
		// only on server side
		if(player instanceof EntityPlayerMP)
		{
			// clear the carving area
			Arrays.fill(carvingData, (byte) 0);
			sendCarvingData(carvingData, (Player) player);
		}
	}

	@Override
	public void onCarvingData(byte[] carvingData)
	{
		this.carvingData = carvingData;
	}
}
