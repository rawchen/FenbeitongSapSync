package com.lundong.sync.execution;

import com.lundong.sync.config.Constants;
import com.lundong.sync.util.FenbeitongSignUtil;
import com.lundong.sync.util.SignUtil;
import com.lundong.sync.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Spring Boot启动后自动执行
 *
 * @author RawChen
 * @date 2023-12-03 17:21
 */
@Slf4j
@Component
@Order(1)
public class InitialOperation implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 初始化ACCESS_TOKEN
        Constants.ACCESS_TOKEN_FENBEITONG = FenbeitongSignUtil.getFenbeitongAccessToken(StringUtil.fenbeitongAppId(), StringUtil.fenbeitongAppKey());
        log.info("初始化分贝通ACCESS_TOKEN: {}", Constants.ACCESS_TOKEN_FENBEITONG);

        Constants.ACCESS_TOKEN = SignUtil.getAccessToken(Constants.APP_ID_FEISHU, Constants.APP_SECRET_FEISHU);
        log.info("初始化飞书ACCESS_TOKEN: {}", Constants.ACCESS_TOKEN);
    }

}
