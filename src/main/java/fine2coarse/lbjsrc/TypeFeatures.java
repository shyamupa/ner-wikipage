// Modifying this comment will cause the next execution of LBJava to overwrite this file.
// F1B88000000000000000D2CC1BA0380401481E7599648B34229A376AD4592306D29B98C22819BD5B0D0EBB76529A6A8FF362B8E3B0D851E1BECCB1B7B5A0534723ACCDF044E886F48F0EE2A60372A8B227330DE0FA1262CBED5014623887A372F96F72B159D87809ABE8EF1AC949FF4E701422E20B2B03167CEF50DDD71B7FA8000000

package fine2coarse.lbjsrc;

import edu.illinois.cs.cogcomp.lbjava.classify.*;
import edu.illinois.cs.cogcomp.lbjava.infer.*;
import edu.illinois.cs.cogcomp.lbjava.io.IOUtilities;
import edu.illinois.cs.cogcomp.lbjava.learn.*;
import edu.illinois.cs.cogcomp.lbjava.parse.*;
import fine2coarse.Wikipage;
import fine2coarse.WikipageReader;
import java.util.List;


/** Simply produces "bag of types" features. */
public class TypeFeatures extends Classifier
{
  public TypeFeatures()
  {
    containingPackage = "fine2coarse.lbjsrc";
    name = "TypeFeatures";
  }

  public String getInputType() { return "fine2coarse.Wikipage"; }
  public String getOutputType() { return "discrete%"; }

  public FeatureVector classify(Object __example)
  {
    if (!(__example instanceof Wikipage))
    {
      String type = __example == null ? "null" : __example.getClass().getName();
      System.err.println("Classifier 'TypeFeatures(Wikipage)' defined on line 9 of WikipageClassifier.lbj received '" + type + "' as input.");
      new Exception().printStackTrace();
      System.exit(1);
    }

    Wikipage d = (Wikipage) __example;

    FeatureVector __result;
    __result = new FeatureVector();
    String __id;
    String __value;

    List types = d.getTypes();
    for (int i = 0; i < types.size(); i++)
    {
      __id = "" + (types.get(i));
      __value = "true";
      __result.addFeature(new DiscretePrimitiveStringFeature(this.containingPackage, this.name, __id, __value, valueIndexOf(__value), (short) 0));
    }
    return __result;
  }

  public FeatureVector[] classify(Object[] examples)
  {
    if (!(examples instanceof Wikipage[]))
    {
      String type = examples == null ? "null" : examples.getClass().getName();
      System.err.println("Classifier 'TypeFeatures(Wikipage)' defined on line 9 of WikipageClassifier.lbj received '" + type + "' as input.");
      new Exception().printStackTrace();
      System.exit(1);
    }

    return super.classify(examples);
  }

  public int hashCode() { return "TypeFeatures".hashCode(); }
  public boolean equals(Object o) { return o instanceof TypeFeatures; }
}

