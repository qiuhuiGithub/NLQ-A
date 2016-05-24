package sparqlqueryAction;

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

public class Similar {

	private static String dir = "/usr/local/WordNet-3.0";
	private static JWS ws = new JWS(dir, "3.0");

	/*
	 * public Similar(String str1,String str2){ this.str1=str1; this.str2=str2;
	 * }
	 */

	public static double getSimilarity(String str1, String str2) {
		String[] strs1 = splitString(str1);
		String[] strs2 = splitString(str2);
		double sum = 0.0;
		for (String s1 : strs1) {
			for (String s2 : strs2) {
				// double sc= maxScoreOfLin(s1,s2); //mark
				// double sc= maxScoreOfJcn(s1,s2);//mark
				// double sc= maxScoreOfLesk(s1,s2);//no answer
				// double sc= maxScoreOfTanimoto(s1,s2);//no answer
				// double sc= maxScoreOfHas(s1,s2);//mark
				// double sc= maxScoreOfLcd(s1,s2);//mark
				double sc = maxScoreOfPath(s1, s2);// mark,tending to choose
													// this
				// double sc = maxScoreOfRes(s1,s2);//mark
				// double sc = maxScoreOfWpm(s1,s2);//mark
				// double sc = maxScoreOfTnh(s1,s2);//no answer
				sum += sc;
				System.out
						.println("当前计算: " + s1 + " VS " + s2 + " 的相似度为:" + sc);
			}
		}
		double Similarity = sum / (strs1.length * strs2.length);
		sum = 0;
		return Similarity;
	}

	private static String[] splitString(String str) {
		String[] ret = str.split(" ");
		return ret;
	}

	// 1.Lin
	@SuppressWarnings("unused")
	private static double maxScoreOfLin(String str1, String str2) {
		Lin lin = ws.getLin();
		double sc = lin.max(str1, str2, "n");
		if (sc == 0) {
			sc = lin.max(str1, str2, "v");
		}
		return sc;
	}

	// 2.Jcn
	@SuppressWarnings("unused")
	private static double maxScoreOfJcn(String str1, String str2) {
		JiangAndConrath jcn = ws.getJiangAndConrath();
		double sc = jcn.max(str1, str2, "n");
		if (sc == 0) {
			sc = jcn.max(str1, str2, "v");
		}
		return sc;
	}

	// 3.Lesk
	@SuppressWarnings("unused")
	private static double maxScoreOfLesk(String str1, String str2) {
		AdaptedLesk lesk = ws.getAdaptedLesk();
		double sc = lesk.max(str1, str2, "n");
		if (sc == 0) {
			sc = lesk.max(str1, str2, "v");
		}
		return sc;
	}

	// 4.Tanimoto
	@SuppressWarnings("unused")
	private static double maxScoreOfTanimoto(String str1, String str2) {
		AdaptedLeskTanimoto tanimoto = ws.getAdaptedLeskTanimoto();
		double sc = tanimoto.max(str1, str2, "n");
		if (sc == 0) {
			sc = tanimoto.max(str1, str2, "v");
		}
		return sc;
	}

	// 5.Has
	@SuppressWarnings("unused")
	private static double maxScoreOfHas(String str1, String str2) {
		HirstAndStOnge has = ws.getHirstAndStOnge();
		double sc = has.max(str1, str2, "n");
		if (sc == 0) {
			sc = has.max(str1, str2, "v");
		}
		return sc;
	}

	// 6.Lcd
	@SuppressWarnings("unused")
	private static double maxScoreOfLcd(String str1, String str2) {
		LeacockAndChodorow lcd = ws.getLeacockAndChodorow();
		double sc = lcd.max(str1, str2, "n");
		if (sc == 0) {
			sc = lcd.max(str1, str2, "v");
		}
		return sc;
	}

	// 7.Path
	private static double maxScoreOfPath(String str1, String str2) {
		Path path = ws.getPath();
		double sc = path.max(str1, str2, "n");
		if (sc == 0) {
			sc = path.max(str1, str2, "v");
		}
		return sc;
	}

	// 8.Res
	@SuppressWarnings("unused")
	private static double maxScoreOfRes(String str1, String str2) {
		Resnik res = ws.getResnik();
		double sc = res.max(str1, str2, "n");
		if (sc == 0) {
			sc = res.max(str1, str2, "v");
		}
		return sc;
	}

	// 9.Wpm
	@SuppressWarnings("unused")
	private static double maxScoreOfWpm(String str1, String str2) {
		WuAndPalmer wpm = ws.getWuAndPalmer();
		double sc = wpm.max(str1, str2, "n");
		if (sc == 0) {
			sc = wpm.max(str1, str2, "v");
		}
		return sc;
	}

	// 10.Tnh
	@SuppressWarnings("unused")
	private static double maxScoreOfTnh(String str1, String str2) {
		AdaptedLeskTanimotoNoHyponyms tanimoto = ws
				.getAdaptedLeskTanimotoNoHyponyms();
		double sc = tanimoto.max(str1, str2, "n");
		if (sc == 0) {
			sc = tanimoto.max(str1, str2, "v");
		}
		return sc;
	}

	public static void main(String args[]) {
		String s1 = "marry";
		String s2 = "get_married";
		System.out.println(getSimilarity(s1, s2));
	}
}