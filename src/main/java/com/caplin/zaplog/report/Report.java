package com.caplin.zaplog.report;

import java.util.ArrayList;
import java.util.List;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.ZapUtils;
import com.caplin.zaplog.ZapLog;
import com.caplin.zaplog.report.plugins.IPlugin;
import com.caplin.zaplog.report.plugins.exceptions.PluginExceptions;
import com.caplin.zaplog.report.plugins.stats.PluginStats;
import com.caplin.zaplog.report.plugins.version.PluginVersion;

public class Report
{

	private List<IPlugin> plugins;

	public Report()
	{
		this.plugins = new ArrayList<IPlugin>();
		plugins.add(new PluginVersion());
		plugins.add(new PluginStats());
		plugins.add(new PluginExceptions());
	}

	public void addLog(Log log)
	{
		for (IPlugin plugin : plugins)
		{
			plugin.addLog(log);
		}
	}

	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(ZapLog.NEW_LINE);
		sb.append("****************** Plugins **********************");
		sb.append(ZapLog.NEW_LINE);
		for (IPlugin plugin : plugins)
		{
			sb.append(ZapLog.NEW_LINE);
			sb.append(plugin.getOutput());
		}

		sb.append(ZapLog.NEW_LINE);
		sb.append("*************************************************");

		return ZapUtils.addHashes(sb.toString());
	}

}
