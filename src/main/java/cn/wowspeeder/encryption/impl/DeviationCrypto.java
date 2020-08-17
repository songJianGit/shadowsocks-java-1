package cn.wowspeeder.encryption.impl;

import cn.wowspeeder.encryption.CryptIOBase;
import cn.wowspeeder.encryption.DeviationUtil;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DeviationCrypto extends CryptIOBase {
    public final static String DEVIATION_1 = "deviation-1";// 只偏移
    public final static String DEVIATION_2 = "deviation-2";// 偏移+混淆（相比于deviation-1，速度较慢，但是更为安全）
    // 偏移量，取值范围[0,256]，建议设置范围为[1-5]。值设置为128其实就是-1了
    public final static int x = 1;
    /**
     * 1.长度大于2
     * 2.只能是数字或英文
     * 3.不要有重复数字或字母
     * 混淆字符串
     */
    public final static String confusionString = "i1u5q3;8x7/6t9";

    public DeviationCrypto(String name, String passWord) {
        super(name, passWord);
    }

    public static Map<String, String> getCiphers() {
        Map<String, String> ciphers = new HashMap<>();
        ciphers.put(DEVIATION_1, DeviationCrypto.class.getName());
        ciphers.put(DEVIATION_2, DeviationCrypto.class.getName());
        return ciphers;
    }

    @Override
    protected void _encrypt(byte[] data, ByteArrayOutputStream stream) {
        this.process(data, stream, true);
    }

    @Override
    protected void _decrypt(byte[] data, ByteArrayOutputStream stream) {
        this.process(data, stream, false);
    }

    void process(byte[] in, ByteArrayOutputStream out, boolean encrypt) {
        if (DEVIATION_1.equals(super._name)) {
            out.write(DeviationUtil.doDeviation(in, encrypt), 0, in.length);
        } else if (DEVIATION_2.equals(super._name)) {
            if (encrypt) {
                out.write(DeviationUtil.confusion(DeviationUtil.deviation_en(in)), 0, in.length);
            } else {
                out.write(DeviationUtil.deviation_de(DeviationUtil.confusion(in)), 0, in.length);
            }
        }
    }
}
