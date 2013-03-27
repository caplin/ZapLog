package com.caplin.zaplog.plugin;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.LogLine;

public class VersionRefiner implements Version
{

	private static final Pattern pattern = Pattern.compile(".*Refiner (\\d+\\.\\d+\\.\\d+-\\d+).*");

	@Override
	public List<ProductVersion> getProductInfo(Log log)
	{
		for (LogLine logLine : log.getLogLines())
		{
			String text = logLine.getText();
			Matcher matcher = pattern.matcher(text);
			if (matcher.matches())
			{
				return Collections.singletonList(new ProductVersion("Refiner", matcher.group(1)));
			}
		}

		return Collections.emptyList();
	}

}
