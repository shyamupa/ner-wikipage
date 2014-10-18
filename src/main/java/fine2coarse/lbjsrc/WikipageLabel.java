// Modifying this comment will cause the next execution of LBJava to overwrite this file.
// F1B88000000000000000B49CC2E4E2A4D2945580FCCCECC284C4F45F94C4A4DC1D081F41254351C64751AA5108A6A4B82F41254F2D35B402A443DA51A61003B51DC9E3000000

package fine2coarse.lbjsrc;

import edu.illinois.cs.cogcomp.lbjava.classify.*;
import edu.illinois.cs.cogcomp.lbjava.infer.*;
import edu.illinois.cs.cogcomp.lbjava.io.IOUtilities;
import edu.illinois.cs.cogcomp.lbjava.learn.*;
import edu.illinois.cs.cogcomp.lbjava.parse.*;
import fine2coarse.Wikipage;
import fine2coarse.WikipageReader;
import java.util.List;


public class WikipageLabel extends Classifier
{
  public WikipageLabel()
  {
    containingPackage = "fine2coarse.lbjsrc";
    name = "WikipageLabel";
  }

  public String getInputType() { return "fine2coarse.Wikipage"; }
  public String getOutputType() { return "discrete"; }


  public FeatureVector classify(Object __example)
  {
    return new FeatureVector(featureValue(__example));
  }

  public Feature featureValue(Object __example)
  {
    String result = discreteValue(__example);
    return new DiscretePrimitiveStringFeature(containingPackage, name, "", result, valueIndexOf(result), (short) allowableValues().length);
  }

  public String discreteValue(Object __example)
  {
    if (!(__example instanceof Wikipage))
    {
      String type = __example == null ? "null" : __example.getClass().getName();
      System.err.println("Classifier 'WikipageLabel(Wikipage)' defined on line 14 of WikipageClassifier.lbj received '" + type + "' as input.");
      new Exception().printStackTrace();
      System.exit(1);
    }

    Wikipage d = (Wikipage) __example;

    return "" + (d.getLabel());
  }

  public FeatureVector[] classify(Object[] examples)
  {
    if (!(examples instanceof Wikipage[]))
    {
      String type = examples == null ? "null" : examples.getClass().getName();
      System.err.println("Classifier 'WikipageLabel(Wikipage)' defined on line 14 of WikipageClassifier.lbj received '" + type + "' as input.");
      new Exception().printStackTrace();
      System.exit(1);
    }

    return super.classify(examples);
  }

  public int hashCode() { return "WikipageLabel".hashCode(); }
  public boolean equals(Object o) { return o instanceof WikipageLabel; }
}

