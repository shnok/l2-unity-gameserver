package net.sf.l2j.gameserver.model.records;

/**
 * This class handles Echo Crystals score partition.
 * @param crystalId : The item id to test.
 * @param okMsg : Sent HTM when the transaction was handled.
 * @param noAdenaMsg : Sent HTM when you miss Adena.
 * @param noScoreMsg : Sent HTM when the item isn't in inventory.
 */
public record ScoreData(int crystalId, String okMsg, String noAdenaMsg, String noScoreMsg)
{
}