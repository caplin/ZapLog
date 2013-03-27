package com.caplin.zaplog;

import org.joda.time.DateTime;
import org.joda.time.Instant;

public class LogLineImpl implements LogLine
{
	private DateTime date;
	private String text;
	private String fileName;
	private long lineNumber;
	private String textColor;
	private Log log;

	public LogLineImpl(Log log, String fileName, long lineNumber, DateTime date, String text, String textColor)
	{
		super();
		this.log = log;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.text = text;
		this.date = date;
		this.textColor = textColor;
	}

	public long getTimestamp()
	{
		return (date == null) ? 0 : date.getMillis();
	}

	public DateTime getDateTime()
	{
		return date;
	}

	public String getText()
	{
		return this.text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public long getLineNumber()
	{
		return this.lineNumber;
	}

	public String getFileName()
	{
		return log.getFile().getName();
	}

	public String getOutput()
	{
		StringBuilder sb = new StringBuilder();
		if (ZapArg.PRETTY && ZapArg.OUTPUT_FILE == null)
		{
			sb.append(textColor);
		}
		if (!ZapArg.NO_FILENAMES)
		{
			sb.append(fileName + " - ");
		}
		if (ZapArg.LINENUMBERS)
		{
			sb.append(String.format("%0" + Long.toString(ZapLog.MAX_FILE_LINE_LENGTH) + "d", lineNumber) + " - ");
		}
		if (!ZapArg.NO_TIMESTAMP)
		{
			sb.append((date == null) ? "" : new Instant(date).toString() + ": ");
		}
		sb.append(text);
		return sb.toString();
	}

	public String toString()
	{
		return getOutput();
	}

}
