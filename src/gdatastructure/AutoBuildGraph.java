package gdatastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AutoBuildGraph {
	public static Map<String, Integer> vertexmap = new HashMap<String, Integer>();
	public static Map<Integer, String> edgemap = new HashMap<Integer, String>();
	public static List<Integer> pointList = new ArrayList<Integer>();
	public static Map<Integer, Integer> so = new HashMap<Integer, Integer>();
	public static Map<Map<Integer, Integer>, Integer> vertexedgemap = new HashMap<Map<Integer, Integer>, Integer>();
	public static Map<Integer, Set<Integer>> findrel = new HashMap<Integer, Set<Integer>>();

	public AutoBuildGraph() {

	}

	public static Graph buildTestGraphForgstore() throws Exception {
		java.io.File file = new java.io.File(
				"/home/qiuhui/gStore-master/LUBM10.db/six_tuples");
		Scanner input = new Scanner(file);
		List<Integer> s = new ArrayList<Integer>();
		List<Integer> r = new ArrayList<Integer>();
		List<Integer> o = new ArrayList<Integer>();
		List<String> s1 = new ArrayList<String>();
		List<String> r1 = new ArrayList<String>();
		List<String> o1 = new ArrayList<String>();
		while (input.hasNext()) {
			s.add(Integer.parseInt(input.next()));
			r.add(Integer.parseInt(input.next()));
			o.add(Integer.parseInt(input.next()));
			s1.add(input.next());
			r1.add(input.next());
			o1.add(input.next());
		}
		input.close();
		java.io.File outFile = new java.io.File(
				"/home/qiuhui/gStore-master/LUBM10.db/predicate");
		if (outFile.exists()) {
			outFile.delete();
		}
		java.io.PrintWriter output = new java.io.PrintWriter(outFile);
		for (java.util.Iterator<String> k = r1.iterator(); k.hasNext();) {
			output.println(k.next() + ".");
		}
		output.close();
		int graphSize = Collections.max(s) > Collections.max(o) ? Collections
				.max(s) : Collections.max(o) + 1;
		Graph mygraph = new Graph(graphSize);
		for (int i = 0; i < graphSize - 1; i++) {
			mygraph.connect(i, i + 1, 10000, -1);
		}
		int count1 = 0;
		try {
			for (int i = 0; i < s.size(); i++) {
				vertexmap.put(s1.get(i), s.get(i));
				vertexmap.put(o1.get(i), o.get(i));
				edgemap.put(r.get(i), r1.get(i));
				if (Math.abs(s.get(i) - o.get(i)) == 1) {
					mygraph.disconnect(s.get(i), o.get(i));
				}
				mygraph.connect(s.get(i), o.get(i), 1, r.get(i));
				count1++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(count1);
		}

		// System.out.println(vertexmap);
		// System.out.println(edgemap);
		return mygraph;
	}

}
