package com.bukkit.jason.pitfall;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * Handle events for all Player related events
 * 
 * @author <Your Name>
 */
public class PitfallBlockListener extends BlockListener
{
	private final Pitfall plugin;

	public PitfallBlockListener(final Pitfall instance)
	{
		plugin = instance;
	}

	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		if (PitfallSettings.redstonePitEnabled )
		{
			Block b = event.getBlock();
			if (event.getOldCurrent() == 0 && event.getNewCurrent() > 0)
			{
				if (b.getRelative(BlockFace.UP).getTypeId() == PitfallSettings.redstonePitItem)
				{
					destroyAbove(b.getRelative(BlockFace.UP));
				}
				if (b.getRelative(BlockFace.DOWN).getTypeId() == PitfallSettings.redstonePitItem)
				{
					destroyBelow(b.getRelative(BlockFace.DOWN),b);
				}
			}
		}
	}

	private void destroyBelow(final Block b, final Block wire)
	{
		if (b.getTypeId() != PitfallSettings.redstonePitItem)
			return;
		final int wireId=wire.getTypeId();
		wire.setTypeId(0);
		b.setTypeId(0);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			public void run()
			{
				try
				{
					wire.setTypeId(wireId);
					b.setTypeId(PitfallSettings.redstonePitItem);
				}
				catch (Exception e)
				{
				}
			}
		}, PitfallSettings.returnDelay);
		if (PitfallSettings.redstoneTriggerCorner)
		{
			destroyBelow(b.getRelative(BlockFace.NORTH_EAST),wire.getRelative(BlockFace.NORTH_EAST));
			destroyBelow(b.getRelative(BlockFace.NORTH_WEST),wire.getRelative(BlockFace.NORTH_WEST));
			destroyBelow(b.getRelative(BlockFace.SOUTH_EAST),wire.getRelative(BlockFace.SOUTH_EAST));
			destroyBelow(b.getRelative(BlockFace.SOUTH_WEST),wire.getRelative(BlockFace.SOUTH_WEST));
		}
		destroyBelow(b.getRelative(BlockFace.NORTH),wire.getRelative(BlockFace.NORTH));
		destroyBelow(b.getRelative(BlockFace.EAST),wire.getRelative(BlockFace.EAST));
		destroyBelow(b.getRelative(BlockFace.WEST),wire.getRelative(BlockFace.WEST));
		destroyBelow(b.getRelative(BlockFace.SOUTH),wire.getRelative(BlockFace.SOUTH));
	}

	private void destroyAbove(final Block b)
	{
		if (b.getTypeId() != PitfallSettings.redstonePitItem)
			return;

		b.setTypeId(0);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			public void run()
			{
				try
				{
					b.setTypeId(PitfallSettings.redstonePitItem);
				}
				catch (Exception e)
				{
				}
			}
		}, PitfallSettings.returnDelay);

		if (PitfallSettings.redstoneTriggerCorner)
		{
			destroyAbove(b.getRelative(BlockFace.NORTH_EAST));
			destroyAbove(b.getRelative(BlockFace.NORTH_WEST));
			destroyAbove(b.getRelative(BlockFace.SOUTH_EAST));
			destroyAbove(b.getRelative(BlockFace.SOUTH_WEST));
		}
		destroyAbove(b.getRelative(BlockFace.NORTH));
		destroyAbove(b.getRelative(BlockFace.EAST));
		destroyAbove(b.getRelative(BlockFace.WEST));
		destroyAbove(b.getRelative(BlockFace.SOUTH));
	}
}
