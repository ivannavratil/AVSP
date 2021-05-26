import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BFS {

	public static void search(Integer s0, List<Set<Integer>> succ, HashSet<Integer> goal) {

		boolean solutionFound = false;
		Node best = null;

		//source state
		LinkedList<Node> open = new LinkedList<>();
		open.add(new Node(s0, 0));

		//visited states
		Set<Integer> visited = new HashSet<>();

		while (!open.isEmpty()) {
			Node n = open.remove(0);

			if (goal.contains(n.state)) {
				if (best == null) {
					solutionFound = true;
					best = new Node(n.state, n.cost);
				} else if ((best.cost > n.cost) || (best.cost == n.cost && best.state > n.state)) {
					best = new Node(n.state, n.cost);
				}
			}

			//if we have reached goal
			if ((solutionFound && (n.cost > best.cost)) || (n.cost > 10)) {
				break;
			}

			//if we haven't reached goal
			visited.add(n.state);

			Set<Integer> nextStates = succ.get(n.state);

			for (Integer c : nextStates) {
				if (!visited.contains(c)) {
					open.add(new Node(c, n.cost + 1));
				}
			}
		}

		if (best == null) {
			ClosestBlackNode.solutions[s0] = new ClosestBlackNode.Structure(-1, -1);
		} else {
			ClosestBlackNode.solutions[s0] = new ClosestBlackNode.Structure(best.state, best.cost);
		}
	}
}
