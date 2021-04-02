import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Util {
	public static String simhash(String text, ReturnType returnType) {
		int[] sh = new int[128];

		String[] tokens = text.split("\\s+");
		for (String token : tokens) {
			char[] md5HashAsChar = byteToBinary(DigestUtils.md5(token));

			for (int i = 0; i < md5HashAsChar.length; i++) {
				if (md5HashAsChar[i] == '1') {
					sh[i] += 1;
				} else {
					sh[i] -= 1;
				}
			}
		}

		for (int i = 0; i < sh.length; i++) {
			sh[i] = sh[i] >= 0 ? 1 : 0;
		}


		StringBuilder sb = new StringBuilder();
		for (int j : sh) {
			sb.append(j);
		}


		return (returnType == ReturnType.Binary) ? (sb.toString()) : (new BigInteger(sb.toString(), 2).toString(16));
	}

	static char[] byteToBinary(byte[] bytes) {
		int size = Byte.SIZE * bytes.length;
		char[] ch = new char[size];

		for (int i = 0; i < size; i++) {
			ch[i] = ((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
		}
		return ch;
	}

	public static BufferedReader setBufferedReader(String path) throws FileNotFoundException {
		BufferedReader br;
		if (path == null) {
			br = new BufferedReader(new InputStreamReader(System.in));
		} else {
			br = new BufferedReader(new FileReader(path));
		}
		return br;
	}

	enum ReturnType {Binary, Hex}
}
