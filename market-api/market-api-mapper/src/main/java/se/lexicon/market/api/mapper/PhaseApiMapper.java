package se.lexicon.account.api.mapper;

import se.lexicon.account.api.Phase;
import se.lexicon.account.component.domain.Balance;
import com.so4it.common.util.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public final class PhaseApiMapper {

    public static com.seb.account.component.domain.Phase map(Phase phase) {
        switch (phase.getNumber()) {
            case Phase.PHASE_UNKNOWN_VALUE:
                return com.seb.account.component.domain.Phase.UNKNOWN;
            case Phase.PENDING_INCOMING_VALUE:
                return com.seb.account.component.domain.Phase.PENDING_INCOMING;
            case Phase.PENDING_OUTGOING_VALUE:
                return com.seb.account.component.domain.Phase.PENDING_OUTGOING;
            case Phase.COMMITTED_VALUE:
                return com.seb.account.component.domain.Phase.COMMITTED;
            default:
                throw new IllegalArgumentException(String.format("Unknown phase enum value '%s'",phase));
        }
    }

    public static Phase map(com.seb.account.component.domain.Phase phase) {
        switch (phase) {
            case UNKNOWN:
                return Phase.PHASE_UNKNOWN;
            case PENDING_INCOMING:
                return Phase.PENDING_INCOMING;
            case PENDING_OUTGOING:
                return Phase.PENDING_OUTGOING;
            case COMMITTED:
                return Phase.COMMITTED;
            default:
                throw new IllegalArgumentException(String.format("Unknown phase enum value '%s'",phase));
        }
    }




}
