/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.tools.excel;

import com.fruit.sys.admin.model.FileUploadRequest;
import com.fruit.sys.admin.utils.PoiExcelUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : ExcelMobilesProcessor.java
 */
public class ExcelMobilesProcessor
{
    public static List<String> process(FileUploadRequest uploadRequest)
    {
        Validate.notNull(uploadRequest);
        Validate.isTrue(null != uploadRequest.getContents() && uploadRequest.getContents().length > 0, "请上传文件!");
        Validate.isTrue(StringUtils.isNotBlank(uploadRequest.getFileName()), "请上传正确的文件!");
        String fileExtension = FilenameUtils.getExtension(uploadRequest.getFileName());
        Validate.isTrue(StringUtils.isNotBlank(fileExtension), "请上传正确的文件!");
        ByteArrayInputStream input = new ByteArrayInputStream(uploadRequest.getContents());
        Workbook workbook = PoiExcelUtil.loadWorkbook(input, fileExtension);
        List<String> mobiles = new ArrayList<String>();
        Sheet sheet = workbook.getSheetAt(0); // 不做Sheet循环，只取第一个Sheet
        // 行循环
        int rows = sheet.getPhysicalNumberOfRows();
        for (int rIndex = 1; rIndex < rows; rIndex++)
        {
            // 处理具体行
            Row row = sheet.getRow(rIndex);
            if (null == row)
            {
                continue;
            }
            String mobile = PoiExcelUtil.getCellAsString(row, 0); // 第一列即为手机号
            if (StringUtils.isNotBlank(mobile) && mobile.length() == 11 && NumberUtils.isDigits(mobile))
            {
                long toNumber = NumberUtils.toLong(mobile, 0);
                if (0 != toNumber)
                {
                    mobiles.add(mobile);
                }
            }
        }
        return mobiles;
    }
}
