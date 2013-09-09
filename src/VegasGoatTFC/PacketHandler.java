package VegasGoatTFC;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	public static final String CHANNEL_NAME = "VegasGoatTFC";
	public static final byte CARVING_ACTION = 1;
	public static final byte CARVING_DATA = 2;

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if(packet.channel.equals(CHANNEL_NAME))
		{
			try
			{
				ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.data);
				DataInputStream dataStream = new DataInputStream(byteStream);
				byte msgType = dataStream.readByte();
				switch(msgType)
				{
					case CARVING_ACTION:
						handleCarvingAction(dataStream, player);
						break;
					case CARVING_DATA:
						handleCarvingData(dataStream, player);
						break;
				}
			}
			catch(Throwable tt)
			{
				tt.printStackTrace();
			}
		}
	}

	private void handleCarvingAction(DataInputStream dataStream, Player player)
	{
		try
		{
			int entityId = dataStream.readInt();
			int tileX = dataStream.readInt();
			int tileY = dataStream.readInt();

			// should be on the server side
			if(player instanceof EntityPlayerMP)
			{
				EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
				if(serverPlayer.openContainer instanceof ContainerCarvingBase)
				{
					ContainerCarvingBase container = (ContainerCarvingBase) serverPlayer.openContainer;
					container.onCarvingAction(tileX, tileY, player);
				}
			}
		}
		catch(Throwable tt)
		{
			tt.printStackTrace();
		}
	}

	private void handleCarvingData(DataInputStream dataStream, Player player)
	{
		try
		{
			int entityId = dataStream.readInt();
			int length = dataStream.readInt();
			byte[] carvingData = new byte[length];
			dataStream.read(carvingData);

			// should be on the client side
			if(player instanceof EntityPlayerSP)
			{
				EntityPlayerSP clientPlayer = (EntityPlayerSP) player;
				if(clientPlayer.openContainer instanceof ContainerCarvingBase)
				{
					ContainerCarvingBase container = (ContainerCarvingBase) clientPlayer.openContainer;
					container.onCarvingData(carvingData);
				}
			}
		}
		catch(Throwable tt)
		{
			tt.printStackTrace();
		}
	}
}

