package pl.edu.pw.blog;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AjaxMessage {
	
	@NonNull
	private Long articlesNumber;
	
	
}
