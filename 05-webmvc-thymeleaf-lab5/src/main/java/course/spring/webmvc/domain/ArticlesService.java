package course.spring.webmvc.domain;

import course.spring.webmvc.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticlesService {
    List<Article> getArticles();
    Optional<Article> getArticleById(String articleId);
    Article add(Article article);
    Article update(Article article);
    Optional<Article> delete(String articleId);

}
