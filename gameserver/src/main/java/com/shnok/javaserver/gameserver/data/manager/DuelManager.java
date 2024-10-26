package com.shnok.javaserver.gameserver.data.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.entity.Duel;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Loads and stores {@link Duel}s for easier management.
 */
public final class DuelManager
{
	private final Map<Integer, Duel> _duels = new ConcurrentHashMap<>();
	
	protected DuelManager()
	{
	}
	
	public Duel getDuel(int duelId)
	{
		return _duels.get(duelId);
	}
	
	/**
	 * Add a Duel on the _duels Map. Both {@link Player}s must exist.
	 * @param playerA : The first {@link Player} to use.
	 * @param playerB : The second {@link Player} to use.
	 * @param isPartyDuel : True if the duel is a party duel.
	 */
	public void addDuel(Player playerA, Player playerB, boolean isPartyDuel)
	{
		if (playerA == null || playerB == null)
			return;
		
		// Compute a new id.
		final int duelId = IdFactory.getInstance().getNextId();
		
		// Feed the Map.
		_duels.put(duelId, new Duel(playerA, playerB, isPartyDuel, duelId));
	}
	
	/**
	 * Remove the duel from the Map, and release the id.
	 * @param duelId : The id to remove.
	 */
	public void removeDuel(int duelId)
	{
		// Release the id.
		IdFactory.getInstance().releaseId(duelId);
		
		// Delete from the Map.
		_duels.remove(duelId);
	}
	
	/**
	 * End the duel by a surrender action.
	 * @param player : The {@link Player} used to retrieve the duelId. The {@link Player} is then used as surrendered opponent.
	 */
	public void doSurrender(Player player)
	{
		if (player == null || !player.isInDuel())
			return;
		
		final Duel duel = getDuel(player.getDuelId());
		if (duel != null)
			duel.doSurrender(player);
	}
	
	/**
	 * End the duel by a defeat action.
	 * @param player : The {@link Player} used to retrieve the duelId. The {@link Player} is then used as defeated opponent.
	 */
	public void onPlayerDefeat(Player player)
	{
		if (player == null || !player.isInDuel())
			return;
		
		final Duel duel = getDuel(player.getDuelId());
		if (duel != null)
			duel.onPlayerDefeat(player);
	}
	
	/**
	 * Remove the {@link Player} set as parameter from duel, enforcing duel cancellation.
	 * @param player : The {@link Player} to check.
	 */
	public void onPartyEdit(Player player)
	{
		if (player == null || !player.isInDuel())
			return;
		
		final Duel duel = getDuel(player.getDuelId());
		if (duel != null)
			duel.onPartyEdit();
	}
	
	/**
	 * Broadcast a packet to the team (or the {@link Player}) opposing the given {@link Player}.
	 * @param player : The {@link Player} used to find the opponent.
	 * @param packet : The {@link L2GameServerPacket} to send.
	 */
	public void broadcastToOppositeTeam(Player player, L2GameServerPacket packet)
	{
		if (player == null || !player.isInDuel())
			return;
		
		final Duel duel = getDuel(player.getDuelId());
		if (duel == null)
			return;
		
		final Player playerA = duel.getPlayerA();
		final Player playerB = duel.getPlayerB();
		
		if (playerA == player || (duel.isPartyDuel() && playerA.isInSameParty(player)))
			duel.broadcastTo(playerB, packet);
		else if (playerB == player || (duel.isPartyDuel() && playerB.isInSameParty(player)))
			duel.broadcastTo(playerA, packet);
	}
	
	public static final DuelManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final DuelManager INSTANCE = new DuelManager();
	}
}