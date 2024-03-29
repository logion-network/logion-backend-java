package logion.backend.model.protectionrequest;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import logion.backend.annotation.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static logion.backend.model.protectionrequest.QLegalOfficerDecision.legalOfficerDecision;
import static logion.backend.model.protectionrequest.QProtectionRequestAggregateRoot.protectionRequestAggregateRoot;

@Repository
public interface ProtectionRequestRepository
        extends CrudRepository<ProtectionRequestAggregateRoot, UUID>,
        QuerydslPredicateExecutor<ProtectionRequestAggregateRoot> {

    Supplier<IllegalArgumentException> requestNotFound = protectionRequestNotFound("Protection request does not exist");

    static Supplier<IllegalArgumentException> protectionRequestNotFound(String message) {
        return () -> new IllegalArgumentException(message);
    }

    default List<ProtectionRequestAggregateRoot> findBy(FetchProtectionRequestsSpecification querySpecification) {

        var whereClause = new BooleanBuilder();

        subQueryNeeded(querySpecification).ifPresent(subQuery -> whereClause.and(protectionRequestAggregateRoot.decisions.any().in(subQuery)));

        querySpecification.getExpectedRequesterAddress().ifPresent(requester -> whereClause.and(protectionRequestAggregateRoot.requesterAddress.eq(requester)));

        var expectedKind = querySpecification.getKind();
        if(expectedKind != ProtectionRequestKind.ANY) {
            whereClause.and(protectionRequestAggregateRoot.isRecovery.eq(expectedKind == ProtectionRequestKind.RECOVERY));
        }

        querySpecification.getExpectedProtectionRequestStatus().ifPresent(status -> whereClause.and(protectionRequestAggregateRoot.status.eq(status)));

        var results = new ArrayList<ProtectionRequestAggregateRoot>();
        findAll(whereClause).forEach(results::add);
        return results;
    }

    private Optional<JPQLQuery<LegalOfficerDecision>> subQueryNeeded(FetchProtectionRequestsSpecification querySpecification) {
        var whereClause = new BooleanBuilder();

        if (!querySpecification.getExpectedDecisionStatuses().isEmpty()) {
            whereClause.and(legalOfficerDecision.status.in(querySpecification.getExpectedDecisionStatuses()));
        }

        querySpecification.getExpectedLegalOfficer().ifPresent(legalOfficer -> whereClause.and(legalOfficerDecision.id.legalOfficerAddress.eq(legalOfficer)));

        if (whereClause.hasValue()) {
            whereClause.and(legalOfficerDecision.id.requestId.eq(protectionRequestAggregateRoot.id));

            return Optional.of(
                    selectFrom(legalOfficerDecision)
                            .where(whereClause));
        }
        return Optional.empty();
    }
}
