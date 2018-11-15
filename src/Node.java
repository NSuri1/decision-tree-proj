import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node {

	private Map<String, String>[] dataSet;
	private String[] attributeList;
	private String attributeToPredict;
	private String decidedClass;
	private String splittingCriterion;
	private Map<String, Node> nextLevel;

	public Node(Map<String, String>[] dataSet, String[] attributeList) {
		this.dataSet = dataSet;
		this.attributeList = attributeList;
		this.attributeToPredict = attributeList[attributeList.length - 1];
		this.decidedClass = null;
		this.splittingCriterion = null;
		this.nextLevel = null;
		generateDecisionTree();
	}
	
	public void generateDecisionTree() {
		// if all tuples of same class, label with class C
		if (this.tuplesAreOfSameClass()) {
			this.decidedClass = dataSet[0].get(attributeToPredict);
			return;
		}
		// if attribute list is 0, label with majority class in dataSet
		if (attributeList.length == 0) {
			this.decidedClass = getMajorityClass();
			return;
		}
		// apply attribute selection
		// get splitting criterion and remove from attribute list
		this.splittingCriterion = attributeSelectionMethod();
		attributeList = Arrays.stream(attributeList)
						.filter(x -> x != splittingCriterion)
						.toArray(String[]::new);
		
		// need to split data and create next levels
	}

	public boolean tuplesAreOfSameClass() {
		String[] attributeLabels = Arrays.stream(dataSet)
									.map(x -> x.get(attributeToPredict))
									.toArray(String[]::new);
		long countOfUniqueLabels = Arrays.stream(attributeLabels)
									.distinct().count();
		return countOfUniqueLabels == 1;
	}
	
	public String getMajorityClass() {
		String[] attributeLabels = Arrays.stream(dataSet)
									.map(x -> x.get(attributeToPredict))
									.toArray(String[]::new);
		
		List<String> labelsAsList = Arrays.asList(attributeLabels);
		Set<String> mySet = new HashSet<String>(labelsAsList);
		int countMajority = 0;
		String majorityClassLabel = "";

		for (String label: mySet) {
			int count = Collections.frequency(labelsAsList, label);
			if (count > countMajority) {
				countMajority = count;
				majorityClassLabel = label;
			}
		}

		return majorityClassLabel;
	}

	public String attributeSelectionMethod() {
		// Assuming discrete valued attributes and 
		// using information gain to select splitting attribute
		
		// calculate information gain for all attributes
		// select attribute with highest gain
		
		return "";
	}

	public String classify(Map<String, String> dataObject) {
		return "";
	}
}
