import java.util.*;

public class BFS {

	static HashMap<Integer, HashMap<Integer, List<List<Node>>>> paths = new HashMap<>();

	public static void pretrazi(int s0, HashMap<Integer, List<Node>> succ, int goal) {

		//source state
		LinkedList<Node> open = new LinkedList<>();
		open.add(new Node(s0, 0));

		//visited states
		Set<Integer> visited = new HashSet<>();

		while (!open.isEmpty()) {
			//get first from queue
			Node n = open.remove(0);

			//if we have reached goal
			if (goal == (n.index)) {
				visited.add(n.index);
				//print path
				List<Node> nodes = reversePath(n);

				addToMap(s0, goal, nodes);
				//return;
			}


			//if we haven't reached goal
			visited.add(n.index);

			List<Node> nextStates = succ.get(n.index);

			for (Node c : nextStates) {
				if (!visited.contains(c.index)) {
					open.add(new Node(c.index, n.weight + c.weight, n));
				}
			}
		}
	}

	private static void addToMap(int s0, int goal, List<Node> nodes) {

		//vraca null ako nije bilo mappinga ili ako je mapping null
		if (paths.putIfAbsent(s0, new HashMap<>() {{
			//ako nema mappinga onda ga napravi
			this.put(goal, new ArrayList<>() {{
				this.add(nodes);
			}});
		}}) != null) {
			//a ako je bilo mappinga onda

			//dohvati sve moguće puteve iz s0 do ostalih cvorova
			HashMap<Integer, List<List<Node>>> map = paths.get(s0);

			//vraca null ako nije bilo mappinga ili ako je mapping null
			if (map.putIfAbsent(goal, new ArrayList<>() {{
				this.add(nodes);
			}}) != null) {
				//ako postoji mapping samo dodajem cvor

				//dodajem samo ako je put jednak ili rkaci

				List<Node> nodesForPath = map.get(goal).get(0);
				int weightForPathsInList = nodesForPath.get(nodesForPath.size() - 1).weight;

				int weightForNewPath = nodes.get(nodes.size() - 1).weight;


				if (weightForNewPath < weightForPathsInList) {
					map.get(goal).clear();
					map.get(goal).add(nodes);
				} else if (weightForNewPath == weightForPathsInList) {
					map.get(goal).add(nodes);
				}
			}
		}
	}

	public static List<Node> reversePath(Node n) {

		List<Node> path = new ArrayList<>();

		while (n != null) {
			path.add(n);
			n = n.from;
		}

		Collections.reverse(path);

		return path;
	}
}
