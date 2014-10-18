package fine2coarse;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.illinois.cs.cogcomp.thrift.base.AnnotationFailedException;



public class Wikipage {

	public int labelId;	// for confusion matrix
	private String label;
	private String title;
	private List<String> types;
	public String prediction;
	
	/**
	 * use this when you do not know the coarse label
	 * 
	 */
	public Wikipage(String title, List<String> types)
	{
		this.title=title;
		this.setTypes(types);
	}
	public Wikipage(String title, List<String> types, String label)
	{
		this.title=title;
		this.setTypes(types);
		this.setLabel(label);
//		this.labelId=getLabelId(label);
		
	}
	/**
	 * use this when you have training data, and know the coarse grain label
	 * @param title
	 * @param label
	 */
	public Wikipage(String title,String label)
	{
		this.setTitle(title);
		this.setLabel(label);
//		this.labelId=getLabelId(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
//	public static String getIdLabel(int id)
//	{
//		switch (id) {
//		case 0:
//			return "PER";
//		case 1:
//			return "ORG";
//		case 2:
//			return "LOC";
//		default:
//			return "MISC";
//		}
//	}
	
//	public static int getLabelId(String label)
//	{
//		switch (label) {
//		case "PER":
//			return 0;
//		case "ORG":
//			return 1;
//		case "LOC":
//			return 2;
//		default:
//			return 3;
//		}
//	}
	
	/**
	 * Code for training a classifier from CoNLL data annotated by MPI
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
//		QueryMQL mql = new QueryMQL("");
//		Fine2CoarseClassifier fine2CoarseClassifier = new Fine2CoarseClassifier(new FeatureManager());
//		fine2CoarseClassifier.loadModel("conll_model");
//		fine2CoarseClassifier.getLabel(new Wikipage("Washington_(U.S._state)", mql.lookupType(mql.buildQuery(mql.lookupMid(mql.buildQuery(null, "/wikipedia/en",
//				QueryMQL.encodeMQL("Washington_(U.S._state)")))))));
		train();
//		testOnUnlabeledData();
	}
	
	
//	public static void testOnUnlabeledData() throws AnnotationFailedException, TException, Exception
//	{
//		Fine2CoarseClassifier fine2CoarseClassifier = new Fine2CoarseClassifier(new FeatureManager());
//		fine2CoarseClassifier.loadModel("conll_model2");
//		List<String> lines = new ArrayList<String>();
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("testDataForFine2Coarse"))));
//		while(true)
//		{
//			String tmp = br.readLine();
//			if(tmp==null)
//				break;
//			lines.add(tmp);
////			System.out.println(tmp);
//		}
//		List<Wikipage> testData = new ArrayList<Wikipage>();
//		for(int i=0;i<lines.size();i++)
//		{
//			String line = lines.get(i);
//			String[] parts = line.split("%%");
//			if(parts.length!=2)
//				continue;
//			List<String> types = Arrays.asList(parts[1].split("\\s+"));
//			List<String> titles = Arrays.asList(parts[0].split("\\s+"));
////			Iterator<String> it = types.iterator();
////			while(it.hasNext())
////			{
////				String next = it.next();
////				if(next.length()==0)
////					it.remove();
////			}
////			System.out.println(titles.get(0));
////			System.out.println(types);
//			testData.add(new Wikipage(titles.get(1), types.subList(1, types.size())));
////			break;
//		}
//		for(Wikipage page:testData)
//		{
//			System.out.println(page.title+" "+fine2CoarseClassifier.getLabel(page)+" "+page.getTypes());
//		}
//	}
	public static void train() throws Exception{
		
//		FileUtils.read
		List<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("trainF2C"))));
		while(true)
		{
			String tmp = br.readLine();
			if(tmp==null)
				break;
			lines.add(tmp);
//			System.out.println(tmp);
		}
//		System.exit(-1);
		List<Wikipage> pages = new ArrayList<Wikipage>();//Lists.newArrayList();
		for(String line:lines)
		{
			String[] parts = line.split("\\t");
			Wikipage wikipage = new Wikipage(parts[0], parts[1]);
			List<String> tmp = Arrays.asList(Arrays.copyOfRange(parts, 2, parts.length));
			wikipage.setTypes(Lists.newArrayList(tmp));
			wikipage.getTypes().remove("/common/topic");
			wikipage.getTypes().remove("/location/location");
			wikipage.getTypes().remove("/people/person");
			wikipage.getTypes().remove("/organization/organization");
			if(!wikipage.getTypes().isEmpty())
				pages.add(wikipage);
		}
		System.out.println(pages.size());
		for(Wikipage page:pages)
		{
			System.out.println(page.label);
			System.out.println(page.title);
			System.out.println(page.getTypes());
		}
		
		double trainFrac = 0.8;
		int rounds = 50;
//		Collections.shuffle(pages);
		List<Wikipage> train = pages.subList(0, (int) (pages.size()*trainFrac));
		List<Wikipage> test = pages.subList((int) (pages.size()*trainFrac),pages.size());
//		Fine2CoarseClassifier fine2CoarseClassifier = new Fine2CoarseClassifier(new FeatureManager());
//		fine2CoarseClassifier.learn(train, rounds);
//		List<Wikipage> incorrect = fine2CoarseClassifier.test(test);
//		System.out.println("Train size: "+train.size());
//		System.out.println("Test size: "+test.size());
//		int[][] confusion_matrix = new int[4][4];
//		
//		for(Wikipage page:incorrect)
//		{
//				System.out.println(page.title+" "+page.label+" "+page.prediction);
//				confusion_matrix[page.labelId][page.getLabelId(page.prediction)]++;
//		}
//		for(int i=0;i<confusion_matrix.length;i++)
//		{
//			System.out.print(Wikipage.getIdLabel(i)+" ");
//			for(int j=0;j<confusion_matrix[0].length;j++)
//			{
//				System.out.print(confusion_matrix[i][j]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println("Incorrect: "+incorrect.size());
////		fine2CoarseClassifier.saveModel("conll_model2");
		
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
}
