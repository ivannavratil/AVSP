import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GNAlgorithm {
	public static void main(String[] args) throws Exception {
		Util.ParsedInput parsedInput = Util.parseWithWeight("lab5\\src\\main\\resources\\upute\\slika11.in");

		HashMap<Integer, List<Node>> connections = parsedInput.connections;
		HashMap<Integer, int[]> properties = parsedInput.properties;

		Set<Map.Entry<Integer, List<Node>>> entries = connections.entrySet();

		//izračun nesličnosti
		//		for (Map.Entry<Integer, List<Node>> e : entries) {
		//			Integer fromVertex = e.getKey();
		//			List<Node> toVertexes = e.getValue();
		//
		//			for (Node n : toVertexes) {
		//				n.weight = calculateNonSimiliarity(fromVertex, n, properties);
		//			}
		//		}

		while (true) {
			int[] allIndexes = connections.keySet().stream().mapToInt(a -> a).toArray();

			for (int i = 0; i < allIndexes.length; i++) {
				for (int j = i + 1; j < allIndexes.length; j++) {
					BFS.pretrazi(allIndexes[i], connections, allIndexes[j]);
				}
			}

			//on int-a do int-a mogu doći putevima koji se nalaze u list
			HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths = BFS.paths;


			//printPaths(paths);


			HashMap<Integer, HashMap<Integer, Double>> edgeBetweenness = calculateEdgeBetweenness(paths);


			double max = edgeBetweenness.values().stream()
			                            .flatMap((Function<HashMap<Integer, Double>, Stream<Double>>) map -> map.values().stream())
			                            .mapToDouble(a -> a)
			                            .max().orElseThrow();


			//for all entries in edgeBetweenness
			HashSet<Pair> removed = removeEdgeWithHighestBetweenness(connections, edgeBetweenness, max);

			if (removed.isEmpty()) {
				return;
			}

			System.out.println(removed);
		}

		//System.out.println("STOP!");
	}

	private static HashSet<Pair> removeEdgeWithHighestBetweenness(
			HashMap<Integer, List<Node>> connections,
			HashMap<Integer, HashMap<Integer, Double>> edgeBetweenness,
			double max) {

		HashSet<Pair> removed = new HashSet<>();


		for (Map.Entry<Integer, HashMap<Integer, Double>> entry : edgeBetweenness.entrySet()) {
			Integer fromVertex = entry.getKey();
			HashMap<Integer, Double> toVertexesAndBetweenness = entry.getValue();


			for (Map.Entry<Integer, Double> toVertexAndBetweenness : toVertexesAndBetweenness.entrySet()) {
				Integer toVertex = toVertexAndBetweenness.getKey();
				Double betweenness = toVertexAndBetweenness.getValue();


				if (betweenness == max) {
					//MICANJE EDGA I ISPIS
					List<Node> nodes = connections.get(fromVertex);

					Iterator<Node> iterator = nodes.iterator();
					while (iterator.hasNext()) {
						Node node = iterator.next();
						if (node.index == toVertex) {
							removed.add(new Pair(fromVertex, toVertex));
							iterator.remove();
						}
					}
				}
			}
		}
		return removed;
	}

	private static HashMap<Integer, HashMap<Integer, Double>> calculateEdgeBetweenness(
			HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths) {

		//za fromVertex -> HM <toVertex, betweenness>
		HashMap<Integer, HashMap<Integer, Double>> edgeBetweenness = new HashMap<>();


		//svaki čvor ide u drugi čvor najkraće putevima u list
		for (HashMap<Integer, List<List<Node>>> entry : paths.values()) {
			//sa paths.values dobivam sve najkraće puteve

			//za svaki odredišni čvor
			for (Map.Entry<Integer, List<List<Node>>> entry2 : entry.entrySet()) {
				Integer key = entry2.getKey();
				List<List<Node>> value = entry2.getValue();

				double N = value.size();

				//za svaki put
				for (List<Node> nodes : value) {

					//za svaki čvor
					for (int i = 0; i < nodes.size() - 1; i++) {
						Node node = nodes.get(i);
						Node nextNode = nodes.get(i + 1);


						if (edgeBetweenness.putIfAbsent(node.index,
								new HashMap<>() {{
									this.put(nextNode.index, 1 / N);
								}}

						) != null) {
							//ako je bilo mappinga za key
							//dohvati sve
							HashMap<Integer, Double> map = edgeBetweenness.get(node.index);
							if (map.putIfAbsent(nextNode.index, 1 / N) != null) {
								Double aDouble = map.get(nextNode.index);
								map.put(nextNode.index, aDouble + 1 / N);
							}
						}
					}
				}
			}
		}
		return edgeBetweenness;
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

	private static class Pair implements Comparable<Pair> {
		private final int lower;

		private final int higher;

		public Pair(int first, int second) {
			this.lower = Math.min(first, second);
			this.higher = Math.max(first, second);
		}

		@Override
		public int compareTo(Pair o) {
			if (o.lower == this.lower) {
				return Integer.compare(o.higher, this.higher);
			} else {
				return Integer.compare(o.lower, this.lower);
			}
		}

		@Override
		public String toString() {
			return "Pair{" +
			       "lower=" + this.lower +
			       ", higher=" + this.higher +
			       '}';
		}
	}
}
