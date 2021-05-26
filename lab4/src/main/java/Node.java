import java.util.Objects;

public class Node {

	int state;

	int cost;

	public Node(int state, int cost) {
		this.state = state;
		this.cost = cost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Node node = (Node) o;
		return node.cost == this.cost &&
		       Objects.equals(this.state, node.state);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.state, this.cost);
	}
}
