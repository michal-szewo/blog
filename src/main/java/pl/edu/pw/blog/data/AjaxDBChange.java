package pl.edu.pw.blog.data;


import java.util.List;


import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Class gathering sizes of articles and likes table along with the newest modificationAt date and refreshed authors names list.
 * Used in conjunction with {@link pl.edu.pw.blog.web.ArticlesController#articlesChanges() articlesChanges()} class.
 * 
 * @author Michal
 *
 */
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
