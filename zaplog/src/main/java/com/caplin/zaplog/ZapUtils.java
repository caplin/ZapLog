package com.caplin.zaplog;

import java.util.List;

import org.fusesource.jansi.AnsiConsole;

import com.caplin.zaplog.color.ColorManager;

public class ZapUtils
{

	private static void printlnPretty(String text)
	{
		if (isPrettyAndNoOutput())
		{
			AnsiConsole.out.println(text + ColorManager.NORMAL);
		}
		else
		{
			System.out.println(text);
		}
	}

	public static void printLogLines(List<LogLine> logLines)
	{
		for (LogLine logLine : logLines)
		{
			printlnPretty(logLine.getOutput());
		}
		addNormalColorAtEnd();
	}

	private static void addNormalColorAtEnd()
	{
		if (isPrettyAndNoOutput())
		{
			printlnPretty(ColorManager.NORMAL);
		}
	}

	public static boolean isPrettyAndNoOutput()
	{
		return ZapArg.PRETTY && ZapArg.OUTPUT_FILE == null;
	}

	public static String addHashes(String text)
	{
		String newText = text;
		if (text.contains(ZapLog.NEW_LINE))
		{
			newText = text.replace(ZapLog.NEW_LINE, ZapLog.NEW_LINE + "#");
		}
		else
		{
			newText = "#" + newText;
		}
		return newText;
	}
	
}
