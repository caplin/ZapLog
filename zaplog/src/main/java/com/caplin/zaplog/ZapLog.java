package com.caplin.zaplog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.caplin.zaplog.io.Tailing;
import com.caplin.zaplog.io.Walker;
import com.caplin.zaplog.report.Report;

public class ZapLog
{

	public static final String NEW_LINE = System.getProperty("line.separator");
	public static Pattern REGEX_PATTERN;
	public static long MAX_FILE_NAME_LENGTH = -1;
	public static long MAX_FILE_LINE_LENGTH = -1;

	private ExecutorService logParserService;
	private List<LogImpl> logs;
	private Header header;
	private Report report;
	private Tailing tailing;

	public ZapLog(String[] inputArgs)
	{
		this.logs = new ArrayList<LogImpl>();
		this.header = new Header(inputArgs);
		this.report = new Report();
		this.logParserService = Executors.newFixedThreadPool(4);
	}

	/**
	 * Init/Report Section
	 */
	public void init()
	{
		header.startHeader();
		header.startLogCap();
		addLogs();
		initLogs();
		disableTailingIfNoLogs();
		readLogsOrTail();
		header.printReport(report);
		header.endLogCap();
		header.endHeader();
	}

	private void addLogs()
	{
		List<String> result = new ArrayList<String>();
		for (String logPath : ZapArg.INPUT_LOGS)
		{
			try
			{
				Set<File> logFiles = Walker.walkTree(new File(logPath), Walker.JUST_LOGS);
				for (File logFilePath : logFiles)
				{
					if (logFilePath.getName().length() > ZapLog.MAX_FILE_NAME_LENGTH)
					{
						ZapLog.MAX_FILE_NAME_LENGTH = logFilePath.getName().length();
					}
					result.add(" \t" + logFilePath);
					if (logFilePath.length() > 0)
					{
						logs.add(new LogImpl(logFilePath, report));
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (logs.size() == 0)
		{
			result.add(0, " No Logs Found!");
		}
		else
		{
			result.add(0, " Adding " + logs.size() + " Log Files:");
		}

		for (String text : result)
		{
			header.println(text);
		}
	}

	private void initLogs()
	{
		for (LogImpl log : logs)
		{
			log.init();
		}
	}

	private void disableTailingIfNoLogs()
	{
		if (logs.size() == 0)
		{
			ZapArg.TAIL = false;
		}
	}

	private void readLogsOrTail()
	{
		if (!ZapArg.TAIL)
		{
			readLogs();
		}
		else
		{
			this.tailing = new Tailing(logs);
		}
	}

	private void readLogs()
	{
		List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		for (Runnable logRunnable : logs)
		{
			Callable<Object> c = Executors.callable(logRunnable);
			callables.add(c);
		}
		try
		{
			logParserService.invokeAll(callables);
			logParserService.shutdown();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Output Section
	 */
	public void printOutput()
	{
		if (!ZapArg.NO_LOG_OUTPUT)
		{
			if (ZapArg.TAIL)
			{
				tailing.start();

				new Timer().schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						for (LogLineImpl logLine : tailing.getNewLogLines())
						{
							System.out.println(logLine.getOutput());
						}
					}
				}, 0, 1500);
			}
			else
			{
				if (ZapArg.CHRONOLOGICAL)
				{
					ZapUtils.printLogLines(getLogOutputChronologically(logs));
				}
				else
				{
					ZapUtils.printLogLines(getLogLines());
				}
			}
		}
	}

	public List<LogLine> getLogLines()
	{
		List<LogLine> result = new ArrayList<LogLine>();

		for (LogImpl log : logs)
		{
			result.addAll(log.getLogLines());
		}

		return result;
	}

	private List<LogLine> getLogOutputChronologically(List<LogImpl> logs)
	{
		List<LogLine> result = new ArrayList<LogLine>();

		for (LogImpl log : logs)
		{
			result.addAll(log.getLogLines());
		}

		List<LogLine> noDateLines = new ArrayList<LogLine>();
		for (LogLine logLine : result)
		{
			if (logLine.getDateTime() == null)
			{
				noDateLines.add(logLine);
			}
		}

		result.removeAll(noDateLines);

		LogLineComparator comparator = new LogLineComparator();
		Collections.sort(result, comparator);

		result.addAll(0, noDateLines);

		return result;
	}

}
