package sparqlqueryAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.io.StringReader;
import java.lang.reflect.Array;

import javabean.GlobalVariable;
import javabean.MyTriples;

import javax.swing.text.html.HTMLDocument.Iterator;

import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class ParserDemo {
	private static int tdlSize = 0;
    private static int i1 = 0;
	private static boolean turnFlag = false;
	private static boolean containNn = false;
	private static List<String> subEntityList = new ArrayList<String>();
	private static List<String> relationList = new ArrayList<String>();
	private static List<String> objEntityList = new ArrayList<String>();
	private static List<TreeGraphNode> subnnList = new ArrayList<TreeGraphNode>();
	private static List<TreeGraphNode> objnnList = new ArrayList<TreeGraphNode>();


  /**
   * The main method demonstrates the easiest way to load a parser.
   * Simply call loadModel and specify the path of a serialized grammar
   * model, which can be a file, a resource on the classpath, or even a URL.
   * For example, this demonstrates loading from the models jar file, which
   * you therefore need to include in the classpath for ParserDemo to work.
   */
  public static void main(String[] args) {
    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    if (args.length > 0) {
      demoDP(lp, args[0]);
    } else {
      demoAPI(lp);
    }
  /* System.out.println(treeString);
   newtreeString =treeString.substring(1,treeString.length()-1); 
   System.out.println(newtreeString);
 */
  }

  /**
   * demoDP demonstrates turning a file into tokens and then parse
   * trees.  Note that the trees are printed by calling pennPrint on
   * the Tree object.  It is also possible to pass a PrintWriter to
   * pennPrint if you want to capture the output.
   */
  public static void demoDP(LexicalizedParser lp, String filename) {
    // This option shows loading, sentence-segmenting and tokenizing
    // a file using DocumentPreprocessor.
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    // You could also create a tokenizer here (as below) and pass it
    // to DocumentPreprocessor
    for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
      Tree parse = lp.apply(sentence);
      parse.pennPrint();
      System.out.println();

      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
      Collection tdl = gs.typedDependenciesCCprocessed();
      System.out.println(tdl);
      System.out.println();
    }
  }

  /**
   * demoAPI demonstrates other ways of calling the parser with
   * already tokenized text, or in some cases, raw text that needs to
   * be tokenized as a single sentence.  Output is handled with a
   * TreePrint object.  Note that the options used when creating the
   * TreePrint can determine what results to print out.  Once again,
   * one can capture the output by passing a PrintWriter to
   * TreePrint.printTree.
   */
  public static void demoAPI(LexicalizedParser lp) {
    // This option shows parsing a list of correctly tokenized words
  /*  String[] sent = { "This", "is", "an", "easy", "sentence", "." };
    List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
    Tree parse = lp.apply(rawWords);
    parse.pennPrint();
    System.out.println();*/
	  Scanner input = new Scanner(System.in);
	  String sent2 = input.nextLine();
	  input.close();
    // This option shows loading and using an explicit tokenizer
    //String sent2 = "Who is the mayor of Berlin?";
	//   String sent2 = "who found Intel?";
	//  String sent2 = "the baby is cute";
    TokenizerFactory<CoreLabel> tokenizerFactory =
        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
    Tokenizer<CoreLabel> tok =
        tokenizerFactory.getTokenizer(new StringReader(sent2));
    List<CoreLabel> rawWords2 = tok.tokenize();
    Tree parse = lp.apply(rawWords2);

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
    System.out.println(tdl);
    System.out.println();
    // //relation.dep谓词 relation.gov 实体 relatio.reln依赖关系,前面是gov,后面是dep
    tdlSize = tdl.size();
    int i0 = 0;
    for(i0 = 0;i0<tdlSize;i0++){
    	if(tdl.get(i0).reln().toString().equals("root")) {
    		break;
    	}
    }
    i1 = tdl.get(i0).dep().index();
    for(int j = 0;j<tdlSize;j++) {
    	if(tdl.get(j).reln().toString().equals("cop") && tdl.get(j).gov().value().equals(tdl.get(i0).dep().value())) {
    		turnFlag = true;
    		break;
    	}
    }
    //extraction relation
    for (int i = 0; i < tdl.size(); i++) {
    	relExtraction(tdl.get(i));
    }
    // deal with nnList
    dealWithNnList(subnnList,1);
    dealWithNnList(objnnList,2);
    
    System.out.println("subject:");
    for(java.util.Iterator<String> j1 = subEntityList.iterator();j1.hasNext(); ){
    	//String str = (String) j.next();
    	System.out.println(j1.next());
    	}
   
    System.out.println("relation:");
    for(java.util.Iterator<String> j2 = relationList.iterator();j2.hasNext(); ){
    	//String str = (String) j.next();
    	System.out.println(j2.next());
    	}
    
    System.out.println("object:");
    for(java.util.Iterator<String> j1 = objEntityList.iterator();j1.hasNext(); ){
    	//String str = (String) j.next();
    	System.out.println(j1.next());
    	}
    GlobalVariable.setSubEntityList(subEntityList);
    GlobalVariable.setRelationList(relationList);
    GlobalVariable.setObjEntityList(objEntityList);
    // You can also use a TreePrint object to print trees and dependencies
    /*TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    tp.printTree(parse);*/

    int maxLen = subEntityList.size()>objEntityList.size()?subEntityList.size():objEntityList.size();
	MyTriples[] myTriples = new MyTriples[maxLen];
	for(int i = 0;i<maxLen;i++) {
		myTriples[i] = new MyTriples();
		myTriples[i].setRelation(relationList.get(i));
		myTriples[i].setSubject(subEntityList.get(i));
		myTriples[i].setObject(objEntityList.get(i));
	}
	for(int i = 0;i<maxLen;i++) {
		System.out.println(myTriples[i].getSubject()+"\t"+myTriples[i].getRelation()+"\t"+myTriples[i].getObject());
	}
  }
  private static void dealWithNnList(List<TreeGraphNode> nnList,int subOrobj) {
	// TODO Auto-generated method stub
	  if(!nnList.isEmpty()) {
	    	int[] flag = new int[nnList.size()];
	    	String ss = "";
			for(int kk = 0;kk<nnList.size();kk++) {
				flag[kk] = nnList.get(kk).index();
		//		System.out.println(flag[kk]);
				//System.out.println(nnList.size());
			}
			java.util.Arrays.sort(flag);
			String[] arr = new String[20];
			for(int k=0;k<nnList.size();k++) {
				for(int kk = 0;kk<nnList.size();kk++) {
					if(nnList.get(kk).index()==flag[k]) {
						arr[k] = nnList.get(kk).value();
						break;
					}
				}
			}
			for(int kk = 0;kk<nnList.size()-1;kk++) {
				ss = ss+arr[kk]+"_";
			}
			ss = ss+arr[nnList.size()-1];
			if(subOrobj==1) {
				if(!subEntityList.contains(ss)) {
					subEntityList.add(ss);
				}
			}else if(subOrobj ==2) {
				if(!objEntityList.contains(ss)) {
					objEntityList.add(ss);
				}
			}
		 }
}

private ParserDemo() {} // static methods only
  
public static void relExtraction(TypedDependency str) {
	 if(str.reln().toString().equals("csubj")||str.reln().toString().equals("csubjpass")||str.reln().toString().equals("rcmod")) {
		 if(!objEntityList.contains(str.gov().value())) {
		 objEntityList.add(str.gov().value());
		 }
		 if(!relationList.contains(str.dep().value())) {
			 relationList.add(str.dep().value());
		 }
	 } else if(str.reln().toString().equals("dobj") || str.reln().toString().equals("iobj") || str.reln().toString().equals("pobj")||str.reln().toString().equals("xcomp")||str.reln().toString().equals("nsubjpass")) {
		 if(!objEntityList.contains(str.dep().value())) {
			 objEntityList.add(str.dep().value());
			 }
			 if(!relationList.contains(str.gov().value())) {
				 relationList.add(str.gov().value());
			 }
	 } else if(str.reln().toString().equals("neg")||str.reln().toString().equals("nn")||str.reln().toString().equals("npadvmod") ||str.reln().toString().equals("num") ||str.reln().toString().equals("number")) {
		 if(str.dep().index()< i1) {
			 if(!subnnList.contains(str.dep())) {
				 subnnList.add(str.dep());
			 }
			 if(!subnnList.contains(str.gov())) {
				 subnnList.add(str.gov());
			 }
		 } else {
			 if(!objnnList.contains(str.dep())) {
				 objnnList.add(str.dep());
			 }
			 if(!objnnList.contains(str.gov())) {
				 objnnList.add(str.gov());
			 }
		 } 
		 containNn = true;
	 } else if(str.reln().toString().equals("nsubj")||str.reln().toString().equals("xsubj")||str.reln().toString().equals("agent")) {
		 if(turnFlag){
			 if(!subEntityList.contains(str.gov().value())) {
				 subEntityList.add(str.gov().value());
			 }
			 if(!relationList.contains(str.dep().value())) {
				 relationList.add(str.dep().value());
			 }
		 } else {
			 if(!subEntityList.contains(str.dep().value())) {
				 subEntityList.add(str.dep().value());
			 }
			 if(!relationList.contains(str.gov().value())) {
				 relationList.add(str.gov().value());
			 }
		 }
	 } else if (str.reln().toString().equals("prt")) {
		 String s = str.gov().value()+"_"+str.dep().value();
		 if(!relationList.contains(s)) {
			 relationList.add(s);
		 }
	 } else if(str.reln().toString().equals("advmod")) {
		 String s = str.dep().value()+"_"+str.gov().value();
		 if(!subEntityList.contains(s)) {
			 subEntityList.add(s);
		 }
	 }
	 
	 if(str.reln().toString().length()>4) {
		if(str.reln().toString().substring(0,4).toString().equals("prep")) {
			if(!objEntityList.contains(str.dep().value())&& containNn==false ) {
	 			objEntityList.add(str.dep().value());
			}

			if(!relationList.contains(str.gov().value())){
				String s  = str.gov().value() + str.reln().toString().substring(str.reln().toString().length()-3,str.reln().toString().length());
				
				relationList.add(s);
			} else {
				for(int l = 0;l<relationList.size();l++) {
					if(relationList.get(l).equals(str.gov().value())) {
						relationList.remove(l);
					}
				}
				String s  = str.gov().value() + str.reln().toString().substring(str.reln().toString().length()-3,str.reln().toString().length());
				relationList.add(s);
			}
		}
	 }
 }
}
