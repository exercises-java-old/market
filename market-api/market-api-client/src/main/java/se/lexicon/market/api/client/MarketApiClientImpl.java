package se.lexicon.account.api.client;

import se.lexicon.account.api.AccountApiServiceGrpc;
import se.lexicon.account.api.mapper.AccountBalanceApiMapper;
import se.lexicon.account.api.mapper.AccountTransactionApiMapper;
import com.so4it.common.util.object.Required;
import com.so4it.ft.core.FaultTolerantBean;
import com.so4it.metric.springframework.MetricsBean;

@FaultTolerantBean(groupKey = AccountApiClientImpl.ACCOUNT_API_CLIENT_NAME)
@MetricsBean(name = AccountApiClientImpl.ACCOUNT_API_CLIENT_NAME)
public class AccountApiClientImpl implements AccountApiClient {

    static final String ACCOUNT_API_CLIENT_NAME = "ACCOUNT_API_CLIENT";

    private final AccountApiServiceGrpc.AccountApiServiceBlockingStub accountApiServiceBlockingStub;

    AccountApiClientImpl(AccountApiServiceGrpc.AccountApiServiceBlockingStub accountApiServiceBlockingStub) {
        this.accountApiServiceBlockingStub = Required.notNull(accountApiServiceBlockingStub, "accountApiServiceBlockingStub");
    }

    @Override
    public com.seb.account.component.domain.AccountBalance createAccountBalance(com.seb.account.component.domain.CreateAccountBalanceRequest request) {
        return AccountBalanceApiMapper.map(accountApiServiceBlockingStub.createAccountBalance(AccountBalanceApiMapper.map(request)));
    }

    @Override
    public com.seb.account.component.domain.AccountTransaction createAccountTransaction(com.seb.account.component.domain.CreateAccountTransactionRequest request) {
        return AccountTransactionApiMapper.map(accountApiServiceBlockingStub.createAccountTransaction(AccountTransactionApiMapper.map(request)));
    }

}
