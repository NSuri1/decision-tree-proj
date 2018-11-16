import java.util.List;
import java.util.Map;

public class DecisionTree {
	private List<Map<String, String>> dataSet;
	private List<String> attributeList;
	private Node root;

	public DecisionTree(List<Map<String, String>> dataSet, List<String> attributeList) {
		this.dataSet = dataSet;
		this.attributeList = attributeList;
	}

	public void learn() {
		root = new Node(dataSet, attributeList);
		root.generateDecisionTree();
	}

	public String predict(Map<String, String> dataObject) {
		if (root == null) {
			throw new Error("Decision Tree has not been generated. Call the learn method to create the actual tree.");
		}
		return root.classify(dataObject);
	}

	public String toString() {
		return root.toString();
	}

}
