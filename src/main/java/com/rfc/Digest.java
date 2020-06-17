package com.rfc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.bitcoinj.core.*;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.bitcoinj.params.MainNetParams;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

public class Digest {
    private static final Logger logger = LoggerFactory.getLogger(Digest.class);

    public static byte[] hash256(byte[] inBytes, int offset) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = Arrays.copyOf(inBytes, offset);
            byte[] encodedHash = digest.digest(bytes);
            return digest.digest(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 dual hash failed", e);
        }
    }

    public static byte[] RIPEMD160(byte[] input) {
        RIPEMD160Digest d = new RIPEMD160Digest();

        d.update(input, 0, input.length);

        byte[] o = new byte[d.getDigestSize()];

        d.doFinal(o, 0);

        return o;
    }

    public static byte[] textToBytes(String text) {
        logger.debug("length:" + text.length());

        if (text.length() % 2 != 0) {
            text = "0" + text;
        }
        byte[] bufOut = new byte[text.length() / 2];
        for (int i = 0; i < bufOut.length; i++) {
            // logger.debug(":" + i);
            byte hi = (byte) (Character.digit(text.charAt(i * 2), 16) & 0xff);
            byte lo = (byte) (Character.digit(text.charAt(i * 2 + 1), 16) & 0xff);

            bufOut[i] = (byte) (hi << 4 | lo);

        }
        return bufOut;
    }

    public static String bytesToText(byte[] buf) {
        StringBuilder strBd = new StringBuilder();

        for (byte b : buf) {
            String strByte = Integer.toHexString(b & 0xff);
            if (strByte.length() < 2) {
                strByte = "0" + strByte;
            }
            strBd.append(strByte);
        }
        return strBd.toString();
    }

    public static int[] bytesToInts(byte[] buf) {
        int[] ints = new int[buf.length];
        for (int i = 0; i < buf.length; i++) {
            ints[i] = buf[i] < 0 ? ((int) buf[i] + 256) : (int) buf[i];
        }
        return ints;
    }

    public static boolean verify() {
        return false;
    }

    public static byte[] sign(String hash, String secret) throws Exception {
        logger.debug("sign()");
        BigInteger priv = new BigInteger(secret, 16);
        ECKey secretKey = ECKey.fromPrivate(priv);
        Sha256Hash shahash = Sha256Hash.wrap(hash);
        ECDSASignature signature = secretKey.sign(shahash);

        StringBuilder strR = new StringBuilder(signature.r.toString(16));
        StringBuilder strS = new StringBuilder(signature.s.toString(16));
        logger.debug("output R");
        logger.debug(strR.toString());
        logger.debug("len: {}" , strR.length());
        if(strR.length() > 64){
            throw new Exception("sign R out of range");
        }
        while (strR.length() != 64) {
            strR.insert(0, "0");
        }
        logger.debug("after prefix padding");
        logger.debug(strR.toString());
        logger.debug("output S");
        logger.debug(strS.toString());
        logger.debug("len: {}" , strS.length());

        if(strS.length() > 64){
            throw new Exception("sign S out of range");
        }
        while (strS.length() != 64) {
            strS.insert(0, "0");
        }
        logger.debug("after prefix padding: {}");
        logger.debug(strS.toString());

        byte[] sign = textToBytes(strR + strS.toString());

        return sign;
    }
//
//    public static byte[] sign1(String hash, String secret) {
//        // hash, secret must be 32 bytes
//        byte[] byteHash = textToBytes(hash);
//        byte[] byteSecret = textToBytes(secret);
//        byte[] sign;
//        try {
//            sign = NativeSecp256k1.sign(byteHash, byteSecret);
//        } catch (NativeSecp256k1Util.AssertFailException e) {
//            System.err.println("sign err");
//            return null;
//        }
//
//        return sign;
//    }
    public static String publicKeyFromSecretKey(String secret) {
        BigInteger priv = new BigInteger(secret, 16);

        ECKey key = ECKey.fromPrivate(priv);
        String pub = key.getPublicKeyAsHex();

        return pub;
    }
    /**
     * address from secret key ,
     *
     * @param secret 32 bytes hex format
     */
    public static String addressFromSecretKey(String secret) {

        BigInteger priv = new BigInteger(secret, 16);

        ECKey key = ECKey.fromPrivate(priv);
        NetworkParameters params = new MainNetParams();
        Address addr = key.toAddress(params);

        return addr.toString(); // lyc: 主动调用 toString，addr + "" 其实也是触发 toString() 方法
    }
    public static String createKey() {

        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            ECKey key = new ECKey(secureRandom);

            BigInteger pvt = key.getPrivKey();

            String strKey = Digest.adjustTo64(pvt.toString(16));

            return strKey;

        } catch (NoSuchAlgorithmException e) {
            logger.error("createKey(): {}" , e);
            return null;
        }
    }

    public static String adjustTo64(final String s) {
        switch (s.length()) {
            case 62:
                return "00" + s;
            case 63:
                return "0" + s;
            case 64:
                return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
    }

    /**
     * check address valid or not
     */
    public static boolean isValidAddress(final String str) {
        NetworkParameters params = new MainNetParams();

        try {
            Address addr = Address.fromBase58(params, str);

        } catch (AddressFormatException e) {
            logger.error("Wrong addr {}", e);
            return false;
        }

        return true;
    }

    public static boolean isValidAmount(final String str) {
        try {
            BigDecimal num = new BigDecimal(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isValidAmount(final BigDecimal dec) {
        try {
            BigDecimal num = dec;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isValidFee(final String str) {
        try {
            double num = Double.parseDouble(str);
            return num >= 0.1;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean isValidFee(final int innum) {
        try {
            int num = innum;
            return num >= 0.1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidFee(final BigDecimal innum) {
        try {
            double num = innum.doubleValue();
            return (num >= 0.1);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
