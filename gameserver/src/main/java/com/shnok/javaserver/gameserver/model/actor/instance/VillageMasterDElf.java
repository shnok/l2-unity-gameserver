package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;

public final class VillageMasterDElf extends VillageMaster
{
	public VillageMasterDElf(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	protected final boolean checkVillageMasterRace(ClassId pclass)
	{
		if (pclass == null)
			return false;
		
		return pclass.getRace() == ClassRace.DARK_ELF;
	}
}