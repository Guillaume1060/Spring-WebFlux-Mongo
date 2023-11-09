package guru.springframework.springwebfluxrest.controllers;

import guru.springframework.springwebfluxrest.domain.Category;
import guru.springframework.springwebfluxrest.domain.Vendor;
import guru.springframework.springwebfluxrest.repositories.CategoryRepository;
import guru.springframework.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorController vendorController;
    VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstname("Pierre").build(),
                        Vendor.builder().firstname("Luc").build()));

        webTestClient.get()
                .uri(VendorController.URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById("id"))
                .willReturn (Mono.just(Vendor.builder().firstname("Hughes").build()));

        webTestClient.get()
                .uri(VendorController.URL+"/id")
                .exchange()
                .expectBodyList(Vendor.class);
    }

    @Test
    void createVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstname("Eric").build());

        webTestClient.post()
                .uri(VendorController.URL)
                .body(vendorToSaveMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateVendor() {
        given(vendorRepository.save(Vendor.builder().build()))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri(VendorController.URL+"/id")
                .body(vendorMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}