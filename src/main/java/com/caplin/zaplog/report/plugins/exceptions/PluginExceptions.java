package com.caplin.zaplog.report.plugins.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.ZapLog;
import com.caplin.zaplog.report.plugins.IPlugin;

public class PluginExceptions implements IPlugin
{

	private List<ExceptionDescription> exceptionDescriptions;
	private List<IException> exceptions;

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
		sb.append(ZapLog.NEW_LINE);

		for (ExceptionDescription exceptionDescription : exceptionDescriptions)
		{
			sb.append("\t");
			sb.append(exceptionDescription.getOutput());
			sb.append(ZapLog.NEW_LINE);
		}

		return sb.toString();
	}

}
