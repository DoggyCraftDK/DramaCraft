package com.dogonfire.dramacraft;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

//This is your trait that will be applied to a npc using the /trait mytraitname command. Each NPC gets its own instance of this class.
//the Trait class has a reference to the attached NPC class through the protected field 'npc' or getNPC().
//The Trait class also implements Listener so you can add EventHandlers directly to your trait.
public class RebelGuardTrait extends Trait
{
	public RebelGuardTrait()
	{
		super("mytraitname");
		//plugin = JavaPlugin.getPlugin(MyPlugin.class);
	}

	//MyPlugin	plugin							= null;

	boolean		SomeSetting						= false;

	// see the 'Persistence API' section
	@Persist("mysettingname")
	boolean		automaticallyPersistedSetting	= false;

	// Here you should load up any values you have previously saved (optional).
	// This does NOT get called when applying the trait for the first time, only
	// loading onto an existing npc at server start.
	// This is called AFTER onAttach so you can load defaults in onAttach and
	// they will be overridden here.
	// This is called BEFORE onSpawn, npc.getEntity() will return null.
	public void load(DataKey key)
	{
		SomeSetting = key.getBoolean("SomeSetting", false);
	}

	// Save settings for this NPC (optional). These values will be persisted to
	// the Citizens saves file
	public void save(DataKey key)
	{
		key.setBoolean("SomeSetting", SomeSetting);
	}

	// An example event handler. All traits will be registered automatically as
	// Bukkit Listeners.
	@EventHandler
	public void click(net.citizensnpcs.api.event.NPCRightClickEvent event)
	{
		// Handle a click on a NPC. The event has a getNPC() method.
		// Be sure to check event.getNPC() == this.getNPC() so you only handle
		// clicks on this NPC!

	}

	// Called every tick
	@Override
	public void run()
	{
	}

	// Run code when your trait is attached to a NPC.
	// This is called BEFORE onSpawn, so npc.getEntity() will return null
	// This would be a good place to load configurable defaults for new NPCs.
	@Override
	public void onAttach()
	{
		//plugin.getServer().getLogger().info(npc.getName() + " has been assigned MyTrait!");
	}

	// Run code when the NPC is despawned. This is called before the entity
	// actually despawns so npc.getEntity() is still valid.
	@Override
	public void onDespawn()
	{
	}

	// Run code when the NPC is spawned. Note that npc.getEntity() will be null
	// until this method is called.
	// This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn()
	{

	}

	// run code when the NPC is removed. Use this to tear down any repeating
	// tasks.
	@Override
	public void onRemove()
	{
	}

}

//// This is your bukkit plugin class. Use it to hook your trait into Citizens and
//// handle any commands.
//public class MyPlugin extends org.bukkit.plugin.java.JavaPlugin
//{
//
//	public void onEnable()
//	{
//
//		// check if Citizens is present and enabled.
//
//		if (getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false)
//		{
//			getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
//			getServer().getPluginManager().disablePlugin(this);
//			return;
//		}
//
//		// Register your trait with Citizens.
//		net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(RebelGuardTrait.class).withName("rebelguard"));
//	}
//
//	@Override
//	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] inargs)
//	{
//		// handle commands for /myplugin
//	}
//}
