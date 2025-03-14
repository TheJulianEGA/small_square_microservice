package small_square_microservice.small_square.domain.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Paginated<T>{
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public Paginated(List<T> content, int pageNumber, int pageSize) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        calculateTotalElements();
        calculateTotalPages();
    }

    public void calculateTotalElements() {
        this.totalElements = content != null ? content.size() : 0;
    }

    public void calculateTotalPages() {
        if (this.pageSize > 0) {
            this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        } else {
            this.totalPages = 0;
        }
    }
}
