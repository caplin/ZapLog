package com.caplin.zaplog.report.plugins.exceptions;

import com.caplin.zaplog.LogLine;
import com.caplin.zaplog.ZapLog;

public class ExceptionDescription
{

	private LogLine logLine;
	private String extraInfo;

	public ExceptionDescription(LogLine logLine, String extraInfo)
	{
		this.logLine = logLine;
		this.extraInfo = extraInfo;
	}

	public ExceptionDescription(LogLine logLine)
	{
		this(logLine, "");
	}

	public String getOutput()
	{
		String formattedExtraInfo = (extraInfo.length() > 0) ? ZapLog.NEW_LINE + "\t\t Explanation: " + extraInfo : "";
		return logLine.getFileName() + " (" + logLine.getLineNumber() + ") - " + logLine.getText() + formattedExtraInfo;
	}

}