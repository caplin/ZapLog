package com.caplin.zaplog.time;

import org.joda.time.DateTime;

public class DateText
{

	private DateTime date;
	private String text;

	public DateText(DateTime date, String text)
	{
		this.date = date;
		this.text = text;
	}

	public DateTime getDate()
	{
		return date;
	}

	public String getText()
	{
		return text;
	}

}
