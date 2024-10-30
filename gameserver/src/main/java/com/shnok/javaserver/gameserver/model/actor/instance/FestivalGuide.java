package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.Calendar;
import java.util.List;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.FestivalOfDarknessManager;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.FestivalType;
import com.shnok.javaserver.gameserver.enums.MessageType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.zone.type.PeaceZone;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class FestivalGuide extends Folk
{
	protected FestivalType _festivalType;
	protected CabalType _festivalOracle;
	
	protected int _blueStonesNeeded;
	protected int _greenStonesNeeded;
	protected int _redStonesNeeded;
	
	public FestivalGuide(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		
		switch (getNpcId())
		{
			case 31127, 31132:
				_festivalType = FestivalType.MAX_31;
				_festivalOracle = CabalType.DAWN;
				_blueStonesNeeded = 900;
				_greenStonesNeeded = 540;
				_redStonesNeeded = 270;
				break;
			
			case 31128, 31133:
				_festivalType = FestivalType.MAX_42;
				_festivalOracle = CabalType.DAWN;
				_blueStonesNeeded = 1500;
				_greenStonesNeeded = 900;
				_redStonesNeeded = 450;
				break;
			
			case 31129, 31134:
				_festivalType = FestivalType.MAX_53;
				_festivalOracle = CabalType.DAWN;
				_blueStonesNeeded = 3000;
				_greenStonesNeeded = 1800;
				_redStonesNeeded = 900;
				break;
			
			case 31130, 31135:
				_festivalType = FestivalType.MAX_64;
				_festivalOracle = CabalType.DAWN;
				_blueStonesNeeded = 4500;
				_greenStonesNeeded = 2700;
				_redStonesNeeded = 1350;
				break;
			
			case 31131, 31136:
				_festivalType = FestivalType.MAX_NONE;
				_festivalOracle = CabalType.DAWN;
				_blueStonesNeeded = 6000;
				_greenStonesNeeded = 3600;
				_redStonesNeeded = 1800;
				break;
			
			case 31137, 31142:
				_festivalType = FestivalType.MAX_31;
				_festivalOracle = CabalType.DUSK;
				_blueStonesNeeded = 900;
				_greenStonesNeeded = 540;
				_redStonesNeeded = 270;
				break;
			
			case 31138, 31143:
				_festivalType = FestivalType.MAX_42;
				_festivalOracle = CabalType.DUSK;
				_blueStonesNeeded = 1500;
				_greenStonesNeeded = 900;
				_redStonesNeeded = 450;
				break;
			
			case 31139, 31144:
				_festivalType = FestivalType.MAX_53;
				_festivalOracle = CabalType.DUSK;
				_blueStonesNeeded = 3000;
				_greenStonesNeeded = 1800;
				_redStonesNeeded = 900;
				break;
			
			case 31140, 31145:
				_festivalType = FestivalType.MAX_64;
				_festivalOracle = CabalType.DUSK;
				_blueStonesNeeded = 4500;
				_greenStonesNeeded = 2700;
				_redStonesNeeded = 1350;
				break;
			
			case 31141, 31146:
				_festivalType = FestivalType.MAX_NONE;
				_festivalOracle = CabalType.DUSK;
				_blueStonesNeeded = 6000;
				_greenStonesNeeded = 3600;
				_redStonesNeeded = 1800;
				break;
		}
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("FestivalDesc"))
		{
			int val = Integer.parseInt(command.substring(13));
			
			showChatWindow(player, val, null, true);
		}
		else if (command.startsWith("Festival"))
		{
			final int festivalIndex = _festivalType.ordinal();
			
			Party playerParty = player.getParty();
			int val = Integer.parseInt(command.substring(9, 10));
			
			switch (val)
			{
				case 1: // Become a Participant
					// Check if the festival period is active, if not then don't allow registration.
					if (SevenSignsManager.getInstance().isSealValidationPeriod())
					{
						showChatWindow(player, 2, "a", false);
						return;
					}
					
					// Check if a festival is in progress, then don't allow registration yet.
					if (FestivalOfDarknessManager.getInstance().isFestivalInitialized())
					{
						player.sendMessage("You cannot sign up while a festival is in progress.");
						return;
					}
					
					// Check if the player is in a formed party already.
					if (playerParty == null)
					{
						showChatWindow(player, 2, "b", false);
						return;
					}
					
					// Check if the player is the party leader.
					if (!playerParty.isLeader(player))
					{
						showChatWindow(player, 2, "c", false);
						return;
					}
					
					// Check to see if the party has at least 5 members.
					if (playerParty.getMembersCount() < Config.FESTIVAL_MIN_PLAYER)
					{
						showChatWindow(player, 2, "b", false);
						return;
					}
					
					// Check if all the party members are in the required level range.
					if (playerParty.getLevel() > _festivalType.getMaxLevel())
					{
						showChatWindow(player, 2, "d", false);
						return;
					}
					
					// Check to see if the player has already signed up, if they are then update the participant list providing all the required criteria has been met.
					if (player.isFestivalParticipant())
					{
						FestivalOfDarknessManager.getInstance().setParticipants(_festivalOracle, festivalIndex, playerParty);
						showChatWindow(player, 2, "f", false);
						return;
					}
					
					showChatWindow(player, 1, null, false);
					break;
				case 2: // Festival 2 xxxx
					int stoneType = Integer.parseInt(command.substring(11));
					int stonesNeeded = 0;
					
					switch (stoneType)
					{
						case SevenSignsManager.SEAL_STONE_BLUE_ID:
							stonesNeeded = _blueStonesNeeded;
							break;
						case SevenSignsManager.SEAL_STONE_GREEN_ID:
							stonesNeeded = _greenStonesNeeded;
							break;
						case SevenSignsManager.SEAL_STONE_RED_ID:
							stonesNeeded = _redStonesNeeded;
							break;
					}
					
					if (!player.destroyItemByItemId(stoneType, stonesNeeded, true))
						return;
					
					FestivalOfDarknessManager.getInstance().setParticipants(_festivalOracle, festivalIndex, playerParty);
					FestivalOfDarknessManager.getInstance().addAccumulatedBonus(festivalIndex, stoneType, stonesNeeded);
					
					showChatWindow(player, 2, "e", false);
					break;
				case 3: // Score Registration
					// Check if the festival period is active, if not then don't register the score.
					if (SevenSignsManager.getInstance().isSealValidationPeriod())
					{
						showChatWindow(player, 3, "a", false);
						return;
					}
					
					// Check if a festival is in progress, if it is don't register the score.
					if (FestivalOfDarknessManager.getInstance().isFestivalInProgress())
					{
						player.sendMessage("You cannot register a score while a festival is in progress.");
						return;
					}
					
					// Check if the player is in a party.
					if (playerParty == null)
					{
						showChatWindow(player, 3, "b", false);
						return;
					}
					
					final List<Integer> prevParticipants = FestivalOfDarknessManager.getInstance().getPreviousParticipants(_festivalOracle, festivalIndex);
					
					// Check if there are any past participants.
					if ((prevParticipants == null) || prevParticipants.isEmpty() || !prevParticipants.contains(player.getObjectId()))
					{
						showChatWindow(player, 3, "b", false);
						return;
					}
					
					// Check if this player was the party leader in the festival.
					if (player.getObjectId() != prevParticipants.get(0))
					{
						showChatWindow(player, 3, "b", false);
						return;
					}
					
					final ItemInstance bloodOfferings = player.getInventory().getItemByItemId(FestivalOfDarknessManager.FESTIVAL_OFFERING_ID);
					
					// Check if the player collected any blood offerings during the festival.
					if (bloodOfferings == null)
					{
						player.sendMessage("You do not have any blood offerings to contribute.");
						return;
					}
					
					final int offeringScore = bloodOfferings.getCount() * FestivalOfDarknessManager.FESTIVAL_OFFERING_VALUE;
					if (!player.destroyItem(bloodOfferings, false))
						return;
					
					final boolean isHighestScore = FestivalOfDarknessManager.getInstance().setFinalScore(player, _festivalOracle, _festivalType, offeringScore);
					
					// Send message that the contribution score has increased.
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CONTRIB_SCORE_INCREASED_S1).addNumber(offeringScore));
					
					if (isHighestScore)
						showChatWindow(player, 3, "c", false);
					else
						showChatWindow(player, 3, "d", false);
					break;
				case 4: // Current High Scores
					final StringBuilder sb = new StringBuilder("<html><body>Festival Guide:<br>These are the top scores of the week, for the ");
					
					final StatSet dawnData = FestivalOfDarknessManager.getInstance().getHighestScoreData(CabalType.DAWN, festivalIndex);
					final StatSet duskData = FestivalOfDarknessManager.getInstance().getHighestScoreData(CabalType.DUSK, festivalIndex);
					final StatSet overallData = FestivalOfDarknessManager.getInstance().getOverallHighestScoreData(festivalIndex);
					
					final int dawnScore = dawnData.getInteger("score");
					final int duskScore = duskData.getInteger("score");
					
					sb.append(_festivalType.getName() + " festival.<br>");
					
					if (dawnScore > 0)
						sb.append("Dawn: " + calculateDate(dawnData.getString("date")) + ". Score " + dawnScore + "<br>" + dawnData.getString("members") + "<br>");
					else
						sb.append("Dawn: No record exists. Score 0<br>");
					
					if (duskScore > 0)
						sb.append("Dusk: " + calculateDate(duskData.getString("date")) + ". Score " + duskScore + "<br>" + duskData.getString("members") + "<br>");
					else
						sb.append("Dusk: No record exists. Score 0<br>");
					
					// If no data is returned, assume there is no record, or all scores are 0.
					if (overallData != null)
					{
						String cabalStr = "Children of Dusk";
						
						if (overallData.getString("cabal").equals("dawn"))
							cabalStr = "Children of Dawn";
						
						sb.append("Consecutive top scores: " + calculateDate(overallData.getString("date")) + ". Score " + overallData.getInteger("score") + "<br>Affilated side: " + cabalStr + "<br>" + overallData.getString("members") + "<br>");
					}
					else
						sb.append("Consecutive top scores: No record exists. Score 0<br>");
					
					sb.append("<a action=\"bypass -h npc_" + getObjectId() + "_Chat 0\">Go back.</a></body></html>");
					
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setHtml(sb.toString());
					player.sendPacket(html);
					break;
				case 8: // Increase the Festival Challenge
					if (playerParty == null)
						return;
					
					if (!FestivalOfDarknessManager.getInstance().isFestivalInProgress())
						return;
					
					if (!playerParty.isLeader(player))
					{
						showChatWindow(player, 8, "a", false);
						break;
					}
					
					if (FestivalOfDarknessManager.getInstance().increaseChallenge(_festivalOracle, festivalIndex))
						showChatWindow(player, 8, "b", false);
					else
						showChatWindow(player, 8, "c", false);
					break;
				case 9: // Leave the Festival
					if (playerParty == null)
						return;
					
					/**
					 * If the player is the party leader, remove all participants from the festival (i.e. set the party to null, when updating the participant list) otherwise just remove this player from the "arena", and also remove them from the party.
					 */
					boolean isLeader = playerParty.isLeader(player);
					
					if (isLeader)
					{
						FestivalOfDarknessManager.getInstance().updateParticipants(player, null);
					}
					else
					{
						FestivalOfDarknessManager.getInstance().updateParticipants(player, playerParty);
						playerParty.removePartyMember(player, MessageType.EXPELLED);
					}
					break;
				case 0: // Distribute Accumulated Bonus
					if (!SevenSignsManager.getInstance().isSealValidationPeriod())
					{
						player.sendMessage("Bonuses cannot be paid during the competition period.");
						return;
					}
					
					if (FestivalOfDarknessManager.getInstance().distribAccumulatedBonus(player) > 0)
						showChatWindow(player, 0, "a", false);
					else
						showChatWindow(player, 0, "b", false);
					break;
				default:
					showChatWindow(player, val, null, false);
			}
		}
		else
		{
			// this class dont know any other commands, let forward
			// the command to the parent class
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		String filename = SevenSignsManager.SEVEN_SIGNS_HTML_PATH;
		
		switch (getTemplate().getNpcId())
		{
			case 31127, 31128, 31129, 31130, 31131: // Dawn Festival Guides
				filename += "festival/dawn_guide.htm";
				break;
			
			case 31137, 31138, 31139, 31140, 31141: // Dusk Festival Guides
				filename += "festival/dusk_guide.htm";
				break;
			
			case 31132, 31133, 31134, 31135, 31136, 31142, 31143, 31144, 31145, 31146: // Festival Witches
				filename += "festival/festival_witch.htm";
				break;
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%festivalMins%", FestivalOfDarknessManager.getInstance().getTimeToNextFestivalStr());
		player.sendPacket(html);
		
		// Send ActionFailed to the player in order to avoid he stucks
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private void showChatWindow(Player player, int val, String suffix, boolean isDescription)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(SevenSignsManager.SEVEN_SIGNS_HTML_PATH + "festival/" + ((isDescription) ? "desc_" : "festival_") + ((suffix != null) ? val + suffix : val) + ".htm");
		html.replace("%objectId%", getObjectId());
		html.replace("%festivalType%", _festivalType.getName());
		html.replace("%cycleMins%", FestivalOfDarknessManager.getInstance().getMinsToNextCycle());
		if (!isDescription && "2b".equals(val + suffix))
			html.replace("%minFestivalPartyMembers%", Config.FESTIVAL_MIN_PLAYER);
		
		// Festival's fee
		if (val == 1)
		{
			html.replace("%blueStoneNeeded%", _blueStonesNeeded);
			html.replace("%greenStoneNeeded%", _greenStonesNeeded);
			html.replace("%redStoneNeeded%", _redStonesNeeded);
		}
		// If the stats or bonus table is required, construct them.
		else if (val == 5)
			html.replace("%statsTable%", getStatsTable());
		else if (val == 6)
			html.replace("%bonusTable%", getBonusTable());
		
		player.sendPacket(html);
		
		// Send ActionFailed to the player in order to avoid he stucks
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private static final String getStatsTable()
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++)
		{
			final int dawnScore = FestivalOfDarknessManager.getInstance().getHighestScore(CabalType.DAWN, i);
			final int duskScore = FestivalOfDarknessManager.getInstance().getHighestScore(CabalType.DUSK, i);
			
			String winningCabal = "Children of Dusk";
			if (dawnScore > duskScore)
				winningCabal = "Children of Dawn";
			else if (dawnScore == duskScore)
				winningCabal = "None";
			
			sb.append("<tr><td width=\"100\" align=\"center\">" + FestivalType.VALUES[i].getName() + "</td><td align=\"center\" width=\"35\">" + duskScore + "</td><td align=\"center\" width=\"35\">" + dawnScore + "</td><td align=\"center\" width=\"130\">" + winningCabal + "</td></tr>");
		}
		
		return sb.toString();
	}
	
	private static final String getBonusTable()
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++)
			sb.append("<tr><td align=\"center\" width=\"150\">" + FestivalType.VALUES[i].getName() + "</td><td align=\"center\" width=\"150\">" + FestivalOfDarknessManager.getInstance().getAccumulatedBonus(i) + "</td></tr>");
		
		return sb.toString();
	}
	
	private static final String calculateDate(String milliFromEpoch)
	{
		long numMillis = Long.parseLong(milliFromEpoch);
		Calendar calCalc = Calendar.getInstance();
		
		calCalc.setTimeInMillis(numMillis);
		
		return calCalc.get(Calendar.YEAR) + "/" + calCalc.get(Calendar.MONTH) + "/" + calCalc.get(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		PeaceZone zone = ZoneManager.getInstance().getZone(this, PeaceZone.class);
		
		// Festival Witches are spawned inside festival, out of peace zone -> skip them
		if (zone != null)
			FestivalOfDarknessManager.getInstance().addPeaceZone(zone, _festivalOracle == CabalType.DAWN);
	}
}