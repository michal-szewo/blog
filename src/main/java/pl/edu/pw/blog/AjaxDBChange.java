package pl.edu.pw.blog;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AjaxDBChange {
	
	@NonNull
	private Long articlesNumber;
	
	@NonNull
	private Long likesNumber;
	
	@NonNull
	private Long maxModifiedDate;
	
	@NonNull
	private List<String> authorsList;
}
