package com.caplin.zaplog.plugin;

import java.util.ArrayList;
import java.util.List;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.ZapLog;
import com.caplin.zaplog.ZapUtils;
import com.caplin.zaplog.plugin.ZapPlugin;

public class Report
{

	private List<ZapPlugin> plugins;

	public Report()
	{
		this.plugins = new ArrayList<ZapPlugin>();
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
		for (ZapPlugin plugin : plugins)
		{
			sb.append(ZapLog.NEW_LINE);
			sb.append(plugin.getOutput());
		}

		sb.append(ZapLog.NEW_LINE);
		sb.append("*************************************************");

		return ZapUtils.addHashes(sb.toString());
	}

}
