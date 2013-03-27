package com.caplin.zaplog.report.plugins;

import com.caplin.zaplog.Log;

public interface IPlugin
{

	String getOutput();

	void addLog(Log log);

}
