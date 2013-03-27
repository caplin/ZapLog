package com.caplin.zaplog.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.ZapLog;
import com.caplin.zaplog.ZapUtils;

public class Report
{

	private List<ZapPlugin> plugins;

	public Report()
	{
		this.plugins = new ArrayList<ZapPlugin>();

		PluginLoader pluginService = PluginLoaderFactory.createPluginLoader();
		pluginService.initPlugins();

		Iterator<ZapPlugin> loadedPlugins = pluginService.getPlugins();
		while (loadedPlugins.hasNext())
		{
			plugins.add(loadedPlugins.next());
		}
	}

	public void addLog(Log log)
	{
		for (ZapPlugin plugin : plugins)
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
		
		if (plugins.size() == 0)
		{
			sb.append(ZapLog.NEW_LINE);
			sb.append(" \tNo Plugins were found!");
			sb.append(ZapLog.NEW_LINE);
		}
		else
		{
			for (ZapPlugin plugin : plugins)
			{
				sb.append(ZapLog.NEW_LINE);
				sb.append(plugin.getOutput());
			}
		}

		sb.append(ZapLog.NEW_LINE);
		sb.append("*************************************************");

		return ZapUtils.addHashes(sb.toString());
	}

}
