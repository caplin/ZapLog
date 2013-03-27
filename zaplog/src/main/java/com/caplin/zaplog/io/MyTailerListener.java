package com.caplin.zaplog.io;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import com.caplin.zaplog.LogImpl;

public class MyTailerListener extends TailerListenerAdapter
{
	private LogImpl log;

	public MyTailerListener(LogImpl log)
	{
		this.log = log;
	}

	@Override
	public void init(Tailer tailer)
	{
		super.init(tailer);
	}

	@Override
	public void handle(String logLine)
	{
		log.addNewLine(logLine);
	}
}
