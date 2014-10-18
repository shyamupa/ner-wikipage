//package edu.illinois.cs.cogcomp.wikifier.utils.freebase;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.io.FileUtils;
//import org.mapdb.DB;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.collect.Lists;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import edu.illinois.cs.cogcomp.wikifier.utils.io.MapDB;
//import edu.illinois.cs.cogcomp.wikifier.utils.spelling.AbstractSurfaceQueryEngine;
//
///**
// * Takes a string (e.g., "obama") and asks the Freebase Search API. Caches if
// * necessary. Outputs a set of entities.
// *
// * @author Percy Liang
// * @modified upadhya3
// */
//public class FreebaseSearch extends AbstractSurfaceQueryEngine {
//	static int counter = 0;
////	public Map<String, List<String>> cache_of_names = null;
//	public Map<String,String> response_cache = null;
//	public DB db;
//	private String apikey;
//	static final String defaultCacheLocation = "/shared/bronte/tac2014/data/freebaseRawResponseCache"; 
//	static final String defaultApikey = "AIzaSyAclVmmn2FbIc6PiN9poGfNTt2CcyU6x48"; // has 10k queries per day limit (Shyam)
////	static final String defaultApikey = "AIzaSyD4X-Y5JK4ONiCrxp_rbyo54VgKFFXcon0"; // has 10k queries per day limit (Chen-Tse)
//	
//	
//	private static final Logger logger = LoggerFactory
//			.getLogger(FreebaseSearch.class);
//
//	public FreebaseSearch() {
//		this.apikey = defaultApikey;
//		this.db = MapDB.newDefaultDb(defaultCacheLocation, "freebase_cache").make();
////		this.cache_of_names = db.getHashMap("freebase_cache");
//		this.response_cache = db.getHashMap("freebase_cache");
//	}
//
//	public FreebaseSearch(String cacheLocation,String key){
//		this.db = MapDB.newDefaultDb(cacheLocation, "freebase_cache").make();
////		this.cache_of_names = db.getHashMap("freebase_cache");
//		this.response_cache = db.getHashMap("freebase_cache");
//		this.apikey = key;
//	}
//	public FreebaseSearch(String cacheLocation) {
//		this(cacheLocation,defaultApikey);
//	}
//
//	public List<FreebaseAnswer> lookup(String query) throws InterruptedException,
//			MalformedURLException, IOException {
//		String response = null;
//		
//		// First, try the cache.
//		List<FreebaseAnswer> output = null;
//		if (response_cache != null) {
//			output = parseJson(response_cache.get(query));
//		}
//		if (output == null) 
//		{
//			output = Lists.newArrayList();
//		} 
//		else 
//		{
////			logger.info("found in cache!");
//			return output;
//		}
//		// If got nothing, then need to hit the server.
//		if (output.size() == 0) {
//			try 
//			{
//				response = getQueryResponse(query);
//			} catch (Exception E) 
//			{
//				E.printStackTrace();
//			}
//		}
//		output=parseJson(response);
//		if (response_cache!= null) {
//			response_cache.put(query, response);
//			db.commit();
//		}
//		return output;
//	}
//
//	private List<FreebaseAnswer> parseJson(String ans) {
//		if(ans==null)
//			return null;
//		List<FreebaseAnswer> output = Lists.newArrayList();
//		JsonElement parse = new JsonParser().parse(ans);
//		JsonObject asJsonObject = parse.getAsJsonObject();
//		JsonArray jarray = asJsonObject.getAsJsonArray("result");
//		for (JsonElement js : jarray) {
//			output.add(new FreebaseAnswer(js));
//		}
//		return output;
//	}
//
//	private String getQueryResponse(String query) throws MalformedURLException,
//			IOException {
//		counter++;
//		logger.info("NOT IN FREEBASE CACHE, QUERYING ... " + counter + " times");
//		String url = String.format(
//				"https://www.googleapis.com/freebase/v1/search?query=%s&key="+apikey,
//				URLEncoder.encode(query, "UTF-8"));
//		System.out.println("QUERY URL: "+url);
//		URLConnection conn = new URL(url).openConnection();
//		InputStream in = conn.getInputStream();
//
//		// Read the response
//		StringBuilder buf = new StringBuilder();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//		String line;
//		while ((line = reader.readLine()) != null)
//			buf.append(line);
//		reader.close();
//		logger.info(buf.toString());
//		return buf.toString();
//	}
//
//	// /en/barack_obama => fb:en.barack_obama, FOR LATER USE
//	private String toRDF(String s) {
//		if (s == null)
//			return s;
//		return "fb:" + s.substring(1).replaceAll("/", ".");
//	}
//
//	@Override
//	public String[] query(String q) throws IOException {
//		System.out.println("in query:" + q);
//		List<String> ans = new ArrayList<String>();
//		try {
//			List<FreebaseAnswer> tmp = lookup(q);
//			for(FreebaseAnswer t:tmp)
//			{
//				ans.add(t.getName());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ans.toArray(new String[ans.size()]);
//	}
//	
////	public List<String> MQLquery(String query) throws IOException{
////		String url = String.format(
////				"https://www.googleapis.com/freebase/v1/mqlread?query=%s"+apikey,
////				URLEncoder.encode(query, "UTF-8"));
////		System.out.println("QUERY URL: "+url);
////		URLConnection conn = new URL(url).openConnection();
////		InputStream in = conn.getInputStream();
////
////		// Read the response
////		StringBuilder buf = new StringBuilder();
////		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
////		String line;
////		while ((line = reader.readLine()) != null)
////			buf.append(line);
////		reader.close();
////		System.out.println(buf.toString());
////		return null;
////	}
//
//	@Override
//	public void close() throws IOException {
//		// TODO Auto-generated method stub
//
//	}
//	
//	public static void populate(FreebaseSearch search) throws IOException, InterruptedException{
//		List<String> lines = FileUtils.readLines(new File("name_queries"), "utf-8");
//		for(String q:lines)
//		{
//			search.lookup(q.replace("_", " "));
//		}
//	}
//	public static List<String> remove_duplicates(List<String> mylist){
//		List<String> ans = Lists.newArrayList();
//		for(String obj:mylist)
//		{
//			if(!ans.contains(obj.trim()))
//				ans.add(obj);
//		}
//		return ans;
//	}
//	
//	
//	public static void main(String[] args) throws MalformedURLException,
//			InterruptedException, IOException {
//		// String query = StrUtils.join(args, " ");
////		FreebaseSearch fb = new FreebaseSearch("/shared/bronte/tac2014/data/freebaseRawResponseCache");
//		FreebaseSearch fb = new FreebaseSearch();
//		System.out.println(fb.response_cache.size());
////		for(FreebaseAnswer ans:fb.lookup("obama"))
////		{
////			System.out.println(ans.getName()+":"+ans.getMid());
////		}
////		DB db = MapDB.newDefaultDb("/shared/bronte/tac2014/data/freebaseCache", "freebase_cache").make();
//////		this.cache_of_names = db.getHashMap("freebase_cache");
////		Map<String, String> name_cache = db.getHashMap("freebase_cache");
////		System.out.println(name_cache.size());
////		int c=0;
////		for(String key:name_cache.keySet())
////		{
//////			System.out.println(key+":");
////			fb.lookup(key);
//////			c++;
//////			if(c==10)
//////				break;
////		}
////		String query = "{\"mid\":\"/m/01nf0c\","
////		 		+ "\"key\": "
////		 		+ "[{\"namespace\": \"/wikipedia/en\",\"value\": null}]}";
////		System.out.println(fb.MQLquery(query));
////		System.out.println(fb.lookup("obama"));
//		// query = "Obomber"; //
//		// query = "Saint Ronnie"; // NO!
//		// query = "Kstewart"; // NO!
//		// query = "Shrub"; /////
//		// query = "Mufc"; ///
//
//		// query = "Hogtown"; /////
//		// query = "Arnie"; //
//		// query = "Owe Bama"; // NO!
//		// query = "Belgie"; //
//
//		// query = "MAN U"; //
//		// query = "Brittania";
//
//		// query = "heels"; //
//		// query = "dprk"; //
//		// query = "CFF"; // NO!
//		// query = "Hayluh Bawbuh"; // NO!
//		// query = "T Dot"; //
//		// query = "Chitown"; // NO!
//		// query = "Rpattz"; ///
//		// query = "Hiltery"; // NO!
//		// query = "Baby Bush"; ///
//		// query = "Mclame"; // NO!
//		// query = "Rob Pattinson";
//		// query = "Cigar City"; //
////		query = "Nobama"; 
////		String[] querys = { "Obomber", "MAN U", "Arnie", "heels", "dprk",
////				"CFF", "Hayluh Bawbuh", "Rob Pattinson", "Cigar City",
////				"Baby Bush", "Hogtown" };	// 11 queries
//
////		FreebaseSearch search = new FreebaseSearch(
////				"/shared/bronte/tac2014/data/freebaseCache");
////		 FreebaseSearch search = new FreebaseSearch();
////		System.out.println(search.lookup("Popolo Delle Liberta'"));
////		FreebaseSearch search = new FreebaseSearch("freebase_cache");
////		System.out.println(search.cache.size());
////		populate(search);
////		System.out.println(search.cache.size());
//		
////		for(String key:search.cache.keySet())
////		{
////			System.out.println(key+" : "+search.cache.get(key));
////			System.out.println(search.cache.get(key).size());
////			
//////			System.out.println(remove_duplicates(search.cache.get(key)));
//////			search.cache.put(key, remove_duplicates(search.cache.get(key)));
//////			System.out.println(remove_duplicates(search.cache.get(key)).size());
////		}		
//		// int c=0;
//		// while(true)
//		// {
//		// try {
//		// search.lookup(query);
//		// } catch (Exception e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// c++;
//		// System.out.println(c);
//		// }
////		for (String query : querys)
////			for (String ans : search.lookup(query)) {
////				System.out.println(ans);
////			}
//	}
//}
