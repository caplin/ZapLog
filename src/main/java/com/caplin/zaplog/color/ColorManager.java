package com.caplin.zaplog.color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ColorManager
{

	public static final String NORMAL = "\u001b[0m";

	public static final String RED = "\u001b[31m";
	public static final String GREEN = "\u001b[32m";
	public static final String YELLOW = "\u001b[33m";
	public static final String MAGENTA = "\u001b[35m";
	public static final String CYAN = "\u001b[36m";

	private static List<String> colors = new ArrayList<String>();
	private static int currentIndex = 0;

	static
	{
		colors.add(GREEN);
		colors.add(CYAN);
		colors.add(YELLOW);
		colors.add(MAGENTA);
		colors.add(RED);
	}

	public static String getTextColor(File logFile)
	{
		String color = (currentIndex < colors.size()) ? colors.get(currentIndex) : NORMAL;
		currentIndex++;
		return color;
	}

}
