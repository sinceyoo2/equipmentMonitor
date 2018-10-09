package com.syo.platform.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.converter.Converter;

//泛型的两个类型，第一个使源的类型，第二个使目标类型
public class MyDateConvertor implements Converter<String, Date> {

	private static Pattern p1 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");	//yyyy-MM-dd HH:mm:ss
	private static Pattern p2 = Pattern.compile("^\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}$");	//yyyy/MM/dd HH:mm:ss
	private static Pattern p3 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$");	//yyyy-MM-dd HH:mm
	private static Pattern p4 = Pattern.compile("^\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}$");	//yyyy/MM/dd HH:mm
	private static Pattern p5 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");	//yyyy-MM-dd
	private static Pattern p6 = Pattern.compile("^\\d{4}/\\d{2}/\\d{2}$");	//yyyy/MM/dd
	
	@Override
	public Date convert(String source) {
		SimpleDateFormat sdf = getDateFormat(source);
		Date rest = null;
		if(sdf!=null) {
			try {
				rest = sdf.parse(source);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
//		if(rest==null) {
//			//其它转换器转换失败也会抛出这个异常
//			throw new TypeMismatchException("", Date.class);
//		}
		return rest;
	}

	private SimpleDateFormat getDateFormat(String source) {
		if(p1.matcher(source).matches()) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		if(p2.matcher(source).matches()) {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
		if(p3.matcher(source).matches()) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		if(p4.matcher(source).matches()) {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm");
		}
		if(p5.matcher(source).matches()) {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
		if(p6.matcher(source).matches()) {
			return new SimpleDateFormat("yyyy/MM/dd");
		}
		return null;
	}

}