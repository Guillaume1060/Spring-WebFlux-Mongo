package guru.springframework.springwebfluxrest.controllers;


import guru.springframework.springwebfluxrest.domain.Category;
import guru.springframework.springwebfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(CategoryController.URL)
public class CategoryController {
    public static final String URL = "/api/v1/categories";
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    Mono<Category> update(@PathVariable String id,@RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
// TODO update with test

//        return categoryRepository
//                .findById(id)
//                .flatMap(foundCategory -> {
//                    category.setId(foundCategory.getId());
//                    return categoryRepository.save(category);
//                })
//                .switchIfEmpty(Mono.error(new Exception("Category not found")));
    }
}
