package com.lundong.sync.config;

import com.lundong.sync.entity.base.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author RawChen
 * @date 2023-06-25 14:02
 */
@EnableConfigurationProperties
public class Constants {

    // 飞书
    public static String ACCESS_TOKEN = "";

    // 分贝通
    public static String ACCESS_TOKEN_FENBEITONG = "";

    // 分贝通（测试）
    public static final String APP_ID_TEST = "6588f5c026c41b60ecxxxxxx";
    public static final String APP_KEY_TEST = "6588f60ef44704046xxxxxx";

    // 分贝通（正式）
    public static final String APP_ID_PROD = "656801c0ce95a36xxxxxx";
    public static final String APP_KEY_PROD = "658ab9bb0af72d67399xxxxxx";

    public static final boolean IS_PROD = true;

    // API接口
    public final static String URL_TEST = "https://openapi-fat3.fenbeijinfu.com";
    public final static String URL_PROD = "https://openapi.fenbeitong.com";
//    public static final String SAP_URL = "https://my820912.businessbydesign.sapcloud.cn/sap/bc/srt/scs/sap/yyqjvuguhy_expensereport";
    public static final String SAP_URL = "https://my820955.businessbydesign.sapcloud.cn/sap/bc/srt/scs/sap/yyqjvuguhy_expensereport";

    // 飞书自建应用 App ID
    public final static String APP_ID_FEISHU = "cli_a51ddefa847xxxxxx";

    // 飞书自建应用 App Secret
    public final static String APP_SECRET_FEISHU = "n6BtqBANymdEUFbtvhK5werTxxxxxxx";

    // 飞书自建应用订阅事件 Encrypt Key
    public final static String ENCRYPT_KEY = "";

    // 飞书自建应用订阅事件 Verification Token
    public final static String VERIFICATION_TOKEN = "iGmFzMtv0HAfIoC8TxXvQfqoMxxxxxx";

    // 分贝通审批差旅FORM_ID
    public final static String APPROVAL_FORM_ID_TRAVEL = "65829505d6c3c27c0d9a12fa";

    // 分贝通审批非差旅FORM_ID
    public final static String APPROVAL_FORM_ID_NOT_TRAVEL = "656801cb8733295cb180f083";

    // 审批生成凭证 多维表格APP_TOKEN
    public static final String APP_TOKEN = "AhFkbh3KXarc5Ds7rXQc2tMjnng";

    // 员工表
    public static final String TABLE_01 = "tblsFoRjCE2TQjv0";
    public static List<Employee> LIST_TABLE_01;

    // 组织架构表
    public static final String TABLE_02 = "tblijY8o9UAUeImF";
    public static List<Department> LIST_TABLE_02;

    // 费用类型表
    public static final String TABLE_03 = "tblNxgroX0cTSUib";
    public static List<CostType> LIST_TABLE_03;

    // 同步记录
    public static final String TABLE_04 = "tblB0i9kk77p12vk";

}
