package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.gameserver.model.location.Location;

public record CursedWeaponInfo(Location pos, int id, int activated)
{
}