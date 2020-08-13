package cn.wowspeeder;

import cn.wowspeeder.config.Constant;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.commons.cli.*;

public class Application {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(Application.class);

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option option = new Option("s", "server", false, "server listen address");
        options.addOption(option);

        option = new Option("c", "client", false, "server connect address");
        options.addOption(option);

        option = new Option("conf", "config", true, "config file path default:conf/config.json");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        String configPath = commandLine.getOptionValue("conf", "conf/config.json");

        logger.info("config path:{}", configPath);
        if (commandLine.hasOption("s")) {
            SSServer.getInstance().start(configPath);
        } else if (commandLine.hasOption("c")) {
            SSLocal.getInstance().start(configPath);
        } else {
            logger.error("not found run type");
        }

        initInfo();
        logger.info("start success!");
    }

    /**
     * 初始化一些数据
     * TODO 需要根据加密类型来判断是否需要初始化一些信息，或者初始化哪些信息
     */
    private static void initInfo() {
        Constant.initListByte();
    }
}
