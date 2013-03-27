package com.caplin.zaplog.report.plugins.version;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.LogLine;

public class VersionJavaProduct implements Version
{

	private static final Pattern patternVersion = Pattern.compile(".*com.(.*) - Version: (\\d+\\.\\d+\\.\\d+.*)");

	@Override
	public List<ProductVersion> getProductInfo(Log log)
	{
		List<ProductVersion> result = new ArrayList<ProductVersion>();

		for (LogLine logLine : log.getLogLines())
		{
			String text = logLine.getText();
			Matcher matcher = patternVersion.matcher(text);
			if (matcher.matches())
			{
				String product = getClassNameFromPackageName(matcher.group(1));
				String version = matcher.group(2);
				result.add(new ProductVersion(product, version));
			}
		}

		return result;
	}

	private String getClassNameFromPackageName(String packageName)
	{
		if (!packageName.contains("."))
		{
			return packageName;
		}
		else
		{
			return packageName.substring(packageName.lastIndexOf(".") + 1, packageName.length());
		}
	}

}
