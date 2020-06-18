package com.rfc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.*;

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

        try{
            tx.sign(secret);
            String strPacket = RPacket.pack(tx);

            logger.debug("strPacket: {}", strPacket);

            RTransaction txRecover = RPacket.unpack(strPacket);


        }catch(Exception e){
            logger.error("sign error: {}" , e);
            assertTrue(false);
        }

        assertEquals(1,1 );
    }
}
