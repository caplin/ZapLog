package com.caplin.zaplog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.caplin.zaplog.color.ColorManager;
import com.caplin.zaplog.report.Report;
import com.caplin.zaplog.time.CTimestamp;
import com.caplin.zaplog.time.DateText;

public class LogImpl implements Runnable, Log
{

	private static final int MAX_FORMAT_LENGTH = 20;
	private List<LogLine> lineList;
	private List<LogLineImpl> newLineList;
	private File logFile;
	private String textColor;
	private String fileName;
	private DateTime currentDateTime;
	private long currentLineNumber = 0;
	private Report report;
	private CTimestamp timestamp;

	public LogImpl(File logFile, Report report)
	{
		this.report = report;
		this.logFile = logFile;
		this.lineList = new ArrayList<LogLine>();
		this.newLineList = new ArrayList<LogLineImpl>();
		this.textColor = ColorManager.getTextColor(logFile);
		this.timestamp = new CTimestamp();
	}

	@Override
	public void run()
	{
		readLogLines();
		if (report != null)
		{
			report.addLog(this);
		}
	}

	public void readLogLines()
	{
		configureFileName();

		try
		{
			List<String> logLines = getLogLines(logFile);

			int length = (logLines.size() == 0) ? 1 : (int) Math.log10(logLines.size()) + 1;
			if (length > ZapLog.MAX_FILE_LINE_LENGTH)
			{
				ZapLog.MAX_FILE_LINE_LENGTH = length;
			}

			for (int i = 0; i < logLines.size(); i++)
			{
				String logLine = logLines.get(i);

				if ((containsStrings(logLine) || containsRegex(logLine)) && !containsNoStrings(logLine))
				{
					currentLineNumber++;
					lineList.add(textToLogLine(fileName, i + 1, currentDateTime, logLine));
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void init()
	{
		configureFileName();
	}

	private void configureFileName()
	{
		fileName = logFile.getName();
		if (ZapArg.FORMAT)
		{
			int length = (int) ((ZapLog.MAX_FILE_NAME_LENGTH < MAX_FORMAT_LENGTH) ? ZapLog.MAX_FILE_NAME_LENGTH
					: MAX_FORMAT_LENGTH);
			fileName = StringUtils.rightPad(StringUtils.abbreviate(logFile.getName(), length), length, " ");
		}
	}

	public LogLineImpl textToLogLine(String fileName, long lineNumber, DateTime date, String text)
	{
		DateText dateText = timestamp.getDate(text);
		if (dateText.getDate() != null)
		{
			date = dateText.getDate();
			currentDateTime = date;
		}

		return new LogLineImpl(this, fileName, lineNumber, date, dateText.getText(), textColor);
	}

	private List<String> getLogLines(File logFile) throws Exception
	{
		return FileUtils.readLines(logFile, "UTF-8");
	}

	private boolean containsRegex(String logLine)
	{
		if (ZapLog.REGEX_PATTERN != null)
		{
			return ZapLog.REGEX_PATTERN.matcher(logLine).matches();
		}
		else if (ZapArg.STRINGS != null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean containsStrings(String logLine)
	{
		if (ZapArg.STRINGS != null)
		{
			for (String sub : ZapArg.STRINGS.split("\\?"))
			{
				if (logLine.contains(sub))
				{
					return true;
				}
			}
			return false;
		}
		else if (ZapLog.REGEX_PATTERN != null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean containsNoStrings(String logLine)
	{
		if (ZapArg.NO_STRINGS != null)
		{
			for (String sub : ZapArg.NO_STRINGS.split("\\?"))
			{
				if (logLine.contains(sub))
				{
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public List<LogLine> getLogLines()
	{
		return lineList;
	}

	public File getFile()
	{
		return logFile;
	}

	public void addNewLine(String logLine)
	{
		synchronized (newLineList)
		{
			if ((containsStrings(logLine) || containsRegex(logLine)) && !containsNoStrings(logLine))
			{
				currentLineNumber++;
				newLineList.add(textToLogLine(fileName, currentLineNumber, currentDateTime, logLine));
			}
		}
	}

	public List<LogLineImpl> getNewLogLines()
	{
		synchronized (newLineList)
		{
			return newLineList;
		}
	}

	public void clearNewLogLines()
	{
		synchronized (newLineList)
		{
			newLineList.clear();
		}
	}

	@Override
	public String toString()
	{
		return logFile.getName();
	}

}
