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
		if (root != null) {
			root = new Node(dataSet, attributeList);
		}
	}
	
	public String predict(Map<String, String> dataObject) {
		return root == null ? null : root.classify(dataObject);
	}
	
	public String toString() {
		return root.toString();
	}

}
