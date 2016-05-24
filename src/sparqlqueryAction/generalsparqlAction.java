package sparqlqueryAction;

import java.io.*;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.net.URL;
import sparqlqueryAction.ValueComparator;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

@SuppressWarnings("serial")
public class generalsparqlAction extends ActionSupport {
	public static int flag = 0;
	public static String prep1 = "";
	private String ret = ERROR;// query error;
	private String sparqlString;
	private Map<String, String> stringRel = new HashMap<String, String>();
	private String key;
	private String choose;
	private String[] keyarray = new String[10];
	private String[] choosearray = new String[10];
	private Map<Integer, String> prepmap = new HashMap<Integer, String>();
	private Map<String, List<String>> keywordMap = new HashMap<String, List<String>>();
	Map<String, Double> scoreOfsparql = new LinkedHashMap<String, Double>();
	ValueComparator bvc = new ValueComparator(scoreOfsparql);
	TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

	public String execute() throws IOException {
		sparqlString = sparqlqueryAction.sparqlString;
		try {
			System.out.println("key:" + key);
			System.out.println("choose:" + choose);
			if (choose == null) {
				return ret = "inputempty";
			}
			String wnhome = System.getenv("WNHOME"); // 获取WordNet根目录环境变量WNHOME
			String path = wnhome + File.separator + "dict";
			URL url = new URL("file", null, path);
			IDictionary dict = new Dictionary(url);
			dict.open();// 打开词典

			// stringRel.put(key, choose);
			key = key.replaceAll(" ", "");
			choose = choose.replaceAll(" ", "");
			keyarray = key.split(",");
			choosearray = choose.split(",");
			String[] valuearray = new String[choosearray.length];
			for (int i = 0; i < choosearray.length; i++) {
				if (choosearray[i].equals("<rdf:type>")) {
					valuearray[i] = choosearray[i];
				} else {
					valuearray[i] = dealwithPredicate(choosearray[i]);
					if (flag == 1)
						prepmap.put(i, prep1);
				}
			}

			printarray(keyarray);
			printarray(choosearray);
			printarray(valuearray);
			for (int i = 0; i < keyarray.length; i++) {
				stringRel.put(keyarray[i], choosearray[i]);
			}
			System.out.println(prepmap);
			for (int j = 0; j < keyarray.length; j++) {
				List<String> list = new ArrayList<String>();
				list = getSynonyms(dict, keyarray[j], 1);
				keywordMap.put(keyarray[j], list);
			}
			for (int j = 0; j < valuearray.length; j++) {
				List<String> list = new ArrayList<String>();
				List<String> list1 = new ArrayList<String>();
				list = getSynonyms(dict, valuearray[j], 2);
				if (prepmap.containsKey(j)) {
					for (int k3 = 0; k3 < list.size(); k3++) {
						list1.add(list.get(k3) + prepmap.get(j));
					}
					keywordMap.put(valuearray[j], list1);
				} else {
					keywordMap.put(valuearray[j], list);
				}

			}
			System.out.println(keywordMap);
			int count = 0;
			String sparql;
			List<String> sparqlList = new ArrayList<String>();
			String[][] triplearray = new String[keyarray.length][20];
			double[][] score = new double[keyarray.length][20];
			double max = 0.4, min = 0.3;
			Random random = new Random();
			double alpha = random.nextDouble() % (max - min) + min;
			for (int k = 0; k < keyarray.length; k++) {
				for (int k1 = 0; k1 < keywordMap.get(keyarray[k]).size(); k1++) {
					for (int k2 = 0; k2 < keywordMap.get(valuearray[k]).size(); k2++) {
						String ss = "";
						if (keywordMap.get(valuearray[k]).get(k2)
								.equals("<rdf:type>")) {
							ss = "?x" + "  "
									+ keywordMap.get(valuearray[k]).get(k2)
									+ "    " + "<"
									+ keywordMap.get(keyarray[k]).get(k1)
									+ ">.   ";
						} else {
							ss = "?x" + "  " + "<ub:"
									+ keywordMap.get(valuearray[k]).get(k2)
									+ ">" + "    " + "<"
									+ keywordMap.get(keyarray[k]).get(k1)
									+ ">.   ";
						}
						score[k][count] = alpha
								* Math.pow(0.9, k2)
								+ (1 - alpha)
								* sparqlqueryAction.getSimilarity(keyarray[k],
										keywordMap.get(keyarray[k]).get(k1))
								* Math.pow(0.9, k1);
						triplearray[k][count] = ss;
						count++;
					}
				}
				count = 0;
			}
			System.out.println(count);
			for (int k = 0; k < keyarray.length; k++) {
				for (int k1 = 0; k1 < keywordMap.get(keyarray[k]).size()
						* keywordMap.get(valuearray[k]).size(); k1++) {
					System.out.println(triplearray[k][k1]);

				}
			}
			if (keyarray.length == 1) {
				for (int k1 = 0; k1 < keywordMap.get(keyarray[0]).size()
						* keywordMap.get(valuearray[0]).size(); k1++) {
					sparql = "select ?x where{  " + triplearray[0][k1] + "}";
					sparqlList.add(sparql);
					scoreOfsparql.put(sparql, score[0][k1]);
				}
			} else if (keyarray.length == 2) {
				for (int k1 = 0; k1 < keywordMap.get(keyarray[0]).size()
						* keywordMap.get(valuearray[0]).size(); k1++) {
					for (int k2 = 0; k2 < keywordMap.get(keyarray[1]).size()
							* keywordMap.get(valuearray[1]).size(); k2++) {
						sparql = "select ?x where{  " + triplearray[0][k1]
								+ triplearray[1][k2] + "}";
						sparqlList.add(sparql);
						scoreOfsparql.put(sparql, score[0][k1] + score[1][k2]);
					}
				}
			} else if (keyarray.length == 3) {
				for (int k1 = 0; k1 < keywordMap.get(keyarray[0]).size()
						* keywordMap.get(valuearray[0]).size(); k1++) {
					for (int k2 = 0; k2 < keywordMap.get(keyarray[1]).size()
							* keywordMap.get(valuearray[1]).size(); k2++) {
						for (int k3 = 0; k3 < keywordMap.get(keyarray[2])
								.size() * keywordMap.get(valuearray[2]).size(); k3++) {
							sparql = "select ?x where{  " + triplearray[0][k1]
									+ triplearray[1][k2] + triplearray[2][k3]
									+ "}";
							sparqlList.add(sparql);
							scoreOfsparql.put(sparql, score[0][k1]
									+ score[1][k2] + score[2][k3]);
						}
					}
				}
			}
			// //////////////////////////////
			sorted_map.putAll(scoreOfsparql);
			Map<String, Double> map1 = new HashMap<String, Double>();
			int mapSize = 0;
			if (sorted_map.size() > 5) {
				for (Map.Entry<String, Double> entry : sorted_map.entrySet()) {
					mapSize++;
					System.out.println("key= " + entry.getKey()
							+ " and value= " + entry.getValue());
					if (mapSize > 5)
						break;
					map1.put(entry.getKey(), entry.getValue());
				}
				sorted_map.clear();
				sorted_map.putAll(map1);
			}
			System.out.println(sorted_map);
			// //////////////////////////////
			System.out.println(sparqlList.size());
			for (int ii = 0; ii < sparqlList.size(); ii++) {
				System.out.println(sparqlList.get(ii));
			}
			ret = "sparql";
		} catch (Exception e) {
			e.printStackTrace();
			ret = "ERROR";
		}
		return ret;
	}

	private String dealwithPredicate(String s) {
		String result = "";
		flag = 0;
		prep1 = "";
		int i;
		for (i = s.length() - 1; i > 0; i--) {
			if (s.charAt(i) > 'A' && s.charAt(i) < 'Z') {
				flag = 1;
				result = s.substring(4, i);
				prep1 = s.substring(i, s.length() - 1);
				break;
			}
		}
		if (i == 0)
			result = s.substring(4, s.length() - 1);
		return result;
	}

	private List<String> getSynonyms(IDictionary dict, String str, int type) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		IIndexWord idxWord = null;
		if (type == 1) {
			idxWord = dict.getIndexWord(str, POS.NOUN);
		} else if (type == 2) {
			idxWord = dict.getIndexWord(str, POS.VERB);
		}
		if (idxWord == null) {
			list.add(str);
			return list;
		}
		IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
		IWord word = dict.getWord(wordID);
		ISynset synset = word.getSynset(); // ISynset是一个词的同义词集的接口
		List<IWord> words = new ArrayList<IWord>();
		words = synset.getWords();
		int nodeWidth = words.size();
		int matchLen = nodeWidth > 3 ? 3 : nodeWidth;
		for (int k = 0; k < matchLen; k++) {
			list.add(words.get(k).getLemma());
		}
		return list;
	}

	public void printarray(String[] s) {
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[i] + " ");
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public TreeMap<String, Double> getSorted_map() {
		return sorted_map;
	}

	public void setSorted_map(TreeMap<String, Double> sorted_map) {
		this.sorted_map = sorted_map;
	}

	public String getSparqlString() {
		return sparqlString;
	}

	public void setSparqlString(String sparqlString) {
		this.sparqlString = sparqlString;
	}

	public Map<String, String> getStringRel() {
		return stringRel;
	}

	public void setStringRel(Map<String, String> stringRel) {
		this.stringRel = stringRel;
	}

}
