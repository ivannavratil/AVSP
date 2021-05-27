import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GNAlgorithm {

	public static void main(String[] args) throws Exception {

		long start = System.nanoTime();
		//GNAlgorithm.start();
		GNAlgorithm.start("lab5\\src\\main\\resources\\test\\t.in");
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	static void start() throws IOException {
		start(null);
	}

	private static void start(String path) throws IOException {
		Util.ParsedInput parsedInput = Util.parse(path);

		HashMap<Integer, List<Node>> connections = parsedInput.connections;


		HashMap<Integer, int[]> properties = parsedInput.properties;

		Set<Map.Entry<Integer, List<Node>>> temp = connections.entrySet();

		//izracun neslicnosti
		for (Map.Entry<Integer, List<Node>> e : temp) {
			Integer fromVertex = e.getKey();
			List<Node> toVertexes = e.getValue();

			if (toVertexes != null) {
				for (Node n : toVertexes) {
					n.weight = calculateNonSimiliarity(fromVertex, n, properties);
				}
			}
		}


		HashMap<Integer, List<Node>> initialConnections = deepCopyOfMap(connections);


		//System.out.println(connections);

		double totalWeightSum = connections.values()
		                                   .stream()
		                                   .flatMapToDouble(nodes -> nodes.stream().mapToDouble(a -> a.weight))
		                                   .sum() / 2.0;

		//System.out.println("suma tezina " + totalWeightSum);


		//HashMap<Integer, HashMap<Integer, List<List<Node>>>> initalPaths = startBFS(connections,
		//		connections.keySet().stream().mapToInt(a -> a).toArray());

		int[] allIndexes = connections.keySet().stream().mapToInt(a -> a).toArray();

		double maxModularity = -Double.MAX_VALUE;


		TreeSet<TreeSet<Integer>> communities = new TreeSet<>((o1, o2) -> {
			if (o1.size() == o2.size()) {
				return o1.first().compareTo(o2.first());
			} else {
				return Integer.compare(o1.size(), o2.size());
			}
		});

		while (connections.values().stream().mapToLong(Collection::size).sum() != 0) {

			//on int-a do int-a mogu doci putevima koji se nalaze u list
			HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths = startBFS(connections, allIndexes);

			//printPaths(paths);

			double modularity = modularity(initialConnections, totalWeightSum, allIndexes, paths);
			//System.out.println("MODULARITY " + modularity);

			if (modularity > maxModularity) {
				maxModularity = modularity;
				communities.clear();
				saveCommunitiesAtMaxModularity(connections, communities);
			}


			HashMap<Integer, HashMap<Integer, Double>> edgeBetweenness = calculateEdgeBetweenness(paths);

			//System.out.println(edgeBetweenness);

			double max = edgeBetweenness.values().stream()
			                            .flatMap(map -> map.values().stream())
			                            .mapToDouble(a -> a)
			                            .max().orElseThrow();


			//for all temp in edgeBetweenness
			TreeSet<Pair> removed = removeEdgeWithHighestBetweenness(connections, edgeBetweenness, max);


			for (Pair pair : removed) {
				System.out.println(pair.lower + " " + pair.higher);
			}
		}

		lastFix(communities);


		StringBuilder sb = new StringBuilder();

		for (TreeSet<Integer> community : communities) {
			List<String> collect = community.stream().map(Object::toString).collect(Collectors.toList());
			String join = String.join("-", collect);
			sb.append(join).append(" ");
		}

		System.out.println(sb.toString().trim());

		//System.out.println("STOP!");
	}

	private static void lastFix(TreeSet<TreeSet<Integer>> communities) {

		ArrayList<ArrayList<Integer>> array = new ArrayList<>();

		for (TreeSet<Integer> ts : communities) {
			array.add(new ArrayList<>(ts));
		}

		for (ArrayList<Integer> al : array) {
			for (Integer i : al) {

				List<TreeSet<Integer>> collect = communities.stream().filter(a -> a.contains(i)).collect(Collectors.toList());
				if (collect.size() > 1) {
					TreeSet<Integer> all = new TreeSet<>();

					communities.removeIf(a -> a.contains(i));


					for (TreeSet<Integer> temp : collect) {
						all.addAll(temp);
					}

					communities.add(all);
				}
			}
		}
	}

	private static void saveCommunitiesAtMaxModularity(HashMap<Integer, List<Node>> connections, TreeSet<TreeSet<Integer>> communities) {

		//		for (Map.Entry<Integer, List<Node>> integerListEntry : connections.entrySet()) {
		//			System.out.println(integerListEntry);
		//		}

		for (Map.Entry<Integer, List<Node>> entries : connections.entrySet()) {
			int key = entries.getKey();
			List<Node> value = entries.getValue();

			List<Integer> mini = new ArrayList<>();
			mini.add(key);
			value.forEach(a -> mini.add(a.index));

			Optional<TreeSet<Integer>> first = Optional.empty();
			boolean atLeastOne = false;

			for (int vertex : mini) {
				first = communities.stream().filter(a -> a.contains(vertex)).findFirst();
				if (first.isPresent()) {
					atLeastOne = true;
					break;
				}
			}

			if (atLeastOne) {
				first.get().addAll(mini);
			} else {
				TreeSet<Integer> treeSet = new TreeSet<>(mini);
				communities.add(treeSet);
			}
		}
	}

	private static HashMap<Integer, List<Node>> deepCopyOfMap(HashMap<Integer, List<Node>> connections) {

		HashMap<Integer, List<Node>> deepCopy = new HashMap<>();

		for (Map.Entry<Integer, List<Node>> entry : connections.entrySet()) {
			int key = entry.getKey();
			List<Node> value = entry.getValue();

			List<Node> collect =

					value.stream().map(a -> new Node(a.index, a.weight, a.from)).collect(Collectors.toList());


			deepCopy.put(key, collect);
		}

		return deepCopy;
	}

	private static double modularity(
			HashMap<Integer, List<Node>> initialConnections,
			double totalWeightSum,
			int[] allIndexes,
			HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths) {
		double sum = 0;

		//za svaki cvor i
		for (int i = 0; i < allIndexes.length; i++) {
			//za svaki cvor j
			for (int j = 0; j < allIndexes.length; j++) {


				double ku = initialConnections.get(allIndexes[i]).stream().mapToDouble(a -> a.weight).sum();


				double kv = initialConnections.get(allIndexes[j]).stream().mapToDouble(a -> a.weight).sum();


				int finalJ = j;
				double Auv = initialConnections.get(allIndexes[i])
				                               .stream()
				                               .filter(a -> a.index == allIndexes[finalJ])
				                               .mapToDouble(a -> a.weight)
				                               .findFirst().orElse(0);

				List<List<Node>> lists = paths.get(allIndexes[i]).get(allIndexes[j]);

				int delta = ((lists == null) ? 0 : 1);


				sum = sum + ((Auv - ((ku * kv) / (2.0 * totalWeightSum))) * delta);
			}
		}

		double Q = sum / (2.0 * totalWeightSum);


		if (Q < Math.pow(10, -5)) {
			return 0;
		} else {
			return round(Q, 4);
		}
	}

	private static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private static HashMap<Integer, HashMap<Integer, List<List<Node>>>> startBFS(HashMap<Integer, List<Node>> connections, int[] allIndexes) {
		BFS.paths.clear();

		for (int firstIndex : allIndexes) {
			for (int secondIndex : allIndexes) {
				BFS.pretrazi(firstIndex, connections, secondIndex);
			}
		}
		return BFS.paths;
	}

	private static TreeSet<Pair> removeEdgeWithHighestBetweenness(
			HashMap<Integer, List<Node>> connections,
			HashMap<Integer, HashMap<Integer, Double>> edgeBetweenness,
			double maxBetweenness) {

		TreeSet<Pair> removed = new TreeSet<>();

		for (Map.Entry<Integer, HashMap<Integer, Double>> fromVertexToVertexWithBetweenness : edgeBetweenness.entrySet()) {
			Integer fromVertex = fromVertexToVertexWithBetweenness.getKey();
			HashMap<Integer, Double> toVertexesWithBetweenness = fromVertexToVertexWithBetweenness.getValue();

			for (Map.Entry<Integer, Double> toVertexAndBetweenness : toVertexesWithBetweenness.entrySet()) {
				Integer toVertex = toVertexAndBetweenness.getKey();
				Double betweenness = toVertexAndBetweenness.getValue();

				if (betweenness == maxBetweenness) {
					//MICANJE EDGA I ISPIS
					List<Node> outgoingFromVertex = connections.get(fromVertex);

					for (Node node : outgoingFromVertex) {
						if (node.index == toVertex) {
							removed.add(new Pair(fromVertex, toVertex));
						}
					}
				}
			}
		}

		//remove connections
		for (Pair pair : removed) {
			connections.get(pair.lower).removeIf(a -> a.index == pair.higher);
			connections.get(pair.higher).removeIf(a -> a.index == pair.lower);
		}

		return removed;
	}

	private static HashMap<Integer, HashMap<Integer, Double>> calculateEdgeBetweenness(
			HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths) {

		//za fromVertex -> HM <toVertex, betweenness>
		HashMap<Integer, HashMap<Integer, Double>> edgeBetweenness = new HashMap<>();


		//all shortest paths between all vertexes
		List<List<List<Node>>> allShortestPathsFromAllVertexes = paths.values()
		                                                              .stream()
		                                                              .flatMap(integerListHashMap -> integerListHashMap.values().stream())
		                                                              .collect(Collectors.toList());

		//all shortest paths from one node to another
		for (List<List<Node>> listOfShortestPaths : allShortestPathsFromAllVertexes) {
			//number of shortest paths between 2 nodes
			int N = listOfShortestPaths.size();

			for (List<Node> shortestPath : listOfShortestPaths) {
				//za svaki cvor
				for (int i = 0; i < shortestPath.size() - 1; i++) {
					Node node = shortestPath.get(i);
					Node nextNode = shortestPath.get(i + 1);


					if (edgeBetweenness.putIfAbsent(node.index,
							new HashMap<>() {{
								this.put(nextNode.index, 1.0 / N);
							}}

					) != null) {
						//ako je bilo mappinga za key
						//dohvati sve
						HashMap<Integer, Double> map = edgeBetweenness.get(node.index);
						if (map.putIfAbsent(nextNode.index, 1.0 / N) != null) {
							Double aDouble = map.get(nextNode.index);
							map.put(nextNode.index, aDouble + 1.0 / N);
						}
					}
				}
			}
		}

		for (Map.Entry<Integer, HashMap<Integer, Double>> map : edgeBetweenness.entrySet()) {
			HashMap<Integer, Double> value = map.getValue();

			for (Map.Entry<Integer, Double> entry : value.entrySet()) {
				Integer key = entry.getKey();
				Double value1 = entry.getValue();
				value.put(key, round(value1, 4));
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
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}
			Pair pair = (Pair) o;
			return this.lower == pair.lower && this.higher == pair.higher;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.lower, this.higher);
		}

		@Override
		public int compareTo(Pair o) {
			if (o.lower == this.lower) {
				return Integer.compare(this.higher, o.higher);
			} else {
				return Integer.compare(this.lower, o.lower);
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
