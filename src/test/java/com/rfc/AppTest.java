package com.rfc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.stream.IntStream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    private  static  JSONObject getTxJSON(){
        String SECRET="da6feae3ca249c359200487934216f45dd1c2159116c3eecc348a74a3c7d16ba";
        String ADDRESS="1KNjtioDXuALgFD2eLonZvLxv3VsyQcBjy";
        String ADDRESS2 ="1AmiZ4rkvb3uw9x71stioXvB6PjswVs26y";
        String amount = "1.2345";
        int NONCE = 1;
        String METHOD="transferTo";

        JSONObject tx = new JSONObject();
        tx.put("method", METHOD);
        tx.put("value", amount);
        tx.put("fee","0.1");
        tx.put("fromAddress", ADDRESS);
        tx.put("fromSecret", SECRET);
        tx.put("toAddress", ADDRESS2);
        tx.put("nonce", NONCE);

        return tx;
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Test
    public void createTx(){
        JSONObject tx = getTxJSON();
        logger.debug(tx.toJSONString());
    }
    @Test
    public void testSignature() {
        int in[] = {41,101,100,53,78,75,169,186,18,225,
                61,91,74,108,23,139,121,43,104,79,84,224,22,123,134,175,
                249,201,34,158,82,227,37,203,78,68,107,217,145,45,221,77,
                69,134,194,27,48,139,58,228,87,182,35,240,112,77,140,50,
                13,46,143,196,60,126};

        byte[] expectedSignature = new byte[64];

        for(int i=0; i< in.length; i++){
            expectedSignature[i] = (byte)in[i];
        }


        JSONObject txJson = getTxJSON();
        logger.debug(txJson.toJSONString());

        RTransaction tx = new RTransaction();
        tx.setMethod(txJson.getString("method"));
        tx.setNonce(txJson.getInteger("nonce"));
        tx.setPublicKey(txJson.getString("fromSecret"));

        JSONObject input = new JSONObject();
        input.put("to", txJson.getString("toAddress"));

        tx.setInput(input);
        tx.setValue(new BigDecimal(txJson.getString("value")));
        tx.setFee(new BigDecimal(txJson.getString("fee")));
        logger.debug("===========================");
        tx.print();
        logger.debug("===========================");

        try{
            tx.sign(txJson.getString("fromSecret"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
        }

        logger.debug("Signature length:{}" , tx.m_signature.length);
        logger.debug("Expected length:{}" ,expectedSignature.length);

        assertEquals(expectedSignature.length,tx.m_signature.length );
        IntStream.range(0, tx.m_signature.length).forEach(
                i -> assertEquals(expectedSignature[i], tx.m_signature[i]));
    }
}
