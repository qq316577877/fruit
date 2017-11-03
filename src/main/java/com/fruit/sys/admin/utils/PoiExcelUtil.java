package com.fruit.sys.admin.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * poi 实现excel帮助类
 *
 * @author zhanghongxin
 */
public class PoiExcelUtil
{
    private static final Log log = LogFactory.getLog(PoiExcelUtil.class);

    /**
     * 功能：将HSSFWorkbook写入Excel文件 并写入指定路径
     *
     * @param wb       HSSFWorkbook
     * @param fileName 文件名 (带路径的比如：D:data\doc\aaa.xls)
     */
    public static void writeWorkbook(HSSFWorkbook wb, String fileName)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(fileName);
            wb.write(fos);
        }
        catch (FileNotFoundException e)
        {
            log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));
            e.printStackTrace();
        }
        catch (IOException e)
        {
            log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (IOException e)
            {
                log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能：将HSSFWorkbook写入Excel文件,保存于客户端
     *
     * @param wb       HSSFWorkbook
     * @param response 相应对象，为了客户端响应时提示保存
     * @param fileName 文件名
     */
    public static void writeWorkbookToDesk(HSSFWorkbook wb, String fileName, HttpServletResponse response)
    {
        OutputStream fos = null;
        try
        {
            fos = response.getOutputStream();
            response.reset();// 清空输出流
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "8859_1"));// 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型 
            wb.write(fos);
        }
        catch (FileNotFoundException e)
        {
            log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));
            e.printStackTrace();
        }
        catch (IOException e)
        {
            log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (IOException e)
            {
                log.error(new StringBuffer("[").append(e.getMessage()).append("]").append(e.getCause()));
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能：创建HSSFSheet工作簿
     *
     * @param wb        HSSFWorkbook
     * @param sheetName String
     * @return HSSFSheet
     */
    public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName)
    {
        HSSFSheet sheet = wb.createSheet(sheetName);//创建sheet并设置sheet名字
        sheet.setDefaultColumnWidth(20);//设置默认列宽
        sheet.setGridsPrinted(true);//设置网格打印
        sheet.setDisplayGridlines(true);//设置显示网格线
        return sheet;
    }

    /**
     * 功能：创建HSSFRow
     *
     * @param sheet  HSSFSheet
     * @param rowNum int
     * @param height int
     * @return HSSFRow
     */
    public static HSSFRow createRow(HSSFSheet sheet, int rowNum, int height)
    {
        HSSFRow row = sheet.createRow(rowNum);//根据行号创建行
        row.setHeight((short) height);//设置行高
        return row;
    }

    /**
     * 功能：创建CellStyle样式
     *
     * @param wb              HSSFWorkbook
     * @param backgroundColor 背景色
     * @param foregroundColor 前置色
     * @param    font            字体
     * @return CellStyle
     */
    public static CellStyle createCellStyle(HSSFWorkbook wb, short backgroundColor, short foregroundColor, short halign, Font font)
    {
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(halign);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setFillBackgroundColor(backgroundColor);
        cs.setFillForegroundColor(foregroundColor);
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setFont(font);
        return cs;
    }

    /**
     * 功能：创建带边框的CellStyle样式
     *
     * @param wb              HSSFWorkbook
     * @param backgroundColor 背景色
     * @param foregroundColor 前置色
     * @param    font            字体
     * @return CellStyle
     */
    public static CellStyle createBorderCellStyle(HSSFWorkbook wb, short backgroundColor, short foregroundColor, short halign, Font font)
    {
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(halign);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setFillBackgroundColor(backgroundColor);
        cs.setFillForegroundColor(foregroundColor);
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setFont(font);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        return cs;
    }

    /**
     * 功能：创建CELL
     *
     * @param row     HSSFRow
     * @param cellNum int
     * @param style   HSSFStyle
     * @return HSSFCell
     */
    public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style)
    {
        HSSFCell cell = row.createCell(cellNum);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 功能：合并单元格
     *
     * @param sheet       HSSFSheet
     * @param firstRow    int
     * @param lastRow     int
     * @param firstColumn int
     * @param lastColumn  int
     * @return int            合并区域号码
     */
    public static int mergeCell(HSSFSheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn)
    {
        return sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
    }

    /**
     * 功能：创建字体
     *
     * @param wb         HSSFWorkbook
     * @param boldweight short
     * @param color      short
     * @return Font
     */
    public static Font createFont(HSSFWorkbook wb, short boldweight, short color, short size)
    {
        Font font = wb.createFont();
        font.setBoldweight(boldweight);
        font.setColor(color);
        font.setFontHeightInPoints(size);
        return font;
    }

    /**
     * 设置合并单元格的边框样式
     *
     * @param ca    CellRangAddress
     * @param style CellStyle
     * @param    sheet    HSSFSheet
     */
    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress ca, CellStyle style)
    {
        for (int i = ca.getFirstRow(); i <= ca.getLastRow(); i++)
        {
            HSSFRow row = HSSFCellUtil.getRow(i, sheet);
            for (int j = ca.getFirstColumn(); j <= ca.getLastColumn(); j++)
            {
                HSSFCell cell = HSSFCellUtil.getCell(row, j);
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 加载excel文件，并返回文件对象
     *
     * @param input
     * @param fileExtension
     * @return
     */
    public static Workbook loadWorkbook(InputStream input, String fileExtension)
    {
        Workbook book = null;
        try
        {
            if (StringUtils.equalsIgnoreCase(fileExtension, "xlsx"))
            {
                book = new XSSFWorkbook(input);
            }
            else
            {
                book = new HSSFWorkbook(input);
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("文件格式错误!");
        }
        finally
        {
            IOUtils.closeQuietly(input);
        }
        return book;
    }

    /**
     * 返回指定Cell的字符串形式
     */
    public static String getCellAsString(Row row, int cellnum)
    {
        Cell cell = row.getCell(cellnum);
        if (null == cell)
        {
            return "";
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        return StringUtils.trimToEmpty(cell.getStringCellValue());
    }

    /**
     * 返回指定Cell的字符串形式
     */
    public static String getCellTypeAsString(Row row, int cellnum)
    {
        Cell cell = row.getCell(cellnum);
        if (null == cell)
        {
            return null;
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        return StringUtils.trimToEmpty(cell.getStringCellValue());
    }

    /**
     * 指定按照相应的cell类型返回相应数值，默认返回null ,方便数据更新，以免将原有数据覆盖
     * @param cell
     * @return
     */
    public static String getValueByCellType(Cell cell) {
        String value = null;
        if(null==cell){
            return value;
        }
        switch (cell.getCellType()) {
            //数值型
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //如果是date类型则 ，获取该cell的date值
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = format.format(date);;
                }else {// 纯数字
                    BigDecimal big=new BigDecimal(cell.getNumericCellValue());
                    value = big.toString();
                    //解决1234.0  去掉后面的.0
                    if(null!=value&&!"".equals(value.trim())){
                        String[] item = value.split("[.]");
                        if(1<item.length&&"0".equals(item[1])){
                            value=item[0];
                        }
                    }
                }
                break;
            //字符串类型
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue().toString();
                break;
            // 公式类型
            case Cell.CELL_TYPE_FORMULA:
                //读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue().toString();
                }
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                value = " "+ cell.getBooleanCellValue();
                break;
            // 空值
            case Cell.CELL_TYPE_BLANK:
                value = null;
                break;
            // 故障
            case Cell.CELL_TYPE_ERROR:
                value = null;
                break;
            default:
                value = cell.getStringCellValue().toString();
        }
        if (value != null)
        {
            if ("null".endsWith(value.trim()))
            {
                value = null;
            }

        }
        return value;
    }
}
