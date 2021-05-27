package logion.backend.commands;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import logion.backend.model.tokenizationrequest.TokenizationRequestAggregateRoot;
import logion.backend.model.tokenizationrequest.TokenizationRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenizationRequestCommandsTest {

    private static final String REJECT_REASON = "Illegal";
    private static final LocalDateTime REJECTED_ON = LocalDateTime.now();

    @Test
    void addTokenizationRequest() {
        givenTokenizationRequest();
        givenTokenizationRequestExists(false);
        whenAddTokenizationRequest();
        thenRequestSaved();
    }

    private void givenTokenizationRequest() {
        var requestId = UUID.randomUUID();
        request = mock(TokenizationRequestAggregateRoot.class);
        when(request.getId()).thenReturn(requestId);
    }

    private TokenizationRequestAggregateRoot request;

    private void givenTokenizationRequestExists(boolean exists) {
        when(tokenizationRequestRepository.existsById(request.getId())).thenReturn(exists);
    }

    @Mock
    private TokenizationRequestRepository tokenizationRequestRepository;

    private void whenAddTokenizationRequest() {
        commands.addTokenizationRequest(request);
    }

    @InjectMocks
    private TokenizationRequestCommands commands;

    private void thenRequestSaved() {
        verify(tokenizationRequestRepository).save(request);
    }

    @Test
    void addTokenizationRequestFailsIfExisting() {
        givenTokenizationRequest();
        givenTokenizationRequestExists(true);
        assertThrows(IllegalArgumentException.class, this::whenAddTokenizationRequest);
    }

    @Test
    void rejectTokenizationRequest() {
        givenTokenizationRequest();
        givenTokenizationRequestFindable();
        whenRejectTokenizationRequest();
        thenRequestRejected();
        thenRequestSaved();
    }

    private void givenTokenizationRequestFindable() {
        when(tokenizationRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));
    }

    private void whenRejectTokenizationRequest() {
        commands.rejectTokenizationRequest(request.getId(), REJECT_REASON, REJECTED_ON);
    }

    private void thenRequestRejected() {
        verify(request).reject(REJECT_REASON, REJECTED_ON);
    }

    @Test
    void rejectTokenizationRequestFailsIfNotExists() {
        givenTokenizationRequest();
        assertThrows(IllegalArgumentException.class, this::whenRejectTokenizationRequest);
    }
}
