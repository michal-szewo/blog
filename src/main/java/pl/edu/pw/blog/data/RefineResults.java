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
		optionsList.add(new Option("publishedAt", "Data publikacji"));
		optionsList.add(new Option("author", "Autora"));
		optionsList.add(new Option("modifiedAt", "Daty modyfikacji"));
		
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
