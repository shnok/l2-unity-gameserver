package net.sf.l2j.gameserver.skills.extractable;

import java.util.List;

public record ExtractableSkill(int skillHash, List<ExtractableProductItem> productItems)
{
}