package small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.categorymapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryEntityMapperTest {

    @InjectMocks
    private CategoryEntityMapper categoryEntityMapper;


    @Test
    void toModel_ShouldReturnCategory_WhenGivenCategoryEntity() {
        CategoryEntity entity = CategoryEntity.builder()
                .id(1L)
                .name("Bebidas")
                .description("Bebidas frías y calientes")
                .build();

        Category model = categoryEntityMapper.toModel(entity);

        assertNotNull(model);
        assertEquals(1L, model.getId());
        assertEquals("Bebidas", model.getName());
        assertEquals("Bebidas frías y calientes", model.getDescription());
    }

    @Test
    void toEntity_ShouldReturnCategoryEntity_WhenGivenCategory() {
        Category model = Category.builder()
                .id(2L)
                .name("Postres")
                .description("Dulces y postres variados")
                .build();

        CategoryEntity entity = categoryEntityMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Postres", entity.getName());
        assertEquals("Dulces y postres variados", entity.getDescription());
    }
}