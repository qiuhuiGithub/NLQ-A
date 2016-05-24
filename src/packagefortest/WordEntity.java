package packagefortest;

public class WordEntity implements Comparable<WordEntity> {
	private String key;
	private Integer count;

	public WordEntity(String key, Integer count) {
		this.key = key;
		this.count = count;
	}

	public int compareTo(WordEntity o) {
		int cmp = count.intValue() - o.count.intValue();
		return (cmp == 0 ? key.compareTo(o.key) : -cmp);
		// 只需在这儿加一个负号就可以决定是升序还是降序排列 -cmp降序排列，cmp升序排列
		// 因为TreeSet会调用WorkForMap的compareTo方法来决定自己的排序
	}

	@Override
	public String toString() {
		return key + " 出现的次数为：" + count;
	}

	public String getKey() {
		return key;
	}

	public Integer getCount() {
		return count;
	}
}
