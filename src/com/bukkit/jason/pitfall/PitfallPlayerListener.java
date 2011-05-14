package com.bukkit.jason.pitfall;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * 
 * @author <Your Name>
 */
public class PitfallPlayerListener extends PlayerListener
{
	private final Pitfall plugin;

	public PitfallPlayerListener(final Pitfall instance)
	{
		plugin = instance;
	}

	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();
		final Block h = player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
		final Block b = player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ());
		int botMaterial = b.getTypeId();
		if (!playerInBlacklist(player))
		{
			if (botMaterial == PitfallSettings.pitItem && h.getTypeId() != 0)
			{
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					public void run()
					{
						try
						{
							destroy(player.getWorld(), b);
						}
						catch (Exception e)
						{
						}
					}
				}, PitfallSettings.trapDelay);
			}
		}
	}

	private boolean playerInBlacklist(Player player)
	{
		return false;
	}
	// TODO Use better method, exception driven testing is BAAAAD
	private boolean isInteger(String input)
	{
		try
		{
			Integer.parseInt(input);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private boolean inBlackList(int typeId)
	{
		for (int i = 0; i < PitfallSettings.blackList.length; i++)
		{
			if (isInteger(PitfallSettings.blackList[i]) && Integer.parseInt(PitfallSettings.blackList[i]) == typeId)
			{
				return true;
			}
		}
		return false;
	}

	public void destroy(World world, final Block b)
	{
		if (b==null)
		{
			System.out.println("b was null: "+world);
			return;
		}
		if (b.getTypeId() != PitfallSettings.pitItem)
			return;
		final Block h = world.getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() + 1, b.getLocation().getBlockZ());
		final int type = h.getTypeId();
		final byte data = h.getData();

		if (!inBlackList(h.getTypeId()))
		{
			if (h.getState() instanceof CraftSign)
			{
				final String[] lines = ((CraftSign) h.getState()).getLines();
				h.setTypeId(0);
				b.setTypeId(0);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					public void run()
					{
						try
						{
							b.setTypeId(PitfallSettings.pitItem);
							h.setTypeId(type);
							h.setData(data);
							if (h.getState() instanceof CraftSign)
							{
								int lineNum = 0;
								for (String line : lines)
								{
									((CraftSign) h.getState()).setLine(lineNum, line);
									lineNum++;
								}
							}
						}
						catch (Exception e)
						{
							System.out.println("Unable to return blocks: ");
							e.printStackTrace();
						}
					}
				}, PitfallSettings.returnDelay);
			}

			else
			{
				h.setTypeId(0);
				b.setTypeId(0);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					public void run()
					{
						try
						{
							b.setTypeId(PitfallSettings.pitItem);
							h.setTypeId(type);
							h.setData(data);
						}
						catch (Exception e)
						{
							System.out.println("Unable to return blocks: ");
							e.printStackTrace();
						}
					}
				}, PitfallSettings.returnDelay);
			}
		}

		destroy(world, world.getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1));
		destroy(world, world.getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1));
		destroy(world, world.getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1));

		destroy(world, world.getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ()));
		destroy(world, world.getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ()));

		destroy(world, world.getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1));
		destroy(world, world.getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1));
		destroy(world, world.getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1));
	}
}
