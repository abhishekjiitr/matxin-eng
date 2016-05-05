package matxin;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.io.FileSequentialCollection;

import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.CoreMap;

import edu.stanford.nlp.pipeline.DefaultPaths;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.TrueCaseAnnotator;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.semgraph.SemanticGraphFormatter;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;

import matxin.ChunkRule;

class StanfordAnalyzer {
    private static void printRequiredProperties(PrintStream os) {
	// TODO some annotators (ssplit, regexner, gender, some parser
	// options, dcoref?) are not documented
	os.println("The following properties can be defined:");
	os.println("(if -props or -annotators is not passed in, default properties will be loaded via the classpath)");
	os.println("\t\"props\" - path to file with configuration properties");
	os.println("\t\"annotators\" - comma separated list of annotators");
	os.println("\tThe following annotators are supported: cleanxml, tokenize, ssplit, pos, lemma, ner, truecase, parse, coref, dcoref, nfl");
	
	os.println("\n\tIf annotator \"tokenize\" is defined:");
	os.println("\t\"tokenize.options\" - PTBTokenizer options (see edu.stanford.nlp.process.PTBTokenizer for details)");
	os.println("\t\"tokenize.whitespace\" - If true, just use whitespace tokenization");
	
	os.println("\n\tIf annotator \"cleanxml\" is defined:");
	os.println("\t\"clean.xmltags\" - regex of tags to extract text from");
	os.println("\t\"clean.sentenceendingtags\" - regex of tags which mark sentence endings");
	os.println("\t\"clean.allowflawedxml\" - if set to false, don't complain about XML errors");
	
	os.println("\n\tIf annotator \"pos\" is defined:");
	os.println("\t\"pos.maxlen\" - maximum length of sentence to POS tag");
	os.println("\t\"pos.model\" - path towards the POS tagger model");
	
	os.println("\n\tIf annotator \"ner\" is defined:");
	os.println("\t\"ner.model.3class\" - path towards the three-class NER model");
	os.println("\t\"ner.model.7class\" - path towards the seven-class NER model");
	os.println("\t\"ner.model.MISCclass\" - path towards the NER model with a MISC class");
	
	os.println("\n\tIf annotator \"truecase\" is defined:");
	os.println("\t\"truecase.model\" - path towards the true-casing model; default: " + DefaultPaths.DEFAULT_TRUECASE_MODEL);
	os.println("\t\"truecase.bias\" - class bias of the true case model; default: " + TrueCaseAnnotator.DEFAULT_MODEL_BIAS);
	os.println("\t\"truecase.mixedcasefile\" - path towards the mixed case file; default: " + DefaultPaths.DEFAULT_TRUECASE_DISAMBIGUATION_LIST);
	
	os.println("\n\tIf annotator \"nfl\" is defined:");
	os.println("\t\"nfl.gazetteer\" - path towards the gazetteer for the NFL domain");
	os.println("\t\"nfl.relation.model\" - path towards the NFL relation extraction model");
	
	os.println("\n\tIf annotator \"parse\" is defined:");
	os.println("\t\"parser.model\" - path towards the PCFG parser model");
	
	os.println("\nCommand line properties:");
	os.println("\t\"file\" - run the pipeline on the content of this file, or on the content of the files in this directory");
	os.println("\t         XML output is generated for every input file \"file\" as file.xml");
	os.println("\t\"extension\" - if -file used with a directory, process only the files with this extension");
	os.println("\t\"filelist\" - run the pipeline on the list of files given in this file");
	os.println("\t             output is generated for every input file as file.outputExtension");
	os.println("\t\"outputDirectory\" - where to put output (defaults to the current directory)");
	os.println("\t\"outputExtension\" - extension to use for the output file (defaults to \".xml\" for XML, \".ser.gz\" for serialized).  Don't forget the dot!");
	os.println("\t\"outputFormat\" - \"xml\" to output XML (default), \"serialized\" to output serialized Java objects");
	os.println("\t\"replaceExtension\" - flag to chop off the last extension before adding outputExtension to file");
	os.println("\t\"noClobber\" - don't automatically override (clobber) output files that already exist");
	os.println("\nIf none of the above are present, run the pipeline in an interactive shell (default properties will be loaded from the classpath).");
	os.println("The shell accepts input from stdin and displays the output at stdout.");
	
	os.println("\nRun with -help [topic] for more help on a specific topic.");
	os.println("Current topics include: parser");
	
	os.println();
    }
    
    private static void printHelp(PrintStream os, String helpTopic) {
	if (helpTopic.toLowerCase().startsWith("pars")) {
	    os.println("StanfordCoreNLP currently supports the following parsers:");
	    os.println("\tstanford - Stanford lexicalized parser (default)");
	    os.println("\tcharniak - Charniak and Johnson reranking parser (sold separately)");
	    os.println();
	    os.println("General options: (all parsers)");
	    os.println("\tparser.type - selects the parser to use");
	    os.println("\tparser.model - path to model file for parser");
	    os.println("\tparser.maxlen - maximum sentence length");
	    os.println();
	    os.println("Stanford Parser-specific options:");
	    os.println("(In general, you shouldn't need to set this flags)");
	    os.println("\tparser.flags - extra flags to the parser (default: -retainTmpSubcategories)");
	    os.println("\tparser.debug - set to true to make the parser slightly more verbose");
	    os.println();
	    os.println("Charniak and Johnson parser-specific options:");
	    os.println("\tparser.executable - path to the parseIt binary or parse.sh script");
	} else {
	    // argsToProperties will set the value of a -h or -help to "true" if no arguments are given
	    if ( ! helpTopic.equalsIgnoreCase("true")) {
		os.println("Unknown help topic: " + helpTopic);
		os.println("See -help for a list of all help topics.");
	    } else {
		printRequiredProperties(os);
	    }
	}
    }
    
    static private int max_lines = 100;
    static private int indent = 4;
    static private int prev_alloc = 0;
    static private List<ChunkRule> chunkRules;

    // working variables -- not thread-safe!!!
    static private StringBuilder out;
    static private Set<Integer> used;

    public static void main (String[] args) throws IOException {
	Properties props = null;
	if(args.length > 0){
	    props = StringUtils.argsToProperties(args);
	    boolean hasH = props.containsKey("h");
	    boolean hasHelp = props.containsKey("help");
	    if (hasH || hasHelp) {
		String helpValue = hasH ? props.getProperty("h") : props.getProperty("help");
		printHelp(System.err, helpValue);
		return;
	    }
	}
	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	
	System.err.print("chunkRules kargatzen...");
	if (!props.containsKey("chunkRules")) {
	    System.err.println("'chunkRules' property is mandatory");
	    return;
	}
	else {
	    BufferedReader f = new BufferedReader(new FileReader(props.getProperty("chunkRules")));
	    String s;
	    chunkRules = new ArrayList<ChunkRule>();
	    while ((s = f.readLine()) != null) {
		ChunkRule r = new ChunkRule(s);
		chunkRules.add(r);
	    }
	    f.close();
	}
	System.err.println("OK");

	// read some text in the text variable
	BufferedReader in;
	if (!props.containsKey("input") || props.getProperty("input").equals("-")) 
	    in = new BufferedReader(new InputStreamReader(System.in));
	else 
	    in = new BufferedReader(new FileReader(props.getProperty("input")));

	String text="";
	String s;

	System.out.println("<?xml version='1.0' encoding='UTF-8' ?>");
//	if (!props.containsKey("noXSLT")) {
//	    System.out.println("<?xml-stylesheet type='text/xsl' href='profit.xsl'?>");
//	}
	int sent_count=1;
	System.out.println("<corpus>");

	prev_alloc=0;
	while ((s = in.readLine()) != null) {
	    text = s;

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	
	    // run all Annotators on this text
	    pipeline.annotate(document);
	
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	
	    for(CoreMap sentence: sentences) {
		Tree tree = sentence.get(TreeAnnotation.class);

		List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
		SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
		//SemanticGraph dependencies = sentence.get(CollapsedDependenciesAnnotation.class);
		//SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);

		String XML = toXML(dependencies, tokens);

		if (props.containsKey("trace") && props.containsKey("TraceFile") && !XML.equals("")) {
		    String TraceFile = props.getProperty("TraceFile") + (sent_count-1) + ".xml";
		    PrintStream out = new PrintStream(new FileOutputStream(TraceFile));
		    
		    out.println("<?xml version='1.0' encoding='UTF-8' ?>");
		    out.println("<?xml-stylesheet type='text/xsl' href='profit.xsl'?>");
		    out.println("<corpus>");
		    out.println("<SENTENCE ord='" + sent_count + "'>");
		    out.println(XML);
		    out.println("</SENTENCE>"); 
		    
		    out.close();
		}
		
		if (!XML.equals("")) {
		    //System.out.println("<!-- " + tree.toString() + " -->");
		    System.out.println("<SENTENCE ord='" + sent_count + "'>");
		    System.out.print(XML);
		    System.out.println("</SENTENCE>"); 
		    sent_count++;
		}
	    }

	    prev_alloc+=text.length()+1;
	}
	in.close();
	
	System.out.println("</corpus>");
    }

    public static String toXML (SemanticGraph dependencies, List<CoreLabel> tokens) {
	if (tokens.isEmpty()) {
	    return "";
	}

	out = new StringBuilder();           // not thread-safe!!!
	used = new HashSet<Integer>();
	for (IndexedWord root: dependencies.getRoots()) {
	    formatSGNode(dependencies, root, 4, "", "root", true);
	}

	for (CoreLabel token: tokens) {
	    IndexedWord node = new IndexedWord(token);
	    if (!used.contains(node.beginPosition())) {
		String rel ="root";
		String parent ="";
		String chunkType = is_chunk(parent, node.tag(), rel);
		out.append(StringUtils.repeat(" ", 4));
		out.append("<CHUNK ord='" + node.index() + "' alloc='" + (prev_alloc+node.beginPosition()) + "'");
		out.append(" type='" + xmlEncode(chunkType) + "' si='" + xmlEncode(rel) + "'>\n");
		out.append(StringUtils.repeat(" ", 4+indent));
		out.append(formatLabel(node, 0));
		used.add(node.beginPosition());
		out.append(StringUtils.repeat(" ", 4));
		out.append("</CHUNK>\n");
	    }
	}

	return out.toString();
    }

    private static void formatSGNode(SemanticGraph sg,
				     IndexedWord node,
				     int spaces,
				     String parent,
				     String rel,
				     boolean head) {
	boolean closeTag = false;
	String chunkType = is_chunk(parent, node.tag(), rel);

	List<SemanticGraphEdge> children = sg.getOutEdgesSorted(node);
	if (!used.contains(node.beginPosition())) {
	    if (!chunkType.equals("0")) {
		out.append(StringUtils.repeat(" ", spaces));
		out.append("<CHUNK ord='" + node.index() + "' alloc='" + (prev_alloc+node.beginPosition()) + "'"); //ord eta alloc ez dira ondo kalkulatzen, buruko nodotik irakurtzen dira...
		out.append(" type='" + xmlEncode(chunkType) + "' si='" + xmlEncode(rel) + "'>\n");
		spaces += indent;
		closeTag = true;
	    }

	    out.append(StringUtils.repeat(" ", spaces));
	    if (node.ner().equals("O")) {
		used.add(node.beginPosition());
		out.append(formatLabel(node, sg.getOutEdgesSorted(node).size()));
	    }
	    else {
		used.add(node.beginPosition());
		out.append(formatNE(sg, node, children));
	    }
	    //System.err.println(node.word() + " -> " + children.size());

	    for (SemanticGraphEdge depcy : children) {
		IndexedWord dep = depcy.getDependent();
		int sp = spaces + indent;
		
		String reln = depcy.getRelation().toString();
		
		if (!used.contains(dep.beginPosition()) && is_chunk(node.tag(), dep.tag(), reln).equals("0")) {
		    formatSGNode(sg, dep, sp, node.tag(), reln, false);
		}
	    }
	    
	    if (sg.getOutEdgesSorted(node).size() > 0) {
		out.append(StringUtils.repeat(" ", spaces));
		out.append("</NODE>\n");
	    }
	}

	if (head) {
	    for (SemanticGraphEdge depcy : children) {
		IndexedWord dep = depcy.getDependent();
		int sp = spaces;
		
		String reln = depcy.getRelation().toString();
		if (!used.contains(dep.beginPosition()) && !is_chunk(node.tag(), dep.tag(), reln).equals("0")) {
		    formatSGNode(sg, dep, sp, node.tag(), reln, true);
		}
	    }
	}

	if (closeTag) {
	    out.append(StringUtils.repeat(" ", spaces-indent));
	    out.append("</CHUNK>\n");
	}
    }

    private static String formatLabel(IndexedWord node, int childrenSize) {
	String s = "<NODE ord='" + node.index() + "'";
	s += " alloc='" + (prev_alloc+node.beginPosition()) + "'";
	s += " form='" + xmlEncode(node.word()) + "'";
	s += " lem='" + xmlEncode(node.lemma()) + "'";
	
	String tag = node.tag();
	if (tag != null && tag.length() > 0) {
	    s += " mi='" + xmlEncode(node.tag()) + "'";
	}

	if (childrenSize > 0) {
	    s += ">\n";
	}
	else {
	    s += "/>\n";
	}

	return s;
    }

    private static String formatNE(SemanticGraph sg, IndexedWord node, List<SemanticGraphEdge> children) {
	String ner = node.ner();
	String normNer = node.getString(NormalizedNamedEntityTagAnnotation.class);

	List<IndexedWord> NE=new ArrayList<IndexedWord>();
	NE.add(node);
	int i = 0;
	List<SemanticGraphEdge> childrenAux = sg.getOutEdgesSorted(NE.get(0));
	while (i<childrenAux.size()) {
	    IndexedWord dep = childrenAux.get(i).getDependent();
	    if (dep.ner().equals(ner) && dep.getString(NormalizedNamedEntityTagAnnotation.class).equals(normNer)) {
		NE.add(dep);
		childrenAux.addAll(sg.getOutEdgesSorted(dep));
	    }
	    i++;
	}
	Collections.sort(NE);

	String form=NE.get(0).word();
	String lemma=NE.get(0).lemma();
	int alloc=(prev_alloc+NE.get(0).beginPosition());
	int index=NE.get(0).index();
	int curIndex=index;
	used.add(NE.get(0).beginPosition());
	children.clear();
	children.addAll(sg.getOutEdgesSorted(NE.get(0)));

	i = 1;
	while (i < NE.size()) {
	    if (curIndex+1 == NE.get(i).index()) {
		form += "_" + NE.get(i).word();
		lemma += "_" + NE.get(i).lemma();
		used.add(NE.get(i).beginPosition());
		children.addAll(sg.getOutEdgesSorted(NE.get(i)));
	    }
	    else if (node.index() >= NE.get(i).index()) {
		form=NE.get(i).word();
		lemma=NE.get(i).lemma();
		alloc=(prev_alloc+NE.get(i).beginPosition());
		index=NE.get(i).index();
		children.clear();
		children.addAll(sg.getOutEdgesSorted(NE.get(0)));
		int j=0;
		while (j<i) {
		    used.remove(NE.get(j).beginPosition());
		    j++;
		}
		NE = NE.subList(i,NE.size());
		i=0;
	    }
	    else {
		NE = NE.subList(0,i);
		break;
	    }

	    curIndex=NE.get(i).index();
	    i++;
	}	

	String s = "<NODE ord='" + index + "'";
	s += " alloc='" + alloc + "'";
	s += " form='" + xmlEncode(form) + "'";
	s += " lem='" + xmlEncode(lemma) + "'";
	
	String tag = node.tag();
	if (tag != null && tag.length() > 0) {
	    s += " mi='" + xmlEncode(tag) + "'";
	}

	if (ner != null && ner.length() > 0) {
	    s += " NE='" + xmlEncode(ner)+ "'";
	}

	if (normNer != null && normNer.length() > 0) {
	    s += " normNE='" + xmlEncode(normNer)+ "'";
	}


	if (sg.getOutEdgesSorted(node).size() > 0) {
	    s += ">\n";
	}
	else {
	    s += "/>\n";
	}

	return s;
    }

    private static String is_chunk (String parent, String child, String rel) {
	// erregela gutxi eta kodean sartutakoak, MALT-ekoan fitxategi batetik irakurtzen dira
	for(ChunkRule rule: chunkRules) {
	    String type = rule.apply(parent,child,rel);
	    if (!type.equals("")) 
		return type;
	}

	return "0"; //defektuzko balioa ('0' -> ez da chunk-a, 'UNK' -> 'UNK' motatako chunk-a)
    }

    public static String xmlEncode(String s) {
	s = s.replaceAll("&", "&amp;");
	s = s.replaceAll("\"", "&quot;");
	s = s.replaceAll("'", "&apos;");
	s = s.replaceAll("<", "&lt;");
	s = s.replaceAll(">", "&gt;");
	
	return s;
    }
}
