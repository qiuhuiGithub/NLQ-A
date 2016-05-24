package packagefortest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileWordCount {
	public static Map<String, Integer> predicateFre = new HashMap<String, Integer>();

	public static void getFrequent() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/home/qiuhui/gStore-master/LUBM10.db/predicate"));
			String s;
			StringBuffer sb = new StringBuffer();
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			Map<String, Integer> map = new HashMap<String, Integer>();
			StringTokenizer st = new StringTokenizer(sb.toString(), " .  \n");
			while (st.hasMoreTokens()) {
				String letter = st.nextToken();
				int count;
				if (map.get(letter) == null) {
					count = 1;
				} else {
					count = map.get(letter).intValue() + 1;
				}
				map.put(letter, count);
			}
			Set<WordEntity> set = new TreeSet<WordEntity>();
			for (String key : map.keySet()) {
				set.add(new WordEntity(key, map.get(key)));
			}
			// 自己拼接字符串，输出我们想要的字符串格式
			System.out.println("输出形式一：");
			for (Iterator<WordEntity> it = set.iterator(); it.hasNext();) {
				WordEntity w = it.next();
				predicateFre.put(w.getKey(), w.getCount());
				System.out.println("单词:" + w.getKey() + " 出现的次数为： "
						+ w.getCount());
			}
			// System.out.println(predicateFre);
			// System.out.println(predicateFre.get("<ub:establish>"));
		} catch (FileNotFoundException e) {
			System.out.println("文件未找到~！");
		} catch (IOException e) {
			System.out.println("文件读异常~！");
		}
	}
}