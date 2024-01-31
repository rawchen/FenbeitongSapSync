package com.lundong.sync.execution;

import cn.hutool.core.util.StrUtil;
import com.lundong.sync.config.Constants;
import com.lundong.sync.entity.base.CostType;
import com.lundong.sync.entity.base.Department;
import com.lundong.sync.entity.base.Employee;
import com.lundong.sync.util.ArrayUtil;
import com.lundong.sync.util.FenbeitongSignUtil;
import com.lundong.sync.util.SignUtil;
import com.lundong.sync.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Spring Boot定时任务
 *
 * @author RawChen
 * @date 2023-12-03 17:21
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduleTask {

    /**
     * 启动延迟4秒执行一次 && 每1小时执行一次
     * 获取最新的多维表格映射表记录到内存
     */
    @Scheduled(initialDelay = 5000, fixedRate = 3600 * 1000)
//	@Scheduled(fixedRate = 30 * 1000)
    private void scheduleTask() {
        // 初始化到内存
        List<Employee> table01 = SignUtil.findBaseList(Constants.APP_TOKEN, Constants.TABLE_01, Employee.class);
        if (!ArrayUtil.isEmpty(table01)) Constants.LIST_TABLE_01 = table01;
        List<Department> table02 = SignUtil.findBaseList(Constants.APP_TOKEN, Constants.TABLE_02, Department.class);
        if (!ArrayUtil.isEmpty(table02)) Constants.LIST_TABLE_02 = table02;
        List<CostType> table03 = SignUtil.findBaseList(Constants.APP_TOKEN, Constants.TABLE_03, CostType.class);
        if (!ArrayUtil.isEmpty(table03)) Constants.LIST_TABLE_03 = table03;
        log.info("初始化或刷新映射表成功");
    }

    /**
     * 每隔10分钟刷新一个token
     */
    @Scheduled(initialDelay = 10 * 60 * 1000, fixedRate = 10 * 60 * 1000)
    private void scheduleRefreshToken() {
        log.info("重新获得一个access_token");
        String accessTokenFenbeitong = FenbeitongSignUtil.getFenbeitongAccessToken(StringUtil.fenbeitongAppId(), StringUtil.fenbeitongAppKey());
        if (!StrUtil.isEmpty(accessTokenFenbeitong)) {
            Constants.ACCESS_TOKEN_FENBEITONG = accessTokenFenbeitong;
        }

        String accessToken = SignUtil.getAccessToken(Constants.APP_ID_FEISHU, Constants.APP_SECRET_FEISHU);
        if (!StrUtil.isEmpty(accessToken)) {
            Constants.ACCESS_TOKEN = accessToken;
        }
    }
}
