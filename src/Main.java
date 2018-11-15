import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		List<String> testAttributeLabels;
		List<Map<String, String>> testDataSet;
		File testDataFile = new File("test-data.csv");
		Scanner fileScanner;
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
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
