package com.caplin.zaplog.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.caplin.zaplog.LogImpl;
import com.caplin.zaplog.LogLineImpl;
import com.caplin.zaplog.LogLineComparator;

public class Tailing
{

	private ExecutorService executor;
	private static final int DELAY = 10;
	private List<LogImpl> logs;

	public Tailing(List<LogImpl> logs)
	{
		this.logs = logs;
		this.executor = Executors.newFixedThreadPool(logs.size());
	}

	public void start()
	{
		for (LogImpl log : logs)
		{
			TailerListener listener = new MyTailerListener(log);
			executor.execute(new Tailer(log.getFile(), listener, DELAY, false, false));
		}
	}

	public synchronized List<LogLineImpl> getNewLogLines()
	{
		List<LogLineImpl> newLogLines = new ArrayList<LogLineImpl>();
		for (LogImpl log : logs)
		{
			newLogLines.addAll(log.getNewLogLines());
			log.clearNewLogLines();
		}
		LogLineComparator comparator = new LogLineComparator();
		Collections.sort(newLogLines, comparator);
		return newLogLines;
	}

}
