package gdatastructure;

public class Link {
	private VNode first;
	private VNode current;

	Link() {
		first = null;
		current = null;
	}

	void add(int to, int linkvalue, int relnumber) {
		VNode tempnode = new VNode(to, linkvalue, relnumber);
		if (first == null)
			first = tempnode;
		else {
			tempnode.next(first);
			first = tempnode;
		}
	}

	boolean hasNext() {
		return current == null ? first != null : current.next() != null;
	}

	VNode next() {
		if (current == null)
			current = first;
		else
			current = current.next();
		return current;
	}

	void reset() {
		current = null;
	}

	void remove(int index) {
		VNode pre = null;
		VNode cur = first;

		while (cur != null) {
			if (cur.index() == index) {
				if (pre == null) {
					first = cur.next();
				} else {
					pre.next(cur.next());
				}
				return;
			}
			pre = cur;
			cur = cur.next();
		}

	}
}
