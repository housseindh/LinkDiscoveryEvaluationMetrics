package org.LinkDiscoveryValidation;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class EvaluationMetrics {

	static HashMap<String, String> goldStandardTable = new HashMap<>();

	public static void main(String[] args) {

		String goldStandardFile = args[0];
		String linkDiscoveryFile = args[1];

		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(goldStandardFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				goldStandardTable.put(line[0], line[1]);
			}

			compare(linkDiscoveryFile);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void compare(String CsvFile) throws FileNotFoundException, IOException {
		CSVReader reader;
		String[] line;
		int errorInCompare = 0;
		int tp = 0;
		int fp = 0;
		int fn = 0;
		int total = 0;
		Double precesion = 0.0;
		Double recall = 0.0;
		double fscore = 0.0;
		reader = new CSVReader(new FileReader(CsvFile), ' ');
		while ((line = reader.readNext()) != null) {
			total++;
			String value = goldStandardTable.get(line[0]); 
			//line[0]->subject  ---  line[1]->predicate  ---  line[2]->object
			if (value == null || value == "")
				fp++;
			else if (!value.equals(line[2])) {
				errorInCompare++;
				fp++;
			} else
				tp++;

		}
		fn = (goldStandardTable.size() - tp);
		precesion = (double) tp / (tp + fp);
		recall = (double) tp / (tp + fn);

		fscore = 2 * precesion * recall / (precesion + recall);

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);

		System.out.println(
				"FP=" + fp + " TP=" + tp + " FN=" + fn + "\nPrecesion=" + df.format(precesion) + " Recall="
				+ df.format(recall) + " F-1=" + df.format(fscore) + "\ntotal=" + total + " errorInCompare="
				+ errorInCompare);
	}
}