package edu.illinois.cs.cogcomp.semeval.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;


public class GazeteerReader {
	public String listName;
	public ArrayList<String> listGazet;
	public ArrayList<String[]> listWords;

	public GazeteerReader(String fileName, String listName, boolean isLowerCase){
		try 
		{
			this.listName=listName;
			this.listGazet=new ArrayList<String>();
			this.listWords=new ArrayList<String[]>();
			
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
				if(isLowerCase)
				{
					line=line.toLowerCase();
				}
				listGazet.add(line);
				listWords.add(line.split(" |\n|\t"));
			}	
			
			reader.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	public static void main(String[] args) 
	{
		GazeteerReader g = new GazeteerReader("new_headers","headers",false);
		System.out.println(g.listName);
		for(String s:g.listGazet)
			System.out.println(s);
		for(String[] a:g.listWords)
			System.out.println(StringUtils.join(a));
	}
	public void addFile(String fileName, boolean isLowerCase){
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if(isLowerCase){
					line=line.toLowerCase();
				}
				listGazet.add(line);
				listWords.add(line.split(" |\n|\t"));
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
		
	public boolean testMembership(String cand){
		return listGazet.contains(cand);
	}
	
//	public boolean testMembership(String[] cand){
//		String[] phrases;
//		for(int i=0;i<listGazet.size();i++){
//			phrases=listGazet.get(i).split(" |\n|\t");
//			if(phrases.length!=cand.length) return false;
//			
//			for(int j=0;j<cand.length;j++){
//				if(!phrases[j].equals(cand[j])){
//					return false;
//				}
//			}
//		}
//		return true;
//	}
	
	
	
//	public boolean subArray(String[] big, String[] small, int index){
//		for(int i=0;i<small.length;i++){
//		
//			if(!big[i+index].equals(small[i])){
//				return false;
//			}
//		}
//		return true;
//	}
	
}
