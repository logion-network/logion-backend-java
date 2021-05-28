package logion.backend.api.view;

import java.util.List;
import logion.backend.annotation.View;
import lombok.Builder;
import lombok.Value;

@View
@Value
@Builder
public class FetchRequestsResponseView {

    List<TokenRequestView> requests;
}
