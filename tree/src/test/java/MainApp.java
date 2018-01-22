import java.util.List;

import tree.control.NodeController;
import tree.model.Node;

public class MainApp {
    public static void main(String[] args) {
	NodeController ctrl = new NodeController();

	Long node1 = ctrl.addNode("blah 1", "blah 1", null, "blah 1");
	System.out.println(node1);

	Long node2 = ctrl.addNode("blah 2", "blah 2", node1, "blah 2");
	System.out.println(node2);

	Long node3 = ctrl.addNode("blah 3", "blah 3", node2, "blah 3");
	System.out.println(node3);

	node3 = ctrl.updateNode(node3, null, null, 1L, null);
	System.out.println(node3);

	List<Node> children = ctrl.getChildren(node1);
	System.out.println(children);

	List<Node> root = ctrl.getRoot();
	System.out.println(root);
    }
}