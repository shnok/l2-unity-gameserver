package com.shnok.javaserver.gameserver.model.records;

import com.shnok.javaserver.gameserver.model.location.Location;

public record TutorialEvent(String initialVoice, String initialHtm, String ce8Htm, Location ce8Loc, String qmc9Htm, Location qmc9Loc, String qmc24Htm, String qmc35Htm, Location ce47Loc)
{
}