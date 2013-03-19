package com.caplin.zaplog.report.plugins.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.LogLine;

public class ExceptionJavaStackTrace implements IException
{

	private static final Pattern patternJavaLangException = Pattern.compile(".*java\\.lang\\..*Exception.*");
	private static final Pattern patternException = Pattern.compile(".*Exception.*");
	private static final Pattern patternAt = Pattern.compile("(\\s+)at .*");

	private static Map<String, String> humanExceptions = new HashMap<String, String>();

	static
	{
		humanExceptions.put(".*TradingException: Resource unavailable", "RET might be down/unavailable");
	}

	@Override
	public List<ExceptionDescription> getExceptionDescriptions(Log log)
	{
		List<ExceptionDescription> result = new ArrayList<ExceptionDescription>();

		for (LogLine logLine : log.getLogLines())
		{
			String text = logLine.getText();
			Matcher matcher = patternException.matcher(text);
			Matcher javaMatcher = patternJavaLangException.matcher(text);
			if (matcher.matches())
			{
				if (hasNextLineGotAt(log.getLogLines(), logLine))
				{
					String extraInfo = getExtraInfo(text);
					result.add(new ExceptionDescription(logLine, extraInfo));
				}
				else if (javaMatcher.matches())
				{
					result.add(new ExceptionDescription(logLine));
				}
			}

		}

		return result;
	}

	private String getExtraInfo(String logText)
	{
		for (Map.Entry<String, String> entry : humanExceptions.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();

			if (logText.matches(key))
			{
				return value;
			}
		}
		return "";
	}

	private boolean hasNextLineGotAt(List<LogLine> logLines, LogLine startingLog)
	{
		int startingIndex = logLines.indexOf(startingLog) + 1;
		if (startingIndex >= 0 && startingIndex < logLines.size())
		{
			String logLine = logLines.get(startingIndex).getText();
			Matcher matcher2 = patternAt.matcher(logLine);
			if (matcher2.matches())
			{
				return true;
			}
		}
		return false;
	}

}
