package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.gameserver.model.location.Location;

public record CursedWeaponInfo(Location pos, int id, int activated)
{
}