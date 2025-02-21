package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.categorymapper;

import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;

public interface ICategoryEntityMapper {

    Category toModel(CategoryEntity entity);

    CategoryEntity toEntity(Category model);

}
