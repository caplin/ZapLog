package com.caplin.zaplog.report.plugins.version;

import java.util.List;

import com.caplin.zaplog.Log;

public interface Version
{

	List<ProductVersion> getProductInfo(Log log);

}
