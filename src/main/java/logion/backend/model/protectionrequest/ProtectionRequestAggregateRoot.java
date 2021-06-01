package logion.backend.model.protectionrequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import logion.backend.annotation.AggregateRoot;
import logion.backend.model.Ss58Address;
import logion.backend.model.adapters.Ss58AddressConverter;
import logion.backend.model.protectionrequest.adapters.EmbeddablePostalAddress;
import logion.backend.model.protectionrequest.adapters.EmbeddableUserIdentity;
import lombok.Getter;

@AggregateRoot
@Entity(name = "protection_request")
public class ProtectionRequestAggregateRoot {

    public ProtectionRequestDescription getDescription() {
        return ProtectionRequestDescription.builder()
                .requesterAddress(requesterAddress)
                .userIdentity(userIdentity())
                .userPostalAddress(userPostalAddress())
                .build();
    }

    public Set<LegalOfficerDecisionDescription> getLegalOfficerDecisionDescriptions() {
        if (decisions == null) {
            return Collections.emptySet();
        }
        return decisions.stream()
                .map(LegalOfficerDecision::getDescription)
                .collect(Collectors.toSet());
    }

    void setUserIdentityDescription(UserIdentity userIdentity) {
        this.userIdentity = new EmbeddableUserIdentity();
        this.userIdentity.setFirstName(userIdentity.getFirstName());
        this.userIdentity.setLastName(userIdentity.getLastName());
        this.userIdentity.setEmail(userIdentity.getEmail());
        this.userIdentity.setPhoneNumber(userIdentity.getPhoneNumber());
    }

    void setUserPostalAddress(PostalAddress postalAddress) {
        userPostalAddress = new EmbeddablePostalAddress();
        userPostalAddress.setLine1(postalAddress.getLine1());
        userPostalAddress.setLine2(postalAddress.getLine2());
        userPostalAddress.setPostalCode(postalAddress.getPostalCode());
        userPostalAddress.setCity(postalAddress.getCity());
        userPostalAddress.setCountry(postalAddress.getCountry());
    }

    void setLegalOfficerDecisions(Set<Ss58Address> legalOfficerAddresses, LocalDateTime createdOn) {
        decisions = legalOfficerAddresses.stream()
                .map(setLegalOfficerDecisions -> {
                    var legalOfficerDecision = new LegalOfficerDecision();
                    legalOfficerDecision.id = new LegalOfficerDecision.LegalOfficerDecisionId(id, setLegalOfficerDecisions);
                    legalOfficerDecision.status = LegalOfficerDecisionStatus.PENDING;
                    legalOfficerDecision.createdOn = createdOn;
                    return legalOfficerDecision;
                })
                .collect(Collectors.toSet());

    }

    private UserIdentity userIdentity() {
        if (userIdentity == null) {
            return UserIdentity.builder().build();
        }
        return UserIdentity.builder()
                .firstName(userIdentity.getFirstName())
                .lastName(userIdentity.getLastName())
                .email(userIdentity.getEmail())
                .phoneNumber(userIdentity.getPhoneNumber())
                .build();
    }

    private PostalAddress userPostalAddress() {
        if (userPostalAddress == null) {
            return PostalAddress.builder().build();
        }
        return PostalAddress.builder()
                .line1(userPostalAddress.getLine1())
                .line2(userPostalAddress.getLine2())
                .postalCode(userPostalAddress.getPostalCode())
                .city(userPostalAddress.getCity())
                .country(userPostalAddress.getCountry())
                .build();
    }

    @Id
    @Getter
    UUID id;

    @Embedded
    EmbeddableUserIdentity userIdentity;

    @Embedded
    EmbeddablePostalAddress userPostalAddress;

    @Getter
    LocalDateTime createdOn;

    @Convert(converter = Ss58AddressConverter.class)
    Ss58Address requesterAddress;

    @OneToMany(mappedBy = "id.requestId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<LegalOfficerDecision> decisions;

    ProtectionRequestAggregateRoot() {
    }
}