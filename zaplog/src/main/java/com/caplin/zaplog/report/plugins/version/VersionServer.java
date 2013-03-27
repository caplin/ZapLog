package com.caplin.zaplog.report.plugins.version;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.LogLine;

public class VersionServer implements Version
{

	private static final Pattern patternProduct = Pattern.compile(".*Product.*: (.*)");
	private static final Pattern patternVersion = Pattern.compile(".*Version.*: (\\d+\\.\\d+\\.\\d+.*)");
	private static final Pattern patternBuild = Pattern.compile(".*Build Number.*: (\\d+.*)");
	private static final Pattern patternBuildDate = Pattern.compile(".*Build Date.*: (\\d+.*)");

	@Override
	public List<ProductVersion> getProductInfo(Log log)
	{
		List<ProductVersion> result = new ArrayList<ProductVersion>();

		for (LogLine logLine : log.getLogLines())
		{
			String text = logLine.getText();
			Matcher matcher = patternProduct.matcher(text);
			if (matcher.matches())
			{
				String product = matcher.group(1);
				String version = getBuildNumber(log.getLogLines(), logLine, 5);
				String buildDate = getBuildDate(log.getLogLines(), logLine, 5);
				result.add(new ProductVersion(product, version, buildDate));
			}
		}

		return result;
	}

	private String getBuildNumber(List<LogLine> logLines, LogLine startingLog, int total)
	{
		StringBuilder sb = new StringBuilder();
		int startingIndex = logLines.indexOf(startingLog);
		if (startingIndex >= 0)
		{
			for (int i = startingIndex; i < (total + startingIndex); i++)
			{
				if (i < logLines.size())
				{
					String logLine = logLines.get(i).getText();
					if (sb.toString().equals(""))
					{
						Matcher matcher = patternVersion.matcher(logLine);
						if (matcher.matches())
						{
							String version = matcher.group(1);
							sb.append(version);
						}
					}
					else
					{
						Matcher matcher = patternBuild.matcher(logLine);
						if (matcher.matches())
						{
							String buildNumber = matcher.group(1);
							sb.append("-");
							sb.append(buildNumber);
						}
					}
				}
			}
		}
		return sb.toString();
	}

	private String getBuildDate(List<LogLine> logLines, LogLine startingLog, int total)
	{
		StringBuilder sb = new StringBuilder();
		int startingIndex = logLines.indexOf(startingLog);
		if (startingIndex >= 0)
		{
			for (int i = startingIndex; i < (total + startingIndex); i++)
			{
				if (i < logLines.size())
				{
					String logLine = logLines.get(i).getText();
					Matcher matcher2 = patternBuildDate.matcher(logLine);
					if (matcher2.matches())
					{
						String buildDate = matcher2.group(1);
						sb.append(buildDate);
					}
				}
			}
		}
		return sb.toString();
	}

}
