package com.caplin.zaplog.plugin;

import com.caplin.zaplog.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;

public class PluginLogCat implements ZapPlugin
{

	public static final String NEW_LINE = System.getProperty("line.separator");

    private Logger logger = LoggerFactory.getLogger(PluginLogCat.class);

	@Override
	public String getName()
	{
		return "Plugin LogCat";
	}

    @Override
    public void init() {
        try {
            File logcatExe = getLogcatExe();
            if (logcatExe.exists()) {
                logger.info("Successfully loaded logcat at: {}", logcatExe.getAbsolutePath());
            }
            else {
                logger.error("Could not find logcat exe at: {}", logcatExe.getAbsolutePath());
            }
        }
        catch (UnsupportedEncodingException e) {
            logger.error("Exception: {}", e);
        }
    }

    @Override
	public void addLog(Log log)
	{
		try
		{
			if (isBinaryFile(log.getFile()))
			{
				log.clearLogLines();

                InputStream inputStream = getInputStream(log);

                BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
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

    private InputStream getInputStream(Log log) throws IOException {
        File file = getLogcatExe();
        String[] cmd = { file.getAbsolutePath(), log.getFile().getAbsolutePath() };
        Process p = Runtime.getRuntime().exec(cmd);
        return p.getInputStream();
    }

    private File getLogcatExe() throws UnsupportedEncodingException {
        return new File(getLogcatDirectory().getAbsolutePath() + "/logcat.exe");
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
		String s2 = s.replaceAll("[a-zA-Z0-9����\\.\\*!\"�\\$\\%&/()=\\?@~'#:,;\\"
				+ "+><\\|\\[\\]\\{\\}\\^���\\\\ \\n\\r\\t_\\-`�����" + "�������������������ҩ��������������]", "");

		double d = (double) (s.length() - s2.length()) / (double) (s.length());
		return !(d > 0.95);
	}

	public static void main(String[] args)
	{
		PluginLogCat logCat = new PluginLogCat();
		logCat.init();
	}

}
