package cn.wowspeeder.encryption.impl;

import cn.wowspeeder.encryption.CryptSteamBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bouncycastle.crypto.StreamCipher;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviationCrypto extends CryptSteamBase {
    public final static String DEVIATION_1 = "deviation-1";// 只偏移
    public final static String DEVIATION_2 = "deviation-2";// 偏移+混淆
    // 偏移量，取值范围[0,256]，建议设置范围为[1-5]。值设置为128其实就是-1了
    private final static int x = 1;
    /**
     * 1.长度大于2
     * 2.只能是数字或英文
     * 3.不要有重复数字或字母
     * 混淆字符串
     */
    private final static String confusionString = "123456";

    private static Map<Byte, Byte> listByte = Maps.newConcurrentMap();

    static {
        int length = confusionString.length();
        if (length >= 2) {
            if (length % 2 != 0) {
                length = length - 1;
            }
        }
        List<String> lists = Lists.newArrayList(confusionString.split(""));
        for (int j = 0; j < length; j += 2) {
            listByte.put(lists.get(j).getBytes()[0], lists.get(j + 1).getBytes()[0]);
            listByte.put(lists.get(j + 1).getBytes()[0], lists.get(j).getBytes()[0]);
        }
    }

    public static Map<String, String> getCiphers() {
        Map<String, String> ciphers = new HashMap<>();
        ciphers.put(DEVIATION_1, DeviationCrypto.class.getName());
        ciphers.put(DEVIATION_2, DeviationCrypto.class.getName());
        return ciphers;
    }

    public DeviationCrypto(String name, String password) {
        super(name, password);
    }

    @Override
    protected StreamCipher getCipher(boolean isEncrypted) throws InvalidAlgorithmParameterException {
        return null;
    }

    @Override
    protected SecretKey getKey() {
        return null;
    }

    @Override
    protected void _encrypt(byte[] data, ByteArrayOutputStream stream) {
        this.process(data, stream, true);
    }

    @Override
    protected void _decrypt(byte[] data, ByteArrayOutputStream stream) {
        this.process(data, stream, false);
    }

    @Override
    public int getIVLength() {
        return 0;
    }

    @Override
    public int getKeyLength() {
        return 0;
    }

    void process(byte[] in, ByteArrayOutputStream out, boolean encrypt) {
        if (DEVIATION_1.equals(super._name)) {
            int len = in.length;
            byte[] nb = new byte[len];
            if (encrypt) {
                for (int i = 0; i < len; i++) {
                    nb[i] = (byte) (in[i] + x);
                }
            } else {
                for (int i = 0; i < len; i++) {
                    nb[i] = (byte) (in[i] - x);
                }
            }
            out.write(nb, 0, nb.length);
        } else if (DEVIATION_2.equals(super._name)) {
            int len = in.length;
            byte[] nByte = new byte[len];
            if (encrypt) {
                for (int i = 0; i < len; i++) {
                    nByte[i] = (byte) (in[i] + x);
                }
                nByte = this.confusion(nByte);
            } else {
                in = this.confusion(in);
                for (int i = 0; i < len; i++) {
                    nByte[i] = (byte) (in[i] - x);
                }
            }
            out.write(nByte, 0, nByte.length);
        }
    }

    private static byte[] confusion(byte[] b) {
        byte[] tb = new byte[b.length];
        if (confusionString.length() < 2) {// 混淆字符不足长度
            return b;
        }
        for (int i = 0; i < b.length; i++) {
            if (listByte.containsKey(b[i])) {
                for (int j = 0; j < listByte.size(); j++) {
                    tb[i] = listByte.get(b[i]);
                }
            } else {
                tb[i] = b[i];
            }
        }
        return tb;
    }
}
