package pl.edu.pw.blog.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import lombok.Data;
import lombok.NonNull;

@Data
public class RefineResults {
	
	@NonNull
	private List<Option> optionsList = new ArrayList<>();
	
	
	public RefineResults() {
		optionsList.add(new Option("publishedAt,desc", "daty publikacji (od najnowszych)"));
		optionsList.add(new Option("authorUsername,asc", "nazwiska autora"));
		optionsList.add(new Option("modifiedAt,desc", "daty modyfikacji (od najnowszych)"));
		optionsList.add(new Option("publishedAt,asc", "daty publikacji (od najstarszych)"));
		
	}
	
	
	
	private class Option{
		private String optionName;
		private String optionText;
		
		public Option(String optionName, String optionText) {
			this.optionName = optionName;
			this.optionText = optionText;
		}
		
		public String getOptionName() {
			return optionName;
		}
		
		public String getOptionText() {
			return optionText;
		}
			
		
		
	}
	
}
