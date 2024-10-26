package com.shnok.javaserver.gameserver.scripting.script.feature;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class ShadowWeapon extends Quest
{
	private static final int D_GRADE_COUPON = 8869;
	private static final int C_GRADE_COUPON = 8870;
	
	public ShadowWeapon()
	{
		super(-1, "feature");
		
		addTalkId(FirstClassChange.FIRST_CLASS_NPCS);
		addTalkId(SecondClassChange.SECOND_CLASS_NPCS);
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		boolean hasD = player.getInventory().hasItem(D_GRADE_COUPON);
		boolean hasC = player.getInventory().hasItem(C_GRADE_COUPON);
		
		if (!hasD && !hasC)
			return "exchange-no.htm";
		
		// let's assume character had both c & d-grade coupons, we'll confirm later
		String multisell = "306893003";
		if (!hasD) // if s/he had c-grade only...
			multisell = "306893002";
		else if (!hasC) // or d-grade only.
			multisell = "306893001";
		
		// finally, return htm with proper multisell value in it.
		return getHtmlText("exchange.htm").replace("%msid%", multisell);
	}
}