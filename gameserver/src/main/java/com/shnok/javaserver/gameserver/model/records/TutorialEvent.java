package net.sf.l2j.gameserver.model.records;

import net.sf.l2j.gameserver.model.location.Location;

public record TutorialEvent(String initialVoice, String initialHtm, String ce8Htm, Location ce8Loc, String qmc9Htm, Location qmc9Loc, String qmc24Htm, String qmc35Htm, Location ce47Loc)
{
}