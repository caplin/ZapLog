package com.caplin.zaplog;

import org.joda.time.DateTime;

public interface LogLine
{

	DateTime getDateTime();

	String getOutput();

	String getText();

	String getFileName();

	long getLineNumber();

}
