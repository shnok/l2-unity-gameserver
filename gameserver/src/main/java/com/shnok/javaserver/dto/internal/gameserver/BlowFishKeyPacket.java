package com.shnok.javaserver.dto.internal.gameserver;

import com.shnok.javaserver.dto.SendablePacket;
import com.shnok.javaserver.enums.packettypes.GameServerPacketType;
import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

@Log4j2
public class BlowFishKeyPacket extends SendablePacket {
    public BlowFishKeyPacket(byte[] blowfishKey, RSAPublicKey publicKey) {
        super(GameServerPacketType.BlowFishKey.getValue());

        try {
            log.debug("Decrypted blowfish key length: {}", blowfishKey.length);
            log.debug("Decrypted blowfish key: {}", Arrays.toString(blowfishKey));
            final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = rsaCipher.doFinal(blowfishKey);
            writeB((byte) 0);
            writeB((byte) 0);
            writeI(encrypted.length);
            writeB(encrypted);

            log.debug("Encrypted blowfish key length: {}", encrypted.length);
            log.debug("Encrypted blowfish key: {}", Arrays.toString(encrypted));
            buildPacket();
        } catch (Exception e) {
            log.error("Error While encrypting blowfish key for transmision (Crypt error): " + e.getMessage(), e);
        }
    }
}
