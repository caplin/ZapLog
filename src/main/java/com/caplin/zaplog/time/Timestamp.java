package com.caplin.zaplog.time;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class Timestamp
{

	private int parts;
	private int startingIndex;

	private static int yearsSince1970;
	private SimpleDateFormat formatter;

	static
	{
		yearsSince1970 = new DateTime().getYear() - 1970;
	}

	public Timestamp(String timestamp, int parts, int startingIndex)
	{
		this.formatter = new SimpleDateFormat(timestamp);
		this.parts = parts;
		this.startingIndex = startingIndex;
	}

	public DateText getTimestampDate(String text)
	{
		String[] split = text.split(" ");

		this.startingIndex = (startingIndex < split.length) ? startingIndex : 0;
		for (int i = startingIndex; i < split.length; i++)
		{
			try
			{
				String sub = getSubSplit(split, i, parts);
				String cleanedSub = (sub.contains("[") || sub.contains("]")) ? sub.replaceAll("[\\[\\]]", "") : sub;
				DateTime dateTime = new DateTime(formatter.parse(cleanedSub));
				if (dateTime.getYear() == 1970)
				{
					dateTime = dateTime.plusYears(yearsSince1970);
				}
				String newText = text.replace(sub, "");
				newText = StringUtils.stripStart(newText, " : ");
				newText = StringUtils.stripStart(newText, " - ");
				return new DateText(dateTime, newText);
			}
			catch (Exception e)
			{
				// do nothing
			}
		}
		return new DateText(null, text);
	}

	private String getSubSplit(String[] split, int index, int parts)
	{
		String sub = split[index];

		if (parts > 1 && (index + parts + 1) < split.length)
		{
			for (int i = 0; i < parts; i++)
			{
				sub += " " + split[index + i + 1];
			}
		}
		return sub;
	}

}
