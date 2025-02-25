package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.categorymapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;


@Component
public class CategoryEntityMapper implements ICategoryEntityMapper {

    @Override
    public Category toModel(CategoryEntity entity) {
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public CategoryEntity toEntity(Category model) {
        return CategoryEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .build();
    }
}
