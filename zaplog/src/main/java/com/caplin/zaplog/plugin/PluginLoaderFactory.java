package com.caplin.zaplog.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.caplin.zaplog.ZapLog;

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
			File directory = new File("plugins");
			if (!directory.exists())
			{
				String path = ZapLog.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				path = path.substring(0, path.lastIndexOf('/'));
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				directory = new File(decodedPath + "/plugins");
			}
			PluginUtils.addDirToClasspath(directory);
		}
		catch (IOException exception)
		{
			logger.log(Level.SEVERE, null, exception);
		}
	}
}