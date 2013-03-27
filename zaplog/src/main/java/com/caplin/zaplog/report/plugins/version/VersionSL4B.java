package com.caplin.zaplog.report.plugins.version;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.LogLine;

public class VersionSL4B implements Version
{

	private static final Pattern pattern = Pattern.compile(".*SL4B (\\d+\\.\\d+\\.\\d+-\\d+).*");
	private static final Pattern patternDate = Pattern.compile(".*Built (\\d+\\-\\w+\\-\\d+).*");

	@Override
	public List<ProductVersion> getProductInfo(Log log)
	{
		for (LogLine logLine : log.getLogLines())
		{
			String text = logLine.getText();
			Matcher matcher = pattern.matcher(text);
			if (matcher.matches())
			{
				String buildDate = getBuildDate(text);
				return Collections.singletonList(new ProductVersion("SL4B", matcher.group(1), buildDate));
			}
		}

		return Collections.emptyList();
	}

	private String getBuildDate(String text)
	{
		Matcher matcher = patternDate.matcher(text);
		if (matcher.matches())
		{
			return matcher.group(1);
		}
		return "";
	}

}
