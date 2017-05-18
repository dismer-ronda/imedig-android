package es.pryades.imedig.android.utils;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarUtils
{
	public static int getCalendarTimeAsInt( Calendar calendar )
	{
		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );

		return day + month * 100 + year * 10000;
	}

	public static int getCalendarDateAsInt( Calendar calendar, int offset )
	{
		calendar.add( Calendar.DATE, offset );

		return getCalendarTimeAsInt( calendar );
	}

	public static long getCalendarTimeAsLong( Calendar calendar )
	{
		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );
		int hour = calendar.get( Calendar.HOUR_OF_DAY );
		int min = calendar.get( Calendar.MINUTE );
		int sec = calendar.get( Calendar.SECOND );

		return (long)sec + (long)min * 100 + (long)hour * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getCalendarDateAsLong( Calendar calendar, int offset )
	{
		calendar.add( Calendar.DATE, offset );

		return getCalendarTimeAsLong( calendar );
	}

	public static long getCalendarHourAsLong( Calendar calendar, int offset )
	{
		calendar.add( Calendar.HOUR_OF_DAY, offset );

		return getCalendarTimeAsLong( calendar );
	}

	public static long getCalendarMinuteAsLong( Calendar calendar, int offset )
	{
		calendar.add( Calendar.MINUTE, offset );

		return getCalendarTimeAsLong( calendar );
	}

	public static int getDateAsInt( Date date )
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( date );

		return getCalendarDateAsInt( calendar, 0 );
	}

	public static long getTodayAsLong( String horario )
	{
		return getCalendarDateAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ), 0 );
	}

	public static long getDayAsLong( TimeZone timezone, int offset )
	{
		return getCalendarDateAsLong( GregorianCalendar.getInstance( timezone ), offset );
	}

	public static long getTodayFirstSecondAsLong( String horario )
	{
		Calendar calendar = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );

		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );

		return (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getTodayLastSecondAsLong( String horario )
	{
		Calendar calendar = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );

		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );
		int hour = 23;
		int min = 59;
		int sec = 59;

		return (long)sec + (long)min * 100 + (long)hour * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getTomorrowFirstSecondAsLong( String horario )
	{
		Calendar calendar = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );

		calendar.add( Calendar.DATE, 1 );

		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );

		return (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static Date getTodayFirstSecondAsDate( String horario )
	{
		return getDateHourFromLong( getTodayFirstSecondAsLong( horario ) ); 
	}

	public static Date getTodayLastSecondAsDate( String horario )
	{
		return getDateHourFromLong( getTodayLastSecondAsLong( horario ) ); 
	}

	public static Date getTodayFirstSecondAsDate()
	{
		Calendar calendar = GregorianCalendar.getInstance();

		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), 0, 0, 0 );

		return calendar.getTime();
	}

	public static Date getTodayLastSecondAsDate()
	{
		Calendar calendar = GregorianCalendar.getInstance();

		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), 23, 59, 59 );

		return calendar.getTime();
	}

	public static Date getDayFirstSecondAsDate( Date date )
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( date );

		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), 0, 0, 0 );

		return calendar.getTime();
	}

	public static Date getDayLastSecondAsDate( Date date )
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( date );

		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), 23, 59, 59 );

		return calendar.getTime();
	}

	public static Date getFirstDayWeekAsDate( String horario )
	{
		return getDateHourFromLong( getCalendarFirstDayWeekAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ) ) );
	}

	public static Date getLastDayWeekAsDate( String horario )
	{
		return getDateHourFromLong( getCalendarLastDayWeekAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ) ) );
	}

	public static Date getFirstDayMonthAsDate( String horario )
	{
		return getDateHourFromLong( getCalendarFirstDayMonthAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ) ) );
	}

	public static Date getFirstDayMonthAsDate( Date date )
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( date );

		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), 1, 0, 0, 0 );
		return calendar.getTime();
	}

	public static Date getLastDayMonthAsDate( String horario )
	{
		return getDateHourFromLong( getCalendarLastDayMonthAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ) ) );
	}

	public static Date getFirstDayYearAsDate( String horario )
	{
		return getDateHourFromLong( getCalendarFirstDayYearAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ) ) );
	}

	public static Date getLastDayYearAsDate( String horario )
	{
		return getDateHourFromLong( getCalendarLastDayYearAsLong( GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) ) ) );
	}
		
	public static long getElapsedTimeUntilNow( Date date, String horario )
	{
		Calendar calendar = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );
		calendar.setTime( date );

		return getDateHourFromLong( getTodayAsLong( horario ) ).getTime() - calendar.getTime().getTime();
	}

	public static Date getCurrentDate( String horario )
	{
		return getDateHourFromLong( getTodayAsLong( horario ) );
	}

	public static long getHourFirstSecondAsLong( Calendar calendar, int offset )
	{
		calendar.add( Calendar.HOUR_OF_DAY, offset );

		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );
		int hour = calendar.get( Calendar.HOUR_OF_DAY );

		return (long)hour * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getHourLastSecondAsLong( Calendar calendar )
	{
		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );
		int hour = calendar.get( Calendar.HOUR_OF_DAY );

		return (long)59 + (long)59 * 100 + (long)hour * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getDayFirstSecondAsLong( Calendar calendar )
	{
		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );

		return (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getCalendarFirstDayWeekAsLong( Calendar calendar )
	{
		Calendar cal = GregorianCalendar.getInstance();
		cal.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ) );

		while ( /*month == calendar.get( Calendar.MONTH ) && */ cal.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY )
		{
			cal.add( Calendar.DAY_OF_MONTH, -1 );
		}

		int day = cal.get( Calendar.DATE );
		int month = cal.get( Calendar.MONTH ) + 1;
		int year = cal.get( Calendar.YEAR );

		return (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getCalendarLastDayWeekAsLong( Calendar calendar )
	{
		Calendar cal = GregorianCalendar.getInstance();
		cal.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ) );

		while ( /*month == calendar.get( Calendar.MONTH ) && */cal.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY )
		{
			cal.add( Calendar.DAY_OF_MONTH, 1 );
		}

		int day = cal.get( Calendar.DATE );
		int month = cal.get( Calendar.MONTH ) + 1;
		int year = cal.get( Calendar.YEAR );

		return (long)59 + (long)59 * 100 + (long)23 * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static int getLastDayOfMonth( Date date )
	{
		int result = -1;

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( date );
		result = calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
		return result;
	}

	public static boolean isFirstDayOfMonth( Date date )
	{
		int monthDay = -1;

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( date );
		monthDay = calendar.get( Calendar.DAY_OF_MONTH );

		return monthDay == 1;
	}

	public static boolean isLastDayOfMonth( Date date )
	{
		int monthDay = -1;
		int lasMonthDay = -2;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime( date );
		monthDay = calendar.get( Calendar.DAY_OF_MONTH );
		lasMonthDay = getLastDayOfMonth( date );

		return monthDay == lasMonthDay;
	}

	public static long getCalendarFirstDayMonthAsLong( Calendar calendar )
	{
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );

		return (long)1 * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getCalendarLastDayMonthAsLong( Calendar calendar )
	{
		Calendar cal = GregorianCalendar.getInstance();
		cal.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );

		int day = cal.get( Calendar.DATE );
		int month = cal.get( Calendar.MONTH ) + 1;
		int year = cal.get( Calendar.YEAR );

		return (long)59 + (long)59 * 100 + (long)23 * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static long getCalendarFirstDayYearAsLong( Calendar calendar )
	{
		int year = calendar.get( Calendar.YEAR );

		return (long)1 * 1000000 + (long)1 * 100000000L + (long)year * 10000000000L;
	}

	public static long getCalendarLastDayYearAsLong( Calendar calendar )
	{
		int year = calendar.get( Calendar.YEAR );

		return (long)59 + (long)59 * 100 + (long)23 * 10000 + (long)31 * 1000000 + (long)12 * 100000000L + (long)year * 10000000000L;
	}

	public static long getDayLastSecondAsLong( Calendar calendar )
	{
		int day = calendar.get( Calendar.DATE );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int year = calendar.get( Calendar.YEAR );

		return (long)59 + (long)59 * 100 + (long)23 * 10000 + (long)day * 1000000 + (long)month * 100000000L + (long)year * 10000000000L;
	}

	public static Calendar getCalendarBefore( Calendar calendar, int field, int offset )
	{
		calendar.add( field, offset * (-1) );

		return calendar;
	}

	public static Calendar getCalendarAfter( Calendar calendar, int field, int offset )
	{
		calendar.add( field, offset );

		return calendar;
	}

	public static Date getDate( Date date, int field, int offset )
	{
		Calendar calendar = GregorianCalendar.getInstance();

		calendar.setTime( date );

		calendar.add( field, offset );

		return calendar.getTime();
	}

	public static long getHowMuchToWaitForFirstMinuteOfNextHourAsLong()
	{
		Calendar calendar = GregorianCalendar.getInstance();

		calendar.add( Calendar.HOUR_OF_DAY, 1 );

		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );

		return calendar.getTime().getTime() - GregorianCalendar.getInstance().getTime().getTime();
	}

	public static Date getDateFromInt( int date )
	{
		try
		{
			return new SimpleDateFormat( "yyyyMMdd" ).parse( Integer.toString( date ) );
		}
		catch ( Throwable e )
		{
			return new Date();
		}
	}

	public static Date getDateHourFromLong( long ldate )
	{
		Date date = null;
		try
		{
			date = new SimpleDateFormat( "yyyyMMddHHmmss" ).parse( Long.toString( ldate ) );
		}
		catch ( ParseException e )
		{
			date = new Date();
		}
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis( date.getTime() );

		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );

		return cal.getTime();
	}

	public static Date getDateHourFromLong( long ldate, String horario )
	{
		Date date = null;
		try
		{
			date = new SimpleDateFormat( "yyyyMMddHHmmss" ).parse( Long.toString( ldate ) );
		}
		catch ( ParseException e )
		{
			date = new Date();
		}
		Calendar cal = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );
		cal.setTimeInMillis( date.getTime() );

		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );

		return cal.getTime();
	}

	public static String getDateFromLongAsString( long date, String format )
	{
		try
		{
			Date input = new SimpleDateFormat( "yyyyMMddHHmmss" ).parse( Long.toString( date ) );

			return new SimpleDateFormat( format ).format( input );
		}
		catch ( Throwable e )
		{
			return "";
		}
	}

	public static int getTodayAsInt()
	{
		return getCalendarDateAsInt( GregorianCalendar.getInstance(), 0 );
	}

	public static Calendar getCalendarNow( String horario )
	{
		return GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );
	}

	public static Calendar getCalendarLastHour( String horario )
	{
		Calendar calendar = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );
		
		calendar.add( Calendar.HOUR_OF_DAY, -1 );
		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), calendar.get( Calendar.HOUR_OF_DAY ), 0, 0 );
		
		return calendar;
	}

	public static Calendar getCalendarCurrentHour( String horario )
	{
		Calendar calendar = GregorianCalendar.getInstance( TimeZone.getTimeZone( horario ) );
		
		calendar.set( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DATE ), calendar.get( Calendar.HOUR_OF_DAY ), 0, 0 );
		
		return calendar;
	}

	public static boolean isSameHour( Date prev, Date next )
	{
		if ( prev == null || next == null )
			return false;

		Calendar calPrev = GregorianCalendar.getInstance();
		calPrev.setTimeInMillis( prev.getTime() );

		Calendar calNext = GregorianCalendar.getInstance();
		calNext.setTimeInMillis( next.getTime() );

		return calPrev.get( Calendar.DAY_OF_MONTH ) == calNext.get( Calendar.DAY_OF_MONTH ) && 
			calPrev.get( Calendar.MONTH ) == calNext.get( Calendar.MONTH ) && 
			calPrev.get( Calendar.YEAR ) == calNext.get( Calendar.YEAR ) && 
			calPrev.get( Calendar.HOUR_OF_DAY ) == calNext.get( Calendar.HOUR_OF_DAY );
	}

	public static boolean isSameDay( Date prev, Date next )
	{
		if ( prev == null || next == null )
			return false;

		Calendar calPrev = GregorianCalendar.getInstance();
		calPrev.setTimeInMillis( prev.getTime() );

		Calendar calNext = GregorianCalendar.getInstance();
		calNext.setTimeInMillis( next.getTime() );

		return calPrev.get( Calendar.DAY_OF_MONTH ) == calNext.get( Calendar.DAY_OF_MONTH ) && calPrev.get( Calendar.MONTH ) == calNext.get( Calendar.MONTH ) && calPrev.get( Calendar.YEAR ) == calNext.get( Calendar.YEAR );
	}

	public static boolean isSameMonth( Date prev, Date next )
	{
		if ( prev == null || next == null )
			return false;

		Calendar calPrev = GregorianCalendar.getInstance();
		calPrev.setTimeInMillis( prev.getTime() );

		Calendar calNext = GregorianCalendar.getInstance();
		calNext.setTimeInMillis( next.getTime() );

		return calPrev.get( Calendar.MONTH ) == calNext.get( Calendar.MONTH ) && calPrev.get( Calendar.YEAR ) == calNext.get( Calendar.YEAR );
	}

	public static boolean isSameYear( Date prev, Date next )
	{
		if ( prev == null || next == null )
			return false;

		Calendar calPrev = GregorianCalendar.getInstance();
		calPrev.setTimeInMillis( prev.getTime() );

		Calendar calNext = GregorianCalendar.getInstance();
		calNext.setTimeInMillis( next.getTime() );

		return calPrev.get( Calendar.YEAR ) == calNext.get( Calendar.YEAR );
	}

	public static boolean isSameWeek( Date prev, Date next )
	{
		if ( prev == null || next == null )
			return false;

		Calendar calPrev = GregorianCalendar.getInstance();
		calPrev.setFirstDayOfWeek( Calendar.MONDAY );
		calPrev.setTimeInMillis( prev.getTime() );

		Calendar calNext = GregorianCalendar.getInstance();
		calNext.setFirstDayOfWeek( Calendar.MONDAY );
		calNext.setTimeInMillis( next.getTime() );

		return calPrev.get( Calendar.WEEK_OF_YEAR ) == calNext.get( Calendar.WEEK_OF_YEAR ) 
			/*&& calPrev.get( Calendar.YEAR ) == calNext.get( Calendar.YEAR )*/;
	}

	public static boolean isSameHour( Calendar from, Calendar to )
	{
		return from.get( Calendar.DATE ) == to.get( Calendar.DATE ) && from.get( Calendar.MONTH ) == to.get( Calendar.MONTH ) && from.get( Calendar.YEAR ) == to.get( Calendar.YEAR ) && from.get( Calendar.HOUR_OF_DAY ) == to.get( Calendar.HOUR_OF_DAY );
	}
}
