package cn.wowspeeder.config;

import cn.wowspeeder.encryption.impl.DeviationCrypto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 放一些常量
 */
public class Constant {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(Constant.class);

    public static Map<Byte, Byte> listByte = Maps.newConcurrentMap();// 混淆用的数据缓存

    public static void initListByte() {// 初始化混淆数据
        int length = DeviationCrypto.confusionString.length();
        if (length >= 2) {
            if (length % 2 != 0) {
                length = length - 1;
            }
        }
        List<String> lists = Lists.newArrayList(DeviationCrypto.confusionString.split(""));
        for (int j = 0; j < length; j += 2) {
            Constant.listByte.put(lists.get(j).getBytes()[0], lists.get(j + 1).getBytes()[0]);
            Constant.listByte.put(lists.get(j + 1).getBytes()[0], lists.get(j).getBytes()[0]);
        }
        logger.info("initListByte-deviation");
    }

}
