package guru.springframework.springwebfluxrest.bootstrap;

import guru.springframework.springwebfluxrest.domain.Category;
import guru.springframework.springwebfluxrest.domain.Vendor;
import guru.springframework.springwebfluxrest.repositories.CategoryRepository;
import guru.springframework.springwebfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count().block() == 0) {
            // load data
            Flux<Category> categoryFluxToInsert = Flux.just(
                    Category.builder()
                            .description("Fruits").build(),
                    Category.builder()
                            .description("Nuts").build(),
                    Category.builder()
                            .description("Breads").build(),
                    Category.builder()
                            .description("Meats").build(),
                    Category.builder()
                            .description("Eggs").build());

            categoryRepository.saveAll(categoryFluxToInsert).subscribe();
            System.out.println("Loaded Categories");

            Flux<Vendor> vendorFluxToInsert = Flux.just(
                    Vendor.builder()
                            .firstname("Pierre").lastname("ramon").build(),
                    Vendor.builder()
                            .firstname("Cedric").lastname("ramon").build(),
                    Vendor.builder()
                            .firstname("Jean").lastname("Valjean").build(),
                    Vendor.builder()
                            .firstname("Luc").lastname("Alphand").build(),
                    Vendor.builder()
                            .firstname("Guy").lastname("Forget").build());

            vendorRepository.saveAll(vendorFluxToInsert).subscribe();
            System.out.println("Loaded Vendors");
        }
    }
}
