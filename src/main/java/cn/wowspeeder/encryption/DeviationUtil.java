package cn.wowspeeder.encryption;

import cn.wowspeeder.config.Constant;
import cn.wowspeeder.encryption.impl.DeviationCrypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviationUtil {

    private static Logger logger = LoggerFactory.getLogger(DeviationUtil.class);

    public static byte[] doDeviation(byte[] a, boolean encrypt) {
        if (encrypt) {
            return deviation_en(a);
        } else {
            return deviation_de(a);
        }
    }

    public static byte[] deviation_en(byte[] a) {
        int len = a.length;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = (byte) (a[i] + DeviationCrypto.x);
        }
        return b;
    }

    public static byte[] deviation_de(byte[] a) {
        int len = a.length;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = (byte) (a[i] - DeviationCrypto.x);
        }
        return b;
    }

    public static byte[] confusion(byte[] b) {
        if (DeviationCrypto.confusionString.length() < 2) {// 混淆字符长度不足，不进行混淆处理
            return b;
        }
        byte[] tb = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            if (Constant.listByte.containsKey(b[i])) {
                tb[i] = Constant.listByte.get(b[i]);// 需要混淆的字段就进行混淆处理
            } else {
                tb[i] = b[i];
            }
        }
        return tb;
    }

}
