package pl.edu.pw.blog;

import java.util.Date;

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
}
