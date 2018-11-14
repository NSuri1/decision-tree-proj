import java.util.HashMap;
import java.util.Map;

public class Node {

	private Map<String, String>[] dataSet;
	private String[] attributeList;
	private String attributeToPredict;
	private String decidedClass;
	private Map<String, Node> nextLevel;

	public Node(Map<String, String>[] dataSet, String[] attributeList) {
		this.dataSet = dataSet;
		this.attributeList = attributeList;
		this.attributeToPredict = attributeList[attributeList.length - 1];
		this.decidedClass = null;
		this.nextLevel = null;
		generateDecisionTree();
	}
	
	public void generateDecisionTree() {
		if (this.tuplesAreOfSameClass()) {
			
		}
	}

	public boolean tuplesAreOfSameClass() {
		for (int i = 0; i < dataSet.length - 1; i++) {
			if (dataSet[i].get(attributeToPredict) != dataSet[i + 1].get(attributeToPredict)) {
				return false;
			}
		}

		return true;
	}

	/*
	 * (2) if tuples in D are all of the same class, C, then (3) return N as a leaf
	 * node labeled with the class C; (4) if attribute list is empty then (5) return
	 * N as a leaf node labeled with the majority class in D; // majority voting (6)
	 * apply Attribute selection method(D, attribute list) to find the “best”
	 * splitting criterion; (7) label node N with splitting criterion; (8) if
	 * splitting attribute is discrete-valued and multiway splits allowed then //
	 * not restricted to binary trees (9) attribute list attribute list 􀀀 splitting
	 * attribute; // remove splitting attribute (10) for each outcome j of splitting
	 * criterion // partition the tuples and grow subtrees for each partition (11)
	 * let Dj be the set of data tuples in D satisfying outcome j; // a partition
	 * (12) if Dj is empty then (13) attach a leaf labeled with the majority class
	 * in D to node N; (14) else attach the node returned by Generate decision
	 * tree(Dj , attribute list) to node N; endfor (15) return N;
	 * 
	 */

	public String attributeSelectionMethod() {
		return "";
	}

	public String classify(Map<String, String> dataObject) {
		return "";
	}
}
