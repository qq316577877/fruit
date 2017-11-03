package com.fruit.sys.admin.utils;

import com.fruit.sys.admin.model.IdValueVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author zhanghongxin
 */
public class BizUtils
{
    private static final Log LOGGER = LogFactory.getLog(BizUtils.class);

    public static final String UTF_8 = "UTF-8";

    private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static final String DECIMAL_FORAMT = "#0.##";

    private static final String DISCOUNT_FORAMT = "0.###";

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");

    /**
     * 两个Double类型比较是否相等
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean doubleCompare(Double d1, Double d2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        if (b1.compareTo(b2) == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 判断某字符是否包含在当前字符串中
     * 根据起始、结束下标截取需比较的字符和传入的字符比较返回boolean
     *
     * @param str        比较字符串
     * @param c          比较字符
     * @param beginIndex 字符串开始下标
     * @param endIndex   字符串结束下标
     * @return
     */
    public static boolean charExistsString(String str, String c, int beginIndex, int endIndex)
    {
        c = c.toUpperCase();
        str = str.toUpperCase();
        String r = "";
        r = str.substring(beginIndex, endIndex);
        if (r.equals(c))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 格式化Double类型
     *
     * @param number  数字
     * @param pattern 格式，例如：##.00
     * @return
     */
    public static Double DecimalFormat(Double number, String pattern)
    {
        DecimalFormat df = new DecimalFormat(pattern);
        return Double.parseDouble(df.format(number));
    }

    /**
     * @param str   字符串
     * @param index 指定的索引位置
     * @return 是字母返回true，反之则返回false
     */
    public static boolean isCharacter(String str, int index)
    {
        char c = str.charAt(index);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 日期运算
     *
     * @param dateStr   字符串格式的日期
     * @param formatStr 格式化类型,例如：yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
     * @param num       运算数，正数为加，负数为减
     * @param year      年
     * @param month     月
     * @param day       日
     * @param hour      时
     * @param minute    分
     * @param second    秒
     * @return String
     * 例子：比如我要在获取的时间上增加一天，参数：2013-10-11,yyyy-MM-dd,1,false,false,true,false,false,false
     */
    public static String DateComput(String dateStr, String formatStr, int num, boolean year, boolean month, boolean day, boolean hour, boolean minute, boolean second)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date dt = null;
        try
        {
            dt = sdf.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(dt);
        if (year)
        {//年
            currentDate.add(Calendar.YEAR, num);
        }
        if (month)
        {//月
            currentDate.add(Calendar.MARCH, num);
        }
        if (day)
        {//日
            currentDate.add(Calendar.DAY_OF_YEAR, num);
        }
        if (hour)
        {//时
            currentDate.add(Calendar.HOUR_OF_DAY, num);
        }
        if (minute)
        {//分
            currentDate.add(Calendar.MINUTE, num);
        }
        if (second)
        {//秒
            currentDate.add(Calendar.SECOND, num);
        }
        Date dtTemp = currentDate.getTime();
        String reStr = sdf.format(dtTemp);
        return reStr;
    }

    /**
     * 转换并格式化日期
     *
     * @param date      日期
     * @param formatStr 格式化类型,例如：yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String dateConvert(Date date, String formatStr)
    {
        if (null == date)
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    public static String getDefaultDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 格式化日期并转换为date类型
     *
     * @param dateStr
     * @param formatStr 格式化类型,例如：yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date dateConvertToString(String dateStr, String formatStr)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date d = new Date();
        try
        {
            d = sdf.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 把String 日期格式化
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static String dateConvert(String dateStr, String formatStr)
    {
        DateFormat df = new SimpleDateFormat(formatStr);
        Date date = null;
        try
        {
            date = df.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return df.format(date);
    }

    /**
     * 根据路径创建对应日期的文件夹
     *
     * @param path
     * @param orderDate
     */
    public static void createFolderByPath(String path, String orderDate)
    {
        File file = new File(path + "/" + orderDate);
        if (!file.exists())
        {
            file.mkdir();
        }
    }

    /**
     * 根据配置文件名称、配置名称获取属性值
     *
     * @param propertieName
     * @param configName
     * @return
     */
    public static String findProperties(String propertieName, String configName)
    {
        InputStream is = BizUtils.class.getClassLoader().getResourceAsStream(propertieName);
        Properties property = new Properties();
        String returnStr = "";
        try
        {
            property.load(is);
            returnStr = property.getProperty(configName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return returnStr;
    }

    /**
     * 下载附件，附件名称字符集转换
     * PS：默认情况下中文名称是乱码，不同的浏览器解码方式不一样，目前只测试了Firefox、IE、Google。Firefox使用getBytes，IE、Google使用URLEncoder
     *
     * @param req
     * @param name
     * @return
     */
    public static String encode(HttpServletRequest req, String name)
    {
        String agent = req.getHeader("USER-AGENT");//获取用户客户端浏览器信息
        if (null != agent && -1 != agent.indexOf("Firefox"))
        {//Firefox
            try
            {
                name = new String(name.getBytes("UTF-8"), "iso-8859-1");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        else if (null != agent && -1 != agent.indexOf("Chrome"))
        {//Chrome
            try
            {
                name = new String(name.getBytes(), "iso-8859-1");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        else
        { //null != agent && -1 != agent.indexOf("MSIE") IE
            try
            {
                name = URLEncoder.encode(name, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        return name;
    }

    /**
     * 字符串截断
     *
     * @param source
     * @param maxLength
     * @return
     */
    public static String abbreviate(String source, int maxLength)
    {
        String result = "";
        if (StringUtils.isNotBlank(source))
        {
            if (source.length() > maxLength)
            {
                result = source.substring(0, maxLength);
            }
            else
            {
                result = source;
            }
        }
        return result;
    }

    /**
     * 功能描述：获取UUID,无连接符<p>
     * <p/>
     * 前置条件：<p>
     * <p/>
     * 方法影响： <p>
     * <p/>
     * Author shang.gao, 2013-9-27
     *
     * @return
     * @since open-platform-common 2.0
     */
    public static String getUUID()
    {
        UUID uuid = UUID.randomUUID();
        return (digits(uuid.getMostSignificantBits() >> 32, 8) + digits(uuid.getMostSignificantBits() >> 16, 4)
                + digits(uuid.getMostSignificantBits(), 4) + digits(uuid.getLeastSignificantBits() >> 48, 4)
                + digits(uuid.getLeastSignificantBits(), 12));
    }

    private static String digits(long val, int digits)
    {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1).toLowerCase();
    }

    public static String getTradeNo()
    {
        return DateFormatUtils.format(new Date(), YYYYMMDDHHMMSS) + "-" + getUUID();
    }

    public static String escapeProductCode(String productCode)
    {
        String result = "";
        if (StringUtils.isNotBlank(productCode))
        {
            result = productCode.replace(" ", "&nbsp;");
        }
        return result;

    }

    public static Date parseDate(String date)
    {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        try
        {
            return format.parse(date);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    public static Date parse(String date)
    {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try
        {
            return format.parse(date);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    public static String formatDate(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        return format.format(date);
    }

    public static String format(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return format.format(date);
    }

    public static String formatPrice(BigDecimal price)
    {
        NumberFormat numberInstance = new DecimalFormat(DECIMAL_FORAMT);
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setMinimumFractionDigits(2);
        return numberInstance.format(price);
    }

    public static String toCurrencyString(BigDecimal value)
    {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.SIMPLIFIED_CHINESE);
        String format = currency.format(value.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
        return format;
    }

    public static String toFriendlyCurrencyString(BigDecimal value)
    {
        if(value == null || value.compareTo(BigDecimal.ZERO) == 0)
        {
            return "0";
        }
        else if(value.compareTo(BigDecimal.valueOf(1000)) < 0)
        {
            return toIntegerString(value);
        }
        else
        {
            BigDecimal divide = value.divide(BigDecimal.valueOf(1000));
            String result = toIntegerString(divide);
            return result.equals("0") ? result : result + "k";
        }
    }

    public static String toPercentString(BigDecimal value)
    {
        if(value.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == 0)
        {
            return "N/A";
        }

        if(value.compareTo(BigDecimal.ZERO) == 0)
        {
            return "0";
        }

        NumberFormat currency = NumberFormat.getPercentInstance(Locale.SIMPLIFIED_CHINESE);
        currency.setMaximumFractionDigits(2);
        String format = currency.format(value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros());
        return format;
    }

    public static String toIntegerString(BigDecimal value)
    {
        NumberFormat currency = NumberFormat.getIntegerInstance(Locale.SIMPLIFIED_CHINESE);
        currency.setMaximumFractionDigits(0);
        String format = currency.format(value.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
        return format;
    }

    public static String toPlainString(long value)
    {
        NumberFormat currency = NumberFormat.getNumberInstance(Locale.SIMPLIFIED_CHINESE);
        String format = currency.format(value);
        return format;
    }

    /**
     *
     * @param denominator - 分母
     * @param numerator - 分子
     * @return
     */
    public static BigDecimal getPercent(BigDecimal denominator, BigDecimal numerator)
    {
        BigDecimal dived = BigDecimal.ZERO;

        if(denominator.compareTo(BigDecimal.ZERO) == 0)
        {
            if(numerator.compareTo(BigDecimal.ZERO) > 0)
            {
                dived = BigDecimal.ONE;
            }
        }

        else
        {
            dived = numerator.divide(denominator, 4, BigDecimal.ROUND_HALF_UP);
        }
        return dived;
    }

    public static BigDecimal getRatio(BigDecimal current, BigDecimal historical)
    {
        if(current == null)
        {
            current = BigDecimal.ZERO;
        }

        if(historical == null)
        {
            historical = BigDecimal.ZERO;
        }

        return getPercent(historical, current.subtract(historical));
    }

    public static BigDecimal getRatio(int currentValue, int historicalValue)
    {
        BigDecimal current = BigDecimal.valueOf(currentValue);
        BigDecimal historical = BigDecimal.valueOf(historicalValue);
        return getPercent(historical, current.subtract(historical));
    }

    public static String formatDiscount(BigDecimal price)
    {
        NumberFormat numberInstance = new DecimalFormat(DISCOUNT_FORAMT);
//        numberInstance.setMaximumFractionDigits(1);
//        numberInstance.setMinimumFractionDigits(1);
        return numberInstance.format(price);
    }

    /**
     * 计算商品折扣率
     *
     * @param originalPrice
     * @param afterDiscount
     * @return 如果任一参数为NULL，则返回空字符串，否则返回afterDiscount/originalPrice的值，且仅保留小数点后3位
     */
    public static String computeDiscount(BigDecimal originalPrice, BigDecimal afterDiscount)
    {
        String discount = "";
        if (null != originalPrice && null != afterDiscount)
        {
            if (originalPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                return "";
            }
            BigDecimal dis = afterDiscount.divide(originalPrice, 3, BigDecimal.ROUND_HALF_UP);
            discount = BizUtils.formatDiscount(dis);
        }
        return discount;
    }

    /**
     * 计算当前工作日的前{days}天的开始时间和结束时间
     *
     * @param days
     * @return
     */
    public static Date[] daysAgoForWeekday(int days)
    {
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        if (Calendar.MONDAY == begin.get(Calendar.DAY_OF_WEEK))  // 周一要干周六、周日、周一三天的活
        {
            begin.set(Calendar.DATE, begin.get(Calendar.DATE) - (days + 2));
            end.set(Calendar.DATE, end.get(Calendar.DATE) - days);
        }
        else // 星期二至周五只需要发6天前注册用户的短信
        {
            begin.set(Calendar.DATE, begin.get(Calendar.DATE) - days); // days天前的凌晨
            end.set(Calendar.DATE, end.get(Calendar.DATE) - days); // days天前的午夜
        }
        setToBeginAndEnd(begin, end);
        return new Date[]{begin.getTime(), end.getTime()};
    }

    /**
     * 计算当前日期的前{days}天的开始时间和结束时间
     *
     * @param days
     * @return
     */
    public static Date[] daysAgo(int days)
    {
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        begin.set(Calendar.DATE, begin.get(Calendar.DATE) - days); // days天前的凌晨
        end.set(Calendar.DATE, end.get(Calendar.DATE) - days); // days天前的午夜
        setToBeginAndEnd(begin, end);
        return new Date[]{begin.getTime(), end.getTime()};
    }

    /**
     * 将开始时间的时分秒清零，将结束时间的时分秒设置为23:59:59
     *
     * @param begin
     * @param end
     */
    public static void setToBeginAndEnd(Calendar begin, Calendar end)
    {
        if (null != begin)
        {
            begin.set(Calendar.HOUR_OF_DAY, 0);
            begin.set(Calendar.MINUTE, 0);
            begin.set(Calendar.SECOND, 0);
        }
        if (null != end)
        {
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
        }
    }

    /**
     * 获取一天的凌晨时间：yyyy-MM-dd 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getBeforDawn(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
        {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天的午夜时间：yyyy-MM-dd 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getMidnight(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
        {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static boolean emailValidate(String email)
    {
        if (StringUtils.isNotBlank(email))
        {
            return EMAIL_PATTERN.matcher(email).matches();
        }
        return false;
    }

    /**
     * 一个比较low的生成随机密码的方法
     *
     * @param pwdLength 密码长度
     * @return
     */
    public static String getRandomPwd(int pwdLength)
    {
        StringBuffer sb = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        StringBuffer result = new StringBuffer();
        Random r = new Random();
        int range = sb.length();
        for (int i = 0; i < pwdLength; i++)
        {
            result.append(sb.charAt(r.nextInt(range)));
        }
        return result.toString();
    }

    public static String generateTransactionNO(int userId)
    {
        return DateFormatUtils.format(new Date(), YYYYMMDDHHMMSS) + "-" + Integer.toString(userId) + "-" + getUUID();
    }

    public static String toJson(Object object)
    {
        return JacksonMapper.toJson(object);
    }

    public static  String buildLocation(String province, String city)
    {
        return province.equals(city) ? city + "市" : province + "省" + city + "市";
    }

    public static List<IdValueVO> map2VOList(Map<Integer, String> map, List<Integer> selectedIds)
    {
        List<IdValueVO> result = new ArrayList<IdValueVO>();
        if (MapUtils.isNotEmpty(map))
        {
            for (Map.Entry<Integer, String> entry : map.entrySet())
            {
                IdValueVO idValueVO = new IdValueVO(entry.getKey(), entry.getValue());
                result.add(idValueVO);
                if (CollectionUtils.isNotEmpty(selectedIds) && selectedIds.contains(entry.getKey()))
                {
                    idValueVO.setSelected(1);
                }
            }
        }
        return result;
    }

    /**
     * 根据输入的String返回BigDecimal，或者若String非数字串，返回null
     *
     * @param _str  待转化的字符串
     * @return BigDecimal对象
     */
    public static BigDecimal toBigDecimal(String _str) {
        BigDecimal bd = null;
        if (_str != null) {
            try {
                bd = new BigDecimal(_str);
            } catch (Exception e) {
                return null;
            }
        }
        return bd;
    }


    /**
     * 对密码做md5摘要
     *
     * @param pwd
     * @return
     */
    public static String md5Password(String pwd)
    {
        return Digests.md5Hex(BizConstants.USER_PASSWORD_MD5_SALT + pwd + BizConstants.USER_PASSWORD_MD5_SALT);
    }

    public static void main(String[] args)
    {
        System.out.print(BizUtils.md5Password("123456"));

    }

    /**
     * 获取6位的随机数(用于短信验证码)
     *
     * @return
     */
    public static int random6Int()
    {
        return (int) (Math.random() * 900000 + 100000);
    }
}
