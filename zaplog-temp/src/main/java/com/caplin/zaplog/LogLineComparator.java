package com.caplin.zaplog;

import java.util.Comparator;

import org.joda.time.DateTime;

public class LogLineComparator implements Comparator<LogLine>
{

	public LogLineComparator()
	{
	}

	public int compare(LogLine l1, LogLine l2)
	{
		return cp(l1.getDateTime(), l2.getDateTime());
	}

	static <T extends Comparable<LogLine>> int cp(DateTime a, DateTime b)
	{
		return a == null ? (b == null ? 0 : Integer.MIN_VALUE) : (b == null ? Integer.MAX_VALUE : a.compareTo(b));
	}

}