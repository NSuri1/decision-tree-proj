package decisiontree;
import java.util.List;
import java.util.Map;

/**
 * A class to represent a basic decision tree. 
 * This class is dependent on the Node class we wrote and 
 * has two steps: a learning phase, and a prediction phase.
 * To be able to predict the class of a tuple, you must first go through the 
 * learning phase, which generates the tree. This true is used
 * as a set of rules to help classify the given tuple.
 * @author Nalin Suri, Jessica Moreno, Martyn Mallo
 *
 */
public class DecisionTree {
	private List<Map<String, String>> dataSet;	// main dataset for tree to work with
	private List<String> attributeList;			// list of tuple attributes
	private Node root;							// root node of decision tree

	/**
	 * Primary constructor of Decision Tree class 
	 * @param dataSet		filled out learning data
	 * @param attributeList	list of attribute labels where the last index 
	 * 						is the label of the attribute we will want to 
	 * 						classify
	 */
	public DecisionTree(List<Map<String, String>> dataSet, List<String> attributeList) {
		this.dataSet = dataSet;
		this.attributeList = attributeList;
	}

	/**
	 * Method to kick off the learning process and generate the actual tree.
	 * Can be called multiple times if you pass in a new dataSet and attributeList
	 */
	public void learn() {
		root = new Node(dataSet, attributeList);
		root.generateDecisionTree();
	}

	/**
	 * Method that attempts to predict the class of a given tuple.
	 * If the actual tree nodes have not been generated and the
	 * tree has not gone through the learning process, an error will be thrown
	 * @param dataObject		A single tuple you would like to predict the class of
	 * @return				Predicted class of the object
	 */
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
