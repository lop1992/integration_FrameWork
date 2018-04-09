package net.integration.framework.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

public class DateUtil {
	
	public static void main(String[] args) {
		System.out.println(getFullTime(new Date()));
	}
	
	public static Logger logger = Logger.getLogger("com.col.frame.util");

	public static final String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";

	public static final String BILL_DATE_PATTERN = "yyyyMMdd";

	public static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String FILE_DATE_PATTERN = "yyyyMMddHHmmss";

	public static final String FULE_DATETIME_PATTERN = "yyyyMMddHHmmssS";

	protected static final String CREATE_TIME_BEGION = " 00:00:00";

	protected static final String CREATE_TIME_LAST = " 23:59:59";

	public static java.sql.Date createSQLDate(String month, String day, String year) {
		GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer
				.parseInt(day));

		return new java.sql.Date(gc.getTimeInMillis());
	}

	/**
	 * converts a Date to a String according to the pattern and local provided
	 */
	public static String dateToString(java.util.Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}
	
	public static Date getDate(String s, String format){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = simpleDateFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getFullTime(java.util.Date date) {
		return dateToString(date, FULE_DATETIME_PATTERN);
	}
	
	public static String dateTimeToString(java.util.Date date){
		return dateToString(date, FULL_DATE_PATTERN);
	}
	
	public static String timeToString(java.util.Date date){
		return dateToString(date, FILE_DATE_PATTERN);
	}
	
	public static String dateToString(java.util.Date date){
		return dateToString(date, SIMPLE_DATE_PATTERN);
	}

	/**
	 * @return a localized month name array based on the provided locale and pattern, i.e. "MMMMM" for full
	 *         representation (January, ...)or "MMM" for three letter representation (Jan, ..)
	 */
	public static String[] getMonthNames(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		DateFormatSymbols dfs = sdf.getDateFormatSymbols();

		return dfs.getMonths();
	}

	/**
	 * converts a String to java.util.Date according to the pattern provided
	 */
	public static java.util.Date timeStampToDate(String pattern, String timeStamp) throws java.text.ParseException {
		java.util.Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		date = formatter.parse(timeStamp);
		return date;
	}

	/**
	 * This method takes in String values representing numbers corresponding to the date, month and year as parameters
	 * and returns a Calendar object with time components were set to 'hour=0', 'minute=0', and 'second=0'. If parsing
	 * error occurred, a run time exception (NumberFormatException) will be thrown.
	 */
	public static Calendar getDate(String date, String month, String year) {

		return new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date), 0, 0, 0);
	}

	/**
	 * This method takes in String values representing numbers corresponding to the date, month and year and a locale
	 * object as parameters and returns a Calendar object set to the given date with the given locale. The time
	 * component is current time (i.e., when the Calendar object was created).
	 */
	public static Calendar getCalendar(String date, String month, String year) {
		return getCalendar(Integer.parseInt(date), Integer.parseInt(month), Integer.parseInt(year));
	}

	/**
	 * This method takes in String values representing numbers corresponding to the date, month and year, a string
	 * representing a formatting pattern and a locale object as parameters and returns a Calendar object set to the
	 * given date with the given locale. The time component is current time (i.e., when the Calendar object was
	 * created).
	 */
	public static String getDateAsString(String date, String month, String year, String pattern) {
		return dateToString(getCalendar(Integer.parseInt(date), Integer.parseInt(month), Integer.parseInt(year))
				.getTime(), pattern);
	}

	/**
	 * This method takes in integer values corresponding to the date, month and year and a locale object as parameters
	 * and returns a Calendar object set to the given date with the given locale. The time component is current time
	 * (i.e., when the Calendar object was created).
	 */
	public static Calendar getCalendar(int date, int month, int year) {
		GregorianCalendar c = new GregorianCalendar();
		c.set(year, month, date);
		return c;
	}

	/**
	 * This method takes in integer values corresponding to the year, month ,date,hour and minute and a locale object as
	 * parameters and returns a Calendar object set to the given date with the given locale. The time component is
	 * current time (i.e., when the Calendar object was created). i think the parameters order should be same as
	 * Calendar class methond and to be esay to understand
	 */
	public static Calendar getCalendar(int year, int month, int date, int hour, int minute) {
		GregorianCalendar c = new GregorianCalendar();
		c.set(year, month, date, hour, minute);
		return c;
	}

	public static Calendar getCalendar(String year, String month, String date, String hour, String minute) {
		return getCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date), Integer
				.parseInt(hour), Integer.parseInt(minute));
	}

	public static String getCurrentDate(String format) {
		return formatTitle(new Date(), format);
	}

	/**
	 * 
	 * @param fCal
	 * @param format(y-MM-dd)
	 * @return
	 */
	private static String formatTitle(Date fDate, String format) {
		if (fDate == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return (formatter.format(fDate.getTime()));
	}

	public static String formatSimpleTitle(Date fDate) {
		return formatTitle(fDate, SIMPLE_DATE_PATTERN);
	}

	public static String formatSimpleTitle(Date fDate, String format) {
		return formatTitle(fDate, format);
	}

	public static String formatFullTitle(Date fDate) {
		return formatTitle(fDate, FULL_DATE_PATTERN);
	}

	private static String formatDate(Date date, String format) {
		if (date == null)
			return "";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static String formatSimpleDate(Date date) {
		return formatDate(date, SIMPLE_DATE_PATTERN);
	}

	/**
	 * y-M-d
	 * 
	 * @param releaseDateStr
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getSimpleCalendar(String releaseDateStr) throws ParseException {
		return getCalendar(SIMPLE_DATE_PATTERN, releaseDateStr);
	}

	public static GregorianCalendar getSimpleGregorianCalendar(String releaseDateStr) throws ParseException {
		return getGregorianCalendar(SIMPLE_DATE_PATTERN, releaseDateStr);
	}

	/**
	 * y-M-d h:m:s
	 * 
	 * @param releaseDateStr
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getFullCalendar(String releaseDateStr) throws ParseException {
		return getCalendar(FULL_DATE_PATTERN, releaseDateStr);
	}

	/**
	 * 获得某一个月有多少天
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getConcreateDaysOfMonth(int year, int month, int day) {
		Calendar cal = new GregorianCalendar(year, month, day);
		cal.add(Calendar.DATE, -1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得某一天是星期几
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getConcreateDaysOfWeek(int year, int month, int day) {
		Calendar calendar = new GregorianCalendar(year, month - 1, day - 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}

	/**
	 * 
	 * @param pattern
	 * @param releaseDateStr
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getCalendar(String pattern, String releaseDateStr) throws ParseException {
		Calendar releaseDate = null;
		DateFormat df = new SimpleDateFormat(pattern);
		releaseDate = new GregorianCalendar();
		releaseDate.setTime(df.parse(releaseDateStr));
		return releaseDate;
	}

	public static GregorianCalendar getGregorianCalendar(String pattern, String releaseDateStr) throws ParseException {
		GregorianCalendar releaseDate = null;
		DateFormat df = new SimpleDateFormat(pattern);
		releaseDate = new GregorianCalendar();
		releaseDate.setTime(df.parse(releaseDateStr));
		return releaseDate;
	}

	/**
	 * 截至到当前日期，组成一个月有多少周，然后封装起来(周6为一周的第一天)
	 * 
	 * @param year
	 * @param month
	 * @param establishTime
	 * @return
	 */
	public static Map getWeeksMap(int year, int month, int establishTime) {
		int firstWeekDays = 0;
		int leftDays = 0;
		int leftWeekAmount = 0;
		int lastWeekDaysAmount = 0;
		// 天际当前天数
		int currentCount = 0;
		// 统计当前周
		int currentWeek = 0;

		Map<Integer, List> weekMonthMap = new LinkedHashMap<Integer, List>();
		// 获得某个月的第一天是星期几
		int dayOfFirstWeek = DateUtil.getConcreateDaysOfWeek(year, month, 1);
		// boolean ifSaturday = true;
		if (6 - dayOfFirstWeek != 0) {
			firstWeekDays = 6 - dayOfFirstWeek;
			if (firstWeekDays < 0) {
				firstWeekDays = 6;
			}
			// 把第一个周的日期封装到list中
			List<Integer> firstWeekList = new ArrayList<Integer>();
			for (int i = 1; i <= firstWeekDays; i++) {
				currentCount++;

				firstWeekList.add(currentCount);
			}
			// 把一周的LIST放到map中
			currentWeek++;
			weekMonthMap.put(currentWeek, firstWeekList);
			// ifSaturday = false;
		}

		// 获得到制单日期为止的剩余天数
		leftDays = establishTime - firstWeekDays;

		// 获得最后一周的天数

		lastWeekDaysAmount = leftDays % 7;

		for (int i = 0; i < leftWeekAmount; i++) {
			currentWeek++;
			if (i < leftWeekAmount - 1) {
				List<Integer> weekDayList = new ArrayList<Integer>();
				for (int m = 0; m < 7; m++) {
					currentCount++;

					weekDayList.add(currentCount);
				}
				weekMonthMap.put(currentWeek, weekDayList);
				// if (ifSaturday) {
				// i--;
				// ifSaturday = false;
				// }

			} else {
				// 如果为lastWeekDaysAmount == 0的话表明还有一周7天的时间没有计算
				List<Integer> lastWeekList = new ArrayList<Integer>();
				for (int j = 0; j < (lastWeekDaysAmount == 0 ? 7 : lastWeekDaysAmount); j++) {
					currentCount++;
					lastWeekList.add(currentCount);
				}
				weekMonthMap.put(currentWeek, lastWeekList);
			}

		}

		return weekMonthMap;
	}

	// 获得某月的下月
	public static Integer nextMonth(String yymm) {
		Integer yy = Integer.parseInt(yymm.substring(0, 4));
		Integer mm = Integer.parseInt(yymm.substring(4, 6));
		mm += 1;
		if (mm == 13) {
			mm = 1;
			yy += 1;
		}
		String MM = null;
		if (mm < 10) {
			MM = "0" + mm;
		} else {
			MM = String.valueOf(mm);
		}
		Integer YYMM = Integer.parseInt(yy + MM);
		return YYMM;
	}

	// 获得某月的上月
	public static Integer lastMonth(String yymm) {
		Integer yy = Integer.parseInt(yymm.substring(0, 4));
		Integer mm = Integer.parseInt(yymm.substring(4, 6));
		mm -= 1;
		if (mm == 0) {
			mm = 12;
			yy -= 1;
		}
		String MM = null;
		if (mm < 10) {
			MM = "0" + mm;
		} else {
			MM = String.valueOf(mm);
		}
		Integer YYMM = Integer.parseInt(yy + MM);
		return YYMM;
	}

	// 获得某月的后三个月
	public static String[] inThreeMonths(String yymm) {
		String[] months = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "01", "02",
				"03" };

		String[] inThreeMonths = new String[3];
		Integer yy = Integer.parseInt(yymm.substring(0, 4));
		Integer mm = Integer.parseInt(yymm.substring(4, 6));
		int befm = ++mm;
		String befym = "";
		int midm = ++mm;
		String midym = "";
		int aftm = ++mm;
		String aftym = "";

		if (befm == 13) {
			yy += 1;
			befym = yy + months[befm];
			midym = yy + months[midm];
			aftym = yy + months[aftm];
		} else if (befm == 12) {
			befym = yy + months[befm];
			yy += 1;
			midym = yy + months[midm];
			aftym = yy + months[aftm];
		} else if (befm == 11) {
			befym = yy + months[befm];
			midym = yy + months[midm];
			yy += 1;
			aftym = yy + months[aftm];
		} else {
			befym = yy + months[befm];
			midym = yy + months[midm];
			aftym = yy + months[aftm];
		}
		inThreeMonths[0] = befym;
		inThreeMonths[1] = midym;
		inThreeMonths[2] = aftym;
		return inThreeMonths;
	}

	public static int getBeforeCurrentDate() {
		Calendar c = Calendar.getInstance();
		int result = 0;
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		if (month % 12 == 0) {
			result = (year - 1) * 100 + 11;
		} else {
			result = (year) * 100 + month - 1;
		}
		return result + 1;
	}

	public static int getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}
	
	/**
	 * 两个日期相减天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDiffDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
		int flag = 0;
		if (d1.after(d2)) {
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
			flag = 1;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		if (flag == 1) {
			return 0 - days;
		}
		return days;
	}
	/**
	 * 计算2个日期之间的相差月数
	 * 
	 * @param pFormer
	 * @param pLatter
	 * @return
	 */
	public static int getMonthsBetween(Calendar pFormer, Calendar pLatter) {
		Calendar vFormer = pFormer, vLatter = pLatter;
		boolean vPositive = true;
		if (pFormer.before(pLatter)) {
			vFormer = pFormer;
			vLatter = pLatter;
		} else {
			vFormer = pLatter;
			vLatter = pFormer;
			vPositive = false;
		}

		int vCounter = 0;
		while (vFormer.get(Calendar.YEAR) != vLatter.get(Calendar.YEAR)
				|| vFormer.get(Calendar.MONTH) != vLatter.get(Calendar.MONTH)) {
			vFormer.add(Calendar.MONTH, 1);
			vCounter++;
		}
		if (vPositive)
			return vCounter;
		else
			return -vCounter;
	}

	/**
	 * 计算2个日期之间的相隔天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int getWorkingDay(java.util.Calendar d1, java.util.Calendar d2) {
		int result = -1;
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}

		int charge_start_date = 0;// 开始日期的日期偏移量
		int charge_end_date = 0;// 结束日期的日期偏移量
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			charge_end_date = etmp - 1;
		}
		// }
		result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7) * 5 + charge_start_date
				- charge_end_date;

		return result;
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 * @return
	 */
	public Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	public static int getDays(GregorianCalendar g1, GregorianCalendar g2) {
		int elapsed = 0;
		GregorianCalendar gc1, gc2;
		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}
		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);
		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);
		while (gc1.before(gc2)) {
			gc1.add(Calendar.DATE, 1);
			elapsed++;
		}
		return elapsed;
	}

	/**
	 * 取得指定日期所在周的第一天 一周以星期天为开始 星期六结束
	 */
	public static Calendar getWeekBegin(Calendar cal) {
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, (1 - day));
		return cal;
	}

	/**
	 * 取得指定日期所在周的最后一天 一周以星期天为开始 星期六结束
	 */
	public static Calendar getWeekEnd(Calendar cal) {
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day < 7) {
			day = 7 - day;
			cal.add(Calendar.DATE, day);
		}
		return cal;
	}

	public static String[] getCurrentDateByCondition(String currentCondition) {
		Calendar todayCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		String[] dateTimes = { null, null };
		// FULL_DATE_PATTERN
		DateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
		String endTime = "";
		if ("today".equals(currentCondition)) {
			// 本天
			currentCondition = formatter.format(todayCalendar.getTime());
			endTime = formatter.format(todayCalendar.getTime());
		} else if ("month".equals(currentCondition)) {
			// 本月
			todayCalendar.set(Calendar.DAY_OF_MONTH, 1);
			currentCondition = formatter.format(todayCalendar.getTime());
			// 下月减一秒
			endCalendar.set(Calendar.MONTH, endCalendar.get(Calendar.MONTH) + 1);
			endCalendar.set(Calendar.DATE, 1);
			endCalendar.set(Calendar.DATE, endCalendar.get(Calendar.DATE) - 1);
			endTime = formatter.format(endCalendar.getTime());
		} else if ("week".equals(currentCondition)) {
			// 本周
			currentCondition = formatter.format(DateUtil.getWeekBegin(todayCalendar).getTime());
			endTime = formatter.format(DateUtil.getWeekEnd(endCalendar).getTime());
		} else if ("lastMonth".equals(currentCondition)) {
			// 上月
			todayCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH) - 1);
			todayCalendar.set(Calendar.DATE, 1);
			currentCondition = formatter.format(todayCalendar.getTime());
			// 本月
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			endCalendar.set(Calendar.DATE, 1);
			endCalendar.set(Calendar.DATE, endCalendar.get(Calendar.DATE) - 1);
			endTime = formatter.format(endCalendar.getTime());
		} else if ("year".equals(currentCondition)) {
			// 本年
			todayCalendar.set(Calendar.DAY_OF_YEAR, 1);
			currentCondition = formatter.format(todayCalendar.getTime());
			// 下年
			endCalendar.set(Calendar.YEAR, endCalendar.get(Calendar.YEAR) + 1);
			endCalendar.set(Calendar.DAY_OF_YEAR, 1);
			endCalendar.set(Calendar.DATE, endCalendar.get(Calendar.DATE) - 1);
			endTime = formatter.format(endCalendar.getTime());

		} else if ("lastYear".equals(currentCondition)) {
			// 上年
			todayCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR) - 1);
			todayCalendar.set(Calendar.DAY_OF_YEAR, 1);
			currentCondition = formatter.format(todayCalendar.getTime());
			// 本年减1
			endCalendar.set(Calendar.DAY_OF_YEAR, 1);
			endCalendar.set(Calendar.DAY_OF_YEAR, 1);
			endCalendar.set(Calendar.DATE, endCalendar.get(Calendar.DATE) - 1);
			endTime = formatter.format(endCalendar.getTime());
		} else {
			todayCalendar.set(1970, 11, 11);
			currentCondition = formatter.format(todayCalendar.getTime());
			endTime = formatter.format(endCalendar.getTime());
		}
		dateTimes[0] = currentCondition + CREATE_TIME_BEGION;
		dateTimes[1] = endTime + CREATE_TIME_LAST;
		return dateTimes;
	}
	
	 /** 
     * 两个时间之间相差距离多少天 
     * @param one 时间参数 1： 
     * @param two 时间参数 2： 
     * @return 相差天数 
     */  
    public static long getDistanceDays(String str1, String str2) throws Exception{  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        Date one;  
        Date two;  
        long days=0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            days = diff / (1000 * 60 * 60 * 24);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return days;  
    }  
      
    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return long[] 返回值为：{天, 时, 分, 秒} 
     */  
    public static long[] getDistanceTimes(String str1, String str2) {  
        DateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        long[] times = {day, hour, min, sec};  
        return times;  
    }  
    /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return String 返回值为：xx天xx小时xx分xx秒 
     */  
    public static String getDistanceTime(String str1, String str2) {  
        DateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";  
    }  
    
    /**  
     * 计算时间差  
     *   
     * @param begin    
     * @param end    
     * @return  
     */  
    public static String countTime(String begin,String end){   
        int hour = 0;   
        int minute = 0;   
        long total_minute = 0;   
        StringBuffer sb = new StringBuffer();   
      
        SimpleDateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);   
        try {   
            Date begin_date = df.parse(begin);   
            Date end_date = df.parse(end);   
            total_minute = (end_date.getTime() - begin_date.getTime())/(1000*60);   
            hour = (int) total_minute/60;   
            minute = (int) total_minute%60;   
      
        } catch (ParseException e) {   
            System.out.println("传入的时间格式不符合规定");   
        }   
      
        sb.append("工作时间为：").append(hour).append("小时").append(minute).append("分钟");   
        return sb.toString();   
    }  
    
    /**  
     * 计算时间差  精确到分
     *   
     * @param begin    
     * @param end    
     * @return  
     */  
    public static int countTimeMinute(String begin,String end){   
    	int minute = 0;   
    	long total_minute = 0;   
    	SimpleDateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);   
    	try {   
    		Date begin_date = df.parse(begin);   
    		Date end_date = df.parse(end);   
    		total_minute = (end_date.getTime() - begin_date.getTime())/(1000*60);   
    		minute = (int) total_minute%60;   
    	} catch (ParseException e) {   
    		System.out.println("传入的时间格式不符合规定");   
    	}   
    	return minute;   
    }  
    
    /**  
     * 计算时间差  精确到小时
     *   
     * @param begin    
     * @param end    
     * @return  
     */  
    public static int countTimeHour(String begin,String end){   
    	int hour = 0;   
    	long total_minute = 0;   
    	SimpleDateFormat df = new SimpleDateFormat(FULL_DATE_PATTERN);   
    	try {   
    		Date begin_date = df.parse(begin);   
    		Date end_date = df.parse(end);   
    		total_minute = (end_date.getTime() - begin_date.getTime())/(1000*60);   
    		hour = (int) total_minute/60;   
    	} catch (ParseException e) {   
    		System.out.println("传入的时间格式不符合规定");   
    	}   
    	return hour;   
    }  
    /**
     * 
     * <b></b>
     * 描述 ： ，Object。
     * @param time
     * @param format
     * @param locale 
     * @return
     */
	public static Date parse(String time, String format)throws Exception {
		return parse(time,format,Locale.CHINA);
	}  
	 /**
     * 
     * <b></b>
     * 描述 ： ，Object。
     * @param time
     * @param format
     * @param locale 
     * @return
     */
	public static Date parse(String time, String format,Locale locale)throws Exception {
		if(time == null )
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,locale);
		
		Date d = null;
		d =sdf.parse(time);
		return d;
	}

	public static String format(Date d, String format) {
		if(d == null)
			return "";
		SimpleDateFormat myFormatter = new SimpleDateFormat(format);
		return myFormatter.format(d);
	}  

	public static String getyyyyMMddhhMMss2() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(FILE_DATE_PATTERN);
		return localSimpleDateFormat.format(new Date());
	}
	public static String getyyyyMMddHHmmss() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(FULL_DATE_PATTERN);
		return localSimpleDateFormat.format(new Date());
	}
	public static String getyyyyMMdd() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(BILL_DATE_PATTERN);
		return localSimpleDateFormat.format(new Date());
	}
}
