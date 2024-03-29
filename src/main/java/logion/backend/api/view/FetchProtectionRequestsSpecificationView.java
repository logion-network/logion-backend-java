package logion.backend.api.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import logion.backend.annotation.View;
import logion.backend.model.protectionrequest.LegalOfficerDecisionStatus;
import logion.backend.model.protectionrequest.ProtectionRequestKind;
import logion.backend.model.protectionrequest.ProtectionRequestStatus;
import lombok.Builder;
import lombok.Value;

@View
@Value
@Builder
@ApiModel(description = "The specification for fetching Protection Requests")
public class FetchProtectionRequestsSpecificationView {

    @ApiModelProperty("The SS58 address of the requester in expected Protection Requests")
    String requesterAddress;

    @ApiModelProperty("The SS58 address of the legal officer in expected Protection Requests")
    String legalOfficerAddress;

    @ApiModelProperty("The statuses of decisions of expected Protection Requests")
    Set<LegalOfficerDecisionStatus> decisionStatuses;

    @ApiModelProperty("The kind of protection request to be returned")
    ProtectionRequestKind kind;

    @ApiModelProperty("The status of expected Protection Requests")
    ProtectionRequestStatus protectionRequestStatus;
}
