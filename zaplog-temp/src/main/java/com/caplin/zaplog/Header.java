package com.caplin.zaplog;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.caplin.zaplog.report.Report;

public class Header
{

	private Instant startTime;
	private String inputOptions;

	public Header(String[] inputArgs)
	{
		this.inputOptions = convertArgsToOptionsString(inputArgs);
	}
	
	public void println(String text)
	{
		if (!ZapArg.NO_HEADER)
		{
			System.out.println(ZapUtils.addHashes(text));
		}
	}

	public void startHeader()
	{
		println("###########################################################");
		addLogo();
		println(" ZapLog Options: " + inputOptions);
		println("");
	}
	
	public void printReport(Report report)
	{
		if (ZapArg.AUDIT && !ZapArg.TAIL)
		{
			println(report.getText());
		}
	}
	
	public void endHeader()
	{
		println("###########################################################");
	}
	
	public void startLogCap()
	{
		String version = getClass().getPackage().getImplementationVersion();
		version = (version == null) ? "" : version + " ";
		this.startTime = new Instant();
		println(" ZapLog " + version + "Started: " + startTime);
		println("");
	}

	public void endLogCap()
	{
		println("");
		Instant endTime = new Instant();
		Duration duration = new Duration(startTime, endTime);
		println(" ZapLog Finished: " + endTime);
		PeriodFormatter formatter = getPeriodFormatter();
		println(" ZapLog Time: " + duration.toPeriod().toString(formatter));
	}

	/**
	 * Helper Methods
	 */
	private void addLogo()
	{
		if (ZapArg.LOGO)
		{
			println("  _____                 __               ");
			println(" / _  /  __ _  _ __    / /   ___    __ _ ");
			println(" \\// /  / _` || '_ \\  / /   / _ \\  / _` |");
			println("  / //\\| (_| || |_) |/ /___| (_) || (_| |");
			println(" /____/ \\__,_|| .__/ \\____/ \\___/  \\__, |");
			println("              |_|                  |___/ ");
			println("");
			println(" -... -.--    -. .. -.-. -.-    -");
			println("");
		}
	}

	private PeriodFormatter getPeriodFormatter()
	{
		PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays().appendSuffix(" day", " days")
				.appendSeparator(" ").printZeroIfSupported().minimumPrintedDigits(2).appendHours().appendSeparator(":")
				.appendMinutes().printZeroIfSupported().minimumPrintedDigits(2).appendSeparator(":").appendSeconds()
				.minimumPrintedDigits(2).appendSeparator(".").appendMillis3Digit().minimumPrintedDigits(3)
				.toFormatter();
		return formatter;
	}

	private static String convertArgsToOptionsString(String[] args)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++)
		{
			String arg = args[i];
			if (arg.startsWith("-"))
			{
				sb.append(arg);
				sb.append(" ");
			}
		}
		if (sb.toString().length() == 0)
		{
			sb.append("NONE");
		}
		return sb.toString();
	}

}
