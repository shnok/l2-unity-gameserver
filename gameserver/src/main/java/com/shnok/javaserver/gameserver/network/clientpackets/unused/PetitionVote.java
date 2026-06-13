package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.PetitionManager;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionRate;
import com.shnok.javaserver.gameserver.model.Petition;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

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