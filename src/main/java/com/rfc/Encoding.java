package com.rfc;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson.JSONObject;

public class Encoding {

    public static final int MAX_DECIMAL_LEN = 9;
    public static final Charset encodingType = StandardCharsets.UTF_8;
    public static final String ONE_HASH = "0100000000000000000000000000000000000000000000000000000000000000";
    public static final String ZERO_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    public static final String MAX_HASH = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    public static final String NULL_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    public static final String HIGH_HASH = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    public static final String ZERO_KEY = "000000000000000000000000000000000000000000000000000000000000000000";
    public static final String ZERO_SIG64 = "0000000000000000000000000000000000000000000000000000000000000000"
            + "0000000000000000000000000000000000000000000000000000000000000000";

    public static String textToHex(String text) {
        byte[] buf = null;
        buf = text.getBytes(Encoding.encodingType);

        char[] HEX_CHARS = "0123456789abcdef".toCharArray();

        char[] chars = new char[2 * buf.length];

        for (int i = 0; i < buf.length; i++) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static String hexToText(String hex) {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        String st = new String(data, Encoding.encodingType);
        return st;
    }

    public static int sizeVarint(int num) {
        if (num < 0xfd) {
            return 1;
        }

        if (num <= 0xffff) {
            return 3;
        }

        if (num <= 0xffffffff) {
            return 5;
        }

        return 9;
    }

    /**
     * a shallow JSONObject here
     *
     * @param input
     * @param parsable
     * @return String
     */
    public static String toStringifiable(JSONObject input, boolean parsable) {
        try {
            // lyc: StringBuffer 一般在多线程时使用，单线程下 StringBuilder 效率高很多
            StringBuilder sb = new StringBuilder("{");

            for( String key : input.keySet()){
                sb.append("\"");
                sb.append(key);
                sb.append("\"");
                sb.append(":");

                Object obj = input.get(key);
                if(obj.getClass() == String.class){
                    sb.append("\"")
                            .append("s").append(obj)
                            .append("\"");
                }else if(obj.getClass() == BigDecimal.class){
                    sb.append("n").append(obj);
                }else if(obj.getClass() == Integer.class){
                    sb.append(obj.toString());
                }
                sb.append(",");
            }

            if(sb.length()>2){
                sb.deleteCharAt(sb.length()-1);
            }

            sb.append("}");
            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    }
}