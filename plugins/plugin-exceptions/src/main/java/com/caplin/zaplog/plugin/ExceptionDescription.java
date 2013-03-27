package com.caplin.zaplog.plugin;

import com.caplin.zaplog.LogLine;

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
		String formattedExtraInfo = (extraInfo.length() > 0) ? PluginExceptions.NEW_LINE + "\t\t Explanation: " + extraInfo : "";
		return logLine.getFileName() + " (" + logLine.getLineNumber() + ") - " + logLine.getText() + formattedExtraInfo;
	}

}