package com.caplin.zaplog.plugin;

import com.caplin.zaplog.Log;

public interface ZapPlugin
{

	void addLog(Log log);

	String getName();
	
	String getOutput();
	
	void init();

}
