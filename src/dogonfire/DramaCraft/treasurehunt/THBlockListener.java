package dogonfire.DramaCraft.treasurehunt;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class THBlockListener implements Listener
{
	public THBlockListener()
	{
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		TreasureHunt h = TreasureHuntManager.getCurrentHunt();
		
		if (h != null)
		{
			event.getPlayer().sendMessage(ChatColor.GRAY + "Du kan ikke �del�gge en skattekiste eller blokken under den!");
			event.setCancelled(true);
			return;
		}
	}
}