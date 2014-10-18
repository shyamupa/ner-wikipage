package fine2coarse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import com.google.common.collect.Lists;

import edu.illinois.cs.cogcomp.core.utilities.commands.InteractiveShell;
import edu.illinois.cs.cogcomp.lbjava.learn.Learner;
import edu.illinois.cs.cogcomp.lbjava.learn.Lexicon;
import edu.illinois.cs.cogcomp.lbjava.parse.Parser;
import edu.illinois.cs.cogcomp.wikifier.utils.freebase.QueryMQL;

/**
 * First arrange your training examples in directories. LOC dir will contain all
 * example wikipages for location eg. historical_places.txt, sports_venue.txt
 * etc. with wikititle (The_Oval_Stadium) on each line of the txt file. Use
 * FineGrainNER class in wikifier for generating these from freebase.
 * 
 * Then call writeTrainData Finally train using lbj on the training data
 * 
 * @author upadhya3
 *
 */
public class WikipageReader implements Parser {
	List<Wikipage> pages;
	private int CurrId;

	public WikipageReader(String filepath) {
		CurrId = 0;
		try {
			loadPages(filepath);
		} catch (Exception e) {
			System.err.println("OMG");
			e.printStackTrace();
		}
	}

	/**
	 * loads all examples from a file. file should contain examples like
	 * Volkswagen<TAB>ORG<TAB>/common/topic<TAB>/automotive/company ...
	 * @param filepath
	 * @throws Exception
	 */
	private void loadPages(String filepath) throws Exception {
		System.out.println("Loading test examples ...");
		List<String> lines = FileUtils.readLines(new File(filepath));
		pages = new ArrayList<Wikipage>();
		for (String line : lines) {
			String[] parts = line.split("\\t");
			ArrayList<String> tmp = Lists.newArrayList();
			for (int i = 2; i < parts.length; i++)
				tmp.add(parts[i]);

			// tmp.remove("/location/location");
			// tmp.remove("/people/person");
			// tmp.remove("/organization/organization");

			Wikipage wikipage = new Wikipage(parts[0], tmp, parts[1]); // parts[0] is title , parts[1] is NER type
//			if(wikipage.getLabel().equals("MISC"))
//				continue;
			if (!wikipage.getTypes().isEmpty())
				pages.add(wikipage);
		}
		// for(Wikipage page:pages)
		// {
		// System.out.println("#"+page.getLabel()+"#");
		// System.out.println("#"+page.getTitle()+"#");
		// System.out.println("#"+page.getTypes()+"#");
		// }
	}

	/**
	 * takes a dir like LOC and gets types for each example location in all
	 * files in that directory. returns the collection of these wikipages
	 * 
	 * @param labelDirs
	 * @return
	 * @throws IOException
	 */
	private static Collection<? extends Wikipage> handleLabelDir(File labelDirs)
			throws IOException {
		String NERlabel = labelDirs.getName();
		ArrayList<Wikipage> ans = Lists.newArrayList();
		QueryMQL mql = new QueryMQL();
		for (File example_files : labelDirs.listFiles()) {
			List<String> lines = FileUtils.readLines(example_files);
			int c=0;
			for (String line : lines) {
				List<String> types;
				try {
					types = mql.lookupTypeFromTitle(mql.buildQuery(
							"/wikipedia/en", mql.encodeMQL(line)));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if (types.size() != 0)
					ans.add(new Wikipage(line.trim(), types, NERlabel));
				if(c++ % 1000 == 0)
					System.out.println("Done "+c);
			}
		}
		return ans;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object next() {
		if (CurrId < pages.size()) {
			return pages.get(CurrId++);
		} else
			return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
//		InteractiveShell<WikipageReader> tester = new InteractiveShell<WikipageReader>(
//				WikipageReader.class);
//		if (args.length == 0)
//			tester.showDocumentation();
//		else {
//			tester.runCommand(args);
//		}
		// WikipageReader wikipageReader = new WikipageReader();
		 writeTrainData();
//		writeTestData();
	}

	/***
	 * writes out examples to a file for loadPage() function above
	 * 
	 * @throws Exception
	 */
	public static void writeTrainData() throws Exception {
		long start_time = System.nanoTime();
		File sourceDir = new File("data/F2CtrainData");
		PrintWriter writer = new PrintWriter("data/F2CtrainData/F2Ctrain");
		List<Wikipage> ans = Lists.newArrayList();
		int c = 0;
		for (File labelDirs : sourceDir.listFiles()) {

			if (!labelDirs.isDirectory())
				continue;

			System.out.println("handling " + labelDirs.getName()); // handles
																	// each
																	// label dir
																	// like LOC,
																	// PER etc.
			ans.addAll(handleLabelDir(labelDirs));

		}

		Collections.shuffle(ans);

		for (Wikipage page : ans) {
			c++;
			writer.print(page.getTitle() + "\t" + page.getLabel());
			for (String type : page.getTypes()) {
				writer.print("\t" + type);
			}
			writer.println();
			if (c % 1000 == 0) {
				System.out.println(c);
			}
		}
		writer.close();
		long end_time = System.nanoTime();
		double difference = (end_time - start_time) / 1e6;
		System.out.println("Time elapsed in ms " + difference);
	}
	static void writeTestData() throws IOException
	{
		List<String> lines = FileUtils.readLines(new File("ner3"));
		PrintWriter w = new PrintWriter(new File("testF2C2"));
		QueryMQL mql = new QueryMQL();
		int c=0;
		for(String line:lines)
		{
			String[] parts = line.split("\\s+");
			List<String> types;
			try {
				types = mql.lookupTypeFromTitle(mql.buildQuery(
						"/wikipedia/en", mql.encodeMQL(parts[1])));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("skipping");
				continue;
			}
			if(types.size()!=0)
			{
				
				w.print(parts[1]+"\t"+parts[0].substring(2)+"\t");
				for(String t:types)
				{
					w.print(t+"\t");
				}
				w.println();
			}
//			System.out.println();
			if(c++ % 1000 ==0)
				System.out.println(c);
		}
		w.close();
	}
}
