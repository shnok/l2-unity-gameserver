package com.shnok.javaserver.gameserver.skills.extractable;

import java.util.List;

public record ExtractableSkill(int skillHash, List<ExtractableProductItem> productItems)
{
}