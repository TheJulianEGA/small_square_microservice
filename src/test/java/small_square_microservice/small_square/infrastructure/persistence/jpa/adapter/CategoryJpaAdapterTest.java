package small_square_microservice.small_square.infrastructure.persistence.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import small_square_microservice.small_square.domain.exception.CategoryNotFundException;
import small_square_microservice.small_square.domain.model.Category;
import small_square_microservice.small_square.infrastructure.persistence.jpa.entity.CategoryEntity;
import small_square_microservice.small_square.infrastructure.persistence.jpa.mapper.categorymapper.ICategoryEntityMapper;
import small_square_microservice.small_square.infrastructure.persistence.jpa.repository.ICategoryRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryJpaAdapterTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @Mock
    private ICategoryEntityMapper categoryEntityMapper;

    @InjectMocks
    private CategoryJpaAdapter categoryJpaAdapter;

    private final Long categoryId = 1L;
    private Category category;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {

        category = new Category();
        category.setId(categoryId);
        category.setName("Bebidas");

        categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setName("Bebidas");
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryEntityMapper.toModel(categoryEntity)).thenReturn(category);

        Category result = categoryJpaAdapter.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Bebidas", result.getName());

        verify(categoryRepository,times(1)).findById(categoryId);
        verify(categoryEntityMapper,times(1)).toModel(categoryEntity);
    }

    @Test
    void getCategoryById_ShouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFundException.class, () -> categoryJpaAdapter.getCategoryById(categoryId));

        verify(categoryRepository).findById(categoryId);
        verify(categoryEntityMapper, never()).toModel(any());
    }
}