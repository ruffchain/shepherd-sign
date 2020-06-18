package com.rfc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RPacketTest {
    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public  void packUnpackPacket(){
        String secret = "23df010f85f301c81498d69bd6be79b17a7e0e0306f26fae8013d910ea372bec";
        RTransaction tx = new RTransaction();

        tx.setMethod("transferTo");
        tx.setNonce(2);
        tx.setPublicKey(secret);

        JSONObject input = new JSONObject();
        input.put("to", "1MtBtCfyymaU5xinAdne9vWKJyDrV3QRyj");

        tx.setInput(input);
        tx.setValue(new BigDecimal("4.7891"));
        tx.setFee(new BigDecimal("0.1"));

        JSONObject txObj;
        try{
            tx.sign(secret);
//            logger.debug("signature: {}", tx.m_signature);

            String strPacket = RPacket.pack(tx);

            logger.debug("strPacket: {}", strPacket);

            txObj = RPacket.unpack(strPacket);

        }catch(Exception e){
            logger.error("sign error: {}" , e);
            assertTrue(false);
            return;
        }

        assertEquals("transferTo",txObj.getString("method"));
//        assertEquals(2,
//                txObj.getInteger("nonce"));
        logger.debug("original signature len:{}",Digest.bytesToText(tx.m_signature).length());
        logger.debug("unpack signature len:{}", txObj.getString("signature").length());
        assertEquals(Digest.bytesToText(tx.m_signature),
                txObj.getString("signature"));
        assertEquals("s" + input.getString("to"),txObj.getJSONObject("input").getString("to"));
        assertEquals(Digest.publicKeyFromSecretKey(secret),
                txObj.getString("publicKey"));
        assertEquals("4.7891",
                txObj.getString("amount"));
        assertEquals("0.1",
                txObj.getString("fee"));
    }
}
