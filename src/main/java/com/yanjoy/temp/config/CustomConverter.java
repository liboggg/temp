package com.yanjoy.temp.config;

import org.springframework.core.convert.converter.Converter;

/**
* 自定义转换器
* 去掉前后空格
* @author liuy
* @version 2018年11月13日 
*/
public class CustomConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		try {
			if(source != null){
				source = source.trim();
				if(!"".equals(source)){
					return source;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

