import java.util.Map;

public class DecisionTree {
	Map<String, String>[] dataSet;
	String[] attributeList;
	private Node root;

	public DecisionTree(Map<String, String>[] dataSet, String[] attributeList) {
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

}
