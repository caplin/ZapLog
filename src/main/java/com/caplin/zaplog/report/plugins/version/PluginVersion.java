package com.caplin.zaplog.report.plugins.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.caplin.zaplog.Log;
import com.caplin.zaplog.ZapLog;
import com.caplin.zaplog.report.plugins.IPlugin;

public class PluginVersion implements IPlugin
{

	private List<Version> productInfos;
	private List<ProductVersion> products;
	private List<String> productText;
	private ProductComparator productComparator;

	private int MAX_PRODUCT_NAME_LENGTH = 0;

	public PluginVersion()
	{
		this.productComparator = new ProductComparator();
		this.products = new ArrayList<ProductVersion>();
		this.productText = new ArrayList<String>();
		this.productInfos = new ArrayList<Version>();
		productInfos.add(new VersionSL4B());
		productInfos.add(new VersionServer());
		productInfos.add(new VersionLiberator());
		productInfos.add(new VersionRefiner());
		productInfos.add(new VersionSLJS());
		productInfos.add(new VersionJavaProduct());
	}

	@Override
	public void addLog(Log log)
	{
		for (Version productInfo : productInfos)
		{
			List<ProductVersion> newProducts = productInfo.getProductInfo(log);
			for (ProductVersion product : newProducts)
			{
				int productNameLength = product.getName().length();
				if (productNameLength > MAX_PRODUCT_NAME_LENGTH)
				{
					MAX_PRODUCT_NAME_LENGTH = productNameLength;
				}
				if (!productText.contains(product.getOutput()))
				{
					productText.add(product.getOutput());
					products.add(product);
				}
			}
		}
	}

	@Override
	public String getOutput()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" Discovery: " + products.size() + " Products Found");
		sb.append(ZapLog.NEW_LINE);

		Collections.sort(products, productComparator);

		for (ProductVersion product : products)
		{
			sb.append("\t");
			sb.append(StringUtils.rightPad(product.getName(), MAX_PRODUCT_NAME_LENGTH)
					+ StringUtils.rightPad(" (" + product.getVersion() + ") ", 20) + product.getBuildVersion());
			sb.append(ZapLog.NEW_LINE);
		}

		return sb.toString();
	}

}
