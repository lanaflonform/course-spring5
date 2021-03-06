package org.iproduct.spring.restmvc.init;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.spring.restmvc.model.Article;
import org.iproduct.spring.restmvc.model.User;
import org.iproduct.spring.restmvc.service.ArticleService;
import org.iproduct.spring.restmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    ArticleService articleService;

    @Autowired
    UserService userService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Users init
        long usersCount = userService.getUsersCount();
        log.info("Users count: {}", usersCount);
//
        if (usersCount == 0) {
            List<User> defaultUsers = Arrays.asList(
                    new User("admin", "admin", "DEFAULT", "ADMIN", "ROLE_ADMIN"),
                    new User("ivan", "ivan", "Ivan", "Petrov", "ROLE_USER")
            );

            userService.createUsersBatch(defaultUsers);

//            SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(users);
//            jdbcTemplate.batchUpdate(UserRepositoryImpl.INSERT_SQL, batch);
        }

        log.info("Querying for user records:");
        List<User> users = userService.getUsers();
        users.forEach(user -> log.info("{}", user.getUsername()));


        // Articles init
        long countBefore = articleService.getArticlesCount();
        if (countBefore == 0 && users.size() > 1) {
            List<Article> mockArticles = Arrays.asList(
                    new Article("Welcome to Spring 5", "Spring 5 is great beacuse ...", users.get(0)),
                    new Article("Dependency Injection", "Should I use DI or lookup ...", users.get(0)),
                    new Article("Ne", "There are several ways to configure Spring beans.", users.get(1))
            );
            try {
                List<Article> created = articleService.createArticlesBatch(mockArticles);
                log.info(">>> Articles batch created: {}", created);
            } catch (ConstraintViolationException ex) {
                log.error(">>> Constraint violation inserting articles: {} - {}", mockArticles, ex.getMessage());
            }
            long countAfter = articleService.getArticlesCount();
            log.info(">>> Total count of articles created: {}", countAfter - countBefore);
        }
    }


}
