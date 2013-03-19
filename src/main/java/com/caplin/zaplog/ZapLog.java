package com.caplin.zaplog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.IOUtils;
import org.fusesource.jansi.AnsiConsole;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.caplin.zaplog.color.ColorManager;
import com.caplin.zaplog.io.Tailing;
import com.caplin.zaplog.io.Walker;
import com.caplin.zaplog.report.Report;

public class ZapLog
{

	public static final String NEW_LINE = System.getProperty("line.separator");
	public static Pattern REGEX_PATTERN;
	public static long MAX_FILE_NAME_LENGTH = -1;
	public static long MAX_FILE_LINE_LENGTH = -1;
	
	private ExecutorService executorService;

	public static void main(String[] args) throws IOException
	{
		JCommander jc = new JCommander(new ZapArg());
		final Scanner s = new Scanner(System.in);
		final ZapLog zapLog = new ZapLog(args);

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				if (ZapArg.PRETTY)
				{
					AnsiConsole.out.println(ColorManager.NORMAL);
				}
				try
				{
					IOUtils.closeQuietly(zapLog.getOldOut());
					s.close();
				}
				catch (Exception e)
				{
					// do nothing
				}
			}
		});

		try
		{
			jc.parse(args);

			if (ZapArg.REGEX != null)
			{
				ZapLog.REGEX_PATTERN = Pattern.compile(ZapArg.REGEX);
			}

			if (ZapArg.OUTPUT_FILE != null)
			{
				File outputFile = zapLog.createOutputFile();
				System.setOut(zapLog.setPipeOut(outputFile));

				zapLog.init();
				zapLog.printOutput();
				if (!ZapArg.TAIL)
				{
					zapLog.closeOutputFile();
				}
			}
			else
			{
				AnsiConsole.systemInstall();
				zapLog.init();
				zapLog.printOutput();
				AnsiConsole.systemUninstall();
			}
		}
		catch (PatternSyntaxException e)
		{
			System.err.println("Invalid Regex: " + e.getMessage());
			jc.usage();
		}
		catch (ParameterException e)
		{
			System.err.println(e.getMessage());
			jc.usage();

			if (jc.getParsedAlias() == null)
			{
				try
				{
					System.out.println("Press any key to continue...");
					s.nextLine();
				}
				catch (NoSuchElementException e2)
				{
					// do nothing
				}
				s.close();
				return;
			}
		}
	}

	private List<Log> logs;
	private PrintStream pipeOut;
	private PrintStream oldOut = System.out;
	private Header header;
	private Report report;
	private Tailing tailing;

	private File outputFile;

	public ZapLog(String[] args)
	{
		this.logs = new ArrayList<Log>();
		this.header = new Header(convertArgsToOptionsString(args));
		this.report = new Report();
		this.executorService = Executors.newFixedThreadPool(4);
	}

	private String convertArgsToOptionsString(String[] args)
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

	private File createOutputFile()
	{
		outputFile = new File(ZapArg.OUTPUT_FILE);
		return outputFile;
	}

	public File getOutputFile()
	{
		return outputFile;
	}

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

	private void initLogs()
	{
		for (Log log : logs)
		{
			log.init();
		}
	}

	private void addLogs()
	{
		header.println(" Adding Logs Files:");
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
					header.println(" \t" + logFilePath);
					if (logFilePath.length() > 0)
					{
						logs.add(new Log(logFilePath, report));
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
			header.println(" \t No Logs Found!");
		}
	}

	private void closeOutputFile()
	{
		if (outputFile != null)
		{
			safeSystemOut("Output File Created: " + outputFile.getAbsolutePath());
			IOUtils.closeQuietly(this.pipeOut);
		}
	}

	public List<LogLine> getLogLines()
	{
		List<LogLine> result = new ArrayList<LogLine>();

		for (Log log : logs)
		{
			result.addAll(log.getLogLines());
		}

		return result;
	}

	private List<LogLine> getLogOutputChronologically(List<Log> logs)
	{
		List<LogLine> result = new ArrayList<LogLine>();

		for (Log log : logs)
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

	public PrintStream getOldOut()
	{
		return this.oldOut;
	}

	public PrintStream getPipeOut()
	{
		return this.pipeOut;
	}

	private void printOutput()
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
						for (LogLine logLine : tailing.getNewLogLines())
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
					Utils.printLogLines(getLogOutputChronologically(logs));
				}
				else
				{
					Utils.printLogLines(getLogLines());
				}
			}
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
			executorService.invokeAll(callables);
			executorService.shutdown();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private synchronized void safeSystemOut(String text)
	{
		synchronized (System.out)
		{
			System.setOut(getOldOut());
			System.out.println(text);
			System.setOut(getPipeOut());
		}
	}

	private PrintStream setPipeOut(File outputFile) throws FileNotFoundException
	{
		if (outputFile != null)
		{
			this.pipeOut = new PrintStream(new FileOutputStream(outputFile));
		}
		return this.pipeOut;
	}

}
