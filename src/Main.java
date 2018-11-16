import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import decisiontree.DecisionTree;

public class Main {

	public static void main(String[] args) {
		List<String> testAttributeLabels;
		List<Map<String, String>> testDataSet;
		File testDataFile = new File("test-data.csv");
		Scanner stdInScanner = new Scanner(System.in);
		Scanner fileScanner = null;
		DecisionTree basicDecisionTree;

		try {
			fileScanner = new Scanner(testDataFile);
			String columnNames = fileScanner.nextLine();
			testAttributeLabels = Arrays.asList(columnNames.split("\\s*,\\s*"));
			testDataSet = new ArrayList<>();
			while (fileScanner.hasNextLine()) {
				Map<String, String> row = new HashMap<>();
				for (String attribute : testAttributeLabels) {
					row.put(attribute, fileScanner.next().replaceAll(",\\s*", ""));
				}
				testDataSet.add(row);
			}

			basicDecisionTree = new DecisionTree(testDataSet, testAttributeLabels);
			basicDecisionTree.learn();
			
			System.out.println("Part 1: Build and print the decision tree.");
			System.out.println("----------------------------");
			System.out.println(basicDecisionTree);
			
			System.out.println("\nPart 2: Predict a user entered tuple.");
			String again = "No";
			
			do {
				System.out.println("----------------------------");
				Map<String, String> userEnteredDataPoint = getUserInput(testAttributeLabels, stdInScanner);
				String predictedClass = basicDecisionTree.predict(userEnteredDataPoint);
				
				System.out.println("For user entered tuple: " + userEnteredDataPoint);
				System.out.println("Predicted class for " 
									+ testAttributeLabels.get(testAttributeLabels.size() - 1) 
									+ ": " + predictedClass);
				
				System.out.println("Would you like to predict the class of another tuple?");
				again = stdInScanner.next();
			} while (again.toLowerCase() == "yes" || again.toLowerCase() == "y");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fileScanner != null) {
				fileScanner.close();
			}
			stdInScanner.close();
		}
	}
	
	public static Map<String, String> getUserInput(List<String> attributeLabels, Scanner systemInScanner) {
		Map<String, String> userInput = new HashMap<>();
		
		// get user input for all fields except desired class to predict
		for (int i = 0; i < attributeLabels.size() - 1; i++) {
			System.out.println("Please enter the lower_case value of the following attribute: " 
								+ attributeLabels.get(i));
			userInput.put(attributeLabels.get(i), systemInScanner.next());
		}
		
		return userInput;
	}

}
