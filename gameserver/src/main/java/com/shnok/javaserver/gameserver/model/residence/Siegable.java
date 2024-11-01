package com.shnok.javaserver.gameserver.model.residence;

import java.util.Calendar;
import java.util.List;

import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.pledge.Clan;

public interface Siegable
{
	public void startSiege();
	
	public void endSiege();
	
	public List<Clan> getAttackerClans();
	
	public List<Clan> getDefenderClans();
	
	public boolean checkSide(Clan clan, SiegeSide type);
	
	public boolean checkSides(Clan clan, SiegeSide... types);
	
	public boolean checkSides(Clan clan);
	
	public Npc getFlag(Clan clan);
	
	public Calendar getSiegeDate();
}