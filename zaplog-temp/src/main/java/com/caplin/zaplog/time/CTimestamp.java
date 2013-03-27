package com.caplin.zaplog.time;

import java.util.ArrayList;
import java.util.List;

public class CTimestamp
{

	private List<Timestamp> timestampsMap;
	private Timestamp bestTimestamp;
	
	public CTimestamp()
	{
		timestampsMap = new ArrayList<Timestamp>();
		timestampsMap.add(new Timestamp("yyyy/MM/dd-HH:mm:ss.SSS Z", 2, 0)); // 2012/09/14-16:26:21.396 -0400 || liberator logs
		timestampsMap.add(new Timestamp("yyyy MMM dd HH:mm:ss.SSS Z", 4, 0)); // 2012 Nov 21 14:40:02.529 +0000 || CalendarService
		timestampsMap.add(new Timestamp("MMM dd HH:mm:ss.SSS Z", 4, 0)); // Sep 14 16:21:05.226 -0400 || SL4B
		timestampsMap.add(new Timestamp("dd/MMM/yyyy:HH:mm:ss Z", 2, 0)); // 14/Sep/2012:16:26:21 -0400 || http logs
		timestampsMap.add(new Timestamp("yyyy/MM/dd HH:mm:ss.SSS Z", 2, 0)); // 2012/10/01 11:42:59.632 -0400 || refiner logs
		timestampsMap.add(new Timestamp("yyyy/MM/dd-HH:mm:ss.SSS", 2, 0)); // 2012/11/13-06:04:02.891 || audit log
		timestampsMap.add(new Timestamp("MMM dd, yyyy HH:mm:ss aaa", 4, 0)); // Nov 13, 2012 6:04:02 PM || stdout logs
	}

	public DateText getDate(String text)
	{
		if (bestTimestamp != null)
		{
			DateText dateText = bestTimestamp.getTimestampDate(text);
			if (dateText.getDate() != null)
			{
				return dateText;
			}
		}
		for (Timestamp timestamp : timestampsMap)
		{
			DateText newDate = timestamp.getTimestampDate(text);
			if (newDate.getDate() != null)
			{
				bestTimestamp = timestamp;
				return newDate;
			}
		}
		return new DateText(null, text);
	}

}
