package com.caplin.zaplog.plugin;

import java.util.ArrayList;
import java.util.List;

import com.caplin.zaplog.Log;

public class PluginExceptions implements ZapPlugin
{
	
	public static final String NEW_LINE = System.getProperty("line.separator");

	private List<ExceptionDescription> exceptionDescriptions;
	private List<IException> exceptions;

	@Override
	public String getName()
	{
		return "Plugin Exceptions";
	}

	@Override
	public void init()
	{
	}

	public PluginExceptions()
	{
		this.exceptionDescriptions = new ArrayList<ExceptionDescription>();
		this.exceptions = new ArrayList<IException>();

		exceptions.add(new ExceptionJavaStackTrace());
	}

	@Override
	public void addLog(Log log)
	{
		for (IException exception : exceptions)
		{
			exceptionDescriptions.addAll(exception.getExceptionDescriptions(log));
		}
	}

	@Override
	public String getOutput()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" Exceptions: " + exceptionDescriptions.size() + " Exceptions Found");
		sb.append(NEW_LINE);

		for (ExceptionDescription exceptionDescription : exceptionDescriptions)
		{
			sb.append("\t");
			sb.append(exceptionDescription.getOutput());
			sb.append(NEW_LINE);
		}

		return sb.toString();
	}

}
