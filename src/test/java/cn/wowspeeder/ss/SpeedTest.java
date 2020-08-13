package cn.wowspeeder.ss;

import cn.wowspeeder.config.Constant;
import cn.wowspeeder.encryption.impl.DeviationCrypto;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static cn.wowspeeder.encryption.impl.DeviationCrypto.DEVIATION_1;
import static cn.wowspeeder.encryption.impl.DeviationCrypto.DEVIATION_2;

public class SpeedTest {

    @Test
    public void testSpeed() {
        byte[] testCase = "1098765432poiuytrewqlkjhgfdsamnbvcxz".getBytes(StandardCharsets.UTF_8);
        Constant.initListByte();
        DeviationCrypto cryptoClient = new DeviationCrypto(DEVIATION_2,"");
        DeviationCrypto cryptoServer = new DeviationCrypto(DEVIATION_2,"");
        long startTime = System.currentTimeMillis();   //获取开始时间
        for (int i = 0; i < 10; i++) {
            doinfo(testCase, cryptoServer, cryptoClient);
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }

    private void doinfo(byte[] testCase, DeviationCrypto cryptoServer, DeviationCrypto cryptoClient) {
        ByteArrayOutputStream bbb1 = new ByteArrayOutputStream(64 * 1024);
        ByteArrayOutputStream bbb2 = new ByteArrayOutputStream(64 * 1024);
        cryptoClient.encrypt(testCase, bbb1);
        cryptoServer.decrypt(bbb1.toString().getBytes(StandardCharsets.UTF_8), bbb2);
        Assert.assertArrayEquals(testCase, bbb2.toByteArray());
    }
}
