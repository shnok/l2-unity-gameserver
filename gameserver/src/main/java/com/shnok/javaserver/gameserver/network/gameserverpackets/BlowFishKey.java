package com.shnok.javaserver.gameserver.network.gameserverpackets;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import com.shnok.javaserver.commons.logging.CLogger;

public class BlowFishKey extends GameServerBasePacket
{
	private static final CLogger LOGGER = new CLogger(BlowFishKey.class.getName());
	
	public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey)
	{
		writeC(0x00);
		byte[] encrypted = null;
		try
		{
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encrypted = rsaCipher.doFinal(blowfishKey);
			
			writeD(encrypted.length);
			writeB(encrypted);
		}
		catch (GeneralSecurityException e)
		{
			LOGGER.error("Error while encrypting blowfish key for transmission.", e);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}