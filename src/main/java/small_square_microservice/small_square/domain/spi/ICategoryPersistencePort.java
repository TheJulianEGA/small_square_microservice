package small_square_microservice.small_square.domain.spi;

import small_square_microservice.small_square.domain.model.Category;

public interface ICategoryPersistencePort {

    Category getCategoryById(Long id);
}
