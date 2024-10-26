package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Map.Entry;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.manager.FestivalOfDarknessManager;
import net.sf.l2j.gameserver.data.manager.SevenSignsManager;
import net.sf.l2j.gameserver.enums.CabalType;
import net.sf.l2j.gameserver.enums.FestivalType;
import net.sf.l2j.gameserver.enums.SealType;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class SSQStatus extends L2GameServerPacket
{
	private final int _objectId;
	private final int _page;
	
	public SSQStatus(int objectId, int recordPage)
	{
		_objectId = objectId;
		_page = recordPage;
	}
	
	@Override
	protected final void writeImpl()
	{
		final CabalType winningCabal = SevenSignsManager.getInstance().getWinningCabal();
		final int totalDawnMembers = SevenSignsManager.getInstance().getTotalMembers(CabalType.DAWN);
		final int totalDuskMembers = SevenSignsManager.getInstance().getTotalMembers(CabalType.DUSK);
		
		writeC(0xf5);
		
		writeC(_page);
		writeC(SevenSignsManager.getInstance().getCurrentPeriod().ordinal());
		
		int dawnPercent = 0;
		int duskPercent = 0;
		
		switch (_page)
		{
			case 1:
				// [ddd cc dd ddd c ddd c]
				writeD(SevenSignsManager.getInstance().getCurrentCycle());
				
				switch (SevenSignsManager.getInstance().getCurrentPeriod())
				{
					case RECRUITING:
						writeD(SystemMessageId.INITIAL_PERIOD.getId());
						writeD(SystemMessageId.UNTIL_TODAY_6PM.getId());
						break;
					
					case COMPETITION:
						writeD(SystemMessageId.QUEST_EVENT_PERIOD.getId());
						writeD(SystemMessageId.UNTIL_MONDAY_6PM.getId());
						break;
					
					case RESULTS:
						writeD(SystemMessageId.RESULTS_PERIOD.getId());
						writeD(SystemMessageId.UNTIL_TODAY_6PM.getId());
						break;
					
					case SEAL_VALIDATION:
						writeD(SystemMessageId.VALIDATION_PERIOD.getId());
						writeD(SystemMessageId.UNTIL_MONDAY_6PM.getId());
						break;
				}
				
				writeC(SevenSignsManager.getInstance().getPlayerCabal(_objectId).ordinal());
				writeC(SevenSignsManager.getInstance().getPlayerSeal(_objectId).ordinal());
				
				writeD(SevenSignsManager.getInstance().getPlayerStoneContrib(_objectId)); // Seal Stones Turned-In
				writeD(SevenSignsManager.getInstance().getPlayerAdenaCollect(_objectId)); // Ancient Adena to Collect
				
				double dawnStoneScore = SevenSignsManager.getInstance().getCurrentStoneScore(CabalType.DAWN);
				int dawnFestivalScore = SevenSignsManager.getInstance().getCurrentFestivalScore(CabalType.DAWN);
				
				double duskStoneScore = SevenSignsManager.getInstance().getCurrentStoneScore(CabalType.DUSK);
				int duskFestivalScore = SevenSignsManager.getInstance().getCurrentFestivalScore(CabalType.DUSK);
				
				double totalStoneScore = duskStoneScore + dawnStoneScore;
				
				/*
				 * Scoring seems to be proportionate to a set base value, so base this on the maximum obtainable score from festivals, which is 500.
				 */
				int duskStoneScoreProp = 0;
				int dawnStoneScoreProp = 0;
				
				if (totalStoneScore != 0)
				{
					duskStoneScoreProp = Math.round(((float) duskStoneScore / (float) totalStoneScore) * 500);
					dawnStoneScoreProp = Math.round(((float) dawnStoneScore / (float) totalStoneScore) * 500);
				}
				
				int duskTotalScore = SevenSignsManager.getInstance().getCurrentScore(CabalType.DUSK);
				int dawnTotalScore = SevenSignsManager.getInstance().getCurrentScore(CabalType.DAWN);
				
				int totalOverallScore = duskTotalScore + dawnTotalScore;
				
				if (totalOverallScore != 0)
				{
					dawnPercent = Math.round(((float) dawnTotalScore / (float) totalOverallScore) * 100);
					duskPercent = Math.round(((float) duskTotalScore / (float) totalOverallScore) * 100);
				}
				
				/* DUSK */
				writeD(duskStoneScoreProp); // Seal Stone Score
				writeD(duskFestivalScore); // Festival Score
				writeD(duskTotalScore); // Total Score
				
				writeC(duskPercent); // Dusk %
				
				/* DAWN */
				writeD(dawnStoneScoreProp); // Seal Stone Score
				writeD(dawnFestivalScore); // Festival Score
				writeD(dawnTotalScore); // Total Score
				
				writeC(dawnPercent); // Dawn %
				break;
			case 2:
				// c cc hc [cd (dc (S))]
				writeH(1);
				
				writeC(5); // Total number of festivals
				
				for (FestivalType level : FestivalType.VALUES)
				{
					final int festivalId = level.ordinal();
					
					writeC(festivalId + 1); // Current client-side festival ID
					writeD(level.getMaxScore());
					
					int duskScore = FestivalOfDarknessManager.getInstance().getHighestScore(CabalType.DUSK, festivalId);
					int dawnScore = FestivalOfDarknessManager.getInstance().getHighestScore(CabalType.DAWN, festivalId);
					
					// Dusk Score \\
					writeD(duskScore);
					
					StatSet highScoreData = FestivalOfDarknessManager.getInstance().getHighestScoreData(CabalType.DUSK, festivalId);
					String[] partyMembers = highScoreData.getString("members").split(",");
					
					if (partyMembers != null)
					{
						writeC(partyMembers.length);
						
						for (String partyMember : partyMembers)
							writeS(partyMember);
					}
					else
					{
						writeC(0);
					}
					
					// Dawn Score \\
					writeD(dawnScore);
					
					highScoreData = FestivalOfDarknessManager.getInstance().getHighestScoreData(CabalType.DAWN, festivalId);
					partyMembers = highScoreData.getString("members").split(",");
					
					if (partyMembers != null)
					{
						writeC(partyMembers.length);
						
						for (String partyMember : partyMembers)
							writeS(partyMember);
					}
					else
					{
						writeC(0);
					}
				}
				break;
			case 3:
				// c cc [ccc (cccc)]
				writeC(10); // Minimum limit for winning cabal to retain their seal
				writeC(35); // Minimum limit for winning cabal to claim a seal
				writeC(3); // Total number of seals
				
				for (Entry<SealType, CabalType> entry : SevenSignsManager.getInstance().getSealOwners().entrySet())
				{
					final SealType seal = entry.getKey();
					final CabalType sealOwner = entry.getValue();
					
					int dawnProportion = SevenSignsManager.getInstance().getSealProportion(seal, CabalType.DAWN);
					int duskProportion = SevenSignsManager.getInstance().getSealProportion(seal, CabalType.DUSK);
					
					writeC(seal.ordinal());
					writeC(sealOwner.ordinal());
					
					if (totalDuskMembers == 0)
					{
						if (totalDawnMembers == 0)
						{
							writeC(0);
							writeC(0);
						}
						else
						{
							writeC(0);
							writeC(Math.round(((float) dawnProportion / (float) totalDawnMembers) * 100));
						}
					}
					else
					{
						if (totalDawnMembers == 0)
						{
							writeC(Math.round(((float) duskProportion / (float) totalDuskMembers) * 100));
							writeC(0);
						}
						else
						{
							writeC(Math.round(((float) duskProportion / (float) totalDuskMembers) * 100));
							writeC(Math.round(((float) dawnProportion / (float) totalDawnMembers) * 100));
						}
					}
				}
				break;
			case 4:
				// c cc [cc (cchh)]
				writeC(winningCabal.ordinal()); // Overall predicted winner
				writeC(3); // Total number of seals
				
				for (Entry<SealType, CabalType> entry : SevenSignsManager.getInstance().getSealOwners().entrySet())
				{
					final SealType seal = entry.getKey();
					final CabalType sealOwner = entry.getValue();
					
					final int dawnProportion = SevenSignsManager.getInstance().getSealProportion(seal, CabalType.DAWN);
					final int duskProportion = SevenSignsManager.getInstance().getSealProportion(seal, CabalType.DUSK);
					
					dawnPercent = Math.round((dawnProportion / (totalDawnMembers == 0 ? 1 : (float) totalDawnMembers)) * 100);
					duskPercent = Math.round((duskProportion / (totalDuskMembers == 0 ? 1 : (float) totalDuskMembers)) * 100);
					
					writeC(sealOwner.ordinal());
					
					switch (sealOwner)
					{
						case NORMAL:
							switch (winningCabal)
							{
								case NORMAL:
									writeC(CabalType.NORMAL.ordinal());
									writeH(SystemMessageId.COMPETITION_TIE_SEAL_NOT_AWARDED.getId());
									break;
								case DAWN:
									if (dawnPercent >= 35)
									{
										writeC(CabalType.DAWN.ordinal());
										writeH(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.SEAL_NOT_OWNED_35_LESS_VOTED.getId());
									}
									break;
								case DUSK:
									if (duskPercent >= 35)
									{
										writeC(CabalType.DUSK.ordinal());
										writeH(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.SEAL_NOT_OWNED_35_LESS_VOTED.getId());
									}
									break;
							}
							break;
						
						case DAWN:
							switch (winningCabal)
							{
								case NORMAL:
									if (dawnPercent >= 10)
									{
										writeC(CabalType.DAWN.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.COMPETITION_TIE_SEAL_NOT_AWARDED.getId());
									}
									break;
								
								case DAWN:
									if (dawnPercent >= 10)
									{
										writeC(sealOwner.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
								
								case DUSK:
									if (duskPercent >= 35)
									{
										writeC(CabalType.DUSK.ordinal());
										writeH(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else if (dawnPercent >= 10)
									{
										writeC(CabalType.DAWN.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
							}
							break;
						
						case DUSK:
							switch (winningCabal)
							{
								case NORMAL:
									if (duskPercent >= 10)
									{
										writeC(CabalType.DUSK.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.COMPETITION_TIE_SEAL_NOT_AWARDED.getId());
									}
									break;
								
								case DAWN:
									if (dawnPercent >= 35)
									{
										writeC(CabalType.DAWN.ordinal());
										writeH(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else if (duskPercent >= 10)
									{
										writeC(sealOwner.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
								
								case DUSK:
									if (duskPercent >= 10)
									{
										writeC(sealOwner.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										writeC(CabalType.NORMAL.ordinal());
										writeH(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
							}
							break;
					}
					writeH(0);
				}
				break;
		}
	}
}