package fine2coarse.lbjsrc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fine2coarse.Wikipage;

public class WikipageClassifierDL {

	private static final Logger logger = LoggerFactory
			.getLogger(WikipageClassifierDL.class);
	Map<String, String> rules;
//	private WikipageClassifier wc;
	private int miss_count=0;
	private int total_count=0;
	private String dlpath;

	// public WikipageClassifierDL(String dlpath,WikipageClassifier wc)
	// {
	// this.dlpath = dlpath;
	// this.wc = wc;
	// List<String> lines = null;
	// try {
	// lines = FileUtils.readLines(new File(dlpath), "utf-8");
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// rules = new LinkedHashMap<String,String>();
	// for(String line:lines)
	// {
	// String[] parts = line.split("\\s");
	// // System.out.println(Arrays.asList(parts));
	// rules.put(parts[0], parts[2]);
	// }
	// }
	public WikipageClassifierDL(String dlpath) {
		this.dlpath = dlpath;
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(dlpath), "utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rules = new LinkedHashMap<String, String>();
		for (String line : lines) {
			String[] parts = line.split("\\s");
			// System.out.println(Arrays.asList(parts));
			rules.put(parts[0], parts[2]);
		}
	}

	public String discreteValue(Wikipage page) {
		total_count++;
		for (String type : rules.keySet()) {
			if (page.getTypes().contains(type)) {
				logger.info("FIRED " + type + " " + rules.get(type));
				return rules.get(type);
			}
		}
		miss_count++;
		return "NON_ENT";
	}
	int getMissCount()
	{
		return miss_count;
	}
	int getTotalCount()
	{
		return total_count;
	}
	String getModelPath() {
		return dlpath;
	}
}
