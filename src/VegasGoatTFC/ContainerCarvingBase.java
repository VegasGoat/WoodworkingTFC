package VegasGoatTFC;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * Base class for carving containers to provide framework for handling carving packets.
 */
public abstract class ContainerCarvingBase extends Container
{
	protected int entityId;

	public ContainerCarvingBase(int entityId)
	{
		this.entityId = entityId;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer player)
	{
		super.onCraftGuiClosed(player);
	}

	/**
	 * Called by CarvingResultSlot when item is picked up.
	 */
	public void onResultPickedUp(EntityPlayer player)
	{
	}

	/**
	 * Called on the server when player clicks on a carving tile from the client.
	 */
	public abstract void onCarvingAction(int tileX, int tileY, Player player);

	/**
	 * Called on the client when carving data comes back from the server.
	 */
	public void onCarvingData(byte[] carvingData)
	{
	}

	public void sendCarvingAction(int tileX, int tileY)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(17);
			DataOutputStream dataStream = new DataOutputStream(byteStream);
			dataStream.writeByte(PacketHandler.CARVING_ACTION);
			dataStream.writeInt(this.entityId);
			dataStream.writeInt(tileX);
			dataStream.writeInt(tileY);

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = PacketHandler.CHANNEL_NAME;
			packet.data = byteStream.toByteArray();
			packet.length = packet.data.length;

			PacketDispatcher.sendPacketToServer(packet);
		}
		catch(Throwable tt)
		{
			tt.printStackTrace();
		}
	}

	public void sendCarvingData(byte[] carvingData, Player player)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(17);
			DataOutputStream dataStream = new DataOutputStream(byteStream);
			dataStream.writeByte(PacketHandler.CARVING_DATA);
			dataStream.writeInt(this.entityId);
			dataStream.writeInt(carvingData.length);
			dataStream.write(carvingData);

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = PacketHandler.CHANNEL_NAME;
			packet.data = byteStream.toByteArray();
			packet.length = packet.data.length;

			PacketDispatcher.sendPacketToPlayer(packet, player);
		}
		catch(Throwable tt)
		{
			tt.printStackTrace();
		}
	}
}
