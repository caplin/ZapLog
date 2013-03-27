package com.caplin.zaplog.plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoaderFactory
{
	
	private static final Logger logger = Logger.getLogger(PluginLoaderFactory.class.getName());
	
	public static PluginLoader createPluginLoader()
	{
		addPluginJarsToClasspath();
		return PluginLoader.getInstance();
	}

	private static void addPluginJarsToClasspath()
	{
		try
		{
			PluginUtils.addDirToClasspath(new File("plugins"));
		}
		catch (IOException exception)
		{
			logger.log(Level.SEVERE, null, exception);
		}
	}
}