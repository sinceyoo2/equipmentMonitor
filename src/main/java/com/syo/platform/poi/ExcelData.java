package com.syo.platform.poi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcelData<T> {
	
	// 表头
    private List<String> titles;

    // 数据
    private List<List<Object>> rows = new ArrayList();
    
    private List<T> datas;
    
    private List<String> fields;
    
    // 页签名称
    private String name;

	public ExcelData(String[] titles, String[] fields, List<T> datas, String name) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		super();
		this.titles = Arrays.asList(titles);
		this.fields = Arrays.asList(fields);
		this.datas = datas;
		this.name = name;
		
		if(datas.size()>0) {
			for(T data : datas) {
				List<Object> row = new ArrayList<>();
				if(data instanceof Map) {
					Map mapdata = (Map) data;
					for(String field : fields) {
						row.add(((Map) data).get(field));
					}
				} else {
					Class dataClass = data.getClass();
					for(String field : fields) {
						Field f = dataClass.getDeclaredField(field);
						f.setAccessible(true);
						row.add(f.get(data));
					}
				}
				rows.add(row);
			}
		}
		
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<List<Object>> getRows() {
		return rows;
	}

	public void setRows(List<List<Object>> rows) {
		this.rows = rows;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}


    
    

}
