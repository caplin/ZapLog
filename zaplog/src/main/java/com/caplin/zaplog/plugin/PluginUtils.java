package com.caplin.zaplog.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class PluginUtils
{

	private static Logger logger = Logger.getLogger(PluginUtils.class.getName());
	private static final Class<?>[] parms = new Class[] { URL.class };

	public static void addDirToClasspath(File directory) throws IOException
	{
		if (directory.exists())
		{
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				File file = files[i];
				addURL(file.toURI().toURL());
			}
		}
		else
		{
			logger.warning("The directory \"" + directory + "\" does not exist!");
		}
	}

	public static void addURL(URL url) throws IOException
	{
		URLClassLoader systemLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		URL urls[] = systemLoader.getURLs();
		for (int i = 0; i < urls.length; i++)
		{
			if (urls[i].toString().equalsIgnoreCase(url.toString()))
			{
				logger.info("URL " + url + " is already in the CLASSPATH");
				return;
			}
		}
		Class<?> urlClassLoader = URLClassLoader.class;
		try
		{
			Method method = urlClassLoader.getDeclaredMethod("addURL", parms);
			method.setAccessible(true);
			method.invoke(systemLoader, new Object[] { url });
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			throw new IOException("Error: could not add URL to System ClassLoader");
		}
	}
}