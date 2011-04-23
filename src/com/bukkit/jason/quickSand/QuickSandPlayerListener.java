package com.bukkit.jason.quickSand;

import java.io.*;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.bukkit.*;

/**
 * Handle events for all Player related events
 * 
 * @author <Your Name>
 */
public class QuickSandPlayerListener extends PlayerListener
{
	private final QuickSand plugin;

	public QuickSandPlayerListener(final QuickSand instance)
	{
		plugin = instance;
	}

	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();
		final Block b = player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 2, player.getLocation().getBlockZ());
		int botMaterial = b.getTypeId();

		if (botMaterial == 82)
		{
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
			{
				public void run()
				{
					try
					{
						destroy(player, b);
					}
					catch (Exception e)
					{
					}
				}
			}, 2l);
		}
	}

	public void destroy(Player player, final Block b)
	{
		if (b.getTypeId() != 82)
			return;
		final Block h = player.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() + 1, b.getLocation().getBlockZ());
		final int type=h.getTypeId();
		h.setTypeId(0);
		b.setTypeId(0);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
		{
			public void run()
			{
				try
				{
					h.setTypeId(type);
					b.setTypeId(82);
				}
				catch (Exception e)
				{
				}
			}
		}, 60l);

		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1));
		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1));
		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1));

		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ()));
		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ()));

		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1));
		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1));
		destroy(player, player.getWorld().getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1));
	}
}
