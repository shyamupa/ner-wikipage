package fine2coarse.lbjsrc;

import fine2coarse.Wikipage;

public class PageClassifierFilter {

	private WikipageClassifier c;
	private WikipageClassifierDL dl;

	public PageClassifierFilter(WikipageClassifier c)
	{
		this.c = c;
		
	}
	public PageClassifierFilter(WikipageClassifierDL dl)
	{
		this.dl = dl;
		
	}
	public String discreteValue(Wikipage p)
	{
		if(p.getTypes().size() == 1)
		{
			System.out.println("Too few features ...");
			return "NON_ENT";
		}
		if(c!=null)
			return c.discreteValue(p);
		else if(dl!=null)
			return dl.discreteValue(p);
		System.err.println("Should not get here in PageFilter!");
		return null;
			
	}
	
}
