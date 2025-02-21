package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.categorymapper;

import org.springframework.stereotype.Component;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;

import java.util.ArrayList;

@Component
public class CategoryEntityMapper implements ICategoryEntityMapper {

    @Override
    public Category toModel(CategoryEntity entity) {

        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                new ArrayList<>()
        );
    }

    @Override
    public CategoryEntity toEntity(Category model) {
        if (model == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());

        return entity;
    }
}
