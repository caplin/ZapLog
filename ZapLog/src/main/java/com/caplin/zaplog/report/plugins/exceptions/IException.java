package com.caplin.zaplog.report.plugins.exceptions;

import java.util.List;

import com.caplin.zaplog.Log;

public interface IException
{

	List<ExceptionDescription> getExceptionDescriptions(Log log);

}
