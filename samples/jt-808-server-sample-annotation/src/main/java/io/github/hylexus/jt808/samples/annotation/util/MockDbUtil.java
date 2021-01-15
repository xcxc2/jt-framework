package io.github.hylexus.jt808.samples.annotation.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j(topic = "mockdb")
public class MockDbUtil {
    public static void save(String tag, Object... var2){
        String infos ="+++"+tag+"\t"+ StringUtils.joinWith("\t", var2);
        log.info(infos);
    }
}
