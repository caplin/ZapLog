package com.caplin.zaplog.plugin;

import java.util.List;

import com.caplin.zaplog.Log;

public interface Version
{

	List<ProductVersion> getProductInfo(Log log);

}
