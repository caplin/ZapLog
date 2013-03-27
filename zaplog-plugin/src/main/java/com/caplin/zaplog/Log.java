package com.caplin.zaplog;

import java.io.File;
import java.util.List;

public interface Log
{
	
	List<LogLine> getLogLines();

	File getFile();

}
