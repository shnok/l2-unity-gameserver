package net.sf.l2j.gameserver.skills.extractable;

import java.util.List;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

public record ExtractableProductItem(List<IntIntHolder> items, double chance)
{
}