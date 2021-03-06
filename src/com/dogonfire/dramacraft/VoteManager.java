package com.dogonfire.dramacraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.dogonfire.dramacraft.LanguageManager.LANGUAGESTRING;
import com.dogonfire.dramacraft.votes.Vote;


public class VoteManager
{
	public String				name;
	public String				voteString;
	public UUID					lastVoterId 		= UUID.randomUUID();
	private long				startVoteTime		= 0;
	private long				lastVoteTime		= 0;
	private VOTE_TYPE			currentVoteType;
	private Random				random				= new Random();
	static private VoteManager 	instance;

	
	public static enum VOTE_TYPE
	{
		VOTE_NONE,
		VOTE_HOF,
		VOTE_GENERAL,
		VOTE_INFO,
		VOTE_HELP,
		VOTE_YES,
		VOTE_NO,
		VOTE_DAY,
		VOTE_NIGHT,
		VOTE_RAIN,
		VOTE_SUN,
		VOTE_REVOLUTION,
		VOTE_NOBLE,
		VOTE_NOBLE_KICK,
		VOTE_INNERCIRCLE,
		VOTE_INNERCIRCLE_KICK,
		VOTE_DISABLE_PHANTOMS,
		MASTER_COIN,
		MASTER_LAW,
		MASTER_WAR,
		VOTE_KING,
		VOTE_QUEEN,
		VOTE_RINGLEADER1,
		VOTE_RINGLEADER2,
		VOTE_FAME,
		VOTE_SHAME,
	}
	
	/*
	most
	least
	
	singer
	builder
	funny
	badass
	prettiest
	
	
	boy
	girl
	
	public void setPlayerSign()
	{
		HOF_QUEEN
		HOF_KING
		HOF_CUTEST_GIRL
		HOF_BEST_BUILDER
		HOF_
	}
	*/	

	public List<String>	all				= new ArrayList<String>();
	public List<String>	yes				= new ArrayList<String>();
	public List<String>	no				= new ArrayList<String>();
	
		
	VoteManager()
	{
		instance = this;		
	}

	private static void resetVotes()
	{
		instance.currentVoteType = VOTE_TYPE.VOTE_NONE;
		instance.yes.clear();
		instance.no.clear();
	}
		
	static public void checkVote()
	{
		String broadcast = "";
		boolean success = false;
				
		if(RevolutionManager.isRevolution())
		{
			Bukkit.broadcastMessage(ChatColor.GRAY + "Revolution!! Will the King and Queen survive the attack by the rebels?");
			Bukkit.broadcastMessage(ChatColor.GRAY + "The Revolution will end in " + ChatColor.GOLD + RevolutionManager.getMinutesUntilRevolutionEnd() + " minutes.");
			return;
		}
		
		if (instance.currentVoteType == VOTE_TYPE.VOTE_NONE)
		{
			switch (instance.random.nextInt(20))
			{
				case 0:
					if(RankManager.getKingName()!=null)
					{
						broadcast = "Hil vores konge, hans majest�t " + ChatColor.GOLD + RankManager.getKingName() + " kongen af DoggyCraft!";
					}
					break;
				case 1:
					if(RankManager.getQueenName()!=null)
					{
						broadcast = "Hil vores dronning, hendes majest�t " + ChatColor.GOLD + RankManager.getQueenName() + " dronningen af DoggyCraft!";
					}
					break;
			}

			if(broadcast.length() > 0)
			{
				DramaCraft.broadcastMessage(broadcast);
			}

			return;
		}
		
		if (System.currentTimeMillis() - instance.lastVoteTime < (DramaCraft.instance().voteBroadcastSeconds * 1000))
		{
			return;
		}
		
		/*
		if(instance.currentVote.isCompleted())
		{
			String broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_FINISHED, ChatColor.AQUA);
			DramaCraft.broadcastMessage(broadcast);

			if (instance.yes.size() + instance.no.size() < reqVotes)
			{
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOT_ENOUGH_VOTES, ChatColor.RED);
				DramaCraft.broadcastMessage(broadcast);
				resetVotes();
				return;
			}

			success = ((float)instance.yes.size()) / ((float)(instance.no.size() + instance.yes.size())) >= reqYesPercentage;
			
			if (instance.isFailed())
			{
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOT_ENOUGH_VOTES, ChatColor.RED);
				DramaCraft.broadcastMessage(broadcast);
				resetVotes();
				return;
			}
			else
			{			
				instance.currentVote.success();
			}
		}
		else
		{
			instance.currentVote.update();
		}
		*/
		

		double reqYesPercentage = DramaCraft.instance().requiredYesPercentage / 100.0D;
		int reqVotes = DramaCraft.instance().requiredVotes;
		boolean rebelMessage = false;

		if ((instance.currentVoteType == VOTE_TYPE.VOTE_DAY) || instance.currentVoteType == VOTE_TYPE.VOTE_NIGHT || instance.currentVoteType == VOTE_TYPE.VOTE_RAIN || instance.currentVoteType == VOTE_TYPE.VOTE_SUN)
		{
			reqVotes = 5;
		}

		if (instance.currentVoteType == VOTE_TYPE.VOTE_REVOLUTION)
		{
			reqVotes = 5; // 7
		}
		
		if (instance.currentVoteType == VOTE_TYPE.VOTE_DISABLE_PHANTOMS)
		{
			reqVotes = 5; 
		}

		if (instance.currentVoteType == VOTE_TYPE.VOTE_KING || instance.currentVoteType == VOTE_TYPE.VOTE_QUEEN)
		{
			reqVotes = 7;//7
		}

		if (instance.currentVoteType == VOTE_TYPE.VOTE_NOBLE || instance.currentVoteType == VOTE_TYPE.VOTE_INNERCIRCLE)
		{
			reqVotes = 5;
		}

		if (instance.currentVoteType == VOTE_TYPE.VOTE_NOBLE_KICK || instance.currentVoteType == VOTE_TYPE.VOTE_INNERCIRCLE_KICK)
		{
			reqVotes = 5;
		}

		if (instance.currentVoteType == VOTE_TYPE.VOTE_RINGLEADER1 || instance.currentVoteType == VOTE_TYPE.VOTE_RINGLEADER2)
		{
			reqVotes = 7;//7
		}

		if ((instance.yes.size() + instance.no.size() >= reqVotes) || (System.currentTimeMillis() - instance.startVoteTime > (DramaCraft.instance().voteLengthSeconds * 1000)))
		{
			broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_FINISHED, ChatColor.AQUA);

			DramaCraft.broadcastMessage(broadcast);

			if (instance.yes.size() + instance.no.size() < reqVotes)
			{
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOT_ENOUGH_VOTES, ChatColor.RED);
				DramaCraft.broadcastMessage(broadcast);
				resetVotes();
				return;
			}

			success = ((float)instance.yes.size()) / ((float)(instance.no.size() + instance.yes.size())) >= reqYesPercentage;
			
			DramaCraft.logDebug("Success " + ((float)instance.yes.size()) / ((float)(instance.no.size() + instance.yes.size())));
			DramaCraft.logDebug("ReqYesPercentage/100 " + reqYesPercentage / 100.0);

			broadcast = "MISSING_SUCCESS";

			switch (instance.currentVoteType)
			{
				case VOTE_DISABLE_PHANTOMS:
					LanguageManager.setAmount1(PhantomPreventer.getDisabledTimeMinutes());

					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_PHANTOMS_SUCCESS, ChatColor.GREEN);
						DramaCraft.disablePhantoms();
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_PHANTOMS_FAILED, ChatColor.RED);
					}
					break;

				case VOTE_RAIN:
					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RAIN_SUCCESS, ChatColor.GREEN);
						DramaCraft.setStorm(instance.voteString);
						// DramaCraft.instance().setN00b(this.voteString);
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RAIN_FAILED, ChatColor.RED);
					}
					break;
					
				case VOTE_SUN:
					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_SUN_SUCCESS, ChatColor.GREEN);
						DramaCraft.setSun(instance.voteString);
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_SUN_FAILED, ChatColor.RED);
					}
					break;
										
				case VOTE_INNERCIRCLE:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE_SUCCESS, ChatColor.GREEN);
						RankManager.setNoble(UUID.fromString(instance.voteString));
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE_FAILED, ChatColor.RED);
					}
					break;

				case VOTE_INNERCIRCLE_KICK:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE_KICK_SUCCESS, ChatColor.GREEN);
						RankManager.downgradeRank(player.getUniqueId());
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE_KICK_FAILED, ChatColor.RED);
					}
					break;

				case VOTE_NOBLE:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOBLE_SUCCESS, ChatColor.GREEN);
						RankManager.setNoble(UUID.fromString(instance.voteString));
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOBLE_FAILED, ChatColor.RED);
					}
					break;

				case VOTE_NOBLE_KICK:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOBLE_KICK_SUCCESS, ChatColor.GREEN);
						RankManager.downgradeRank(player.getUniqueId());
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOBLE_KICK_FAILED, ChatColor.RED);
					}
					break;

				case VOTE_KING:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_KING_SUCCESS, ChatColor.GREEN);
						RankManager.setKing(UUID.fromString(instance.voteString));
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_KING_FAILED, ChatColor.RED);
					}
					break;

				case VOTE_QUEEN:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_QUEEN_SUCCESS, ChatColor.GREEN);
						RankManager.setQueen(UUID.fromString(instance.voteString));
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_QUEEN_FAILED, ChatColor.RED);
					}
					break;
					
				case VOTE_RINGLEADER1:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER1_SUCCESS, ChatColor.GREEN);
						RankManager.setRingLeader1(UUID.fromString(instance.voteString));
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER1_FAILED, ChatColor.RED);
					}
					break;
					
				case VOTE_RINGLEADER2:
					if (success)
					{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
						LanguageManager.setPlayerName(player.getName());
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER2_SUCCESS, ChatColor.GREEN);
						RankManager.setRingLeader2(UUID.fromString(instance.voteString));
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER2_FAILED, ChatColor.RED);
					}
					break;

					// case VOTE_NOOB:
				// if (success)
				// {
				// broadcast =
				// DramaCraft.instance().getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_n00b_SUCCESS);
				// Vote.setStorm(this.voteString);
				// }
				// else
				// {
				// broadcast =
				// DramaCraft.instance().getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_STORM_FAILED);
				// }
				// break;
				case VOTE_DAY:
					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_DAY_SUCCESS, ChatColor.GREEN);
						DramaCraft.setDay(instance.voteString);
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_DAY_FAILED, ChatColor.RED);
					}
					break;
				case VOTE_NIGHT:
					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NIGHT_SUCCESS, ChatColor.GREEN);
						DramaCraft.setNight(instance.voteString);
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NIGHT_FAILED, ChatColor.RED);
					}
					break;
				case VOTE_GENERAL:
					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_GENERAL_SUCCESS, ChatColor.GREEN);
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_GENERAL_FAILED, ChatColor.RED);
					}

					// broadcast = broadcast.replaceAll("%voteString%",
					// ChatColor.WHITE + this.voteString + ChatColor.AQUA);
					break;
				case VOTE_REVOLUTION:
					if (success)
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_REVOLUTION_SUCCESS, ChatColor.GREEN);
						RevolutionManager.startRevolution();
					}
					else
					{
						broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_REVOLUTION_FAILED, ChatColor.RED);
					}
					break;
					
				default: break;
			}

			DramaCraft.broadcastMessage(broadcast);

			resetVotes();

			return;
		}

		switch (instance.currentVoteType)
		{
			case VOTE_DISABLE_PHANTOMS:
				LanguageManager.setAmount1(PhantomPreventer.getDisabledTimeMinutes());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_PHANTOMS, ChatColor.AQUA);
				break;

			case VOTE_RAIN:
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RAIN, ChatColor.AQUA);
				break;
				
			case VOTE_SUN:
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_SUN, ChatColor.AQUA);
				break;
			
			case VOTE_NOBLE:
			{
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOBLE, ChatColor.AQUA);
			} break;

			case VOTE_NOBLE_KICK:
			{
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NOBLE_KICK, ChatColor.AQUA);
			} break;

			case VOTE_KING:
			{
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_KING, ChatColor.AQUA);
			} break;

			case VOTE_QUEEN:
			{
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_QUEEN, ChatColor.AQUA);
			} break;

			case VOTE_INNERCIRCLE:
			{
				rebelMessage = true;
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE, ChatColor.AQUA);
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE, ChatColor.AQUA, player.getName());
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_REBEL_HIDDEN, ChatColor.GRAY, player.getName());
			} break;

			case VOTE_INNERCIRCLE_KICK:
			{
				rebelMessage = true;
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE_KICK, ChatColor.AQUA);
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_INNERCIRCLE_KICK, ChatColor.AQUA, player.getName());
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_REBEL_HIDDEN, ChatColor.GRAY, player.getName());
			} break;

			case VOTE_RINGLEADER1:
			{
				rebelMessage = true;
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER1, ChatColor.AQUA);
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER1, ChatColor.AQUA, player.getName());
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_REBEL_HIDDEN, ChatColor.GRAY, player.getName());
			} break;

			case VOTE_RINGLEADER2:
			{
				rebelMessage = true;
				OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(instance.voteString));
				LanguageManager.setPlayerName(player.getName());
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER2, ChatColor.AQUA);
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_RINGLEADER2, ChatColor.AQUA, player.getName());
				DramaCraft.broadcastToRebels(LANGUAGESTRING.VOTE_BROADCAST_REBEL_HIDDEN, ChatColor.GRAY, player.getName());
			} break;

			case VOTE_DAY:
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_DAY, ChatColor.AQUA);
				break;
				
			case VOTE_NIGHT:
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_NIGHT, ChatColor.AQUA);
				break;
				
			case VOTE_REVOLUTION:
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_REVOLUTION, ChatColor.AQUA);
				break;

			case VOTE_GENERAL:				
				LanguageManager.setPlayerName(instance.voteString);
				broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_GENERAL, ChatColor.AQUA);
				break;
				
			default: break;	
		}

		if(!rebelMessage)
		{
			DramaCraft.broadcastMessage(broadcast);
		}

		int percent = 100;
		percent = (int) (100.0F * instance.yes.size() / (instance.yes.size() + instance.no.size()));

		LanguageManager.setAmount1(percent);
		LanguageManager.setAmount2(instance.yes.size() + instance.no.size());
		LanguageManager.setAmount3(reqVotes);

		broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_PROGRESS, ChatColor.AQUA);
		DramaCraft.broadcastMessage(broadcast);

		broadcast = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_BROADCAST_HELP, ChatColor.AQUA);
		DramaCraft.broadcastMessage(broadcast);

		instance.lastVoteTime = System.currentTimeMillis();
	}

	static public VOTE_TYPE getCurrentVoteType()
	{
		return instance.currentVoteType;
	}
	
	private HashMap<VOTE_TYPE, Vote> voteRegistry = new HashMap<VOTE_TYPE, Vote>();

	static public boolean newVote(World world, Player voter, String voteText, boolean vote, VOTE_TYPE voteType)
	{
		String broadcast = "";

		int reqVotes = DramaCraft.instance().requiredVotes;
		int voteCost = 0;//DramaCraft.instance().startVoteCost;
		long voteInterval = DramaCraft.instance().voteSecondsBetween;
		long timeIntervalSeconds = (System.currentTimeMillis() - instance.startVoteTime)  / (1000);
		
		if (timeIntervalSeconds < voteInterval)
		{
			int seconds = (int)(instance.startVoteTime / 1000 + voteInterval) - (int)(System.currentTimeMillis() / 1000);//(int) ((instance.startVoteTime + voteInterval - System.currentTimeMillis()()) / 60000000L); // 60000000L
			LanguageManager.setAmount1(seconds);
			voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOSOON, ChatColor.RED));
			DramaCraft.logDebug(voter.getName() + " tried to start a vote too soon");
			return false;
		}

		//if (voter.getUniqueId() == instance.lastVoterId)
		//{
		//	voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_NOTAGAIN, ChatColor.RED));
		//	DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but now allowed to start another one");
		//	return false;
		//}
		
		//currentVote = voteRegistry[voteType].newVote(world, voter, voteText, vote, voteType);
		//currentVote.newVote(world, voter, voteText, vote, voteType);

		if (DramaCraft.economy.getBalance(voter.getName()) < voteCost)
		{
			LanguageManager.setAmount1(DramaCraft.instance().startVoteCost);
			String message = LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_NOTENOUGHMONEY, ChatColor.RED);
			voter.sendMessage(message);
			DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but did not have money for it");
			return false;
		}

		if (voteType == VOTE_TYPE.VOTE_REVOLUTION)
		{
			if(RankManager.getOnlineRebels() < 5)
			{
				LanguageManager.setAmount1(5);
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWREBELS_ONLINE, ChatColor.RED));
				DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but there are too few rebel players online");
				return false;
				//voter.sendMessage(ChatColor.RED + "Not yet ;-)");
				//return false;
			}
			
			if(!RankManager.isRebel(voter.getUniqueId()))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANREVOLUTION, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote but player was not rebel");
				return false;
			}			
		}

		if (voteType == VOTE_TYPE.VOTE_KING || voteType == VOTE_TYPE.VOTE_QUEEN)
		{
			reqVotes = 5;
			
			UUID targetPlayerId = UUID.fromString(voteText);

			if(Bukkit.getServer().getOnlinePlayers().size() < reqVotes)
			{
				LanguageManager.setAmount1(reqVotes);
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWPLAYERS, ChatColor.RED));
				DramaCraft.logDebug(voter.getName() + " tried to vote king, but there are too few players online");
				return false;
			}

			if(!RankManager.isImperial(voter.getUniqueId()))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYIMPERIALSCANVOTEFORKING, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote king but player was not an imperial");
				return false;
			}			

			if(!RankManager.isNoble(targetPlayerId))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYNOBLESCANBEKING, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote king but target player was not a noble");
				return false;
			}			
		}
		
		if (voteType == VOTE_TYPE.VOTE_RINGLEADER1 || voteType == VOTE_TYPE.VOTE_RINGLEADER2)
		{
			reqVotes = 5;
			
			UUID targetPlayerId = UUID.fromString(voteText);

			if(Bukkit.getServer().getOnlinePlayers().size() < reqVotes)
			{
				LanguageManager.setAmount1(reqVotes);
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWPLAYERS, ChatColor.RED));
				DramaCraft.logDebug(voter.getName() + " tried to vote ringleader, but there are too few players online");
				return false;
			}

			if(!RankManager.isRebel(voter.getUniqueId()))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANVOTEFORRINGLEADER, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote ringleader but player was not a rebel");
				return false;
			}			

			if(!RankManager.isInnerCircle(targetPlayerId))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYINNERCIRCLECANBERINGLEADER, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote ringleader but target player was not a Inner Circles");
				return false;
			}			
		}		

		if (voteType == VOTE_TYPE.VOTE_INNERCIRCLE)
		{
			reqVotes = 5;
			
			UUID targetPlayerId = UUID.fromString(voteText);

			if(Bukkit.getServer().getOnlinePlayers().size() < reqVotes)
			{
				LanguageManager.setAmount1(reqVotes);
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWPLAYERS, ChatColor.RED));
				DramaCraft.logDebug(voter.getName() + " tried to vote king, but there are too few players online");
				return false;
			}

			if(!RankManager.isRebel(voter.getUniqueId()))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANVOTEFORRINGLEADER, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote ringleader but player was not a rebel");
				return false;
			}			

			if(!RankManager.isInnerCircle(targetPlayerId))
			{
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYINNERCIRCLECANBERINGLEADER, ChatColor.RED));
				DramaCraft.log(voter.getName() + " tried to vote ringleader but target player was not a rebel");
				return false;
			}			
		}

		if (voteType == VOTE_TYPE.VOTE_NOBLE)
		{
			reqVotes = 5;
			
			UUID targetPlayerId = UUID.fromString(voteText);

			if(RankManager.getActiveNobles() < 3)
			{
				if(!RankManager.isImperial(voter.getUniqueId()))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYIMPERIALSCANVOTEFORNOBLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but player was not an imperial");
					return false;
				}

				if(!RankManager.isImperial(targetPlayerId))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYIMPERIALSCANBENOBLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but target player was not an imperial");
					return false;
				}

				if(RankManager.getOnlineImperials() < reqVotes)
				{
					LanguageManager.setAmount1(reqVotes);
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWIMPERIALS_ONLINE, ChatColor.RED));
					DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but there are too few imperials online");
					return false;
				}
			}		
			else
			{
				if(!RankManager.isImperial(voter.getUniqueId()))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYIMPERIALCANVOTEFORNOBLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but player was not a imperial");
					return false;
				}
				
				if(!RankManager.isImperial(targetPlayerId))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYIMPERIALSCANBENOBLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but target player was not an imperial");
					return false;
				}

				if(RankManager.getOnlineNobles() < reqVotes)
				{
					LanguageManager.setAmount1(reqVotes);
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWNOBLES_ONLINE, ChatColor.RED));
					DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but there are too few imperial noble online");
					return false;
				}
			}
		}
		
		if (voteType == VOTE_TYPE.VOTE_INNERCIRCLE)
		{
			reqVotes = 5;
			
			UUID targetPlayerId = UUID.fromString(voteText);

			if(RankManager.getActiveInnerCircle() < 3)
			{
				if(!RankManager.isRebel(voter.getUniqueId()))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANVOTEFORINNERCIRCLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but player was not a rebel");
					return false;
				}

				if(!RankManager.isRebel(targetPlayerId))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANBEINNERCIRCLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but target player was not a rebel");
					return false;
				}

				if(RankManager.getOnlineRebels() < reqVotes)
				{
					LanguageManager.setAmount1(reqVotes);
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWREBELS_ONLINE, ChatColor.RED));
					DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but there are too few rebels online");
					return false;
				}
			}		
			else
			{
				if(!RankManager.isInnerCircle(voter.getUniqueId()))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELCANVOTEFORINNERCIRCLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but player was not inner circle");
					return false;
				}
				
				if(!RankManager.isRebel(targetPlayerId))
				{
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_ONLYREBELSCANBEINNERCIRCLE, ChatColor.RED));
					DramaCraft.log(voter.getName() + " tried to vote but target player was not a rebel");
					return false;
				}

				if(RankManager.getOnlineInnerCircle() < reqVotes)
				{
					LanguageManager.setAmount1(reqVotes);
					voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWINNERCIRCLE_ONLINE, ChatColor.RED));
					DramaCraft.logDebug(voter.getName() + " tried to start a vote again, but there are too few inner circle online");
					return false;
				}
			}
		}

		if (voteType == VOTE_TYPE.VOTE_NOBLE_KICK)
		{
			reqVotes = 3;
			
			if(RankManager.getOnlineNobles() < 3)
			{
				LanguageManager.setAmount1(reqVotes);
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWNOBLES_ONLINE, ChatColor.RED));
				DramaCraft.logDebug(voter.getName() + " tried to vote noble kick, but there are too few noble online");
				return false;
			}		
		}

		if (voteType == VOTE_TYPE.VOTE_INNERCIRCLE_KICK)
		{
			reqVotes = 3;
			
			if(RankManager.getOnlineRebels() < 3)
			{
				LanguageManager.setAmount1(reqVotes);
				voter.sendMessage(LanguageManager.getLanguageString(LANGUAGESTRING.ERROR_TOOFEWINNERCIRCLE_ONLINE, ChatColor.RED));
				DramaCraft.logDebug(voter.getName() + " tried to vote innercircle kick, but there are too few inner circle online");
				return false;
			}		
		}
		
		instance.currentVoteType = voteType;
		instance.startVoteTime = System.currentTimeMillis();
		instance.lastVoterId = voter.getUniqueId();

		//TODO: Encapsulate votes logic into its own class and replace entire content with this:
		//if(currentVote.newVote(world, voter, voteText, vote, voteType))
		//{
		//this.broadcast = currentVote.getBroadcastText();
		//this.voteString =currentVote.getVoteText();
		//DramaCraft.broadcastMessage(broadcast);

		//DramaCraft.instance().getLanguageManager().setAmount1(voteCost);
		//String message = DramaCraft.instance().getLanguageManager().getLanguageString(LANGUAGESTRING.VOTE_COST, ChatColor.AQUA);
		//voter.sendMessage(message);
		//DramaCraft.economy.withdrawPlayer(voter.getName(), voteCost);
		//}
		
		
		switch (voteType)
		{
			case VOTE_REVOLUTION:
				broadcast = "A new vote for REVOLUTION was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_RINGLEADER1:
			case VOTE_RINGLEADER2:
				broadcast = "A new vote for ringleader was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_KING:
				broadcast = "A new vote for king was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_QUEEN:
				broadcast = "A new vote for queen was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_DAY:
				broadcast = "A new vote for day was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voter.getWorld().getName();
				break;
			case VOTE_NIGHT:
				broadcast = "A new vote for night was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voter.getWorld().getName();
				break;
			case VOTE_SUN:
				broadcast = "A new vote for sun was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voter.getWorld().getName();
				break;
			case VOTE_DISABLE_PHANTOMS:
				broadcast = "A new vote for disabling phantoms was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voter.getWorld().getName();
				break;
			case VOTE_RAIN:
				broadcast = "A new vote for rain was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voter.getWorld().getName();
				break;
			case VOTE_GENERAL:
				broadcast = "A new vote for a question was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_NOBLE:
				broadcast = "A new vote for a noble was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_NOBLE_KICK:
				broadcast = "A new vote for a removing a noble was started!";
				instance.voteString = voteText;
				break;
			case VOTE_INNERCIRCLE:
				broadcast = "A new vote for the rebel inner circle was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			case VOTE_INNERCIRCLE_KICK:
				broadcast = "A new vote for removing a member from the inner circle was started by " + ChatColor.WHITE + voter.getDisplayName() + "!";
				instance.voteString = voteText;
				break;
			default:
				broadcast = "A new vote was started.";
				return false;
		}

		DramaCraft.broadcastMessage(broadcast);

		LanguageManager.setAmount1(voteCost);
		String message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_COST, ChatColor.AQUA);
		voter.sendMessage(message);

		DramaCraft.economy.withdrawPlayer(voter, voteCost);

		return true;
	}

	static public void doVote(World world, Player voter, boolean vote, VOTE_TYPE voteType)
	{
		boolean firstVote = true;

		if (voteType == VOTE_TYPE.VOTE_YES)
		{
			vote = true;
		}
		else if (voteType == VOTE_TYPE.VOTE_NO)
		{
			vote = false;
		}
		else
		{
			voter.sendMessage("INVALID Vote");
			return;
		}
		
		switch (voteType)
		{
			case VOTE_REVOLUTION:
				if(RankManager.isImperial(voter.getUniqueId()))
				{
					voter.sendMessage(ChatColor.RED + "You are an imperial! You can't vote for a revolution!");
					return;
				} break;
				
			case VOTE_RINGLEADER1:
			case VOTE_RINGLEADER2:
				if(!RankManager.isRebel(voter.getUniqueId()))
				{
					voter.sendMessage(ChatColor.RED + "You are not a rebel! Only rebels can vote for the king!");
					return;
				} break;

			case VOTE_KING:
				if(!RankManager.isImperial(voter.getUniqueId()))
				{
					voter.sendMessage(ChatColor.RED + "You are not a noble! Only imperials can vote for the king!");
					return;
				} break;
				
			case VOTE_QUEEN:
				if(!RankManager.isImperial(voter.getUniqueId()))
				{
					voter.sendMessage(ChatColor.RED + "You are not a noble! Only imperials can vote for the queen!");
					return;
				} break;
				
			case VOTE_NOBLE:
				if(RankManager.getActiveNobles() < 3)
				{
					if(!RankManager.isImperial(voter.getUniqueId()))
					{				
						voter.sendMessage(ChatColor.RED + "You are not an imperial! Only imperials can vote for nobles when there are less than 3 active nobles!");
						return;
					}
				}
				else 
				{
					if(!RankManager.isNoble(voter.getUniqueId()))
					{				
						voter.sendMessage(ChatColor.RED + "You are not an imperial noble!");
						return;
					}										
				} break;

			case VOTE_NOBLE_KICK:
				{
					if(!RankManager.isNoble(voter.getUniqueId()))
					{				
						voter.sendMessage(ChatColor.RED + "You are not an imperial noble!");
						return;
					}										
				} break;

			case VOTE_INNERCIRCLE:
				if(RankManager.getNumberOfInnerCircle() < 3)
				{
					if(!RankManager.isRebel(voter.getUniqueId()))
					{				
						voter.sendMessage(ChatColor.RED + "You are not a rebel! Only rebels can vote for inner circle when there are less than 3 in the inner circle!");
						return;
					}
				}
				else 
				{
					if(!RankManager.isInnerCircle(voter.getUniqueId()))
					{				
						voter.sendMessage(ChatColor.RED + "You are not in the rebel inner circle!");
						return;
					}										
				} break;

			default: break;
		}

		if (vote)
		{
			voter.sendMessage("You voted yes");
			if (!instance.yes.contains(voter.getName()))
			{
				instance.yes.add(voter.getName());
			}
			else
			{
				firstVote = false;
			}
			
			if (instance.no.contains(voter.getName()))
			{
				instance.no.remove(voter.getName());
				firstVote = false;
			}
		}
		else
		{
			voter.sendMessage("You voted no");
			
			if (instance.yes.contains(voter.getName()))
			{
				instance.yes.remove(voter.getName());
				firstVote = false;
			}
			
			if (!instance.no.contains(voter.getName()))
			{
				instance.no.add(voter.getName());
			}
			
			else
			{
				firstVote = false;
			}
		}

		if (firstVote)
		{
			LanguageManager.setAmount1(DramaCraft.instance().votePayment);
			String message = LanguageManager.getLanguageString(LANGUAGESTRING.VOTE_PAYMENT, ChatColor.AQUA);

			voter.sendMessage(message);

			DramaCraft.economy.depositPlayer(voter, DramaCraft.instance().votePayment);
		}
	}
}