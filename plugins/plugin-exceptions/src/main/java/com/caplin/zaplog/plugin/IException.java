package com.caplin.zaplog.plugin;

import java.util.List;

import com.caplin.zaplog.Log;

public interface IException
{

	List<ExceptionDescription> getExceptionDescriptions(Log log);

}
