import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Node {

	private List<Map<String, String>> dataSet;
	private List<String> attributeList;
	private String attributeToPredict;
	private String decidedClass;
	private String splittingCriterion;
	private Map<String, Node> nextLevel;

	public Node(List<Map<String, String>> dataSet, List<String> attributeList) {
		this.dataSet = dataSet;
		this.attributeList = attributeList;
		this.attributeToPredict = attributeList.get(attributeList.size() - 1);
		this.decidedClass = null;
		this.splittingCriterion = null;
		this.nextLevel = null;
		// recursively call generateDecisionTree to finish creating the tree
		// generateDecisionTree();
	}

	public void generateDecisionTree() {
		// if all tuples of same class, label with class C
		if (this.tuplesAreOfSameClass()) {
			this.decidedClass = dataSet.get(0).get(attributeToPredict);
			return;
		}
		// if attribute list is empty (only contains attribute we want to predict), 
		// label with majority class in dataSet
		if (attributeList.size() == 1) {
			this.decidedClass = getMajorityClass();
			return;
		}
		// apply attribute selection
		// get splitting criterion and remove from attribute list
		this.splittingCriterion = attributeSelectionMethod();
		attributeList = attributeList.stream().filter(x -> x != splittingCriterion).collect(Collectors.toList());

		// split data and create next levels
		this.nextLevel = new HashMap<>();
		List<String> splittingCriterionClasses = dataSet.stream().map(x -> x.get(splittingCriterion))
				.collect(Collectors.toList());
		Set<String> mySet = new HashSet<String>(splittingCriterionClasses);
		
		for (String classLabel : mySet) {
			List<Map<String, String>> relevantDataSet = dataSet.stream().filter(x -> x.get(splittingCriterion) == classLabel)
					.collect(Collectors.toList());
			nextLevel.put(classLabel, new Node(relevantDataSet, attributeList));
		}
	}

	public boolean tuplesAreOfSameClass() {
		List<String> attributeLabels = dataSet.stream().map(x -> x.get(attributeToPredict)).collect(Collectors.toList());;
		long countOfUniqueLabels = attributeLabels.stream().distinct().count();
		return countOfUniqueLabels == 1;
	}

	public String getMajorityClass() {
		List<String> attributeLabels = dataSet.stream().map(x -> x.get(attributeToPredict)).collect(Collectors.toList());
		Set<String> mySet = new HashSet<String>(attributeLabels);
		int countMajority = 0;
		String majorityClassLabel = "";

		for (String label : mySet) {
			int count = Collections.frequency(attributeLabels, label);
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

		// Calculate Info(D)
		double totalEntropy = calculateEntropy(attributeToPredict, dataSet);

		double maxGain = 0;
		String attributeSelected = "";

		// Calculate Info_Attr(D) for each attribute
		// Calculate Info Gain using these two and pick attribute with max
		for (String attribute : attributeList) {
			double infoAttribute = 0;
			List<String> attributeClasses = dataSet.stream().map(x -> x.get(attribute)).collect(Collectors.toList());
			Set<String> classSet = new HashSet<String>(attributeClasses);
			for (String label : classSet) {
				// Figure out a better way to fix this warning
				List<Map<String, String>> relevantDataSet = dataSet.stream().filter(x -> x.get(attribute) == label)
						.collect(Collectors.toList());
				double frequencyOfLabel = ((double) Collections.frequency(attributeClasses, label))
						/ attributeClasses.size();
				infoAttribute += frequencyOfLabel * calculateEntropy(attributeToPredict, relevantDataSet);
			}

			double gain = totalEntropy - infoAttribute;
			if (gain > maxGain) {
				maxGain = gain;
				attributeSelected = attribute;
			}
		}

		// select attribute with highest gain
		return attributeSelected;
	}

	public double calculateEntropy(String entropyAttribute, List<Map<String, String>> data) {
		// Also known as Info(D) in the book
		double entropy = 0;
		List<String> attributeToPredictLabels = data.stream().map(x -> x.get(entropyAttribute))
				.collect(Collectors.toList());
		Set<String> classSet = new HashSet<String>(attributeToPredictLabels);

		for (String label : classSet) {
			int countOfLabel = Collections.frequency(attributeToPredictLabels, label);
			double probabilityOfLabel = ((double) countOfLabel) / attributeToPredictLabels.size();
			entropy -= probabilityOfLabel * logBase2(probabilityOfLabel);
		}

		return entropy;
	}

	public double logBase2(double number) {
		return Math.log(number) / Math.log(2);
	}

	public String classify(Map<String, String> dataObject) {
		return "";
	}
	
	@Override
	public String toString() {
		return "Node [splittingCriterion=" + splittingCriterion + ", nextLevel=" + nextLevel + ", decidedClass="
				+ decidedClass + "]";
	}
}
