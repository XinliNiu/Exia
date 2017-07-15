package com.iostate.exia.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.iostate.exia.util.Assert;
import com.iostate.exia.util.MyLogger;

public class OtherUtil {

	public static String mapDebugString(Map<?,?> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("map.size = ");
		sb.append(map.size());
		sb.append('\n');
		for (Entry<?, ?> entry : map.entrySet()) {
			sb.append(entry);
			sb.append('\n');
		}
		return sb.toString();
	}
	
	/**
	 * Readin a list from string, and "null" is regarded as null
	 * @param string a string representation of list, elements mustn't contain ','!
	 * @return the desired list
	 */
	public static List<String> readListRegardNull(String string) {
	  Assert.assertTrue(string.startsWith("["));
	  Assert.assertTrue(string.endsWith("]"));
	  String eleStr = string.substring(1, string.length()-1);
	  String[] elements = eleStr.split(", ");
	  
	  List<String> list = new ArrayList<String>();
	  for (String e : elements) {
	    Assert.assertTrue(e.indexOf(' ') == -1);
	    if (e.equals("null")) {
	      e = null;
	    }
	    list.add(e);
	  }
	  Assert.assertEquals(elements.length, list.size());
	  return list;
	}
}
