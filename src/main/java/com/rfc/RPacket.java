package com.rfc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPacket is used to turn a RTransaction into a packet(string)
 * Then it can be post to the RPC port
 *
 */
public class RPacket {
    private static final Logger logger =
            LoggerFactory.getLogger(RPacket.class);

    public static String pack(RTransaction tx) throws  Exception{
        byte[] dataBuf = tx.render();

        JSONObject objArgs = new JSONObject();
        JSONObject objTx = new JSONObject();
        JSONObject sendObj = new JSONObject();

        objArgs.put("data", Digest.bytesToInts(dataBuf));
        objArgs.put("type", "Buffer");

        objTx.put("tx", objArgs);

        sendObj.put("funName", "sendTransaction");
        sendObj.put("args", objTx);

        return sendObj.toJSONString();
    }

    /**
     * parse the Transaction
     * get method, nonce, input, signature
     * @param strPacket
     * @return
     * @throws Exception
     */
    public static JSONObject unpack(String strPacket) throws  Exception{
        logger.debug("unpack the packet");
        JSONObject packetObj =  JSON.parseObject(strPacket);

        String funName = packetObj.getString("funName");
        logger.debug("funName:{}", funName);
        JSONArray dataArr = packetObj.getJSONObject("args")
                            .getJSONObject("tx")
                            .getJSONArray("data");
        logger.debug("data array: {}", dataArr.toString());
        logger.debug("data array length: {}", dataArr.size());

        int [] dataInt = new int[dataArr.size()];
        byte[] dataByte = new byte[dataArr.size()];

        for(int i =0; i< dataArr.size(); i++){
            dataInt[i] = dataArr.getInteger(i);
            dataByte[i] = dataArr.getByte(i);
        }
        logger.debug("data byte[]: {}", dataInt);

        JSONObject objTx = RTransaction.parse(dataByte);
        logger.debug("unpack to JSONObject: {}", objTx);
        return objTx;
    }
}
