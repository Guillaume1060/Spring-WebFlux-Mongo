package guru.springframework.springwebfluxrest.controllers;

import guru.springframework.springwebfluxrest.domain.Category;
import guru.springframework.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void listTest() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("cat1").build(),
                        Category.builder().description("cat2").build()));

        webTestClient.get()
                .uri(CategoryController.URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getByIdTest() {
        BDDMockito.given(categoryRepository.findById("id"))
                .willReturn (Mono.just(Category.builder().description("cat1").build()));

        webTestClient.get()
                .uri(CategoryController.URL+"/id")
                .exchange()
                .expectBodyList(Category.class);
    }

    @Test
    void createTest() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("some description").build());

        webTestClient.post()
                .uri(CategoryController.URL)
                .body(catToSaveMono,Category.class)
                .exchange()
                .expectStatus()
                .isCreated(); // Link to @ResponseStatus(HttpStatus.CREATED) in the method in the controller.

    }

    @Test
    void updateTest() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Some cat").build());

        webTestClient.put()
                .uri(CategoryController.URL+"/idValue")
                .body(catToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}