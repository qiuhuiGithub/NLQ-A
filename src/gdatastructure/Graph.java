package gdatastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import packagefortest.FileWordCount;
import sparqlqueryAction.ValueComparator;

/*
 * Graph用来表示整个图
 */
public class Graph {
	private static Vertex[] vertexs;
	private int pos = -1;
	private int size = 0;

	// size表示结点个数，link的最大数目为等于size
	Graph(int size) {
		this.size = size;
		vertexs = new Vertex[size + 1];
		for (int i = 0; i < size; i++)
			vertexs[i] = new Vertex(new Integer(i));
	}

	public int size() {
		return size;
	}

	public Vertex getVertex(int num) {
		return vertexs[num];
	}

	// 增加一个实际节点,目前暂不使用此功能
	void add(Object obj) {
		assert pos < size;
		vertexs[++pos] = new Vertex(obj);
	}

	void connect(int from, int to, int linkvalue, int relnumber) {
		// 单向表
		vertexs[from].link.add(to, linkvalue, relnumber);
		vertexs[to].link.add(from, linkvalue, relnumber);
		// 双向表
		// vertexs[from].link.add(to);
		// vertexs[to].link.add(from);
	}

	void disconnect(int from, int to) {
		// 单向表
		vertexs[from].link.remove(to);
		vertexs[to].link.remove(from);
		// 双向表
		// vertexs[from].link.remove(to);
		// vertexs[to].link.remove(from);
	}

	// 查询两点之间边的权值,若不存在边，则返回-1表示无穷大
	public static int getEdgeValue(int from, int to) {
		int value = -1;
		VNode temp = null;
		vertexs[from].link.reset();
		while ((temp = vertexs[from].link.next()) != null) {
			if (temp.index() == to) {
				value = temp.linkvalue();
			}
		}
		return value;
	}

	// 查询关系序号
	public static int getRelValue(int from, int to) {
		int value = -1;
		VNode temp = null;
		vertexs[from].link.reset();
		while ((temp = vertexs[from].link.next()) != null) {
			if (temp.index() == to) {
				value = temp.relnumber();
			}
		}
		return value;
	}

	// 返回一个结点的所有邻接点
	public static LinkedList<Integer> getAllNeighbours(int index) {
		LinkedList<Integer> neighbours = new LinkedList<Integer>();
		VNode temp = null;
		vertexs[index].link.reset();
		while ((temp = vertexs[index].link.next()) != null) {
			neighbours.add(temp.index());
		}
		return neighbours;
	}

	// 打印图
	public static void showGraph(int size) {
		VNode temp = null;
		for (int index = 0; index < size; index++) {
			vertexs[index].link.reset();
			LinkedList<Integer> neighbours = new LinkedList<Integer>();
			neighbours = getAllNeighbours(index);
			Set<Integer> set = new HashSet<Integer>();
			for (int in = 0; in < neighbours.size(); in++) {
				if (getRelValue(index, neighbours.get(in)) >= 0)
					set.add(getRelValue(index, neighbours.get(in)));
			}
			System.out.print(vertexs[index].getValue() + ":");
			while ((temp = vertexs[index].link.next()) != null) {
				System.out.print(temp.index() + "->");
			}
			System.out.println(" ");
			AutoBuildGraph.findrel
					.put((Integer) vertexs[index].getValue(), set);
			System.out.println(AutoBuildGraph.findrel);
		}

	}

	public static Set<Integer> getrelSet(String s) {
		Set<Integer> set = new HashSet<Integer>();
		set.clear();
		if (AutoBuildGraph.vertexmap.containsKey(s)) {
			int index = AutoBuildGraph.vertexmap.get(s);
			vertexs[index].link.reset();
			LinkedList<Integer> neighbours = new LinkedList<Integer>();
			neighbours = getAllNeighbours(index);
			for (int in = 0; in < neighbours.size(); in++) {
				if (getRelValue(index, neighbours.get(in)) >= 0)
					set.add(getRelValue(index, neighbours.get(in)));
			}
			// System.out.print(vertexs[index].getValue()+":");
			// System.out.println(set);
		}
		return set;

	}

	// 根据关键词，获取可能的关系
	public static List<String> getStringRelation(String s) {

		List<String> stringRelation = new ArrayList<String>();
		Map<String, Double> predicatemap = new HashMap<String, Double>();
		ValueComparator bvc = new ValueComparator(predicatemap);
		TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		stringRelation.clear();
		// Map<Integer, Set<String>> relStringMap= new
		// HashMap<Integer,Set<String>>();
		Set<Integer> newSet = new HashSet<Integer>();
		newSet = getrelSet(s);
		if (newSet.isEmpty()) {
			return stringRelation;
		}
		Iterator<Integer> it = newSet.iterator();
		while (it.hasNext()) {
			// stringRelation.add(AutoBuildGraph.edgemap.get(it.next()));
			String ss = AutoBuildGraph.edgemap.get(it.next());
			double fre = (double) FileWordCount.predicateFre.get(ss);
			System.out.println(ss + "" + fre);
			predicatemap.put(ss, fre);
		}
		// //////////////////////////////
		sorted_map.putAll(predicatemap);
		Map<String, Double> map1 = new HashMap<String, Double>();
		int mapSize = 0;
		if (sorted_map.size() > 5) {
			for (Map.Entry<String, Double> entry : sorted_map.entrySet()) {
				mapSize++;
				System.out.println("key= " + entry.getKey() + " and value= "
						+ entry.getValue());
				if (mapSize > 5)
					break;
				map1.put(entry.getKey(), entry.getValue());
			}
			sorted_map.clear();
			sorted_map.putAll(map1);
		}
		System.out.println(sorted_map);
		// //////////////////////////////
		for (String e : sorted_map.keySet()) {
			stringRelation.add(e);
		}
		return stringRelation;

	}
}
