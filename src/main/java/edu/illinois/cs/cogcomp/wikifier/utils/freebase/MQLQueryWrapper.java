package edu.illinois.cs.cogcomp.wikifier.utils.freebase;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MQLQueryWrapper {

	@Deprecated
	public String mid;
	public String MQLquery;
	public String value;

	// public MQLQuery(String mid, String namespace, String value)
	// {
	// this.mid = mid;
	// JSONObject obj = new JSONObject();
	// JSONArray key = new JSONArray();
	// obj.put("mid", mid);
	// JSONObject contents = new JSONObject();
	// contents.put("namespace", namespace);
	// contents.put("value", value);
	// key.add(contents);
	// obj.put("key", key);
	// }
	@Deprecated
	public MQLQueryWrapper(String mid) {
		this.mid = mid;
		JSONObject obj = new JSONObject();
		JSONArray type = new JSONArray();
		obj.put("mid", mid);
		obj.put("type", type);
		this.MQLquery = StringEscapeUtils
				.unescapeJavaScript(obj.toJSONString());
	}
	@Deprecated
	public MQLQueryWrapper(String mid, String namespace, String value) {
		
		this.value=value;
		JSONObject obj = new JSONObject();
		JSONArray key = new JSONArray();
		obj.put("mid", mid);
		JSONObject contents = new JSONObject();
		contents.put("namespace", namespace);
		contents.put("value", value);
		key.add(contents);
		obj.put("key", key);
		this.MQLquery = StringEscapeUtils
				.unescapeJavaScript(obj.toJSONString());
	}

	public MQLQueryWrapper(String namespace, String value) {
		this.value=value;
		JSONObject obj = new JSONObject();
		JSONArray key = new JSONArray();
		JSONArray type = new JSONArray();
		obj.put("mid", null);
		obj.put("type", type);
		JSONObject contents = new JSONObject();
		contents.put("namespace", namespace);
		contents.put("value", QueryMQL.encodeMQL(value));
		key.add(contents);
		obj.put("key", key);
		this.MQLquery = StringEscapeUtils
				.unescapeJavaScript(obj.toJSONString());
	}
}
