package small_square_microservice.small_square.infrastructure.http.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import small_square_microservice.small_square.application.dto.traceabilitydto.OrderStatusHistoryRequest;
import small_square_microservice.small_square.infrastructure.util.InfrastructureConstants;

@FeignClient(name = InfrastructureConstants.TRACEABILITY_SERVICE_NAME, url = InfrastructureConstants.TRACEABILITY_SERVICE_URL)
public interface ITraceabilityFeignClient {

    @PostMapping("/api/order-status-history/create")
    void saveOrderStatusHistory (@RequestBody OrderStatusHistoryRequest request);
}
