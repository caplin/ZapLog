package com.caplin.zaplog.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.caplin.zaplog.Log;

public class PluginLogCat implements ZapPlugin
{

	public static final String NEW_LINE = System.getProperty("line.separator");

	@Override
	public String getName()
	{
		return "Plugin LogCat";
	}

	@Override
	public void init()
	{
		
	}

	@Override
	public void addLog(Log log)
	{
		try
		{
			if (isBinaryFile(log.getFile()))
			{
				log.clearLogLines();

				File file = new File(getLogcatDirectory().getAbsolutePath() + "/logcat.exe");
				String[] cmd = { file.getAbsolutePath(), log.getFile().getAbsolutePath() };
				Process p = Runtime.getRuntime().exec(cmd);

				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String logLine = null;
				while ((logLine = input.readLine()) != null)
				{
					log.addLogLine(logLine);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public File getLogcatDirectory() throws UnsupportedEncodingException
	{
		File directory = new File("logcat");
		if (!directory.exists())
		{
			String path = PluginLogCat.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.substring(0, path.lastIndexOf('/'));
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			directory = new File(decodedPath + "/logcat");
		}
		return directory;
	}

	@Override
	public String getOutput()
	{
		return "";
	}

	private boolean isBinaryFile(File file) throws IOException
	{
		FileInputStream in = new FileInputStream(file);
		int size = in.available();
		if (size > 1000)
		{
			size = 1000;
		}
		byte[] data = new byte[size];
		in.read(data);
		in.close();
		if (size == 0)
		{
			return true;
		}
		String s = new String(data, "ISO-8859-1");
		return isBinaryData(s);
	}

	private boolean isBinaryData(String s)
	{
		String s2 = s.replaceAll("[a-zA-Z0-9ßöäü\\.\\*!\"§\\$\\%&/()=\\?@~'#:,;\\"
				+ "+><\\|\\[\\]\\{\\}\\^°²³\\\\ \\n\\r\\t_\\-`´âêîô" + "ÂÊÔÎáéíóàèìòÁÉÍÓÀÈÌÒ©‰¢£¥€±¿»«¼½¾™ª]", "");

		double d = (double) (s.length() - s2.length()) / (double) (s.length());
		return !(d > 0.95);
	}

	public static void main(String[] args)
	{
		PluginLogCat logCat = new PluginLogCat();
		logCat.init();
	}

}
