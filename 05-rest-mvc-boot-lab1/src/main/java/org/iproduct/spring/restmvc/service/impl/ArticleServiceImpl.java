package org.iproduct.spring.restmvc.service.impl;

import org.iproduct.spring.restmvc.dao.ArticleRepository;
import org.iproduct.spring.restmvc.exception.EntityNotFoundException;
import org.iproduct.spring.restmvc.model.Article;
import org.iproduct.spring.restmvc.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Primary
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository repo;

    @Override
    public List<Article> getArticles() {
        return repo.findAll();
    }

    @Override
    public List<Article> getArticlesByAuthorId(String userId) {
        return repo.findAllByAuthorId(userId);
    }

    @Override
    public Article createArticle(Article article) {
        article.setCreated(LocalDateTime.now());
        article.setUpdated(LocalDateTime.now());
        return repo.insert(article);
    }

    @Override
    public Article updateArticle(Article article) {
        article.setUpdated(LocalDateTime.now());
        return repo.save(article);
    }

    @Override
    public Article getArticleById(String id) {
        return repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Article with ID=%s not found.", id)));
    }

    @Override
    public Article deleteArticle(String id) {
        Article old = repo.findById(id).orElseThrow( () ->
                new EntityNotFoundException(String.format("Article with ID=%s not found.", id)));
        repo.deleteById(id);
        return old;
    }
}
