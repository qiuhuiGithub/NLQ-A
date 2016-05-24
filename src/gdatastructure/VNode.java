package gdatastructure;

public class VNode {
	private int linkvalue;
	private int relnumber;
	private int index;
	private VNode next;

	VNode(int index, int linkvalue, int relnumber) {
		this.linkvalue = linkvalue;
		this.relnumber = relnumber;
		this.index = index;
		this.next = null;
	}

	void next(VNode next) {
		this.next = next;
	}

	VNode next() {
		return next;
	}

	int linkvalue() {
		return linkvalue;
	}

	int index() {
		return index;
	}

	int relnumber() {
		return relnumber;
	}
}