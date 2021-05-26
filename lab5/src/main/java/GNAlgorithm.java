import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class GNAlgorithm {
	public static void main(String[] args) throws Exception {
		Util.ParsedInput parsedInput = Util.parse("lab5\\src\\main\\resources\\upute\\t2.in");

		HashMap<Integer, List<Node>> connections = parsedInput.connections;
		HashMap<Integer, int[]> properties = parsedInput.properties;

		Set<Map.Entry<Integer, List<Node>>> entries = connections.entrySet();

		//izračun nesličnosti
		for (Map.Entry<Integer, List<Node>> e : entries) {
			Integer fromVertex = e.getKey();
			List<Node> toVertexes = e.getValue();


			for (Node n : toVertexes) {
				n.weight = calculateNonSimiliarity(fromVertex, n, properties);
			}
		}

		int[] integers = connections.keySet().stream().mapToInt(a -> a).toArray();

		for (int i = 0; i < integers.length; i++) {
			for (int j = i + 1; j < integers.length; j++) {
				BFS.pretrazi(integers[i], connections, integers[j]);
			}
		}

		HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths = BFS.paths;


		printPaths(paths);


		System.out.println("STOP!");
	}

	private static void printPaths(HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths) {
		for (Map.Entry<Integer, HashMap<Integer, List<List<Node>>>> entry : paths.entrySet()) {
			System.out.println("ENTRY = " + entry.getKey());

			HashMap<Integer, List<List<Node>>> value = entry.getValue();

			for (Map.Entry<Integer, List<List<Node>>> e : value.entrySet()) {
				System.out.print(e.getKey() + "\t");
				System.out.println(e.getValue());
			}
		}
	}

	private static int calculateNonSimiliarity(Integer fromVertex, Node n, HashMap<Integer, int[]> properties) {

		int[] fromProperties = properties.get(fromVertex);
		int[] toProperties = properties.get(n.index);

		int same = (int) IntStream.range(0, fromProperties.length).filter(i -> fromProperties[i] == toProperties[i]).count();


		return fromProperties.length - (same - 1);
	}
}
