package com.dogonfire.dramacraft;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;



public class TreasuryManager implements Listener
{		
	static private TreasuryManager 			instance;
	private Random 							random = new Random();
	private FileConfiguration				config		= null;
	private File							configFile	= null;
		
	public TreasuryManager()
	{
		instance = this;
	}
	
	public void load()
	{
		try
		{
			this.configFile = new File(DramaCraft.instance().getDataFolder(), "resources.yml");

			this.config = YamlConfiguration.loadConfiguration(this.configFile);

			DramaCraft.log("Loaded resources.yml.");				
		}
		catch(Exception ex)
		{
			DramaCraft.log("No resources.yml file found.");			
		}
		
	}
	
	public void save()
	{
		if (this.config == null || this.configFile == null)
		{
			DramaCraft.log("Config: " + this.config);
			DramaCraft.log("Configfile: " + this.configFile);
			return;
		}
		
		try
		{
			this.config.save(this.configFile);
		}
		catch (Exception ex)
		{
			DramaCraft.log("Could not save config to " + this.configFile + ": " + ex.getMessage());
		}
		
		DramaCraft.log("Saved configuration.");
	}
	
	static public void depositToImperialTreasury(int amount)
	{
		int balance = instance.config.getInt("Imperials.Treasury.Balance") + amount;
		instance.config.set("Imperials.Treasury.Balance", balance);
		instance.save();
	}
	
	static public void depositToRebelStash(int amount)
	{
		int balance = instance.config.getInt("Rebels.Stash.Balance") + amount;
		instance.config.set("Rebels.Stash.Balance", balance);
		instance.save();		
	}

	static public boolean withdrawFromImperialTreasury(int amount)
	{
		int balance = getImperialBalance();
		
		if(amount > balance)
		{
			return false;
		}

		balance -= amount;
		
		instance.config.set("Imperials.Treasury.Balance", balance);
		instance.save();
		
		return true;		
	}
	
	static public boolean withdrawFromRebelStash(int amount)
	{
		int balance = getRebelsBalance();
		
		if(amount > balance)
		{
			return false;
		}

		balance -= amount;
		
		instance.config.set("Rebels.Treasury.Balance", balance);
		instance.save();
		
		return true;		
	}

	static public int getImperialBalance()
	{
		return instance.config.getInt("Imperials.Treasury.Balance");		
	}
	
	static public int getRebelsBalance()
	{
		return instance.config.getInt("Rebels.Stash.Balance");		
	}

	static public void setImperialBankLocation(UUID ownerId, Location location)
	{
		//setHead(ownerId, location);
		
		instance.config.set("Imperials.Treasury.Balance.World", location.getWorld().getName());
		instance.config.set("Imperials.Treasury.Balance.X", location.getBlockX());
		instance.config.set("Imperials.Treasury.Balance.Y", location.getBlockY());
		instance.config.set("Imperials.Treasury.Balance.Z", location.getBlockZ());
		
		instance.save();				
	}
		
	static public void setRebelStashLocation(UUID ownerId, Location location)
	{
		//setHead(ownerId, location);
		
		instance.config.set("Rebels.Treasury.Balance.World", location.getWorld().getName());
		instance.config.set("Rebels.Treasury.Balance.X", location.getBlockX());
		instance.config.set("Rebels.Treasury.Balance.Y", location.getBlockY());
		instance.config.set("Rebels.Treasury.Balance.Z", location.getBlockZ());
		
		instance.save();				
	}
			
	public boolean isFameBlock(Block block)
	{
		if (block == null || false)
		{
			return false;
		}

		if (block.getRelative(BlockFace.UP).getType() != Material.PLAYER_HEAD)
		{
			return false;
		}
		
		for (BlockFace face : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST })
		{
			if (block.getRelative(face).getType() == Material.OAK_WALL_SIGN)
			{
				return true;
			}
		}
		
		return false;
	}

	public boolean isBankSign(Block block)
	{
		if ((block == null) || (block.getType() != Material.OAK_WALL_SIGN))
		{
			return false;
		}		

		DramaCraft.logDebug("isFameSign(): FameSignBlock block is " + block.getType().name());
		
		//if (getFameTypeFromBlock(block) == null)
		//{
		//	return false;
		//}
				
		return true;
	}
	
	private Block getBankBlockFromSign(Block block)
	{
		WallSign wallSign = (org.bukkit.block.data.type.WallSign) block.getBlockData();
		Block fameBlock = block.getRelative(wallSign.getFacing().getOppositeFace());
		return fameBlock;
	}

	@EventHandler
	public void OnBlockBreak(BlockBreakEvent event)
	{
		if(RankManager.isImperial(event.getPlayer().getUniqueId()))
		{
			int amount = 0;
			
			switch(event.getBlock().getType())
			{
				case COAL_ORE 		: amount = 1; break;
				case IRON_ORE 		: amount = 2; break;
				case EMERALD_ORE 	: amount = 3; break;
				case GOLD_ORE 		: amount = 4; break;
				case DIAMOND_ORE 	: amount = 5; break;
				default : break;
			}
						
			if(amount > 0)
			{
				depositToImperialTreasury(amount);
				event.getPlayer().sendMessage(ChatColor.GREEN + "You contributed " + ChatColor.GOLD + amount + " wanks" + ChatColor.GREEN + " to the Imperial Treasury!");				
			}
		}

		if(RankManager.isRebel(event.getPlayer().getUniqueId()))
		{
			int amount = 0;
			
			switch(event.getBlock().getType())
			{
				case COAL_ORE 		: amount = 1; break;
				case IRON_ORE 		: amount = 2; break;
				case EMERALD_ORE 	: amount = 3; break;
				case GOLD_ORE 		: amount = 4; break;
				case DIAMOND_ORE 	: amount = 5; break;
				default : break;
			}
						
			if(amount > 0)
			{
				depositToRebelStash(amount);
				event.getPlayer().sendMessage(ChatColor.GREEN + "You contributed " + ChatColor.GOLD + amount + " wanks" + ChatColor.GREEN + " to the Rebel Stash!");				
			}
		}
	}
	
	@EventHandler
	public void OnSignChange(SignChangeEvent event)
	{
		
		if (!isBankSign(event.getBlock()))
		{
			return;
		}
		
		Block fameBlock = getBankBlockFromSign(event.getBlock());

		if (!isFameBlock(fameBlock))
		{
			return;
		}
				
		//if (!handleNewBankBlock(event.getBlock(), fameBlock))
		//{
		// TODO add this block to list of blocks for this fametype	
		//}
	}
	
	
}