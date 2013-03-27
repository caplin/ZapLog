package com.caplin.zaplog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.IOUtils;
import org.fusesource.jansi.AnsiConsole;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.caplin.zaplog.color.ColorManager;

public class ZapMain
{

	public static PrintStream oldOut;
	private static File outputFile;
	private static PrintStream fileStream;

	static
	{
		ZapMain.oldOut = System.out;
	}

	public static void main(String[] args) throws IOException
	{
		JCommander jc = new JCommander(new ZapArg());
		final Scanner inputScanner = new Scanner(System.in);
		final ZapLog zaplog = new ZapLog(args);

		addShutdownHook(inputScanner, zaplog);

		try
		{
			jc.parse(args);
			if (ZapArg.REGEX != null)
			{
				ZapLog.REGEX_PATTERN = Pattern.compile(ZapArg.REGEX);
			}
			if (ZapArg.OUTPUT_FILE != null)
			{
				outputFile = new File(ZapArg.OUTPUT_FILE);
				fileStream = new PrintStream(new FileOutputStream(outputFile));
				System.setOut(fileStream);
			}

			// Start ZapLog
			if (ZapUtils.isPrettyAndNoOutput())
			{
				AnsiConsole.systemInstall();
			}
			zaplog.init();
			zaplog.printOutput();
			ZapMain.close();
			if (ZapUtils.isPrettyAndNoOutput())
			{
				AnsiConsole.systemUninstall();
			}
		}
		catch (PatternSyntaxException e)
		{
			System.err.println("Invalid Regex: " + e.getMessage());
			jc.usage();
		}
		catch (ParameterException e)
		{
			System.err.println(e.getMessage());
			jc.usage();

			if (jc.getParsedAlias() == null)
			{
				try
				{
					System.out.println("Press enter to continue...");
					inputScanner.nextLine();
				}
				catch (NoSuchElementException e2)
				{
					// do nothing
				}
				inputScanner.close();
				return;
			}
		}
	}

	private static void addShutdownHook(final Scanner inputScanner, final ZapLog zapLog)
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				if (ZapUtils.isPrettyAndNoOutput())
				{
					AnsiConsole.out.println(ColorManager.NORMAL);
				}
				try
				{
					inputScanner.close();
				}
				catch (Exception e)
				{
					// do nothing
				}
			}
		});
	}

	public static void close()
	{
		if (outputFile != null && !ZapArg.TAIL)
		{
			oldOut.println("Output File Created: " + outputFile.getAbsolutePath());
			IOUtils.closeQuietly(fileStream);
		}
	}

}
