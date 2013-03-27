package com.caplin.zaplog.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.caplin.zaplog.ZapArg;

public class Walker
{
	protected Walker()
	{
	}

	public static FileFilter JUST_LOGS = new FileFilter()
	{
		@Override
		public boolean accept(File file)
		{
			return file.isFile()
					&& (endsWith(file, ".log") || endsWith(file, ".old") || endsWith(file, ".txt") || endWithNumber(file));
		}

		private boolean endsWith(File file, String ext)
		{
			return file.getAbsolutePath().toLowerCase().endsWith(ext);
		}

		private boolean endWithNumber(File file)
		{
			for (int i = 0; i <= 9; i++)
			{
				String path = file.getAbsolutePath().toLowerCase();
				if (path.contains("."))
				{
					path = path.substring(path.lastIndexOf(".") + 1);
					if (path.equals(Integer.toString(i)))
					{
						return true;
					}
				}
			}
			return false;
		}
	};

	public static Set<File> filter(Set<File> files, FileFilter filter)
	{
		Set<File> result = new TreeSet<File>(files);
		Iterator<File> itr = result.iterator();
		while (itr.hasNext())
		{
			File f = itr.next();
			if (!filter.accept(f))
			{
				itr.remove();
			}
		}
		return result;
	}

	public static Set<File> walkTree(File root, FileFilter filter) throws IOException
	{
		return filter(walkTree(root, new TreeSet<File>()), filter);
	}

	public static Set<File> walkTree(File root, Set<File> result) throws IOException
	{
		if (root.exists() && !result.contains(root.getCanonicalFile()))
		{
			result.add(root.getCanonicalFile());
			if (root.isDirectory())
			{
				File[] listFiles = root.listFiles();
				for (File f : listFiles)
				{
					if (ZapArg.RECURSIVE)
					{
						walkTree(f, result);
					}
				}
			}
		}
		return result;
	}

}