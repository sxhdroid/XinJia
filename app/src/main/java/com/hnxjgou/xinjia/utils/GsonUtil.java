package com.hnxjgou.xinjia.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gson解析工具类
 * 
 * @author xiao
 * 
 */
public class GsonUtil {

	private static final String TAG = "GsonUtil";
	
	/**
	 * 实体类转化为json
	 * 
	 * @param bean
	 * @return
	 */
	public static String bean2json(Object bean) {
		return new Gson().toJson(bean);
	}

	public static String list2json(List datas) {
		if (datas == null || datas.isEmpty()) return null;
		JsonArray jsonArray = new JsonArray();
		for (Object obj: datas) {
			JsonElement element = new JsonParser().parse(bean2json(obj));
			jsonArray.add(element);
		}
		return jsonArray.toString();
	}

	/**
	 * 
	 * @param json
	 * @param type
	 *            转化的目标实体类
	 * @return
	 */
	public static <T> T json2Obj(String json, Type type) throws JsonSyntaxException {

		JsonElement element = new JsonParser().parse(json);
		if (element.isJsonNull()) {
			return null;
		}

		return new Gson().fromJson(element, type);
	}

	/**
	 *
	 * @param json
	 * @param elementName json字符串中目标对象的名称
	 * @param type
	 *            转化的目标实体类
	 * @return
	 */
	public static <T> T json2Obj(String json, String elementName, Type type) throws JsonSyntaxException {

		JsonElement element = new JsonParser().parse(json);
		if (element.isJsonNull()) {
			return null;
		}
		return new Gson().fromJson(element.getAsJsonObject().get(elementName), type);
	}

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @param arrayElement 数组元素名称
	 * @param type 转换的目标对象类
	 * @return
	 */
	public static <T> List<T> jsonArray2List(String json, String arrayElement, Type type) {
		
		List<T> t = new ArrayList<T>();
		JsonElement element = new JsonParser().parse(json);
		if (element.isJsonNull() || element.getAsJsonObject().isJsonNull()) {
			return t;
		}
		
		try {
			JsonArray jsonarray = element.getAsJsonObject().getAsJsonArray(arrayElement);
			
			if (jsonarray == null) {
				return t;
			}
			
			for (int i = 0; i < jsonarray.size(); i++) {
				JsonObject obj = jsonarray.get(i).getAsJsonObject();
				
				t.add((T) json2Obj(obj.toString(), type));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 使用默认列表名称
	 * @param <T>
	 * @param json
	 * @param type 转换的目标对象类
	 * @return
	 */
	public static <T> List<T> jsonArray2List(String json, Type type) {
		
		List<T> t = new ArrayList<T>();
		
		JsonElement element = new JsonParser().parse(json);
		if (element.isJsonNull() || element.getAsJsonObject().isJsonNull()) {
			return t;
		}
		
		JsonArray jsonarray = element.getAsJsonObject().getAsJsonArray();
		if (jsonarray == null) {
			return Collections.emptyList();
		}
		for (int i = 0; i < jsonarray.size(); i++) {
			JsonObject obj = jsonarray.get(i).getAsJsonObject();
			
			t.add((T) json2Obj(obj.toString(), type));
		}
		return t;
	}
}
