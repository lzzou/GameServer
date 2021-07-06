package com.base.component;

import com.base.config.AllConfigList;
import com.zlz.util.FileUtil;
import com.zlz.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * 全局配置组件
 */
@Component
public class GlobalConfigComponent extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(GlobalConfigComponent.class);

    private static final String CONFIG_FILE = "bootstrap.xml";

    private static AllConfigList config;

    public static AllConfigList getConfig() {
        return config;
    }

    private static boolean init(String path) {
        try {

            // String xmlStr = FileUtil.readTxt(path, "UTF-8");
            String xmlStr = FileUtil.readTxt(path, "UTF-8");

            config = XmlUtil.toObject(xmlStr, AllConfigList.class);
            if (config == null) {
                log.error("Server Config[{}] Load Failed.", path);
                return false;
            }

            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean initialize() {
        URL resource = getClass().getClassLoader().getResource(CONFIG_FILE);
        if (Objects.nonNull(resource)) {
            log.info("Global config file path : {}", resource.getPath());
            init(resource.getPath());
            return true;
        } else {
            log.error("Can not find the global config file path, config file : {}", CONFIG_FILE);
            return false;
        }
    }

    @Override
    public void stop() {
        config = null;
    }
}