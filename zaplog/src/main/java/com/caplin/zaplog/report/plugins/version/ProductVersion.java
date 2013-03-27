package com.caplin.zaplog.report.plugins.version;

public class ProductVersion
{

	private String name;
	private String version;
	private String buildDate = "";

	public ProductVersion(String name, String version)
	{
		this.name = name;
		this.version = version;
	}

	public ProductVersion(String name, String version, String buildDate)
	{
		this(name, version);
		this.buildDate = buildDate;
	}

	public String getName()
	{
		return name;
	}

	public String getVersion()
	{
		return version;
	}

	public String getBuildVersion()
	{
		return buildDate;
	}

	public String getOutput()
	{
		return name + " - " + version;
	}

}
