package logion.backend.chain.view;

import logion.backend.annotation.View;
import lombok.Data;

@View
@Data
public class Info {

    Long partialFee;
    String error;
}
