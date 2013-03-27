package com.caplin.zaplog.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.LogLine;
import com.caplin.zaplog.LogLineComparator;

public class Tailing
{

	private ExecutorService executor;
	private static final int DELAY = 10;
	private List<Log> logs;

	public Tailing(List<Log> logs)
	{
		this.logs = logs;
		this.executor = Executors.newFixedThreadPool(logs.size());
	}

	public void start()
	{
		for (Log log : logs)
		{
			TailerListener listener = new MyTailerListener(log);
			executor.execute(new Tailer(log.getFile(), listener, DELAY, false, false));
		}
	}

	public synchronized List<LogLine> getNewLogLines()
	{
		List<LogLine> newLogLines = new ArrayList<LogLine>();
		for (Log log : logs)
		{
			newLogLines.addAll(log.getNewLogLines());
			log.clearNewLogLines();
		}
		LogLineComparator comparator = new LogLineComparator();
		Collections.sort(newLogLines, comparator);
		return newLogLines;
	}

}
