package com.rfc;

import com.alibaba.fastjson.JSONObject;
import com.rfc.bufferwriter.BufferWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BufferWriterTest {
    private static final Logger logger = LogManager.getLogger(BufferWriterTest.class);

    @Test
    public void testWriteBytes() throws  Exception{
        String strExpected ="02b845a774be73a005d27ad4667c3a3ada4b087cb0fbffbe2d704a450506de16dc";
        String secret = "da6feae3ca249c359200487934216f45dd1c2159116c3eecc348a74a3c7d16ba";

        String publicKey = Digest.publicKeyFromSecretKey(secret);

        BufferWriter writer = new BufferWriter();
        writer.writeBytes(Digest.textToBytes(publicKey));
        byte[] content = writer.render();
        String strContent = Digest.bytesToText(content);

        logger.debug(strContent);
        assertEquals(strExpected, strContent);

    }

    @Test
    public void testWriteVarString() throws  Exception{
        String str = "transferTo";
        String strExpected = "0a7472616e73666572546f";

        BufferWriter writer = new BufferWriter();
        writer.writeVarString(str);
        byte[] content = writer.render();
        String strContent = Digest.bytesToText(content);

        logger.debug(strContent);
        assertEquals(strExpected, strContent);
    }
    @Test
    public void testWriteU32() throws Exception{
        String strExpected = "01000000";
        BufferWriter writer = new BufferWriter();
        writer.writeU32(1);
        byte[] content = writer.render();
        String strContent = Digest.bytesToText(content);
        logger.debug(strContent);
        assertEquals(strExpected, strContent);
    }

    @Test
    public  void testToStringifiable() throws  Exception{
        String strExpected ="2c7b22746f223a227331416d695a34726b7662337577397837317374696f58764236506a73775673323679227d";
        JSONObject inputObj = new JSONObject();
        inputObj.put("to", "1AmiZ4rkvb3uw9x71stioXvB6PjswVs26y");

        String input = Encoding.toStringifiable(inputObj, true);
        BufferWriter writer = new BufferWriter();
        writer.writeVarString(input);

        byte[] content = writer.render();
        String strContent = Digest.bytesToText(content);
        logger.debug(strContent);
        assertEquals(strExpected, strContent);

    }
    @Test
    public void testWriteBigNumber()throws Exception{
        String strExpected ="05312e323435";
        BigDecimal val = new BigDecimal("1.245");
        BufferWriter writer = new BufferWriter();
        writer.writeBigNumber(val);

        byte[] content = writer.render();
        String strContent = Digest.bytesToText(content);
        logger.debug(strContent);
        assertEquals(strExpected, strContent);
    }
}
