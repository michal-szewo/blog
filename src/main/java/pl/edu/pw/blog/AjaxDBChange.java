package pl.edu.pw.blog;

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
}
