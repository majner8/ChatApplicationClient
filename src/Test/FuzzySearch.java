package Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class FuzzySearch {


	public static void main(String[] args) {
		
		ArrayList<String> value=new ArrayList<String>();
		value.add("mink");
		value.add("ton");
		value.add("michal");
	
		
		List<String> ret=FuzzySearch.sortBySimilarity("ton", value);
		ret.forEach((st)->{
		});
	}
	
	 private static final LevenshteinDistance LEVENSHTEIN = new LevenshteinDistance();


	  public static List<String> sortBySimilarity(String input, List<String> candidates) {
		  Comparator<String> comparator = new Comparator<String>() {
	            @Override
	            public int compare(String s1, String s2) {
	                return Integer.compare(LEVENSHTEIN.apply(s1, input), LEVENSHTEIN.apply(s2, input));
	            }
	        };

	        // Sort the candidates list using the comparator
	        Collections.sort(candidates, comparator);

	        return candidates;
	    }
	}

	  

