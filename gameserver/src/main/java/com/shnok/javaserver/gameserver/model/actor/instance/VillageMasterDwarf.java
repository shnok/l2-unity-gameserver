package com.shnok.javaserver.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

public final class VillageMasterDwarf extends VillageMaster
{
	public VillageMasterDwarf(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	protected final boolean checkVillageMasterRace(ClassId pclass)
	{
		if (pclass == null)
			return false;
		
		return pclass.getRace() == ClassRace.DWARF;
	}
}