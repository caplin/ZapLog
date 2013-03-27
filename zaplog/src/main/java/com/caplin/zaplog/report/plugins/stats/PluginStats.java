package com.caplin.zaplog.report.plugins.stats;

import java.text.NumberFormat;
import java.util.Locale;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.ZapLog;
import com.caplin.zaplog.plugin.ZapPlugin;

public class PluginStats implements ZapPlugin
{

	private int totalLines = 0;
	private int totalLogFiles = 0;
	private long startTime;

	@Override
	public String getName()
	{
		return "Plugin Statistics";
	}

	@Override
	public void init()
	{
	}

	@Override
	public void addLog(Log log)
	{
		if (totalLogFiles == 0)
		{
			this.startTime = System.currentTimeMillis();
		}
		this.totalLines += log.getLogLines().size();
		this.totalLogFiles++;
	}

	@Override
	public String getOutput()
	{
		long endTime = System.currentTimeMillis();
		long millis = endTime - startTime;
		double linesPerSecond = (totalLines * 1.0) / (millis / 1000.0);

		StringBuilder sb = new StringBuilder();
		sb.append(" Stats: ");
		sb.append(ZapLog.NEW_LINE);
		sb.append("\t");
		sb.append(getFormattedNumber(totalLogFiles) + " Logs");
		sb.append(ZapLog.NEW_LINE);
		sb.append("\t");
		sb.append(getFormattedNumber(totalLines) + " Lines");
		sb.append(ZapLog.NEW_LINE);
		sb.append("\t");
		sb.append(getFormattedNumber((int) linesPerSecond) + " Lines Per Second");
		sb.append(ZapLog.NEW_LINE);

		return sb.toString();
	}

	private String getFormattedNumber(int num)
	{
		NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
		return formatter.format(num);
	}

}
