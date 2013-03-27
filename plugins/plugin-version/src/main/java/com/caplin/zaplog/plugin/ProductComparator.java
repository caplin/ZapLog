package com.caplin.zaplog.plugin;

import java.util.Comparator;

public class ProductComparator implements Comparator<ProductVersion>
{

	public ProductComparator()
	{

	}

	public int compare(ProductVersion o1, ProductVersion o2)
	{
		return cp(o1.getName(), o2.getName());
	}

	static <T extends Comparable<ProductVersion>> int cp(String a, String b)
	{
		return a == null ? (b == null ? 0 : Integer.MIN_VALUE) : (b == null ? Integer.MAX_VALUE : a.compareTo(b));
	}

}