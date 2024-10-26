package com.shnok.javaserver.gameserver.skills.extractable;

import java.util.List;

import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;

public record ExtractableProductItem(List<IntIntHolder> items, double chance)
{
}