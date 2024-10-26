package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.PetitionManager;
import net.sf.l2j.gameserver.enums.petitions.PetitionRate;
import net.sf.l2j.gameserver.model.Petition;
import net.sf.l2j.gameserver.model.actor.Player;

public final class PetitionVote extends L2GameClientPacket
{
	private int _rate;
	private String _feedback;
	
	@Override
	protected void readImpl()
	{
		readD(); // Always 1
		_rate = readD();
		_feedback = readS();
	}
	
	@Override
	public void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Petition petition = PetitionManager.getInstance().getFeedbackPetition(player);
		if (petition == null)
			return;
		
		petition.setFeedback(PetitionRate.VALUES[_rate], _feedback.trim());
	}
}