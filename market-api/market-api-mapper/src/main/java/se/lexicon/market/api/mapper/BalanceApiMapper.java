package se.lexicon.account.api.mapper;

import se.lexicon.account.component.domain.Balance;
import com.so4it.common.util.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public final class BalanceApiMapper {

    public static Set<Balance> map(Collection<com.seb.account.api.Balance> balances) {
        return balances.stream().map(BalanceApiMapper::map).collect(Collectors.toSet());
    }

    public static List<com.seb.account.api.Balance> map(Set<Balance> balances) {
        return balances.stream().map(BalanceApiMapper::map).collect(Collectors.toList());
    }


    public static Balance map(com.seb.account.api.Balance balance) {
        return Mapper.of(balance, Balance::builder)
                .map(com.seb.account.api.Balance::getAddress, Balance.Builder::withAddress)
                .map(com.seb.account.api.Balance::getAmount, MoneyApiMapper::map, Balance.Builder::withAmount)
                .map(com.seb.account.api.Balance::getPhase, PhaseApiMapper::map, Balance.Builder::withPhase)
                .build(Balance.Builder::build);
    }

    public static com.seb.account.api.Balance map(Balance balance) {
        return Mapper.of(balance, com.seb.account.api.Balance::newBuilder)
                .map(Balance::getAddress, com.seb.account.api.Balance.Builder::setAddress)
                .map(Balance::getAmount, MoneyApiMapper::map, com.seb.account.api.Balance.Builder::setAmount)
                .map(Balance::getPhase, PhaseApiMapper::map, com.seb.account.api.Balance.Builder::setPhase)
                .build(com.seb.account.api.Balance.Builder::build);
    }


}
