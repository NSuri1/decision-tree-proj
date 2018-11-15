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
		// recursively call generateDecisionTree to finish creating the tree
		// generateDecisionTree();
	}

	public void generateDecisionTree() {
		// if all tuples of same class, label with class C
		if (this.tuplesAreOfSameClass()) {
			this.decidedClass = dataSet[0].get(attributeToPredict);
			return;
		}
		// if attribute list is empty (only contains attribute we want to predict), 
		// label with majority class in dataSet
		if (attributeList.length == 1) {
			this.decidedClass = getMajorityClass();
			return;
		}
		// apply attribute selection
		// get splitting criterion and remove from attribute list
		this.splittingCriterion = attributeSelectionMethod();
		attributeList = Arrays.stream(attributeList).filter(x -> x != splittingCriterion).toArray(String[]::new);

		// split data and create next levels
		String[] splittingCriterionClasses = Arrays.stream(dataSet).map(x -> x.get(splittingCriterion))
				.toArray(String[]::new);
		List<String> labelsAsList = Arrays.asList(splittingCriterionClasses);
		Set<String> mySet = new HashSet<String>(labelsAsList);
		
		for (String classLabel : mySet) {
			@SuppressWarnings("unchecked")
			Map<String, String>[] relevantDataSet = Arrays.stream(dataSet).filter(x -> x.get(splittingCriterion) == classLabel)
					.toArray(HashMap[]::new);
			nextLevel.put(classLabel, new Node(relevantDataSet, attributeList));
		}
	}

	public boolean tuplesAreOfSameClass() {
		String[] attributeLabels = Arrays.stream(dataSet).map(x -> x.get(attributeToPredict)).toArray(String[]::new);
		long countOfUniqueLabels = Arrays.stream(attributeLabels).distinct().count();
		return countOfUniqueLabels == 1;
	}

	public String getMajorityClass() {
		String[] attributeLabels = Arrays.stream(dataSet).map(x -> x.get(attributeToPredict)).toArray(String[]::new);

		List<String> labelsAsList = Arrays.asList(attributeLabels);
		Set<String> mySet = new HashSet<String>(labelsAsList);
		int countMajority = 0;
		String majorityClassLabel = "";

		for (String label : mySet) {
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

		// Calculate Info(D)
		double totalEntropy = calculateEntropy(attributeToPredict, dataSet);

		double maxGain = 0;
		String attributeSelected = "";

		// Calculate Info_Attr(D) for each attribute
		// Calculate Info Gain using these two and pick attribute with max
		for (String attribute : attributeList) {
			double infoAttribute = 0;
			String[] attributeClasses = Arrays.stream(dataSet).map(x -> x.get(attribute)).toArray(String[]::new);
			List<String> classesAsList = Arrays.asList(attributeClasses);
			Set<String> classSet = new HashSet<String>(classesAsList);
			for (String label : classSet) {
				// Figure out a better way to fix this warning
				@SuppressWarnings("unchecked")
				Map<String, String>[] relevantDataSet = Arrays.stream(dataSet).filter(x -> x.get(attribute) == label)
						.toArray(HashMap[]::new);
				double frequencyOfLabel = ((double) Collections.frequency(classesAsList, label))
						/ attributeClasses.length;
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

	public double calculateEntropy(String entropyAttribute, Map<String, String>[] data) {
		// Also known as Info(D) in the book
		double entropy = 0;
		String[] attributeToPredictLabels = Arrays.stream(data).map(x -> x.get(entropyAttribute))
				.toArray(String[]::new);
		List<String> labelsAsList = Arrays.asList(attributeToPredictLabels);
		Set<String> classSet = new HashSet<String>(labelsAsList);

		for (String label : classSet) {
			int countOfLabel = Collections.frequency(labelsAsList, label);
			double probabilityOfLabel = ((double) countOfLabel) / attributeToPredictLabels.length;
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
}
