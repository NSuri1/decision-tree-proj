package decisiontree;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class to represent a node in a basic decision tree.
 * Currently, can be either an inner node, or a leaf
 * node. Inner nodes contain a splitting criterion, or attribute, which
 * is somewhat of a test to run on a tuple telling us which way to branch.
 * Leaf nodes contain the class of a tuple that ends on its path after applying
 * a set of tests (which are the above branches).
 * @author Nalin Suri, Jessica Moreno, Martyn Mallo
 *
 */
public class Node {

	private List<Map<String, String>> dataSet;	// data set at each node
	private List<String> attributeList;			// list of attributes remaining at this node
	private String attributeToPredict;			// attribute we want to classify/predict
	private String decidedClass;					// class of an object on this path, used only at leaf nodes
	private String splittingCriterion;			// attribute to perform test on, used only at inner nodes
	private Map<String, Node> nextLevel;			// branches leading to next inner nodes or leaf nodes

	/**
	 * Primary constructor of Node class to be used by Decision Tree
	 * @param dataSet		filled out learning data
	 * @param attributeList	list of attribute labels where the last index 
	 * 						is the label of the attribute we will want to 
	 * 						classify
	 */
	public Node(List<Map<String, String>> dataSet, List<String> attributeList) {
		this.dataSet = dataSet;
		this.attributeList = attributeList;
		this.attributeToPredict = attributeList.get(attributeList.size() - 1);
		this.decidedClass = null;
		this.splittingCriterion = null;
		this.nextLevel = null;
	}

	/**
	 * Method to go through the act of generating the decision tree recursively.
	 * Follows the basic algorithm for inducing a decision tree from training tuples.
	 * Figure 8.3 in "Data Mining: Concepts and Techniques" by Han, Kamber, and Pei
	 */
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
			List<Map<String, String>> relevantDataSet = dataSet.stream()
					.filter(x -> x.get(splittingCriterion).equals(classLabel)).collect(Collectors.toList());
			nextLevel.put(classLabel, new Node(relevantDataSet, attributeList));
			// recursively generate the tree
			nextLevel.get(classLabel).generateDecisionTree();	
		}
	}

	/**
	 * Method to check if all tuples in the data set are of the same class
	 * @return true if all tuples are off same class in the attribute we want to predict,
	 * 			else false
	 */
	public boolean tuplesAreOfSameClass() {
		List<String> attributeLabels = dataSet.stream().map(x -> x.get(attributeToPredict))
				.collect(Collectors.toList());
		long countOfUniqueLabels = attributeLabels.stream().distinct().count();
		return countOfUniqueLabels == 1;
	}

	/**
	 * Figures out what the dominant class of the attribute we want to predict in the data set
	 * @return majority class, or the class that occurs the most in the attribute we want to predict
	 */
	public String getMajorityClass() {
		List<String> attributeLabels = dataSet.stream().map(x -> x.get(attributeToPredict))
				.collect(Collectors.toList());
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

	/**
	 * Performs the attribute selection method to choose the attribute to split on.
	 * For this purpose, we will assume discrete valued attributes and use
	 * the information gain algorithm to choose the attribute to split on
	 * @return attribute that gives best split if chosen as a test on the data set
	 */
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
			if (attribute == attributeToPredict) {
				continue;
			}
			
			double infoAttribute = 0;
			List<String> attributeClasses = dataSet.stream().map(x -> x.get(attribute)).collect(Collectors.toList());
			Set<String> classSet = new HashSet<String>(attributeClasses);
			for (String label : classSet) {
				// Figure out a better way to fix this warning
				List<Map<String, String>> relevantDataSet = dataSet.stream().filter(x -> x.get(attribute).equals(label))
						.collect(Collectors.toList());
				double probabilityOfClass = ((double) Collections.frequency(attributeClasses, label))
						/ attributeClasses.size();
				infoAttribute += probabilityOfClass * calculateEntropy(attributeToPredict, relevantDataSet);
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

	/**
	 * Calculate Info(D) = -Σ (p_i * log(p_i)) from i = 1 to m
	 * where p_i is the nonzero probability that an arbitrary tuple in D belongs to class C_i
	 * Also thought of as "average amount of information needed to identify 
	 * the class label of a tuple in D"
	 * @param entropyAttribute 
	 * @param data
	 * @return Info(D)
	 */
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

	/**
	 * Helper method used to calculate the log of a number
	 * @param number arbitrary number
	 * @return log base 2 of the input number
	 */
	public double logBase2(double number) {
		return Math.log(number) / Math.log(2);
	}

	/**
	 * Method to classify a new tuple
	 * @param dataObject		tuple to predict the class of	
	 * @return				predicted class of input tuple
	 */
	public String classify(Map<String, String> dataObject) {
		// if we have reached a leaf node, return classification
		if (decidedClass != null) {
			return decidedClass;
		}
		
		// inner test node. 
		// we have splitting criteria attribute in object we want to predict
		if (dataObject.containsKey(splittingCriterion)) {
			String value = dataObject.get(splittingCriterion);
			
			// if it is "valid" or something we have seen before,
			// recursively step down the tree
			if (nextLevel.containsKey(value)) {
				return nextLevel.get(value).classify(dataObject);
			}
		}
		
		// we have don't know how to classify this object so
		// we will just return the majority class in our dataset
		// at this point in the tree
		System.out.println("Not sure how to classify this, best guess is the majority class at this point in the tree!");
		return getMajorityClass();
	}

	@Override
	public String toString() {
		if (splittingCriterion == null) 
			return String.format("Node [decidedClass=%s]", decidedClass);
		return String.format("Node [splittingCriterion=%s, nextLevel=%s]", splittingCriterion, nextLevel);
	}
}
