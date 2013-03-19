package com.caplin.zaplog;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class ZapArg
{

	@Parameter(description = "log-paths", required = true)
	public static List<String> INPUT_LOGS = new ArrayList<String>();

	@Parameter(names = { "-nt", "-notimestamp" }, description = "Output doesn't includes Timestamp")
	public static boolean NO_TIMESTAMP = false;

	@Parameter(names = { "-nf", "-nofilenames" }, description = "Output doesn't includes File Names")
	public static boolean NO_FILENAMES = false;

	@Parameter(names = { "-nh", "-noheader" }, description = "Output doesn't contains a header")
	public static boolean NO_HEADER = false;

	@Parameter(names = { "-c", "-chronological" }, description = "Output in chronological order")
	public static boolean CHRONOLOGICAL = false;

	@Parameter(names = { "-ln", "-linenumbers" }, description = "Output includes Line Numbers")
	public static boolean LINENUMBERS = false;

	@Parameter(names = { "-s", "-strings" }, description = "Filter Strings (contains)")
	public static String STRINGS;

	@Parameter(names = { "-ns", "-nostrings" }, description = "Filter Strings (does not contain)")
	public static String NO_STRINGS;

	@Parameter(names = { "-regex" }, description = "Filter by Regex")
	public static String REGEX;

	@Parameter(names = { "-r" }, description = "Recursively finds logs in folders")
	public static boolean RECURSIVE = false;

	@Parameter(names = { "-f", "-format" }, description = "Formats the Output")
	public static boolean FORMAT = false;

	@Parameter(names = { "-a", "-audit" }, description = "Outputs Reports from Plugins")
	public static boolean AUDIT = false;

	@Parameter(names = { "-p", "-pretty" }, description = "Colourizes the Output (Linux) - Supports 5 different colors")
	public static boolean PRETTY = false;

	@Parameter(names = { "-tail" }, description = "(experimental) Tails the Output")
	public static boolean TAIL = false;

	@Parameter(names = { "-o", "-output" }, description = "Output to File")
	public static String OUTPUT_FILE;

	@Parameter(names = { "-logo" }, description = "Secret Logo!", hidden = true)
	public static boolean LOGO = false;

	@Parameter(names = { "-nologoutput" }, description = "No Log Output is Printed - Only the Header")
	public static boolean NO_LOG_OUTPUT = false;
}
