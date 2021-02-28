package dogonfire.DramaCraft;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dogonfire.DramaCraft.LanguageManager.LANGUAGESTRING;


public class Commands implements Listener
{
	private DramaCraft	plugin;
	private World	currentWorld;

	public Commands(DramaCraft plugin)
	{
		this.plugin = plugin;
	}
	
	private void doVote(CommandSender sender, Player player, VoteManager.VOTE_TYPE voteType)
	{
		if (DramaCraft.getVoteManager().getCurrentVoteType() == VoteManager.VOTE_TYPE.VOTE_NONE)
		{
			sender.sendMessage(ChatColor.RED + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.ERROR_NOTHING_TO_VOTE, ChatColor.RED));
			return;
		}

		/*
		if (KingVote.getVoteManager().getCurrentVoteType() == VoteManager.VOTE_TYPE.VOTE_REVOLUTION)
		{
			if(plugin.getRevolutionManager().isRebel(player))
			{
				sender.sendMessage(ChatColor.RED + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANREVOLUTION, ChatColor.RED));
				return;
			}
		}*/

		DramaCraft.getVoteManager().doVote(this.currentWorld, player, true, voteType);
		DramaCraft.getVoteManager().checkVote(40);
	}

	private boolean newVote(CommandSender sender, Player player, VoteManager.VOTE_TYPE voteType, String text)
	{
		if (DramaCraft.getVoteManager().getCurrentVoteType() != VoteManager.VOTE_TYPE.VOTE_NONE)
		{
			String message = "NO_ALREADY";

			switch (DramaCraft.getVoteManager().getCurrentVoteType())
			{
				case VOTE_INNERCIRCLE: 	message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_KING, ChatColor.RED); break;
				case VOTE_NOBLE: 	message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_KING, ChatColor.RED); break;
				case VOTE_KING: 	message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_KING, ChatColor.RED); break;
				case VOTE_QUEEN: 	message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_QUEEN, ChatColor.RED); break;
				case VOTE_NIGHT: 	message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_NIGHT, ChatColor.RED); break;
				case VOTE_DAY: 		message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_DAY, ChatColor.RED); break;
				case VOTE_SUN: 		message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_SUN, ChatColor.RED); break;
				case VOTE_RAIN:		message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_RAIN, ChatColor.RED); break;
				case VOTE_GENERAL: 	message = plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_ALREADY_GENERAL, ChatColor.RED); break;
				case VOTE_HELP: 	voteHelp(sender); break;
			}
			
			sender.sendMessage(ChatColor.RED + message);

			return false;
		}

		return DramaCraft.getVoteManager().newVote(this.currentWorld, player, text, true, voteType);
	}

	private void voteHelp(CommandSender sender)
	{
		//sender.sendMessage("" + ChatColor.WHITE + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_HEAD, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_INFO, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote day " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_DAY, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote night " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NIGHT, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote sun " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_SUN, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote rain " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_RAIN, ChatColor.AQUA));
		
		Player player = (Player)sender;
		
		if(plugin.isNoble(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote king <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_KING, ChatColor.AQUA));
			sender.sendMessage("" + ChatColor.WHITE + "/vote queen <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_QUEEN, ChatColor.AQUA));
		}

		if(plugin.isNoble(player.getUniqueId()) || plugin.getActiveNobles() < 3)
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote noble <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE, ChatColor.AQUA));
		}

		if(plugin.isNoble(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote kicknoble <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE_KICK, ChatColor.AQUA));
		}

		if(plugin.isInnerCircle(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote ringleader <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_KING, ChatColor.AQUA));
		}

		if(plugin.isInnerCircle(player.getUniqueId()) || plugin.getActiveInnerCircle() < 3)
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote innercircle <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE, ChatColor.AQUA));
		}

		if(plugin.isInnerCircle(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote kickinnercircle <playername> " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE_KICK, ChatColor.AQUA));
		}

		sender.sendMessage("" + ChatColor.WHITE + "/vote question " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_QUESTION, ChatColor.AQUA));

		if(plugin.isRebel(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote revolution " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_REVOLUTION, ChatColor.AQUA));
		}
		
	}

	private void dramaCraftInfo(CommandSender sender)
	{
		sender.sendMessage(ChatColor.YELLOW + "------------------ " + this.plugin.getDescription().getFullName() + " ------------------");
		sender.sendMessage(ChatColor.AQUA + "By DogOnFire");
		sender.sendMessage("" + ChatColor.AQUA);
		
		sender.sendMessage("" + ChatColor.GOLD + plugin.getNumberOfRebels() + ChatColor.RED + " Rebels vs " + ChatColor.GOLD + plugin.getNumberOfImperials() + ChatColor.AQUA + " Imperials");
		sender.sendMessage("" + ChatColor.AQUA);
		sender.sendMessage("" + ChatColor.AQUA + "Imperials has " + ChatColor.GOLD + plugin.instance().getResourceManager().getImperialResources() + ChatColor.AQUA + " resources.");
		sender.sendMessage("" + ChatColor.AQUA + "Rebels has " + ChatColor.GOLD + plugin.instance().getResourceManager().getRebelResources() + ChatColor.AQUA + " resources.");
		sender.sendMessage("" + ChatColor.AQUA);

		sendKingQueenWho(sender);

		sender.sendMessage("" + ChatColor.AQUA);

		Player player = (Player)sender;

		if(plugin.isNoble(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are an Imperial Noble");
		}

		if(plugin.isInnerCircle(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are part of the Rebel Inner Circle");
		}

		if(plugin.isNeutral(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are Neutral");
		}
		
		if(plugin.isImperial(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are an " + ChatColor.AQUA + " Imperial");
		}

		if(plugin.isRebel(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are a " + ChatColor.RED + " Rebel");
		}

		sender.sendMessage("" + ChatColor.AQUA);

		if(plugin.isImperial(player.getUniqueId()))
		{			
			sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/imperials" + ChatColor.AQUA + " to see info about the Imperials");
		}
		
		if(plugin.isRebel(player.getUniqueId()))
		{			
			sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/rebels" + ChatColor.AQUA + " to see info about the Rebels");
		}
		
		sender.sendMessage("" + ChatColor.AQUA);
		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/vote help" + ChatColor.AQUA + " to see how to vote");

		if(plugin.isImperial(((Player)sender).getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/dc imperials " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_IMPERIALS, ChatColor.AQUA));
		}
		
		if(plugin.isRebel(((Player)sender).getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/dc rebels " + plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_REBELS, ChatColor.AQUA));
		}

		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/dc <player>" + ChatColor.AQUA + " to view info about a player");

	}

	private void sendKingQueenWho(CommandSender sender)
	{
		String kingName = plugin.getKingName();
		String queenName = plugin.getQueenName();
		
		if (kingName == null)
		{
			sender.sendMessage(ChatColor.AQUA + "DoggyCraft has no King!");
		}
		else
		{
			long kingDays = plugin.getKingElectionDays();
			
			if(kingDays == 0)
			{
				sender.sendMessage(ChatColor.AQUA + "The king of DoggyCraft is " + ChatColor.GOLD + kingName);
			}
			else
			{
				sender.sendMessage(ChatColor.AQUA + "The king of DoggyCraft is " + ChatColor.GOLD + kingName + ChatColor.AQUA + " for " + ChatColor.GOLD + kingDays + " days");				
			}
		}
		
		if (queenName == null)
		{
			sender.sendMessage(ChatColor.AQUA + "DoggyCraft has no Queen!");
		}
		else
		{
			long queenDays = plugin.getQueenElectionDays();

			if(queenDays == 0)
			{
				sender.sendMessage(ChatColor.AQUA + "The queen of DoggyCraft is " + ChatColor.GOLD + queenName);
			}
			else
			{
				sender.sendMessage(ChatColor.AQUA + "The queen of DoggyCraft is " + ChatColor.GOLD + queenName + ChatColor.AQUA + " for " + ChatColor.GOLD + queenDays + " days");				
			}
		}
	}

	public void addBounty(Player player, Player targetPlayer, int bounty)
	{
		if(!plugin.isImperial(player.getUniqueId()))
		{
			player.sendMessage(ChatColor.RED + "Only an imperial can set a bounty a rebel");
			return;			
		}

		if(!plugin.isRebel(targetPlayer.getUniqueId()))
		{
			player.sendMessage(ChatColor.RED + "You can only set a bounty on a rebel");
			return;			
		}

		if(!plugin.getEconomyManager().has(player.getName(), bounty))
		{
			player.sendMessage(ChatColor.RED + "You do not have " + bounty + " wanks");
			return;
		}
		
		plugin.getEconomyManager().withdrawPlayer(player.getName(), bounty);
		plugin.getBountyManager().addBounty(targetPlayer, bounty);
		
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "A bounty of " + ChatColor.GOLD + bounty + " wanks " + ChatColor.AQUA + " was put on " + ChatColor.GOLD + targetPlayer.getName());
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "The total bounty on " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.AQUA + " is now " + ChatColor.GOLD + plugin.getBountyManager().getBounty(targetPlayer.getUniqueId()) + " wanks");
	}

	public void listBounties(Player player)
	{		
		List<Bounty> bounties = plugin.getBountyManager().getBounties();
		
		if(bounties==null)
		{
			player.sendMessage(ChatColor.RED + "There are no bounties on rebels");
			return;
		}
		
		int n = 1;
		
		player.sendMessage("");
		player.sendMessage(ChatColor.YELLOW + " --------- The Most Wanted Rebels --------- ");
		player.sendMessage("");
		
		for(Bounty bounty : bounties)
		{
			if(n<10)
			{
				player.sendMessage(ChatColor.WHITE + "  " + n + ". " + plugin.getServer().getOfflinePlayer(bounty.PlayerId).getName() + " - " + bounty.Bounty + " wanks");
			}
			
			n++;
		}
		
		//player.sendMessage("");
		//player.sendMessage("The Empire wants these players dead!");
	}

	public void updatePrefix(CommandSender sender, String[] args)
	{		
		Player player = plugin.getServer().getPlayer(args[0]);

		DramaCraft.instance().updatePrefix(player.getUniqueId());

		sender.sendMessage("Prefix updated.");
	}
	
	@EventHandler
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player player;

		if (sender instanceof Player)
		{
			player = (Player) sender;

			this.currentWorld = player.getWorld();
		}
		else
		{
			this.plugin.log(ChatColor.YELLOW + "------------------ " + this.plugin.getDescription().getFullName() + " ------------------");
			this.plugin.log("" + ChatColor.GOLD + plugin.getNumberOfRebels() + ChatColor.AQUA + " Rebels vs " + ChatColor.GOLD + plugin.getNumberOfImperials() + ChatColor.AQUA + " Imperials");
			this.plugin.log("");

			sendKingQueenWho(sender);
			this.plugin.log("");
					
			if (command.getName().equalsIgnoreCase("dc"))
			{
				if (args[0].equalsIgnoreCase("setimperial"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setImperial(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setrebel"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setRebel(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setnoble"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setNoble(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setinnercircle"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setInnerCircle(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setneutral"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setNeutral(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setking"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setKing(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setqueen"))
				{
					// if(args[1].equalsIgnoreCase("13370x"))
					{
						Player targetPlayer = plugin.getServer().getPlayer(args[1]);

						if (targetPlayer != null)
						{
							plugin.setQueen(targetPlayer.getUniqueId());
						}
						else
						{
							plugin.log("No such player " + args[0]);
						}
					}

					return true;
				}

				if(args[0].equalsIgnoreCase("updateprefix"))
				{
					//if(args[1].equalsIgnoreCase("13370x"))
					{
						this.updatePrefix(sender, args);
					}								

					return true;
				}		
			}

			return false;
		}
	
		// Player commands
		if(command.getName().equalsIgnoreCase("imperials"))
		{
			if(args.length==1)
			{
				if(args[0].equals("help"))
				{
					if(plugin.isRebel(player.getUniqueId()))
					{			
						imperialsHelp(sender);
						return true;
					}
				}

				if(args[0].equals("nobles"))
				{
					if(plugin.isImperial(player.getUniqueId()))
					{			
						noblesHelp(sender);
						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Only imperials can view members of the Imperial Nobility");
						return true;
					}
					
				}
			}
			else if(args.length==2)
			{
				if(args[0].equals("help"))
				{
					if(plugin.isRebel(player.getUniqueId()))
					{			
						imperialsHelp(sender);
						return true;
					}
				}
			}

			imperialsHelp(sender);
			return true;
		}			
		
		if(command.getName().equalsIgnoreCase("rebels"))
		{
			if(args.length==1)
			{
				if(args[0].equals("help"))
				{
					if(plugin.isRebel(player.getUniqueId()))
					{			
						rebelsHelp(sender);
						return true;
					}
				}
				
				if(args[0].equals("innercircle"))
				{
					if(plugin.isRebel(player.getUniqueId()))
					{			
						innerCircleHelp(sender);
						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Only rebels can view members of the Rebel Inner Circle");
						return true;
					}
					
				}

				if(args[0].equals("transmitter"))
				{
					if(plugin.isRebel(player.getUniqueId()))
					{			
						rebelsTransmitterHelp(sender);
						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Only rebels can view information about transmitters");
						return true;
					}
					
				}
			}

			rebelsHelp(sender);			

			return true;
		}							

		if(command.getName().equalsIgnoreCase("addbounty"))
		{
			if(args.length == 2)
			{
				Player targetPlayer = plugin.getServer().getPlayer(args[0]);
				int bounty = Integer.parseInt(args[1]);

				if(targetPlayer!=null)
				{
					addBounty((Player)sender, targetPlayer, bounty);
				}
				else
				{
					plugin.log("No such online player " + args[0]);
				}
			}								
			else
			{
				sender.sendMessage("Usage: /addbounty <playername> <bounty>");
			}

			return true;
		}		
			
		if(command.getName().equalsIgnoreCase("bounty"))
		{
			if(args.length == 0)
			{
				listBounties(player);
			}								

			return true;
		}		

		if(command.getName().equalsIgnoreCase("guard"))
		{
			if(plugin.isNoble(player.getUniqueId()))
			{			
				if(args.length == 0)
				{
					plugin.getBodyguardManager().spawnGuard(player);
				}	
			}

			return true;
		}		

		if(command.getName().equalsIgnoreCase("attack"))
		{
			//if(plugin.isNoble(player.getUniqueId()))
			/*
			{			
				if(args.length == 1)
				{
					Player target = plugin.getServer().getPlayer(args[0]);
					plugin.getBodyguardManager().spawnTerminator(player, target);
					plugin.getServer().broadcastMessage(ChatColor.AQUA + "A terminator has been sent towards " + ChatColor.GOLD + target.getName() + ChatColor.AQUA + "...");
				}	
				else
				{
					player.sendMessage("Usage: /terminator <playername>");
				}
			}
			*/

			return true;
		}		

		if(command.getName().equalsIgnoreCase("appoint"))
		{
			if(args.length!=2)
			{
				player.sendMessage(ChatColor.RED + "Usage: /appoint <playername> <rankname>");													
				return false;
			}
		
			String rankname = args[1]; 
			
			if(plugin.isNoble(player.getUniqueId()))
			{			
				if(rankname.equals("wizard") || rankname.equals("knight") || rankname.equals("farmer") || rankname.equals("shopkeeper"))
				{
					Player targetPlayer = plugin.getServer().getPlayer(args[0]);

					if(targetPlayer!=null)
					{					
						if(!plugin.isImperial(player.getUniqueId()))
						{
							player.sendMessage(ChatColor.RED + "Target player must be an Imperial");
							return true;							
						}
						
						if(plugin.isRoyal(player.getUniqueId()))
						{
							player.sendMessage(ChatColor.RED + "Target player cannot be an Imperial Noble or Royal");
							return true;							
						}

						if(rankname.equals("knight"))
						{
							rankname = "police";
						}
						
						plugin.getPermissionsManager().setRankGroup(targetPlayer, rankname);
						plugin.setNobleClientRank(player, targetPlayer.getUniqueId(), rankname);
						plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + " appointed " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.AQUA + " to imperial " + rankname);
						targetPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + " appointed you to " + rankname);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Target player must be online to be appointed");													
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + "Valid ranks are: wizard, knight, farmer or shopkeeper");																		
				}
			}
			else
			if(plugin.isInnerCircle(player.getUniqueId()))
			{			
				if(rankname.equals("wizard") || rankname.equals("rogue") || rankname.equals("farmer") || rankname.equals("shopkeeper"))
				{
					Player targetPlayer = plugin.getServer().getPlayer(args[0]);

					if(targetPlayer!=null)
					{					
						if(!plugin.isRebel(player.getUniqueId()))
						{			
							player.sendMessage(ChatColor.RED + "Target player must be a Rebel");																											
							return true;
						}
						
						if(plugin.isRingLeader(targetPlayer.getUniqueId()))
						{			
							player.sendMessage(ChatColor.RED + "Target player cannot be in the Rebel inner circle or boss");																											
							return true;
						}

						if(rankname.equals("rogue"))
						{
							rankname = "police";
						}

						plugin.getPermissionsManager().setRankGroup(targetPlayer, rankname);
						plugin.setInnerCircleClientRank(player, targetPlayer.getUniqueId(), rankname);
						plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + " appointed " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.AQUA + " to rebel " + rankname);
						targetPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + " appointed you to " + rankname);
					}
					else
					{
						player.sendMessage(ChatColor.RED + "Target player must be online to be appointed");													
					}
				}								
				else
				{
					player.sendMessage(ChatColor.RED + "Valid ranks are: wizard, rogue, farmer or shopkeeper");																		
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You do not have the rights to use this command");								
			}

			return true;
		}			
	
		if(command.getName().equalsIgnoreCase("dramacraft") || command.getName().equalsIgnoreCase("dc"))
		{
			if(args.length == 0)
			{
				dramaCraftInfo(sender);				
				return true;								
			}
			
			else if(args.length == 1)
			{
				if (args[0].equalsIgnoreCase("setkinghead"))
				{
					setKingHead(sender, player);
					return true;
				}
				else if (args[0].equalsIgnoreCase("setqueenhead"))
				{
					setQueenHead(sender, player);
					return true;
				}
				else 
				{
					playerInfo(sender, args);				
					return true;								
				}
			}

			else if(args.length == 2)
			{
			}
			
			dramaCraftInfo(sender);
			
			return true;
		}

		if(command.getName().equalsIgnoreCase("track"))
		{
			if(plugin.isImperial(player.getUniqueId()))
			{
				double distance = plugin.getTransmitterManager().getClosestDistanceToTransmitter(player.getLocation());
				
				if(distance < 999999)
				{
					player.sendMessage(ChatColor.GRAY + "Distance to nearest Rebel Transmitter: " + distance + " blocks");
				}
				else
				{
					player.sendMessage(ChatColor.RED + "There are no Rebel Transmitters in this world");
				}
			}								
/*
			if(plugin.isRebel(player.getUniqueId()))
			{
				double distance = plugin.getStatueManager().getClosestDistanceToStatue(player.getLocation());
				
				if(distance < 999999)
				{
					player.sendMessage(ChatColor.GRAY + "Distance to nearest Imperial Statue: " + distance + " blocks");
				}
				else
				{
					player.sendMessage(ChatColor.RED + "There are no Imperial Statues in this world");
				}
			}								
*/
			return true;
		}			


		
		if (command.getName().equalsIgnoreCase("vote"))
		{
			if (args.length == 0)
			{
				voteHelp(sender);

				return true;
			}
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info"))
				{
					voteHelp(sender);
					return true;
				}

				else if (args[0].equalsIgnoreCase("day"))
				{
					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_DAY, ""))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				else if (args[0].equalsIgnoreCase("night"))
				{
					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_NIGHT, ""))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				else if (args[0].equalsIgnoreCase("sun"))
				{
					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_SUN, ""))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				else if (args[0].equalsIgnoreCase("rain"))
				{
					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_RAIN, ""))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				else if (args[0].equalsIgnoreCase("revolution"))
				{
					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_REVOLUTION, ""))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				else if (args[0].equalsIgnoreCase("yes"))
				{
					doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					this.plugin.printlog(player.getName() + " voted yes");

					return true;
				}
				else if (args[0].equalsIgnoreCase("no"))
				{
					doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_NO);
					this.plugin.printlog(player.getName() + " voted no");

					return true;
				}

			}
			else if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("king"))
				{
					Player targetPlayer = plugin.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.RED));
						return true;
					}

					if (targetPlayer.isOp())
					{
						sender.sendMessage(ChatColor.RED + "No.");
						return true;
					}

					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_KING, targetPlayer.getUniqueId().toString()))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				else if (args[0].equalsIgnoreCase("queen"))
				{
					Player targetPlayer = plugin.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.RED));
						return true;
					}

					if (targetPlayer.isOp())
					{
						sender.sendMessage(ChatColor.RED + "No.");
						return true;
					}

					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_QUEEN, targetPlayer.getUniqueId().toString()))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				if (args[0].equalsIgnoreCase("noble"))
				{
					Player targetPlayer = plugin.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.RED));
						return true;
					}

					if (targetPlayer.isOp())
					{
						sender.sendMessage(ChatColor.RED + "Admins can not be an Imperial Noble.");
						return true;
					}

					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_NOBLE, targetPlayer.getUniqueId().toString()))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
				if (args[0].equalsIgnoreCase("innercircle"))
				{
					Player targetPlayer = plugin.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(plugin.getLanguageManager().getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.RED));
						return true;
					}

					if (targetPlayer.isOp())
					{
						sender.sendMessage(ChatColor.RED + "Admins can not be in the Rebel Inner Circle.");
						return true;
					}

					if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_INNERCIRCLE, targetPlayer.getUniqueId().toString()))
					{
						doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_YES);
					}

					return true;
				}
			}
			else if (args[0].equalsIgnoreCase("question"))
			{
				String questionText = "";

				for (int i = 1; i < args.length; i++)
				{
					questionText += args[i] + " ";
				}

				questionText = questionText.trim();

				if (newVote(sender, player, VoteManager.VOTE_TYPE.VOTE_GENERAL, questionText))
				{
				}

				return true;
			}
		}

		dramaCraftInfo(sender);

		return true;
	}
	
	private void playerInfo(CommandSender sender, String[] args)
	{
		OfflinePlayer player = Bukkit.getPlayer(args[0]);
		
		if(player == null)
		{
			sender.sendMessage(ChatColor.DARK_RED + "No such player '" + args[0] + "'");			
			return;
		}

		String title = "--------- Info about " + player.getName() + "  ---------";
		
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + title);		
		sender.sendMessage("");
		
		if(DramaCraft.instance().isImperial(player.getUniqueId()))
		{
			if(DramaCraft.instance().isKing(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.GOLD + "King");			
			}
			if(DramaCraft.instance().isQueen(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.GOLD + "Queen");			
			}
			if(DramaCraft.instance().isNoble(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.DARK_BLUE + "Imperial Noble");			
			}
			else
			{
				sender.sendMessage(ChatColor.AQUA + "Imperial");							
			}
			
			Date joinDate = DramaCraft.instance().getJoinDate(player.getUniqueId());
			
			if(joinDate != null)
			{
				sender.sendMessage("");							
				sender.sendMessage(ChatColor.GRAY + "Joined " + joinDate.toString());						
			}
		}
		
		else if(DramaCraft.instance().isRebel(player.getUniqueId()))
		{
			if(DramaCraft.instance().isRingLeader(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.GOLD + "Ringleader");			
			}
			if(DramaCraft.instance().isInnerCircle(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.DARK_BLUE + "Inner Circle");			
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Rebel ");							
			}			
	
			Date joinDate = DramaCraft.instance().getJoinDate(player.getUniqueId());
			
			if(joinDate != null)
			{
				sender.sendMessage("");							
				sender.sendMessage(ChatColor.GRAY + "Joined " + joinDate.toString());						
			}
		}

		else 
		{
			sender.sendMessage(ChatColor.GRAY + "Neutral - Not part of DramaCraft");										
		}

		sender.sendMessage(ChatColor.YELLOW + StringUtils.repeat("-", title.length()));		
	}

	private void setKingHead(CommandSender sender, Player player)
	{
		if(player==null || !player.isOp())
		{
			return;
		}
		
		String kingName = plugin.getKingName();	
		
		if(kingName!=null)
		{
			plugin.setKingHead(player.getUniqueId(), player.getLocation());
		}
	}

	private void setQueenHead(CommandSender sender, Player player)
	{
		if(player==null || !player.isOp())
		{
			return;
		}
		
		String queenName = plugin.getQueenName();		
		
		if(queenName!=null)
		{
			plugin.setQueenHead(player.getUniqueId(), player.getLocation());
		}
	}
	
	private boolean isDay(long currenttime, int offset)
	{
		return (currenttime < 12000 + offset) && (currenttime > offset);
	}

	private boolean isSun(World world)
	{
		if ((world.hasStorm()) || (world.isThundering()))
		{
			return false;
		}
		return true;
	}
	
	private void noblesHelp(CommandSender sender)
	{
		//sender.sendMessage(ChatColor.WHITE + "The Empire has " + ChatColor.GOLD + plugin.getStatueManager().getStatues() + ChatColor.WHITE + " statues placed across these lands");
		
		Set<String> nobles = plugin.getNobles();
		List<Member> members = new ArrayList<Member>();
		
		for(String member : nobles)
		{
			UUID playerId = UUID.fromString(member);
			long days = plugin.getNobleElectionDays(playerId);
			
			members.add(new Member(playerId, days));
		}

		Collections.sort(members, new MemberComparator());
		
		String title = " --------- The Imperial Nobility --------- ";
		
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + title);

		for(Member m : members)
		{
			OfflinePlayer player = plugin.getServer().getOfflinePlayer(m.PlayerId);
			
			if(m.Days<=7)
			{
				sender.sendMessage(" " + player.getName() + "   Last login: " + ChatColor.GREEN + m.Days + ChatColor.WHITE + " days ago");
			}
			else
			{
				sender.sendMessage(" " + player.getName() + "   Last login: " + ChatColor.RED + m.Days + ChatColor.WHITE + " days ago");
			}
		}

		sender.sendMessage(ChatColor.YELLOW + StringUtils.repeat("-", title.length()));		
	}

	private void rebelsHelp(CommandSender sender)
	{	
		String title = " --------- " + ChatColor.RED + "Rebels" + ChatColor.YELLOW + " -------- ";

		sender.sendMessage(ChatColor.YELLOW + title);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA + "As a " + ChatColor.RED + "REBEL" + ChatColor.WHITE + " it is your duty to challenge the King, Queen and the evil empire they rule!");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA + "- Spread the truth about the Empire by building transmitters");
		sender.sendMessage(ChatColor.AQUA + "- Mine ore to contribute resources to the rebel stash");			
		sender.sendMessage(ChatColor.AQUA + "- Vote players into the rebel inner circle");			
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/rebels revolution" + ChatColor.AQUA + " to see how to start a revolution");			
		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/rebels innercircle" + ChatColor.AQUA + " to see info about the Inner Circle");			
		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/rebels transmitter" + ChatColor.AQUA + " to see how to build a rebel transmitter");			
		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/rebels resources" + ChatColor.AQUA + " to see how to provide resources for the rebel cause");			

		sender.sendMessage(ChatColor.YELLOW + StringUtils.repeat("-", title.length()));		
	}

	private void innerCircleHelp(CommandSender sender)
	{	
		Set<String> innerCircle = plugin.getInnerCircle();
		List<Member> members = new ArrayList<Member>();
		
		for(String member : innerCircle)
		{
			UUID playerId = UUID.fromString(member);
			long days = plugin.getNobleElectionDays(playerId);
			
			members.add(new Member(playerId, days));
		}

		Collections.sort(members, new MemberComparator());
		
		String title = "--------- The Rebel Inner Circle --------";

		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + title);

		for(Member m : members)
		{
			Player player = plugin.getServer().getPlayer(m.PlayerId);
			if(m.Days<=7)
			{
				sender.sendMessage(" " + player.getName() + "   Last login: " + ChatColor.GREEN + m.Days + ChatColor.WHITE + " days ago");
			}
			else
			{
				sender.sendMessage(" " + player.getName() + "   Last login: " + ChatColor.RED + m.Days + ChatColor.WHITE + " days ago");
			}
		}

		sender.sendMessage(ChatColor.YELLOW + StringUtils.repeat("-", title.length()));		
	}

	private void rebelsTransmitterHelp(CommandSender sender)
	{	
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + "--------- How to build a Rebel Transmitter --------");
		sender.sendMessage(ChatColor.WHITE + "  1) Place a STONE block");
		sender.sendMessage(ChatColor.WHITE + "  2) Place a TORCH on top of the STONE block");
		sender.sendMessage(ChatColor.WHITE + "  3) Place an OAK SIGN on the STONE block");
		sender.sendMessage(ChatColor.WHITE + "  4) Write your TRUTH message on the sign");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.WHITE + "Try to be creative and dramatic in your message ;-)");			
	}

	private void imperialsHelp(CommandSender sender)
	{	
		sender.sendMessage(ChatColor.YELLOW + "--------- " + ChatColor.AQUA + "Imperials" + ChatColor.YELLOW + " --------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA + "As an " + ChatColor.AQUA + "IMPERIAL" + ChatColor.AQUA + " it is your duty to protect the empire and keep the peace!");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA + "- Vote players into the Imperial Nobles");			
		sender.sendMessage(ChatColor.AQUA + "- Make sure that all rebel transmitters are destroyed");			
		sender.sendMessage(ChatColor.AQUA + "- Mine ore to contribute resources to the imperial treasury");			
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA + "Use " + ChatColor.WHITE + "/imperials nobles" + ChatColor.AQUA + " to see info about the Inner Circle");			
		//sender.sendMessage(ChatColor.WHITE + "Use /imperials help to see more information");			
	}

	double roundTwoDecimals(double d)
	{
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d)).doubleValue();
	}
}