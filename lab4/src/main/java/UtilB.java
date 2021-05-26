import java.io.*;
import java.util.*;

public class UtilB {

	public static ParsedInput parse() throws IOException {
		return parse(null);
	}

	public static ParsedInput parse(String path) throws IOException {
		BufferedReader br = setBufferedReader(path);

		String[] firstLine = br.readLine().split("\\s+");

		int numberOfVertices = Integer.parseInt(firstLine[0]);
		double numberOfEdges = Integer.parseInt(firstLine[1]);

		HashSet<Integer> blackVertices = new HashSet<>(numberOfVertices / 3);

		List<Set<Integer>> transitions = new ArrayList<>(numberOfVertices);
		for (int i = 0; i < numberOfVertices; i++) {
			int anInt = Integer.parseInt(br.readLine());
			if (anInt == 1) {
				blackVertices.add(i);
			}
			transitions.add(i, new HashSet<>());
		}

		for (int i = 0; i < numberOfEdges; i++) {
			String[] split = br.readLine().split("\\s+");
			int[] ints = Arrays.stream(split)
			                   .mapToInt(Integer::parseInt)
			                   .toArray();
			transitions.get(ints[0]).add(ints[1]);
			transitions.get(ints[1]).add(ints[0]);
		}

		br.close();

		return new ParsedInput(blackVertices, transitions);
	}

	private static BufferedReader setBufferedReader(String path) throws FileNotFoundException {
		return path == null ? new BufferedReader(new InputStreamReader(System.in)) : new BufferedReader(new FileReader(path));
	}

	public static class ParsedInput {
		HashSet<Integer> blackVertices;

		List<Set<Integer>> transitions;

		public ParsedInput(HashSet<Integer> blackVertices, List<Set<Integer>> transitions) {
			this.blackVertices = blackVertices;
			this.transitions = transitions;
		}
	}
}
