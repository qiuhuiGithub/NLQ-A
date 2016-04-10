package javabean;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.TreeGraphNode;


public class GlobalVariable {
	int nodeDepth;
	int nodeWidth;
	private static List<String> subEntityList = new ArrayList<String>();
	private static List<String> relationList = new ArrayList<String>();
	private static List<String> objEntityList = new ArrayList<String>();
	
	public int getNodeDepth() {
		return nodeDepth;
	}
	public void setNodeDepth(int nodeDepth) {
		this.nodeDepth = nodeDepth;
	}
	public int getNodeWidth() {
		return nodeWidth;
	}
	public void setNodeWidth(int nodeWidth) {
		this.nodeWidth = nodeWidth;
	}
	
	public static List<String> getSubEntityList() {
		return subEntityList;
	}
	public static void setSubEntityList(List<String> subEntityList) {
		GlobalVariable.subEntityList = subEntityList;
	}
	public static List<String> getRelationList() {
		return relationList;
	}
	public static void setRelationList(List<String> relationList) {
		GlobalVariable.relationList = relationList;
	}
	public static List<String> getObjEntityList() {
		return objEntityList;
	}
	public static void setObjEntityList(List<String> objEntityList) {
		GlobalVariable.objEntityList = objEntityList;
	}
}
