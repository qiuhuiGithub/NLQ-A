package sparqlqueryAction;
import jgsc.GstoreConnector;
import java.io.*;
import java.util.*;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.HTMLDocument.Iterator;

import sun.rmi.runtime.Log;
import sparqlqueryAction.ValueComparator;

import javabean.MyTriples;
import javabean.GlobalVariable;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
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
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 
import edu.sussex.nlp.jws.AdaptedLesk;
import edu.sussex.nlp.jws.AdaptedLeskTanimoto;
import edu.sussex.nlp.jws.AdaptedLeskTanimotoNoHyponyms;
import edu.sussex.nlp.jws.HirstAndStOnge;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.JiangAndConrath;
import edu.sussex.nlp.jws.LeacockAndChodorow;
import edu.sussex.nlp.jws.Lin;
import edu.sussex.nlp.jws.Path;
import edu.sussex.nlp.jws.Resnik;
import edu.sussex.nlp.jws.WuAndPalmer;

@SuppressWarnings("serial")
public class sparqlqueryAction extends ActionSupport {
	private static String dir = "/usr/local/WordNet-3.0";
     	private static JWS    ws = new JWS(dir, "3.0");
     
	private static int tdlSize = 0;
    	private static int i1 = 0;
	private static boolean turnFlag = false;
	private static boolean containNn = false;
	private static List<String> subEntityList = new ArrayList<String>();
	private static List<String> relationList = new ArrayList<String>();
	private static List<String> objEntityList = new ArrayList<String>();
	
	private  List<String> subSupportList = new ArrayList<String>();
	private  List<String> relSupportList = new ArrayList<String>();
	private  List<String> objSupportList = new ArrayList<String>();
	private  List<String> relPrepList = new ArrayList<String>(); 
    	private ArrayList<String> sparqlList = new ArrayList<String>();
	
	private static List<TreeGraphNode> subnnList = new ArrayList<TreeGraphNode>();
	private static List<TreeGraphNode> objnnList = new ArrayList<TreeGraphNode>();
	private List<MyTriples> tripleList = new ArrayList<MyTriples>();
	private String ret = ERROR;//query error;
	private String sparqlString;
	private int queryType;
	private String answer;
	private String answerNumber;
	
	private static List<Double>  subScore = new ArrayList<Double>();
	private static List<Double> relScore = new ArrayList<Double>() ;
	private static List<Double> objScore = new ArrayList<Double>();
	private static List<Double> sparqlScore = new ArrayList<Double>();
	Map<String,Double> scoreOfsparql = new LinkedHashMap<String,Double>();
	ValueComparator bvc =  new ValueComparator(scoreOfsparql);  
	TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);  

	public String execute(){
		System.out.println("查询类型"+queryType);
		System.out.print("查询语句"+sparqlString);
		init();
		if(queryType ==1) {
		try{
				//if(queryType ==1){
					    // initialize the GStore server's IP address and port.
						GstoreConnector gc = new GstoreConnector("127.0.0.1", 3305);
				
							// build a new database by a RDF file.
							// note that the relative path is related to gserver.
							gc.build("LUBM10.db", "data/LUBM_10.n3");
								
								// then you can execute SPARQL query on this database.
								/*	String sparql = "select ?x where "
							+ "{"
							+ "?x    <rdf:type>    <ub:UndergraduateStudent>. "
							+ "?y    <ub:name> <Course1>. "
							+ "?x    <ub:takesCourse>  ?y. "
							+ "?z    <ub:teacherOf>    ?y. "
							+ "?z    <ub:name> <FullProfessor1>. "
							+ "?z    <ub:worksFor>    ?w. "
							+ "?w    <ub:name>    <Department0>. "
							+ "}";				*/
								String sparql = sparqlString;
								String queryanswer = gc.query(sparql);
								System.out.println(queryanswer);
								if(queryanswer.charAt(0)!='['){
									String[] array= new String[10];
									array=queryanswer.split("\\?x");
									answerNumber = array[0];
									answer = array[1];
									System.out.print(answerNumber);
									System.out.print(answer);
								}else{
									answer = "[empty result]";
									answerNumber = "0";
								}
								// unload this database.
								gc.unload("LUBM10.db");
								ret = SUCCESS;
				//}
		}catch(Exception e ){
			e.printStackTrace();
			ret = ERROR;
			}
		}else if (queryType ==2) {
			try{
				LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
				demoAPI(lp);
				generateSparql();
				ret = "transuccess";
			}catch(Exception e) {
				e.printStackTrace();
				ret = "tranerror";
			}
		}
		return ret;
	}
	
	private void init() {
		// TODO Auto-generated method stub
		tdlSize = 0;
	    	i1 = 0;
		turnFlag = false;
		containNn = false;
		subEntityList.clear();
		relationList.clear();
		objEntityList.clear();
		subSupportList.clear();
		relSupportList.clear();
		objSupportList.clear();
		relPrepList.clear();
	    	sparqlList.clear();
		subnnList.clear();
		objnnList .clear();
		tripleList.clear();
	}

	private void generateSparql()  throws IOException{
		// TODO Auto-generated method stub
	       String wnhome = System.getenv("WNHOME"); //获取WordNet根目录环境变量WNHOME
	       String path = wnhome + File.separator+ "dict";       
	       File wnDir=new File(path);
	       URL url=new URL("file", null, path);
	       IDictionary dict=new Dictionary(url);
	       dict.open();//打开词典
	       int supportSize = subEntityList.size();
	       String subCandi,relCandi,objCandi;
	       ArrayList<String> supTripleList1 = new ArrayList<String>();
	       ArrayList<String> supTripleList2 = new ArrayList<String>();
	       ArrayList<String> supTripleList3 = new ArrayList<String>();
	       ArrayList<String> subCloneList = new ArrayList<String>();
	       ArrayList<String> relCloneList = new ArrayList<String>();
	       ArrayList<String> objCloneList = new ArrayList<String>();
	       String lemma;
	       if(supportSize==1){
	    	   subSupportList.add(subEntityList.get(0));
	    	   subScore.add(1.0);
	    	   relCandi = relationList.get(0);
	    	   String tmp1  = relCandi;
	     	  if(relCandi.charAt(relCandi.length()-3)=='_'){
	        		relCandi = relCandi.substring(0,relCandi.length()-3);
	        		lemma = lemmatization(relCandi);
	        //	System.out.println(str);
	        		getSynonyms(dict,lemma,2); //testing
	        		getHypernyms(dict);//testing
	        		String rel;
	        		for(int i=0;i<relSupportList.size();i++){
	        			rel =relSupportList.get(i)+tmp1.substring(tmp1.length()-2,tmp1.length()-1).toUpperCase()+tmp1.substring(tmp1.length()-1,tmp1.length());
	        			relPrepList.add(rel);
	        		}
	        	}else {
	        		lemma = lemmatization(relCandi);
	        		getSynonyms(dict,lemma,2); //testing
	        		getHypernyms(dict);//testing
	        		String rel;
	        		for(int i=0;i<relSupportList.size();i++){
	        			rel =relSupportList.get(i);
	        			relPrepList.add(rel);
	        		}
	        	}
<<<<<<< HEAD
	     	 System.out.println(relPrepList);
	     	 System.out.println(lemma+""+relSupportList.get(0));
	     	 System.out.println("wocao");
	     	  for(int index = 0;index<relSupportList.size();index++){
	     		  double score = getSimilarity(lemma,relSupportList.get(index))*Math.pow(0.9, index);
	     		  relScore.add(score);
	     	  }
	     	 for(int index = 0;index<relSupportList.size();index++){
	  
	     		  System.out.print(relScore.get(index));
	     	  }
	        		 getSynonyms(dict,objEntityList.get(0),3); //testing
	        	     getHypernyms(dict);//testing
	        	     for(int index = 0;index<objSupportList.size();index++){
	   	     		  double score = getSimilarity(objEntityList.get(0),objSupportList.get(index))*Math.pow(0.9, index);
	   	     		System.out.println(score);
	   	     		  objScore.add(score);   	     		
	   	     	  }
	        	     System.out.println(objScore);
	        	  //  ArrayList<String> sparqlList = new ArrayList<String>();
	        	       int relLen = relPrepList.size();
	        	       int objLen = objSupportList.size();
	        	       String rel1,obj1;
	        	       double alpha;
	        	       double max = 0.4,min = 0.3;
	        	       Random random = new Random();
	        	       alpha = random.nextDouble()%(max-min) + min;
	        	       for(int i =0;i<relLen;i++) {
	        	    	   for(int j =0;j<objLen;j++) {
	        	    		   //map2.put(relSupportList.get(i), map.get(relSupportList.get(i)));
=======
	     	System.out.println(lemma+""+relSupportList.get(0));
	     	System.out.println("wocao");
	     	for(int index = 0;index<relSupportList.size();index++){
	     	  double score = getSimilarity(lemma,relSupportList.get(index))*Math.pow(0.9, index);
	     	  relScore.add(score);
	     	}
	     	for(int index = 0;index<relSupportList.size();index++){
	     	  System.out.print(relScore.get(index));
	     	}
	        getSynonyms(dict,objEntityList.get(0),3); //testing
	        getHypernyms(dict);//testing
	        for(int index = 0;index<objSupportList.size();index++){
	      	   double score = getSimilarity(objEntityList.get(0),objSupportList.get(index))*Math.pow(0.9, index);
	   	   System.out.print(score);
	   	   objScore.add(score);   	     		
	   	}
		 //  ArrayList<String> sparqlList = new ArrayList<String>();
	        int relLen = relPrepList.size();
	        int objLen = objSupportList.size();
	        String rel1,obj1;
	        double alpha;
	        double max = 0.4,min = 0.3;
	        Random random = new Random();
	        alpha = random.nextDouble()%(max-min) + min;
	        for(int i =0;i<relLen;i++) {
	            		for(int j =0;j<objLen;j++) {
>>>>>>> f8b36cc4357c571ce803b0188e01450d294bf96a
	        	    		   rel1 ="<ub:"+relPrepList.get(i)+">";
	        	    		   obj1 = "<"+objSupportList.get(j)+">.";
	        	    		   String sparql = "select ?x where { "+"?x  " +rel1+"  "+obj1+"  }";
	        	    		   double score1  =alpha*relScore.get(i)+(1-alpha)*objScore.get(j);
	        	    		   sparqlScore.add(score1);
	        	    		   sparqlList.add(sparql);
	        	    		   scoreOfsparql.put(sparql, score1);
	        	    	   }
	        	       }
	        	       sorted_map.putAll(scoreOfsparql);  
	        	       System.out.println( sorted_map);
	        	       System.out.println( sparqlScore);
	        	       Map<String, Double> map1 =new HashMap<String, Double>();
	        		      int mapSize=0;
	        		      if(sorted_map.size()>3){
	        		    	  for (Map.Entry<String, Double> entry : sorted_map.entrySet()) {
	        			    	  mapSize++;
	        			           System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	        			           if(mapSize>3)
	        		    			   break;
	        		               map1.put(entry.getKey() , entry.getValue() );
	        			          }
	        		    	  sorted_map.clear();
		        		      sorted_map.putAll(map1);
	        		      }
	        		     
	        	      /* for(java.util.Iterator<String> k = sparqlList.iterator();k.hasNext(); ){
	        	        	String str = k.next();
	        	        	System.out.println(str);
	        	     }
	        	  
	        	       for(java.util.Iterator<Double> k = sparqlScore.iterator();k.hasNext(); ){
	        	        	double str = k.next();
	        	        	System.out.println(str);
	        	     }*/
		        	       System.out.println( sorted_map);
	        	       
	       }else{
	    	   double alpha;
		   double max = 0.4,min = 0.3;
		   Random random = new Random();
		   alpha = random.nextDouble()%(max-min) + min;
	      for(int i0 = 0;i0<supportSize;i0++){
	    	  subCandi = subEntityList.get(i0);
	    	  relCandi = relationList.get(i0);
	    	  objCandi = objEntityList.get(i0);
	    	  String tmp  = relCandi;
	    	  if(relCandi.charAt(relCandi.length()-3)=='_'){
	       		relCandi = relCandi.substring(0,relCandi.length()-3);
	       		lemma = lemmatization(relCandi);
	       	   	System.out.println(relCandi);
	       		getSynonyms(dict,lemma,2); //testing
	       		getHypernyms(dict);//testing
	       		String rel;
	       		for(int i=0;i<relSupportList.size();i++){
	       			rel =relSupportList.get(i)+tmp.substring(tmp.length()-2,tmp.length()-1).toUpperCase()+tmp.substring(tmp.length()-1,tmp.length());
	       			relPrepList.add(rel);
	       		}
	       	}else {
	       		lemma = lemmatization(relCandi);
	       		getSynonyms(dict,lemma,2); //testing
	       		getHypernyms(dict);//testing
	       		String rel;
	       		for(int i=0;i<relSupportList.size();i++){
	       			rel =relSupportList.get(i);
	       			relPrepList.add(rel);
	       		}
	      }

	      getSynonyms(dict,objCandi,3); //testing
	      getHypernyms(dict);//testing
	      

	      int relLen = relPrepList.size();
	      int objLen = objSupportList.size();
	      String rel,obj;

	      if(i0==0){
	    	  for(int i =0;i<relLen;i++) {
	    		  rel = "<ub:"+relPrepList.get(i)+">";
	    		  String supTriple1 ="?x  "+ rel +"  "+"?y.  ";
	    		  supTripleList1.add(supTriple1);
	    		  subScore.add(getSimilarity(relSupportList.get(i),lemma)*Math.pow(0.9, i));
	    	  }
	    	  for(int j =0;j<objLen;j++) {
	    		  //map2.put(relSupportList.get(i), map.get(relSupportList.get(i)));
	    		  obj = "<"+objSupportList.get(j)+">. ";
	    		  String supTriple2 ="?y  "+"<rdf:type>  "+obj;
	    		  //  String sparql = "select ?x where { "+"?x  " +rel+"  "+obj+"  }";
	    		  supTripleList2.add(supTriple2);
	    		  relScore.add(getSimilarity(objSupportList.get(j),objCandi)*Math.pow(0.9, j));
	    	  }
	      }else{
	    	  for(int i =0;i<relLen;i++) {
	       	   for(int j =0;j<objLen;j++) {
	       		   //map2.put(relSupportList.get(i), map.get(relSupportList.get(i)));
	       		   rel = "<ub:"+relPrepList.get(i)+">";
	       		   obj = "<"+objSupportList.get(j)+">";
	       		   String supTriple = "?y  "+rel+"  "+obj+". ";
	       		   supTripleList3.add(supTriple);
	       		   objScore.add(alpha*getSimilarity(relSupportList.get(i),lemma)*Math.pow(0.9, i)+(1-alpha)*getSimilarity(objSupportList.get(j),objCandi)*Math.pow(0.9, j));
	       	   }
	          }
	      }
    	  if(i0==0){
    		  subCloneList.add(subEntityList.get(0));
    	  }else {
    		  subCloneList.addAll(objCloneList);
    	  }
    	  relCloneList.addAll(relPrepList);
    	  objCloneList.addAll(objSupportList);
    	  initSup();
	      }
	      subSupportList.addAll(subCloneList);
	      relPrepList.addAll(relCloneList);
	      objSupportList.addAll(objCloneList);
	      System.out.println(subCloneList);
	      System.out.println(subCloneList.size());
	      System.out.println(relCloneList);
	      System.out.println(objCloneList);
	      //ArrayList<String> sparqlList = new ArrayList<String>();

	      for(int i = 0;i<supTripleList1.size();i++){
	    	  for(int j =0;j<supTripleList2.size();j++){
	    		  for(int k = 0;k<supTripleList3.size();k++){
	    			  String sparql = "select ?x where{ "+supTripleList1.get(i)+supTripleList2.get(j)+supTripleList3.get(k)+" }";
	    			  double score1  = alpha*subScore.get(i)+(1-alpha)*relScore.get(j)+objScore.get(k);
	    			  sparqlList.add(sparql);
	    			  scoreOfsparql.put(sparql, score1);
	    		  }
	    	  }
	      }
	      sorted_map.putAll(scoreOfsparql);  
	      Map<String, Double> map1 =new HashMap<String, Double>();
	      int mapSize=0;
	      if(sorted_map.size()>3){
	    	  for (Map.Entry<String, Double> entry : sorted_map.entrySet()) {
		    	  mapSize++;
		           System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		           if(mapSize>3)
	    			   break;
	               map1.put(entry.getKey() , entry.getValue() );
		          }
	    	  sorted_map.clear();
		      sorted_map.putAll(map1);
	      }
	      
	    /*  Map<String, Double> map1 =new HashMap<String, Double>();
	      int mapSize=0;
	       if(sorted_map.size()>3){
	    	   for (String key : sorted_map.keySet()) {
	    		   mapSize++;
	    		   if(mapSize>3)
	    			   break;
	               map1.put(key, sorted_map.get(key));
	              }
	       }
	      sorted_map.putAll(map1);*/
	      System.out.println(sorted_map);
	       }
	}

	 

	private double getSimilarity(String str1, String str2) {
		// TODO Auto-generated method stub
		 String[] strs1 = splitString(str1);
         String[] strs2 = splitString(str2);
         double sum = 0.0;
         for(String s1 : strs1){
             for(String s2: strs2){
                 //double sc= maxScoreOfLin(s1,s2); //mark
            	 //double sc= maxScoreOfJcn(s1,s2);//mark
            	 //double sc= maxScoreOfLesk(s1,s2);//no answer
            	 //double sc= maxScoreOfTanimoto(s1,s2);//no answer
            	 //double sc= maxScoreOfHas(s1,s2);//mark
            	//double sc= maxScoreOfLcd(s1,s2);//mark
            	 double sc = maxScoreOfPath(s1,s2);//mark,tending to choose this
            	 //double sc = maxScoreOfRes(s1,s2);//mark
            	 //double sc = maxScoreOfWpm(s1,s2);//mark
            	 //double sc = maxScoreOfTnh(s1,s2);//no answer
                 sum+= sc;
                 System.out.println("当前计算: "+s1+" VS "+s2+" 的相似度为:"+sc);
             }
         }
         double Similarity = sum /(strs1.length * strs2.length);
         sum=0;
         return Similarity;
	}

	private String lemmatization(String text) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		 Properties props = new Properties(); 
		 props.put("annotators", "tokenize, ssplit, pos, lemma"); 
		 StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
		 Annotation document = pipeline.process(text); 
		 CoreMap sentence = document.get(SentencesAnnotation.class).get(0);
		 CoreLabel token = sentence.get(TokensAnnotation.class).get(0);
		 String lemma = token.get(LemmaAnnotation.class);
		 System.out.println("lemmatized version :" + lemma);
		 return lemma;
	}

	private void initSup() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				subSupportList.clear();
				relSupportList.clear();
				objSupportList.clear();
				relPrepList.clear();
	}

	private void getSynonyms(IDictionary dict,String str,int type) {
	       // look up first sense of the word "go"
	    	IIndexWord idxWord1 =dict.getIndexWord(str, POS.NOUN);
	    	IIndexWord idxWord2 =dict.getIndexWord(str, POS.VERB);
	    	IIndexWord idxWord = null;
	    	if(idxWord1 == null &&idxWord2 ==null) {
	    		if(type==1){
	    			subSupportList.add(str);
	    		}else if(type ==2){
	    			relSupportList.add(str);
	    		}else{
	    			objSupportList.add(str);
	    		}
	    		return;
	    	} else if(idxWord2 ==null) {
	    		idxWord = idxWord1;
	    	} else if(idxWord1 ==null){
	    		idxWord = idxWord2;
	    	} else{
	    		int wordCount1 = idxWord1.getWordIDs().size();
	        	int wordCount2 = idxWord2.getWordIDs().size();
	        	idxWord = wordCount1>wordCount2?idxWord1:idxWord2;
	    	}
	    	/* System.out.println(idxWord.getWordIDs().size());
	         int nodeWidth = idxWord.getWordIDs().size();
	         int matchLen = nodeWidth>3?3:nodeWidth;
	         //List<String> list = new ArrayList<String>();
	         for(int in =0;in<matchLen;in++){
	      	   IWordID wordID = idxWord.getWordIDs().get(in) ; // 1st meaning
	             IWord word = dict.getWord(wordID);
	             ISynset synset = word.getSynset (); //ISynset是一个词的同义词集的接口

	             if(type ==1) {
	            	 if(!subSupportList.contains(synset.getWords().get(0).getLemma())&&!subSupportList.contains(change(synset.getWords().get(0).getLemma()))){
	            		 subSupportList.add(synset.getWords().get(0).getLemma());
	    	             }else{
	    	            	 if(synset.getWords().size()>1)
	    	            	 subSupportList.add(synset.getWords().get(1).getLemma());
	    	             }
		             }else if(type==2){
		            	 if(!relSupportList.contains(synset.getWords().get(0).getLemma())&&!relSupportList.contains(change(synset.getWords().get(0).getLemma()))){
		            		 relSupportList.add(synset.getWords().get(0).getLemma());
		    	             }else{
		    	            	 if(synset.getWords().size()>1)
		    	            	 relSupportList.add(synset.getWords().get(1).getLemma());
		    	             }
		             }else{
		            	 if(!objSupportList.contains(synset.getWords().get(0).getLemma())&&!objSupportList.contains(change(synset.getWords().get(0).getLemma()))){
		            		 objSupportList.add(synset.getWords().get(0).getLemma());
		    	             }else{
		    	            	 if(synset.getWords().size()>1)
		    	            	 objSupportList.add(synset.getWords().get(1).getLemma());
		    	             }
		             }
	         System.out.println(subSupportList);
	         System.out.println(relSupportList);
	         System.out.println(objSupportList);
	      }*/
	        IWordID wordID = idxWord.getWordIDs().get(0) ; // 1st meaning
	        IWord word = dict.getWord(wordID);
	        ISynset synset = word.getSynset (); //ISynset是一个词的同义词集的接口
	        List<IWord> words = new ArrayList<IWord>();
	        words = synset.getWords();
	        int nodeWidth = words.size();
	        int matchLen = nodeWidth>3?3:nodeWidth;
	        if(type ==1) {
	        	 for(int i = 0;i<matchLen;i++) {
	             	subSupportList.add(words.get(i).getLemma());
	             }
	             for(java.util.Iterator<String> j2 = subSupportList.iterator();j2.hasNext();){
	             	System.out.println(j2.next());
	             }
	        } else if(type==2) {
	        	for(int i = 0;i<matchLen;i++) {
	             	relSupportList.add(words.get(i).getLemma());
	             }
	             for(java.util.Iterator<String> j2 = relSupportList.iterator();j2.hasNext();){
	             	System.out.println(j2.next());
	             }
	        } else {
	        	for(int i = 0;i<matchLen;i++) {
	             	objSupportList.add(words.get(i).getLemma());
	             }
	             for(java.util.Iterator<String> j2 = objSupportList.iterator();j2.hasNext();){
	             	System.out.println(j2.next());
	             }
	        }
	       
	        // iterate over words associated with the synset
	        for(IWord w : synset.getWords()) {
	        	 System.out.println(w.getLemma());//打印同义词集中的每个同义词
	        	 nodeWidth++;
	        }
	        
	        System.out.println("nodeWidth"+" "+nodeWidth);
	     }
	
	private String change(String s){
		String ss;
		if(s.charAt(0)>='a'&&s.charAt(0)<='z'){
			ss = s.substring(0,1).toUpperCase()+s.substring(1,s.length());
		}else{
			ss = s.substring(0,1).toLowerCase()+s.substring(1,s.length());
		}
		return ss;
	}
	
	    private void getHypernyms(IDictionary dict){
	    	 
	        //get synset
	        IIndexWord idxWord = dict.getIndexWord("article", POS.NOUN);//获取article的IndexWord
	        IWordID wordID = idxWord.getWordIDs().get(0); //取出第一个词义的词的ID号
	        IWord word = dict.getWord(wordID); //获取词
	        ISynset synset = word.getSynset(); //获取该词所在的Synset
	  
	     // 获取hypernyms
	      /*  List<ISynsetID> hypernyms =synset.getRelatedSynsets(Pointer.HYPERNYM );//通过指针类型来获取相关的词集，其中Pointer类型为HYPERNYM
	        // print out each hypernyms id and synonyms
	        List <IWord > words ;
	        for( ISynsetID sid : hypernyms ){
	            words = dict.getSynset(sid).getWords();  //从synset中获取一个Word的list
	            System.out.print(sid + "{");
	            for( Iterator<IWord > i = words.iterator(); i.hasNext();){
	               System.out.print(i.next().getLemma ());
	               if(i. hasNext ()){
	                   System.out.print(", ");
	               }
	            }
	            System .out . println ("}");
	        }*/
	        List<ISynsetID> hypernyms =synset.getRelatedSynsets(Pointer.HYPERNYM );//通过指针类型来获取相关的词集，其中Pointer类型为HYPERNYM
	        int nodeDepth = 0;
	        while(hypernyms.size()>0) {
	        nodeDepth++;
	        ISynsetID sid = hypernyms.get(0);
	        word = dict.getSynset(sid).getWords().get(0);
	        synset = word.getSynset();
	        hypernyms =synset.getRelatedSynsets(Pointer.HYPERNYM );
	        }
	        System.out.println("nodeDepth"+" "+nodeDepth);
	     }
	
	private void demoAPI(LexicalizedParser lp) {
		// TODO Auto-generated method stub
		String sent2 = sparqlString;
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
				MyTriples  tmp = new MyTriples();
				tmp.setRelation(relationList.get(i));
				tmp.setSubject(subEntityList.get(i));
			    tmp.setObject(objEntityList.get(i));
			    tripleList.add(tmp);
			}
			tripleList.toArray();
			
			for(int i = 0;i<maxLen;i++) {
				System.out.println(myTriples[i].getSubject()+"\t"+myTriples[i].getRelation()+"\t"+myTriples[i].getObject());
			}
			
	}

	private  void dealWithNnList(List<TreeGraphNode> nnList,int subOrobj) {
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

	private void relExtraction(TypedDependency str) {
		 if(str.reln().toString().equals("csubj")||str.reln().toString().equals("csubjpass")||str.reln().toString().equals("rcmod")) {
			 if(!objEntityList.contains(str.gov().value())) {
			 objEntityList.add(str.gov().value());
			 }
			 if(!relationList.contains(str.dep().value())) {
				 relationList.add(str.dep().value());
			 }
		 } else if(str.reln().toString().equals("dobj") || str.reln().toString().equals("iobj") || str.reln().toString().equals("pobj")||str.reln().toString().equals("xcomp")) {
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
		 } else if(str.reln().toString().equals("nsubj")||str.reln().toString().equals("nsubjpass")||str.reln().toString().equals("xsubj")||str.reln().toString().equals("agent")) {
			 if(str.gov().index()==i1&&turnFlag){
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
     private   String[] splitString(String str){
         String[] ret = str.split(" ");
         return ret;
     }
     //1.Lin
     private  double maxScoreOfLin(String str1,String str2){
         Lin lin = ws.getLin();
         double sc = lin.max(str1, str2, "n");
         if(sc==0){
             sc = lin.max(str1, str2, "v");
         }
         return sc;
     }
     //2.Jcn
     private  double maxScoreOfJcn(String str1,String str2){
    	 JiangAndConrath jcn = ws.getJiangAndConrath();
         double sc = jcn.max(str1, str2, "n");
         if(sc==0){
             sc = jcn.max(str1, str2, "v");
         }
         return sc;
     }
     //3.Lesk
     private  double maxScoreOfLesk(String str1,String str2) {
    	  AdaptedLesk lesk = ws.getAdaptedLesk();
    	  double sc = lesk.max(str1, str2, "n");
    	  if(sc == 0 ){
    		  sc = lesk.max(str1, str2, "v");
    	  }
    	  return sc;
     }
     //4.Tanimoto
     private  double maxScoreOfTanimoto(String str1,String str2) {
   	  AdaptedLeskTanimoto tanimoto = ws.getAdaptedLeskTanimoto();
   	  double sc = tanimoto.max(str1, str2, "n");
   	  if(sc == 0 ){
   		  sc = tanimoto.max(str1, str2, "v");
   	  }
   	  return sc;
    }
     //5.Has
     private  double maxScoreOfHas(String str1,String str2) {
    	 HirstAndStOnge has = ws.getHirstAndStOnge();
      	  double sc = has.max(str1, str2, "n");
      	  if(sc == 0 ){
      		  sc = has.max(str1, str2, "v");
      	  }
      	  return sc;
       }
     //6.Lcd
     private  double maxScoreOfLcd(String str1,String str2) {
    	 LeacockAndChodorow lcd = ws.getLeacockAndChodorow();
      	  double sc = lcd.max(str1, str2, "n");
      	  if(sc == 0 ){
      		  sc = lcd.max(str1, str2, "v");
      	  }
      	  return sc;
       }
     //7.Path
     private double maxScoreOfPath(String str1,String str2) {
    	   Path path = ws.getPath();
      	  double sc = path.max(str1, str2, "n");
      	  if(sc == 0 ){
      		  sc = path.max(str1, str2, "v");
      	  }
      	  return sc;
       }
     //8.Res
     private  double maxScoreOfRes(String str1,String str2) {
  	   Resnik res = ws.getResnik();
    	  double sc = res.max(str1, str2, "n");
    	  if(sc == 0 ){
    		  sc = res.max(str1, str2, "v");
    	  }
    	  return sc;
     }
     //9.Wpm
     private  double maxScoreOfWpm(String str1,String str2) {
    	   WuAndPalmer wpm = ws.getWuAndPalmer();
      	  double sc = wpm.max(str1, str2, "n");
      	  if(sc == 0 ){
      		  sc = wpm.max(str1, str2, "v");
      	  }
      	  return sc;
       }
     //10.Tnh
     private  double maxScoreOfTnh(String str1,String str2) {
  	    AdaptedLeskTanimotoNoHyponyms tanimoto = ws.getAdaptedLeskTanimotoNoHyponyms();
    	  double sc = tanimoto.max(str1, str2, "n");
    	  if(sc == 0 ){
    		  sc = tanimoto.max(str1, str2, "v");
    	  }
    	  return sc;
     }
     /***********计算相似性*********************** */
     
	/*Getters and Setters*/
	public String getSparqlString() {
		return sparqlString;
	}
	public void setSparqlString(String sparqlString) {
		this.sparqlString = sparqlString;
	}
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(String answerNumber) {
		this.answerNumber = answerNumber;
	}

	public  List<MyTriples> getTripleList() {
		return tripleList;
	}

	public  void setTripleList(List<MyTriples> tripleList) {
		this.tripleList = tripleList;
	}

	public ArrayList<String> getSparqlList() {
		return sparqlList;
	}

	public void setSparqlList(ArrayList<String> sparqlList) {
		this.sparqlList = sparqlList;
	}

	public List<String> getSubSupportList() {
		return subSupportList;
	}

	public void setSubSupportList(List<String> subSupportList) {
		this.subSupportList = subSupportList;
	}

	public List<String> getObjSupportList() {
		return objSupportList;
	}

	public void setObjSupportList(List<String> objSupportList) {
		this.objSupportList = objSupportList;
	}

	public List<String> getRelPrepList() {
		return relPrepList;
	}

	public void setRelPrepList(List<String> relPrepList) {
		this.relPrepList = relPrepList;
	}

	public static List<Double> getSparqlScore() {
		return sparqlScore;
	}

	public static void setSparqlScore(List<Double> sparqlScore) {
		sparqlqueryAction.sparqlScore = sparqlScore;
	}

	public Map<String, Double> getScoreOfsparql() {
		return scoreOfsparql;
	}

	public void setScoreOfsparql(Map<String, Double> scoreOfsparql) {
		this.scoreOfsparql = scoreOfsparql;
	}

	public TreeMap<String, Double> getSorted_map() {
		return sorted_map;
	}

	public void setSorted_map(TreeMap<String, Double> sorted_map) {
		this.sorted_map = sorted_map;
	}
	
	
}
