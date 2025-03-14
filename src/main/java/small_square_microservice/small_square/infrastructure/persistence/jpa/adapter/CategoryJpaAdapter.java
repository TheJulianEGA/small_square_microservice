package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import small_square_microservice.small_square.domain.exception.notfound.CategoryNotFoundException;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.domain.spi.ICategoryPersistencePort;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.categorymapper.ICategoryEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.ICategoryRepository;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@RequiredArgsConstructor
@Service
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toModel)
                .orElseThrow( () -> new CategoryNotFoundException(InfrastructureConstants.CATEGORY_NOT_FOUND));
    }
}
