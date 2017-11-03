package com.fruit.sys.admin.utils;

import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.sys.admin.service.EnvService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟旋 on 2017/9/29.
 */
public class SmsContractLoanConfig {



    public static String getCurrentConfig(LoanSmsBizTypeEnum loanSmsBizTypeEnum) {
        String value;

        LoanSmsContactsConfigService loanSmsContactsConfigService =  SpringBeanUtils.getBean(LoanSmsContactsConfigService.class);

        LoanSmsContactsConfigDTO config = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                loanSmsBizTypeEnum.getType());

        if ("product".equals(EnvService.getEnv())) {
            value = config.getProduct();
        } else if ("beta".equals(EnvService.getEnv())) {
            value = config.getBeta();
        } else if ("dev".equals(EnvService.getEnv())) {
            value = config.getDev();
        } else {
            value = config.getAlpha();
        }
        return value;
    }


}
