//package edu.illinois.cs.cogcomp.wikifier.utils.freebase;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.Field.Index;
//import org.apache.lucene.document.Field.Store;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//
//import com.google.common.collect.Lists;
//
//import edu.illinois.cs.cogcomp.wikifier.utils.lucene.Lucene;
//
///**
// * @author cheng88
// * @modified upadhya3
// *
// */
//
//public class Indexer {
//
//	private IndexWriter writer;
//	private IndexSearcher searcher;
//	private static QueryParser parser = Lucene.newQueryParser("wikititle",
//			Lucene.SIMPLE);
//
////	public Indexer() throws IOException {
////		// this(Params.indexPath);
////	}
//
//	public Indexer(String path) throws IOException {
//		writer = Lucene.simpleWriter(path);
//		searcher = new IndexSearcher(DirectoryReader.open(writer, false));
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			public void run() {
//				try {
//					writer.commit();
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	public void add(String id, String text) {
//		Document doc = makeDocument(id, text);
//		try {
//			writer.addDocument(doc);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private Document makeDocument(String id, String text) {
////		System.out.println("MID" + id);
////		System.out.println(text);
////		System.out.println("?????????????");
//		List<String> lines = null;
//		try {
//			BufferedReader rdr = new BufferedReader(new StringReader(text));
//			lines = new ArrayList<String>();
//			for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
//			    lines.add(line);
//			}
//			rdr.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if(lines.size()==0)
//		{
//			System.out.println(id);
//			System.err.println("Something terribly wrong has happened");
//		}
//		Document doc = new Document();
//		doc.add(new StringField("mid",id,Store.YES));
//		
//		for(String line:lines)
//			doc.add(new Field("wikititle",text,Lucene.JUST_INDEX));
//		
//		return doc;
//	}
//
//	public List<Document> search(String query) {
//		List<Document> results = Lists.newArrayList();
//		try {
//			Query q = parser.parse(query);
//			for (ScoreDoc sc : searcher.search(q, 20).scoreDocs) {
//				results.add(searcher.doc(sc.doc));
//			}
//		// } catch (IOException | ParseException e) {
//		// 	e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return results;
//	}
//
//	public long numDocs() {
//		return searcher.getIndexReader().numDocs();
//	}
//
//	private void populate() throws IOException {
//
//		BufferedReader br = new BufferedReader(new FileReader(
//				("/shared/bronte/tac2014/data/mids_freebase")));
//		String line;
//		String mid = null;
//		int c = 0;
//		StringBuilder sb = null;
//		while ((line = br.readLine()) != null) {
//			if (line.startsWith("======")) 
//			{
//				continue;
//			}
//			
//			if (line.startsWith("MID: ")) 
//			{
//				if(mid!=null)
//					add(mid, sb.toString().trim());
//				String[] parts = line.split(" ");
//				mid = parts[1];
//				sb=new StringBuilder();
//			} 
//			else 
//			{
//				sb.append(line+"\n");
//			}
////			if (c++ == 150)
////				break;
//			// System.out.println(c++);
//		}
//		br.close();
//	}
//
//	/**
//	 * Please dont run this multiple times
//	 * 
//	 * @param args
//	 * @throws Exception
//	 */
//	public static void main(String[] args) throws Exception {
//		Indexer indexer = new Indexer("/shared/bronte/tac2014/data/freebaseRawResponseCache/wikititle2mid/mapIndex");
//		indexer.populate();
////		List<Document> docs;
////		docs = indexer.search("Barack_Obama");
////		docs = indexer.search("India");
////		System.out.println(docs.size());
////		for(Document doc:docs)
////		{
////			System.out.println(doc.getField("mid"));
////		}
//	}
//
//}
