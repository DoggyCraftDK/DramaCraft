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
	static private Commands	instance;
	private World			currentWorld;

	public Commands()
	{
		instance = this;
	}
	
	private void doVote(CommandSender sender, Player player, VoteManager.VOTE_TYPE voteType)
	{
		if (VoteManager.getCurrentVoteType() == VoteManager.VOTE_TYPE.VOTE_NONE)
		{
			sender.sendMessage(ChatColor.RED + LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_NOTHING_TO_VOTE, ChatColor.RED));
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

		VoteManager.doVote(this.currentWorld, player, true, voteType);
		VoteManager.checkVote(40);
	}

	static private boolean newVote(CommandSender sender, Player player, VoteManager.VOTE_TYPE voteType, String text)
	{
		if (VoteManager.getCurrentVoteType() != VoteManager.VOTE_TYPE.VOTE_NONE)
		{
			String message = "NO_ALREADY";

			switch (VoteManager.getCurrentVoteType())
			{
				case VOTE_INNERCIRCLE: 	message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_KING, ChatColor.RED); break;
				case VOTE_NOBLE: 		message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_KING, ChatColor.RED); break;
				case VOTE_KING: 		message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_KING, ChatColor.RED); break;
				case VOTE_QUEEN: 		message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_QUEEN, ChatColor.RED); break;
				case VOTE_NIGHT: 		message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_NIGHT, ChatColor.RED); break;
				case VOTE_DAY: 			message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_DAY, ChatColor.RED); break;
				case VOTE_SUN: 			message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_SUN, ChatColor.RED); break;
				case VOTE_RAIN:			message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_RAIN, ChatColor.RED); break;
				case VOTE_GENERAL: 		message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_ALREADY_GENERAL, ChatColor.RED); break;
				case VOTE_HELP: 		voteHelp(sender); break;
				default:				break;
			}
			
			sender.sendMessage(ChatColor.RED + message);

			return false;
		}

		return VoteManager.newVote(instance.currentWorld, player, text, true, voteType);
	}

	static private void voteHelp(CommandSender sender)
	{
		//sender.sendMessage("" + ChatColor.WHITE + plugin.getLanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_HEAD, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_INFO, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote day " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_DAY, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote night " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NIGHT, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote sun " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_SUN, ChatColor.AQUA));
		sender.sendMessage("" + ChatColor.WHITE + "/vote rain " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_RAIN, ChatColor.AQUA));
		
		Player player = (Player)sender;
		
		if(RankManager.isNoble(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote king <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_KING, ChatColor.AQUA));
			sender.sendMessage("" + ChatColor.WHITE + "/vote queen <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_QUEEN, ChatColor.AQUA));
		}

		if(RankManager.isNoble(player.getUniqueId()) || RankManager.getActiveNobles() < 3)
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote noble <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE, ChatColor.AQUA));
		}

		if(RankManager.isNoble(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote kicknoble <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE_KICK, ChatColor.AQUA));
		}

		if(RankManager.isInnerCircle(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote ringleader <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_KING, ChatColor.AQUA));
		}

		if(RankManager.isInnerCircle(player.getUniqueId()) || RankManager.getActiveInnerCircle() < 3)
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote innercircle <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE, ChatColor.AQUA));
		}

		if(RankManager.isInnerCircle(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote kickinnercircle <playername> " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_NOBLE_KICK, ChatColor.AQUA));
		}

		sender.sendMessage("" + ChatColor.WHITE + "/vote question " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_QUESTION, ChatColor.AQUA));

		if(RankManager.isRebel(player.getUniqueId()))
		{	
			sender.sendMessage("" + ChatColor.WHITE + "/vote revolution " + ChatColor.GRAY + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_REVOLUTION, ChatColor.AQUA));
		}
		
	}

	private void dramaCraftInfo(CommandSender sender)
	{
		sender.sendMessage(ChatColor.YELLOW + "------------------ " + DramaCraft.instance().getDescription().getFullName() + " ------------------");
		sender.sendMessage(ChatColor.GRAY + "By DogOnFire");
		sender.sendMessage("");
		
		sender.sendMessage("" + ChatColor.RED + RankManager.getNumberOfRebels() + " Rebels " + ChatColor.GRAY + "vs " + ChatColor.AQUA + RankManager.getNumberOfImperials() + " Imperials");
		sender.sendMessage("");
		sender.sendMessage("" + ChatColor.GRAY + "Imperials has " + ChatColor.GOLD + ResourceManager.getImperialResources() + ChatColor.AQUA + " resources.");
		sender.sendMessage("" + ChatColor.GRAY + "Rebels has " + ChatColor.GOLD + ResourceManager.getRebelResources() + ChatColor.AQUA + " resources.");
		sender.sendMessage("");

		sendKingQueenWho(sender);

		sender.sendMessage("");

		Player player = (Player)sender;

		if(RankManager.isKing(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are the " + ChatColor.GOLD + "King");
		}
		else if(RankManager.isQueen(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are the " + ChatColor.GOLD + "Queen");
		}
		else if(RankManager.isNoble(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are an Imperial Noble");
		}
		else if(RankManager.isImperial(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are an " + ChatColor.AQUA + "Imperial");
		}

		else if(RankManager.isRingLeader(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are the " + ChatColor.RED + "Rebel Ringleader");
		}
		else if(RankManager.isInnerCircle(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are part of the " + ChatColor.RED + "Rebel Inner Circle");
		}
		else if(RankManager.isRebel(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are a " + ChatColor.RED + "Rebel");
		}

		else if(RankManager.isNeutral(player.getUniqueId()))
		{	
			sender.sendMessage(ChatColor.WHITE + "You are Neutral");
		}
		
		sender.sendMessage("");

		if(RankManager.isImperial(player.getUniqueId()))
		{			
			sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/imperials" + ChatColor.GRAY + " to see info about the Imperials");
		}
		
		if(RankManager.isRebel(player.getUniqueId()))
		{			
			sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/rebels" + ChatColor.GRAY + " to see info about the Rebels");
		}
		
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/dc <player>" + ChatColor.GRAY + " to view info about a player");
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/vote help" + ChatColor.GRAY + " to see how to vote");

		/*
		if(RankManager.isImperial(((Player)sender).getUniqueId()))
		{	
			sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/dc imperials " + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_IMPERIALS, ChatColor.GRAY));
		}
		
		if(RankManager.isRebel(((Player)sender).getUniqueId()))
		{	
			sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/dc rebels " + LanguageManager.getLanguageString(LANGUAGESTRING.VOTING_COMMANDS_VOTE_DESC_REBELS, ChatColor.GRAY));
		}
		*/


	}

	private void sendKingQueenWho(CommandSender sender)
	{
		String kingName = RankManager.getKingName();
		String queenName = RankManager.getQueenName();
		
		if (kingName == null)
		{
			sender.sendMessage(ChatColor.GRAY + "DoggyCraft has no King!");
		}
		else
		{
			long kingDays = RankManager.getKingElectionDays();
			
			if(kingDays == 0)
			{
				sender.sendMessage(ChatColor.GRAY + "The king of DoggyCraft is " + ChatColor.GOLD + kingName);
			}
			else
			{
				sender.sendMessage(ChatColor.GRAY + "The king of DoggyCraft is " + ChatColor.GOLD + kingName + ChatColor.GRAY + " for " + ChatColor.GOLD + kingDays + " days");				
			}
		}
		
		if (queenName == null)
		{
			sender.sendMessage(ChatColor.GRAY + "DoggyCraft has no Queen!");
		}
		else
		{
			long queenDays = RankManager.getQueenElectionDays();

			if(queenDays == 0)
			{
				sender.sendMessage(ChatColor.GRAY + "The queen of DoggyCraft is " + ChatColor.GOLD + queenName);
			}
			else
			{
				sender.sendMessage(ChatColor.GRAY + "The queen of DoggyCraft is " + ChatColor.GOLD + queenName + ChatColor.GRAY + " for " + ChatColor.GOLD + queenDays + " days");				
			}
		}
	}

	public void addBounty(Player player, Player targetPlayer, int bounty)
	{
		if(!RankManager.isImperial(player.getUniqueId()))
		{
			player.sendMessage(ChatColor.DARK_RED + "Only an imperial can set a bounty a rebel");
			return;			
		}

		if(!RankManager.isRebel(targetPlayer.getUniqueId()))
		{
			player.sendMessage(ChatColor.DARK_RED + "You can only set a bounty on a rebel");
			return;			
		}

		if(!DramaCraft.instance().getEconomyManager().has(player.getName(), bounty))
		{
			player.sendMessage(ChatColor.DARK_RED + "You do not have " + bounty + " wanks");
			return;
		}
		
		DramaCraft.instance().getEconomyManager().withdrawPlayer(player.getName(), bounty);
		BountyManager.addBounty(targetPlayer, bounty);
		
		Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "A bounty of " + ChatColor.GOLD + bounty + " wanks " + ChatColor.GRAY + " was put on " + ChatColor.GOLD + targetPlayer.getName());
		Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "The total bounty on " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GRAY + " is now " + ChatColor.GOLD + BountyManager.getBounty(targetPlayer.getUniqueId()) + " wanks");
	}

	public void listBounties(Player player)
	{		
		List<Bounty> bounties = BountyManager.getBounties();
		
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
				player.sendMessage(ChatColor.WHITE + "  " + n + ". " + Bukkit.getServer().getOfflinePlayer(bounty.PlayerId).getName() + " - " + bounty.Bounty + " wanks");
			}
			
			n++;
		}
		
		//player.sendMessage("");
		//player.sendMessage("The Empire wants these players dead!");
	}

	public void updatePrefix(CommandSender sender, String[] args)
	{		
		Player player = Bukkit.getServer().getPlayer(args[0]);

		RankManager.updatePrefix(player.getUniqueId());

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
			DramaCraft.log(ChatColor.YELLOW + "------------------ " + DramaCraft.instance().getDescription().getFullName() + " ------------------");
			DramaCraft.log("" + ChatColor.RED + RankManager.getNumberOfRebels() + " Rebels" + ChatColor.GOLD + " vs " + ChatColor.AQUA + RankManager.getNumberOfImperials() + " Imperials");
			DramaCraft.log("");

			sendKingQueenWho(sender);
			DramaCraft.log("");
					
			if (command.getName().equalsIgnoreCase("dc"))
			{
				if (args.length == 0)
				{
					sender.sendMessage(ChatColor.YELLOW + "------------------ " + DramaCraft.instance().getDescription().getFullName() + " ------------------");
					sender.sendMessage(ChatColor.GRAY + "By DogOnFire");
					sender.sendMessage("");

					sender.sendMessage("" + ChatColor.RED + RankManager.getNumberOfRebels() + " Rebels " + ChatColor.GRAY + "vs " + ChatColor.AQUA + RankManager.getNumberOfImperials() + " Imperials");
					sender.sendMessage("" + ChatColor.GRAY);
					sender.sendMessage("" + ChatColor.GRAY + "Imperials has " + ChatColor.GOLD + ResourceManager.getImperialResources() + ChatColor.GRAY + " resources.");
					sender.sendMessage("" + ChatColor.GRAY + "Rebels has " + ChatColor.GOLD + ResourceManager.getRebelResources() + ChatColor.GRAY + " resources.");
					sender.sendMessage("" + ChatColor.GRAY);
					return true;
				}

				if (args[0].equalsIgnoreCase("setimperial"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setImperial(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setrebel"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setRebel(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setnoble"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setNoble(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setinnercircle"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setInnerCircle(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setneutral"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setNeutral(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setking"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setKing(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setqueen"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setQueen(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setringleader1"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setRingLeader1(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("setringleader2"))
				{
					final OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

					if (targetPlayer != null)
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(DramaCraft.instance(), new Runnable()
						{
							@Override
							public void run()
							{
								RankManager.setRingLeader2(targetPlayer.getUniqueId());
							}
						});
					}
					else
					{
						DramaCraft.log("No such player " + args[1]);
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
					if(RankManager.isRebel(player.getUniqueId()))
					{			
						imperialsHelp(sender);
						return true;
					}
				}

				if(args[0].equals("nobles"))
				{
					if(RankManager.isImperial(player.getUniqueId()))
					{			
						noblesHelp(sender);
						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "Only imperials can view members of the Imperial Nobility");
						return true;
					}
					
				}
			}
			else if(args.length==2)
			{
				if(args[0].equals("help"))
				{
					if(RankManager.isRebel(player.getUniqueId()))
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
					if(RankManager.isRebel(player.getUniqueId()))
					{			
						rebelsHelp(sender);
						return true;
					}
				}
				
				if(args[0].equals("innercircle"))
				{
					if(RankManager.isRebel(player.getUniqueId()))
					{			
						innerCircleHelp(sender);
						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "Only rebels can view members of the Rebel Inner Circle");
						return true;
					}
					
				}

				if(args[0].equals("transmitter"))
				{
					if(RankManager.isRebel(player.getUniqueId()))
					{			
						rebelsTransmitterHelp(sender);
						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "Only rebels can view information about transmitters");
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
				Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
				int bounty = Integer.parseInt(args[1]);

				if(targetPlayer!=null)
				{
					addBounty((Player)sender, targetPlayer, bounty);
				}
				else
				{
					DramaCraft.log("No such online player " + args[0]);
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
			if(RankManager.isNoble(player.getUniqueId()))
			{			
				if(args.length == 0)
				{
					BodyguardManager.spawnGuard(player);
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
				player.sendMessage(ChatColor.DARK_RED + "Usage: /appoint <playername> <rankname>");													
				return false;
			}
		
			String rankname = args[1]; 
			
			if(RankManager.isNoble(player.getUniqueId()))
			{			
				if(rankname.equals("wizard") || rankname.equals("knight") || rankname.equals("farmer") || rankname.equals("treasurer"))
				{
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);

					if(targetPlayer!=null)
					{					
						if(!RankManager.isImperial(player.getUniqueId()))
						{
							player.sendMessage(ChatColor.DARK_RED + "Target player must be an Imperial");
							return true;							
						}
						
						if(RankManager.isRoyal(player.getUniqueId()))
						{
							player.sendMessage(ChatColor.DARK_RED + "Target player cannot be an Imperial Noble or Royal");
							return true;							
						}

						if(rankname.equals("knight"))
						{
							rankname = "police";
						}
						
						PermissionsManager.setRankGroup(targetPlayer, rankname);
						RankManager.setNobleClientRank(player, targetPlayer.getUniqueId(), rankname);
						Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " appointed " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GRAY + " to imperial " + rankname);
						targetPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " appointed you to " + rankname);
					}
					else
					{
						player.sendMessage(ChatColor.DARK_RED + "Target player must be online to be appointed");													
					}
				}
				else
				{
					player.sendMessage(ChatColor.DARK_RED + "Valid ranks are: wizard, knight, farmer or treasurer");																		
				}
			}
			else
			if(RankManager.isInnerCircle(player.getUniqueId()))
			{			
				if(rankname.equals("wizard") || rankname.equals("rogue") || rankname.equals("farmer") || rankname.equals("dealer"))
				{
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);

					if(targetPlayer!=null)
					{					
						if(!RankManager.isRebel(player.getUniqueId()))
						{			
							player.sendMessage(ChatColor.DARK_RED + "Target player must be a Rebel");																											
							return true;
						}
						
						if(RankManager.isRingLeader(targetPlayer.getUniqueId()))
						{			
							player.sendMessage(ChatColor.DARK_RED + "Target player cannot be in the Rebel inner circle or boss");																											
							return true;
						}

						if(rankname.equals("rogue"))
						{
							rankname = "police";
						}

						PermissionsManager.setRankGroup(targetPlayer, rankname);
						RankManager.setInnerCircleClientRank(player, targetPlayer.getUniqueId(), rankname);
						Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + " appointed " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.AQUA + " to rebel " + rankname);
						targetPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.AQUA + " appointed you to " + rankname);
					}
					else
					{
						player.sendMessage(ChatColor.DARK_RED + "Target player must be online to be appointed");													
					}
				}								
				else
				{
					player.sendMessage(ChatColor.DARK_RED + "Valid ranks are: wizard, rogue, farmer or shopkeeper");																		
				}
			}
			else
			{
				player.sendMessage(ChatColor.DARK_RED + "You do not have the rights to use this command");								
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
					if(sender.isOp())
					{
						setKingHead(sender, player);
					}
					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "Only players with op can set king head blocks");																		
					}
					
					return true;
				}
				else if (args[0].equalsIgnoreCase("setqueenhead"))
				{
					if(sender.isOp())
					{
						setQueenHead(sender, player);
					}
					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "Only players with op can set queen head blocks");																		
					}
					
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
			if(RankManager.isImperial(player.getUniqueId()))
			{
				double distance = RebelTransmitterManager.getClosestDistanceToTransmitter(player.getLocation());
				
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
					DramaCraft.log(player.getName() + " voted yes");

					return true;
				}
				else if (args[0].equalsIgnoreCase("no"))
				{
					doVote(sender, player, VoteManager.VOTE_TYPE.VOTE_NO);
					DramaCraft.log(player.getName() + " voted no");

					return true;
				}

			}
			else if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("king"))
				{
					Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.DARK_RED));
						return true;
					}

					if (targetPlayer.isOp())
					{
						sender.sendMessage(ChatColor.DARK_RED + "No.");
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
					Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.DARK_RED));
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
					Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.DARK_RED));
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
					Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);

					if (targetPlayer == null)
					{
						sender.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_PLAYER_NOT_ONLINE, ChatColor.DARK_RED));
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
		
		if(RankManager.isImperial(player.getUniqueId()))
		{
			if(RankManager.isKing(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.GOLD + "King");			
			}
			if(RankManager.isQueen(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.GOLD + "Queen");			
			}
			if(RankManager.isNoble(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.DARK_BLUE + "Imperial Noble");			
			}
			else
			{
				sender.sendMessage(ChatColor.AQUA + "Imperial");							
			}
			
			Date joinDate = RankManager.getJoinDate(player.getUniqueId());
			
			if(joinDate != null)
			{
				sender.sendMessage("");							
				sender.sendMessage(ChatColor.GRAY + "Joined " + joinDate.toString());						
			}
		}
		
		else if(RankManager.isRebel(player.getUniqueId()))
		{
			if(RankManager.isRingLeader(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.GOLD + "Ringleader");			
			}
			if(RankManager.isInnerCircle(player.getUniqueId()))
			{
				sender.sendMessage(ChatColor.DARK_BLUE + "Inner Circle");			
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Rebel ");							
			}			
	
			Date joinDate = RankManager.getJoinDate(player.getUniqueId());
			
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
		
		String kingName = RankManager.getKingName();	
		
		if(kingName!=null)
		{
			RankManager.setKingHead(player.getUniqueId(), player.getLocation());
		}
	}

	private void setQueenHead(CommandSender sender, Player player)
	{
		if(player==null || !player.isOp())
		{
			return;
		}
		
		String queenName = RankManager.getQueenName();		
		
		if(queenName!=null)
		{
			RankManager.setQueenHead(player.getUniqueId(), player.getLocation());
		}
	}
		
	private void noblesHelp(CommandSender sender)
	{
		//sender.sendMessage(ChatColor.WHITE + "The Empire has " + ChatColor.GOLD + plugin.getStatueManager().getStatues() + ChatColor.WHITE + " statues placed across these lands");
		
		Set<String> nobles = RankManager.getNobles();
		List<Member> members = new ArrayList<Member>();
		
		for(String member : nobles)
		{
			UUID playerId = UUID.fromString(member);
			long days = RankManager.getNobleElectionDays(playerId);
			
			members.add(new Member(playerId, days));
		}

		Collections.sort(members, new MemberComparator());
		
		String title = " --------- The Imperial Nobility --------- ";
		
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + title);

		if(members.size() == 0)
		{
			sender.sendMessage(ChatColor.GRAY + " There are currently no members of the Imperial Nobility");			
		}
		
		for(Member m : members)
		{
			OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(m.PlayerId);
			
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

	private void royalHelp(CommandSender sender)
	{	
		String title = " --------- " + ChatColor.RED + "King & Queen" + ChatColor.YELLOW + " -------- ";

		sender.sendMessage(ChatColor.YELLOW + title);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "As the " + ChatColor.GOLD + "KING" + ChatColor.GRAY + " or " + ChatColor.GOLD + "QUEEN" + " it is your duty to rule the Kingdom and preserve order!");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "- Set bounties on Rebels and bring them to justice");
		sender.sendMessage(ChatColor.GRAY + "- Buy cannons to protect the kingdom");			
		sender.sendMessage(ChatColor.GRAY + "- Hire guards in order to protect the Kingdom tresury");			
		sender.sendMessage(ChatColor.GRAY + "- Hire knights to attack the Rebel city");			
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/imperials revolution" + ChatColor.GRAY + " to see how to start a revolution");			
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/imperials innercircle" + ChatColor.GRAY + " to see info about the Inner Circle");			
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/imperials transmitter" + ChatColor.GRAY + " to see how to build a rebel transmitter");			
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/imperials resources" + ChatColor.GRAY + " to see how to provide resources for the rebel cause");			

		sender.sendMessage(ChatColor.YELLOW + StringUtils.repeat("-", title.length()));		
	}

	private void rebelsHelp(CommandSender sender)
	{	
		String title = " --------- " + ChatColor.RED + "Rebels" + ChatColor.YELLOW + " -------- ";

		sender.sendMessage(ChatColor.YELLOW + title);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "As a " + ChatColor.RED + "REBEL" + ChatColor.GRAY + " it is your duty to challenge the King, Queen and the evil empire they rule!");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "- Spread the truth about the Empire by building transmitters");
		sender.sendMessage(ChatColor.GRAY + "- Mine ore to contribute resources to the rebel stash");			
		sender.sendMessage(ChatColor.GRAY + "- Vote players into the rebel inner circle");			
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/rebels revolution" + ChatColor.GRAY + " to see how to start a revolution");			
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/rebels innercircle" + ChatColor.GRAY + " to see info about the Inner Circle");			
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/rebels transmitter" + ChatColor.GRAY + " to see how to build a rebel transmitter");			
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/rebels resources" + ChatColor.GRAY + " to see how to provide resources for the rebel cause");			

		sender.sendMessage(ChatColor.YELLOW + StringUtils.repeat("-", title.length()));		
	}

	private void innerCircleHelp(CommandSender sender)
	{	
		Set<String> innerCircle = RankManager.getInnerCircle();
		List<Member> members = new ArrayList<Member>();
		
		for(String member : innerCircle)
		{
			UUID playerId = UUID.fromString(member);
			long days = RankManager.getNobleElectionDays(playerId);
			
			members.add(new Member(playerId, days));
		}

		Collections.sort(members, new MemberComparator());
		
		String title = "--------- The Rebel Inner Circle --------";

		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + title);

		if(members.size() == 0)
		{
			sender.sendMessage(ChatColor.GRAY + " There are currently no members of the Inner Circle");			
		}

		for(Member m : members)
		{
			Player player = Bukkit.getServer().getPlayer(m.PlayerId);
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
		sender.sendMessage(ChatColor.GRAY + "  1) Place a STONE block");
		sender.sendMessage(ChatColor.GRAY + "  2) Place a TORCH on top of the STONE block");
		sender.sendMessage(ChatColor.GRAY + "  3) Place an OAK SIGN on the STONE block");
		sender.sendMessage(ChatColor.GRAY + "  4) Write your TRUTH message on the sign");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Try to be creative and dramatic in your message ;-)");			
	}

	private void imperialsHelp(CommandSender sender)
	{	
		sender.sendMessage(ChatColor.YELLOW + "--------- " + ChatColor.AQUA + "Imperials" + ChatColor.YELLOW + " --------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "As an " + ChatColor.AQUA + "IMPERIAL" + ChatColor.GRAY + " it is your duty to protect the empire and keep the peace!");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "- Vote players into the Imperial Nobles");			
		sender.sendMessage(ChatColor.GRAY + "- Make sure that all rebel transmitters are destroyed");			
		sender.sendMessage(ChatColor.GRAY + "- Mine ore to contribute resources to the imperial treasury");			
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.WHITE + "/imperials nobles" + ChatColor.GRAY + " to see info about the Inner Circle");			
		//sender.sendMessage(ChatColor.WHITE + "Use /imperials help to see more information");			
	}

	double roundTwoDecimals(double d)
	{
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d)).doubleValue();
	}
}