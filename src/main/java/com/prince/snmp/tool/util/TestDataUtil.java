package com.prince.snmp.tool.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataUtil {
	public static int randomInt(int start, int end){
		return new Random().nextInt(end - start) + start;
	}
	
	public static String randomString(int length){
		String result = "";
		for (int i = 0; i < length; i++) {
			char a = (char) (new Random().nextInt(24) + 98);
			result = result + a;
		}
		return result;
	}
	
	public static String nowTime(String dateFormat){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}
	
	public static File getResourceFile(String fileName){
		File resource = null;
		String path = TestDataUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File parent = new File(path);
		if(parent.isDirectory()){
			resource = new File(parent, fileName);
		}else{
			resource = new File(parent.getParent(), fileName);
		}
		
		return resource;
	}

	public static String parseInnerMethod(String src) {
		Pattern p = Pattern.compile(".*\\$\\{(.*)\\}.*");
		Matcher m = p.matcher(src);
		String matInStr = "";
		if(m.find()){
			matInStr = m.group(1);
		}
		
		String replaced = getReplaced(matInStr);

		return src.replaceAll("\\$\\{.*\\}", replaced);
	}
	
	private static String getReplaced(String matInStr) {
		String methodName = null;
		String parameter = null;
		Pattern p = Pattern.compile("(.*)\\((.*)\\)");
		Matcher m = p.matcher(matInStr);
		if(m.find()){
			methodName = m.group(1);
			parameter = m.group(2);
		}
		
		return executeMethod(methodName, parameter);
	}

	private static String executeMethod(String methodName, String parameter) {
		String result = null;
		if("randomInt".equals(methodName)){
			result = Integer.toString(randomInt(Integer.parseInt(parameter.split(", *")[0]), Integer.parseInt(parameter.split(", *")[1])));
		}
		if("randomString".equals(methodName)){
			result = randomString(Integer.parseInt(parameter));
		}
		if("nowTime".equals(methodName)){
			result = nowTime(parameter);
		}
		return result;
	}
	
	public static void main(String[] args) throws ParseException {
		String aa = "Wed Jul 17 11:50:28 2013";
		String bb = "Wed Jul 17 15:50:28 2013";
		System.out.println(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new SimpleDateFormat("E MMM d HH:mm:ss yyyy", Locale.ENGLISH).parse(aa)));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new SimpleDateFormat("E MMM d HH:mm:ss yyyy", Locale.ENGLISH).parse(bb)));
	}
}
