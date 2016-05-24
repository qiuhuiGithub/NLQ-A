package packagefortest;
/*实现关键词查询功能时，调试时使用，在项目中不具备功能*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import gdatastructure.AutoBuildGraph;
import gdatastructure.Graph;

public class Main {
	public static int flag = 0;
	public static String prep1 = "";

	public static void main(String[] args) throws Exception {
		String key;
		String choose;
		String[] keyarray = new String[10];
		String[] choosearray = new String[10];
		Map<Integer, String> prepmap = new HashMap<Integer, String>();
		Graph gstoregraph = AutoBuildGraph.buildTestGraphForgstore();
		// Graph.showGraph(gstoregraph.size());
		System.out.println(gstoregraph.size());
		System.out.println(Graph.getStringRelation("<Tom_Hanks>"));

		String sparql = "<Intel>,<Tom_Hanks>";
		String[] array = new String[10];
		array = sparql.split(",");
		System.out.println(array[0]);
		Map<String, List<String>> stringRel = new HashMap<String, List<String>>();
		for (int in = 0; in < array.length; in++) {
			List<String> list = new ArrayList<String>();
			list = Graph.getStringRelation(array[in]);
			stringRel.put(array[in], list);
		}
		System.out.println(stringRel);
		key = "Tom_Hanks, Intel";
		choose = "<rdf:type>,<ub:establish>,<ub:comeFrom>";
		System.out.println("key:" + key);
		System.out.println("choose:" + choose);
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
		System.out.println(prepmap);
		System.out.println(prepmap.keySet());
		for (int s : prepmap.keySet()) {
			for (int j = 0; j < valuearray.length; j++) {
				System.out.println(prepmap.get(s) + " " + valuearray[j]);
			}
		}
	}

	public static void printarray(String[] s) {
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[i] + " ");
		}
	}

	public static String dealwithPredicate(String s) {
		String result = "";
		flag = 0;
		prep1 = "";
		System.out.println(s + "" + s.length());
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
}