package fine2coarse.lbjsrc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import edu.illinois.cs.cogcomp.core.utilities.commands.CommandDescription;
import edu.illinois.cs.cogcomp.core.utilities.commands.InteractiveShell;
import edu.illinois.cs.cogcomp.core.utilities.commands.CommandIgnore;
import edu.illinois.cs.cogcomp.lbjava.classify.FeatureVector;
import edu.illinois.cs.cogcomp.wikifier.utils.freebase.QueryMQL;
import edu.illinois.cs.cogcomp.lbjava.learn.Learner;
import edu.illinois.cs.cogcomp.lbjava.learn.Lexicon;
import fine2coarse.Wikipage;
import fine2coarse.WikipageReader;

public class WikipageClassifierWrapper {

	static WikipageClassifier classifier = WikipageClassifier.getInstance();
	static WikipageClassifierDL dl = new WikipageClassifierDL(
			"data/F2CtrainData/80_conf_dl");

	public static void classifyTitle(String title) throws Exception {
		QueryMQL mql = new QueryMQL();
		Wikipage page = new Wikipage(title, mql.lookupTypeFromTitle(mql
				.buildQuery("/wikipedia/en", title)));
		System.out.println("TYPES: " + page.getTypes());
		// System.out.println("Using models from "
		// + classifier.getLexiconLocation().getPath() + ","
		// + classifier.getModelLocation().getPath());
		// PageClassifierFilter cf = new PageClassifierFilter(classifier);
		PageClassifierFilter dlf = new PageClassifierFilter(dl);

		// System.out.println("NB says " + classifier.discreteValue(page));
		// System.out.println("DL says " + dl.discreteValue(page));
		// System.out.println("NB says " + cf.discreteValue(page));
		System.out.println("DL says " + dlf.discreteValue(page));
	}

	@CommandDescription(description = "path to dlist")
	public static void testOnF2C(String dlpath) throws IOException {

		// WikipageClassifier c = WikipageClassifier.getInstance();
		// WikipageClassifier c2 = new WikipageClassifier(
		// "data/Models/pageNER/WikipageClassifier.lc",
		// "data/Models/pageNER/WikipageClassifier.lex");
		WikipageClassifierDL dl = new WikipageClassifierDL(dlpath);
		// PageClassifierFilter cf = new PageClassifierFilter(c);
		PageClassifierFilter dlf = new PageClassifierFilter(dl);
		WikipageReader reader = new WikipageReader("data/F2CtrainData/F2Ctrain");
		Wikipage next = (Wikipage) reader.next();
		int[][] confusion_matrix = new int[4][4];
		int per_correct=0,loc_correct=0,org_correct=0,nonent_correct=0;
		int per_total=0,loc_total=0,org_total=0,nonent_total=0;
		int correct = 0, total = 0;
		while (next != null) {
			String gold = next.getLabel();
			String pred = dlf.discreteValue(next);
			if (gold.equals(pred))
			{
				correct++;
				switch (gold) {
				case "PER":
					per_correct++;
					break;
				case "LOC":
					loc_correct++;
					break;
				case "ORG":
					org_correct++;
					break;
				case "NON_ENT":
					nonent_correct++;	
					break;
				default:
					break;
				}
			}
			switch (gold) {
			case "PER":
				per_total++;
				break;
			case "LOC":
				loc_total++;
				break;
			case "ORG":
				org_total++;
				break;
			case "NON_ENT":
				nonent_total++;	
				break;
			default:
				break;
			}
			total++;
//			updateConfusionMatrix(confusion_matrix,gold,pred);
			next = (Wikipage) reader.next();
		}
		System.out.println("DL missed " + dl.getMissCount() + " out of "
				+ dl.getTotalCount());
		System.out.println(correct + " " + total + " "
				+ ((correct * 1.0) / total));
		System.out.println(per_correct+" "+org_correct+" "+loc_correct+" "+nonent_correct);
		System.out.println(per_total+" "+org_total+" "+loc_total+" "+nonent_total);
	}

//	private static void updateConfusionMatrix(int[][] confusion_matrix,
//			String gold, String pred) {
//		
//	}

	@CommandIgnore
	public static void main(String[] args) throws Exception {
		InteractiveShell<WikipageClassifierWrapper> tester = new InteractiveShell<WikipageClassifierWrapper>(
				WikipageClassifierWrapper.class);
		if (args.length == 0)
			tester.showDocumentation();
		else {
			tester.runCommand(args);
		}

	}

	public static void seeFeatWeights() {
		Map<String, Double> featureWeights = getFeatureWeights(classifier);
		for (String k : featureWeights.keySet()) {
			System.out.println(featureWeights.get(k));
		}
	}

	@CommandIgnore
	public static Map<String, Double> getFeatureWeights(Learner c) {
		ByteArrayOutputStream sout = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(sout);
		c.write(out);
		String s = sout.toString();

		String[] lines = s.split("\n");
		Lexicon lexicon = c.getLexicon();

		Map<String, Double> feats = new HashMap<String, Double>();
		for (int i = 2; i < lines.length - 1; ++i) {
			String line = lines[i];
			try {
				String featid = lexicon.lookupKey(i - 2).toStringNoPackage(); // .getStringIdentifier();
				System.out.println(featid + " " + line);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			// feats.put(featid, Double.parseDouble(line));
			// String[] parts = line.split("   *");
			// if (1 < parts.length)
			// feats.put(parts[0], Double.parseDouble(parts[1]));
		}
		// TODO: A non-hard-coded way to deal with conjunctions:
		// Map<String,Double> mins = getMinimumPartsOfCompounds(feats);
		// Map<String,Double> totals = getSumsOfParts(feats, mins);
		// Map<String, Double> totals = getTotalsOfPartsOfCompounds(feats);
		// for (Map.Entry<String, Double> pair : totals.entrySet()) {
		// feats.put(pair.getKey() + "_ALL", pair.getValue());
		// }
		return feats;
	}
}
