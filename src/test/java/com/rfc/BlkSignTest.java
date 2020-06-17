package com.rfc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.Test;


import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;


public class BlkSignTest {
    private static final Logger logger = LoggerFactory.getLogger(BlkSignTest.class);

    private  void readFile(StringBuffer buffer, String filename) throws IOException {
        InputStream is = new FileInputStream(filename);
        logger.debug("read File");
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();

        while (line != null) {
        buffer.append(line);
        buffer.append("\n");
        line = reader.readLine();
        }
        reader.close();
        is.close();
        logger.debug("finished read File");
    }
    @Test
    public void test1() throws IOException ,Exception {
        logger.debug("Test signature in bulk volume");
        String filename = "new2001.json";
        logger.debug(BlkSignTest.class.getClassLoader().getResource(filename).getPath());

        String filepath = BlkSignTest.class.getClassLoader().getResource(filename).getPath();
        StringBuffer buffer = new StringBuffer();
        readFile(buffer, filepath);

        JSONObject obj = JSON.parseObject(buffer.toString());

//        System.out.println(obj);
        JSONArray arrOutput = new JSONArray();
        JSONArray arrObj = obj.getJSONArray("data");

        for(int i=0; i< arrObj.size(); i++){
            logger.debug("{}",i);
            JSONObject objTx = arrObj.getJSONObject(i);
            String secret = objTx.getString("fromSecret");
            int nonce = objTx.getInteger("nonce");
            String toAddress = objTx.getString("toAddress");
            String method = objTx.getString("method");
            BigDecimal amount = new BigDecimal(objTx.getString("value"));
            BigDecimal fee = new BigDecimal(objTx.getString("fee"));

            RTransaction tx = new RTransaction();
            tx.setMethod(method);
            tx.setNonce(nonce);
            tx.setPublicKey(secret);

            JSONObject input = new JSONObject();
            input.put("to", toAddress);
            tx.setInput(input);
            tx.setValue(amount);
            tx.setFee(fee);
            tx.sign(secret);

            JSONObject objton = new JSONObject();
            objton.put("signature", Arrays.toString(Digest.bytesToInts(tx.m_signature)));
            objton.put("hash", tx.m_hash);
            arrOutput.add(objton);
        }
        JSONObject objAll = new JSONObject();
        objAll.put("data", arrOutput);

        String path = filepath + "-java-sign.json";
        File file = new File(path);
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }

        file.createNewFile();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        writer.write(objAll.toJSONString());
        writer.flush();
        writer.close();
        assert(true);
    }
}
