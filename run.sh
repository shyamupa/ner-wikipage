#mvn -q dependency:copy-dependencies
#mvn -q compile

CP="./target/classes/:./target/dependency/*:./config/" # use this 

OPTIONS="-cp $CP"
PACKAGE_PREFIX="edu.illinois.cs.cogcomp"

#MAIN="$PACKAGE_PREFIX.metamap.MetamapHandler"
#MAIN="$PACKAGE_PREFIX.semeval.Contiguous"
#MAIN="$PACKAGE_PREFIX.semeval.Experiment"
#MAIN="$PACKAGE_PREFIX.semeval.XMLReader"
#MAIN="$PACKAGE_PREFIX.semeval.ConllGenerator"
#MAIN="fine2coarse.Wikipage"
#MAIN="fine2coarse.WikipageReader"
#MAIN="fine2coarse.lbjsrc.WikipageClassifierWrapper"
#MAIN="fine2coarse.ColumnReader"
#MAIN="$PACKAGE_PREFIX.semeval.indexing.Indexer"
#MAIN="$PACKAGE_PREFIX.quant.lbj.CTakesFeatureManager"
MAIN="$PACKAGE_PREFIX.wikifier.utils.freebase.FineGrainNER"
time nice java $OPTIONS $MAIN $*
