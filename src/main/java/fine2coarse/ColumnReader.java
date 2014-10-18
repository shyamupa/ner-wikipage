package fine2coarse;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;

import com.google.common.collect.Lists;

import edu.illinois.cs.cogcomp.core.io.IOUtils;
import edu.illinois.cs.cogcomp.edison.data.curator.CuratorClient;
import edu.illinois.cs.cogcomp.thrift.base.AnnotationFailedException;
import edu.illinois.cs.cogcomp.thrift.base.ServiceUnavailableException;

public class ColumnReader {

	public static CuratorClient curator = new CuratorClient(
			"trollope.cs.illinois.edu", 9010);
	String filename = null;
	private Iterator<String> it;

	public ColumnReader(String filename) throws IOException {
		List<String> lines = FileUtils.readLines(new File(filename));
		it = lines.iterator();
	}

	String[] next() {
		if (!it.hasNext())
			return null;
		String line = it.next();
		String[] parts = line.split("\\s+");
		// if (line.length() == 0 || parts.length == 0) {
		// return next();
		// if (it.hasNext()) {
		// line = it.next();
		// parts = line.split("\\s+");
		// return parts;
		// } else
		// return null;
		// } else
		return parts;
	}

	public static void main(String[] args) throws IOException, ServiceUnavailableException, AnnotationFailedException, TException {
		List<String> files = FileUtils.readLines(new File("finalfiles"));
		for(String file:files)
			processFile(file);
//		processFile("mydata/mydev/170.txt");
//		processFile("mydata/mydev/65.txt");
	}

	static void makeCoNLLTestData()
	{
		
	}
	/**
	 * Will write out a .conll02 file which can then be used in brat
	 * @throws IOException
	 */
	public static void generateCoNLL2002Format() throws IOException {
		List<String> paths = FileUtils.readLines(new File("paths"));
		for (String path : paths) {
			ColumnReader reader = new ColumnReader(path);
			PrintStream w = System.out;
//			PrintWriter w = new PrintWriter(new File(path + ".conll02"));
			String[] line = reader.next();
			while (line != null) {
				System.out.println(Arrays.asList(line) + " " + line.length);
				if (line.length == 1)
					w.println();
				else if (!line[4].equals("-X-"))
					w.println(line[5] + " " + line[0]);
				line = reader.next();
			}
			w.close();
		}
	}


	/**
	 * expects 97.txt
	 * @param filepath
	 * @throws ServiceUnavailableException
	 * @throws AnnotationFailedException
	 * @throws TException
	 * @throws IOException
	 */
	private static void processFile(String filepath)
			throws ServiceUnavailableException, AnnotationFailedException,
			TException, IOException {
		System.out.println("processing " + filepath);
		PrintWriter w = new PrintWriter(new File(filepath+".final"));
		w.println("O       0       0       O       -X-     -DOCSTART-      x       x       0");
//		w.println();
		List<List<String>> tokens = getAllTokens(filepath);
		List<List<LabeledToken>> annotations = getAllAnnotations(filepath);
		List<List<LabeledToken>> tokenizedAnnotations = tokenize(annotations,tokens);
		for(List<LabeledToken> ann:tokenizedAnnotations)
		{
			for(LabeledToken tok:ann)
			{
				System.out.println(tok.getLabel()+" "+tok.getToken());
			}
		}
//		System.exit(-1);
		List<LabeledToken> top = tokenizedAnnotations.remove(0);
		LabeledToken nextLabeledToken=top.remove(0);
		boolean finished = false;
		for(List<String> sent:tokens)
			{
				for(String tok:sent)
				{
					
					if(nextLabeledToken!=null && tok.equals(nextLabeledToken.getToken()))	// match
					{
						w.println(nextLabeledToken.getLabel()+" - - - - "+tok+" - - - "+StringUtils.substringBeforeLast(nextLabeledToken.getLabel(), "-"));
						if(top.size()==0)
						{
							if(tokenizedAnnotations.size()!=0)
							{
								top=tokenizedAnnotations.remove(0);
								nextLabeledToken = top.remove(0);
							}
							else
							{
								System.out.println("Finsihed");
								finished=true;
								nextLabeledToken = null;
							}
						}
						else
						{
							nextLabeledToken=top.remove(0);
						}
					}
					else
					{
						w.println("O"+" - - - - "+tok+" - - - "+"O");
					}
				}
				w.println();
			}
		w.close();
		if(!finished)
		{
			System.err.println("Oh No!");
			System.exit(-1);
		}
	}


//	private static void correctAnnotations(List<List<LabeledToken>> annotations,
//			List<List<String>> tokens) {
//		for(List<LabeledToken> ann:annotations)
//		{
//			for(LabeledToken tok:ann)
//			{
//				if(StringUtils.isAlphanumeric(tok.getToken()))
//					continue;
//				else
//				{
//					TextAnnotation ta = AnnotationHelper.newTextAnnotation("", tok.getToken());
//					System.out.println(Arrays.asList(ta.getTokens()));
//				}
//			}
//		}
//	}


	private static List<List<LabeledToken>> tokenize(List<List<LabeledToken>> annotations,
			List<List<String>> tokens) {
		if(annotations.size()==0)
			return annotations;
		List<String> alltoks = Lists.newArrayList();
		for(List<String> tok:tokens)
		{
				alltoks.addAll(tok);
		}
		List<LabeledToken> entity = annotations.remove(0);
		LabeledToken entityChunk = entity.remove(0);
		String currtok;
		int i=0;
		List<List<LabeledToken>> ans = Lists.newArrayList();
		while(i<alltoks.size())
		{
			currtok = alltoks.get(i);
			if(entityChunk!=null && entityChunk.getToken().startsWith(currtok))	// partial match
			{
				String buf = currtok;
				List<LabeledToken> tmp = Lists.newArrayList();
				String type = StringUtils.substringAfterLast(entityChunk.getLabel(), "-");
				tmp.add(new LabeledToken(currtok, "B-"+type));
				while(buf.length()<entityChunk.getToken().length() && !buf.equals(entityChunk.getToken()))
				{
					currtok=alltoks.get(++i);	// this should not fail
					buf+=currtok;
					tmp.add(new LabeledToken(currtok, "I-"+type));
				}
				if(buf.length()==entityChunk.getToken().length()) // full match
				{
					buf=null;
					// move to next
					ans.add(tmp);
					while(entity.size()!=0)
					{
						entityChunk = entity.remove(0);
						currtok=alltoks.get(++i);
						System.out.println("here "+entityChunk.getToken()+" "+currtok);
						if(entityChunk!=null && entityChunk.getToken().startsWith(currtok))	// partial match
						{
							buf = currtok;
//							tmp = Lists.newArrayList();
							type = StringUtils.substringAfterLast(entityChunk.getLabel(), "-");
							tmp.add(new LabeledToken(currtok, "I-"+type));
							while(buf.length()<entityChunk.getToken().length() && !buf.equals(entityChunk.getToken()))
							{
								currtok=alltoks.get(++i);	// this should not fail
								buf+=currtok;
								tmp.add(new LabeledToken(currtok, "I-"+type));
							}
						}
					}

					if(annotations.size()!=0){
						entity = annotations.remove(0);
						entityChunk = entity.remove(0);
					}
					else
					{
						entityChunk=null;
					}
				}
			}
			i++;
		}
		for(List<LabeledToken> sent:ans)
		{
			for(LabeledToken tok:sent)
			{
				System.out.println(tok.getLabel()+" "+tok.getToken());
			}
			System.out.println("##");
		}
		return ans;
	}

	private static List<List<String>> getAllTokens(String filepath) throws IOException {
		ColumnReader reader = new ColumnReader(filepath);
		String[] row = reader.next();
		List<List<String>> ans = Lists.newArrayList();
		List<String> sentence = Lists.newArrayList();
		
		while (row != null) {
//			System.out.println(Arrays.asList(row) + " " + row.length);
			if (row.length == 1)
			{
				ans.add(sentence);
				sentence = Lists.newArrayList();
//				System.out.println();
			}
			else if (!row[4].equals("-X-"))
			{
//				System.out.println(row[5] + " " + row[0]);
				sentence.add(row[5]);
			}
			row = reader.next();
		}
		return ans;
	}

	private static List<List<LabeledToken>> getAllAnnotations(String filepath) throws IOException {
		int i=1;
		List<List<LabeledToken>> ans = Lists.newArrayList();
		while(true)
		{
			String currfile = filepath+".conll02-doc-"+i+".ann";
			if(IOUtils.exists(currfile))
			{
				System.out.println("Reading "+currfile);
				List<String> lines = FileUtils.readLines(new File(currfile));
				for (String line : lines) 
				{
					String[] parts = line.split("\\s+");
//					int span_start = Integer.parseInt(parts[2]);
//					int span_end = Integer.parseInt(parts[3]);
					String type = parts[1];
					String[] tokens = Arrays.copyOfRange(parts, 4, parts.length);
					List<LabeledToken> entity = Lists.newArrayList();
					for(int j=0;j<tokens.length;j++)
					{
						if(j==0)
						{
							entity.add(new LabeledToken(tokens[j], "B-"+type));
						}
						else
						{
							entity.add(new LabeledToken(tokens[j], "I-"+type));
						}
					}
					ans.add(entity);
				}
			}
			else
				break;
			i++;
		}
		return ans;
	}

	


}
