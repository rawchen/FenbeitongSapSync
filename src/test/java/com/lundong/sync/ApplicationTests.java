package com.lundong.sync;

import cn.hutool.core.util.ArrayUtil;
import com.lundong.sync.entity.BitableParam;
import com.lundong.sync.entity.base.Bitable;
import com.lundong.sync.entity.bitable.approval.PaymentRequestOne;
import com.lundong.sync.entity.bitable.bitable.ConsumptionEstimation;
import com.lundong.sync.entity.bitable.bitable.IncomeEstimation;
import com.lundong.sync.entity.kingdee.AccountingDimension;
import com.lundong.sync.util.FenbeitongSignUtil;
import com.lundong.sync.util.SignUtil;
import com.lundong.sync.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void t01() {
        List<PaymentRequestOne> baseList = SignUtil.findBaseList("N7T2bfpf0a34kWser2mc3rgmnBc", "tblXFhGLneZiJhph", PaymentRequestOne.class);
        for (PaymentRequestOne paymentRequest : baseList) {
            System.out.println(paymentRequest);
        }
    }

    @Test
    void t04() {
        String result = SignUtil.getFeishuUserName("fa222fd1");
        System.out.println(result);
    }

    @Test
    void t05() {
        AccountingDimension accountingDimension = new AccountingDimension();
        accountingDimension.setFflex4("123");
        try {
            Class<?> clazz = accountingDimension.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Object value;
                value = field.get(accountingDimension);
                if (value == null) {
                    field.set(accountingDimension, "");
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(accountingDimension);
    }

    @Test
    void t06() {
        Bitable bitable = new Bitable();
        bitable.setCostCategory("123");
        bitable.setCostSubcategory("567（）789");
        StringUtil.bracketReplace(bitable);
        System.out.println(bitable);
    }

    @Test
    void t07() {
        List<Bitable> bitables = Collections.emptyList();
        System.out.println(ArrayUtil.isEmpty(bitables));
    }

    @Test
    void t08() {
        BitableParam bitableParam = new BitableParam();
        bitableParam.setAppToken("Aw02btCEVa3GuBskRUfcSNelnI8");
        bitableParam.setTableId("tblXtdpvOXX9Jweq");
        bitableParam.setRecordId("recncnIEwm");
        IncomeEstimation baseRecord = SignUtil.findBaseRecord(bitableParam, IncomeEstimation.class);
        System.out.println(baseRecord);
    }

    @Test
    void t09() {
        BitableParam bitableParam = new BitableParam();
        bitableParam.setAppToken("Aw02btCEVa3GuBskRUfcSNelnI8");
        bitableParam.setTableId("tblXtdpvOXX9Jweq");
        bitableParam.setRecordId("recncnIEwm");
        SignUtil.updateHasGenerate("1", bitableParam);
    }

    @Test
    void t10() {
        BitableParam bitableParam = new BitableParam();
        bitableParam.setAppToken("Aw02btCEVa3GuBskRUfcSNelnI8");
        bitableParam.setTableId("tblINVYVClMC2ufd");
        bitableParam.setRecordId("recoQiMj5Z");
        ConsumptionEstimation baseRecord = SignUtil.findBaseRecord(bitableParam, ConsumptionEstimation.class);
        System.out.println(baseRecord);
    }

    @Test
    void t11() {
        System.out.println(FenbeitongSignUtil.getFenbeitongAccessToken(StringUtil.fenbeitongAppId(), StringUtil.fenbeitongAppKey()));
    }
}
