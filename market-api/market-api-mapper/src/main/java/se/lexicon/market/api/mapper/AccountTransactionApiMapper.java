package se.lexicon.account.api.mapper;

import se.lexicon.account.component.domain.AccountTransaction;
import com.so4it.api.mapper.ValueMapper;
import com.so4it.common.util.Mapper;


public final class AccountTransactionApiMapper {

    public static AccountTransaction map(com.seb.account.api.AccountTransaction accountTransaction){

        return Mapper.of(accountTransaction,AccountTransaction:: builder)
                .map(com.seb.account.api.AccountTransaction::getId,AccountTransaction.Builder::withId)
                .map(com.seb.account.api.AccountTransaction::getArrangementId,AccountTransaction.Builder::withArrangementId)
                .map(com.seb.account.api.AccountTransaction::getBatchId,AccountTransaction.Builder::withBatchId)
                .map(com.seb.account.api.AccountTransaction::getInsertionTimestamp, ValueMapper::map, AccountTransaction.Builder::withInsertionTimestamp)
                .map(com.seb.account.api.AccountTransaction::getAddress, AccountTransaction.Builder::withAddress)
                .map(com.seb.account.api.AccountTransaction::getPhase, PhaseApiMapper::map, AccountTransaction.Builder::withPhase)
                .map(com.seb.account.api.AccountTransaction::getAmount, MoneyApiMapper::map, AccountTransaction.Builder::withAmount)
                .build(AccountTransaction.Builder::build);

    }

    public static  com.seb.account.api.AccountTransaction map(AccountTransaction accountTransaction){
        return Mapper.of(accountTransaction,com.seb.account.api.AccountTransaction::newBuilder)
                .map(AccountTransaction::getId,com.seb.account.api.AccountTransaction.Builder::setId)
                .map(AccountTransaction::getArrangementId,com.seb.account.api.AccountTransaction.Builder::setArrangementId)
                .map(AccountTransaction::getBatchId,com.seb.account.api.AccountTransaction.Builder::setBatchId)
                .map(AccountTransaction::getInsertionTimestamp, ValueMapper::map, com.seb.account.api.AccountTransaction.Builder::setInsertionTimestamp)
                .map(AccountTransaction::getAddress, com.seb.account.api.AccountTransaction.Builder::setAddress)
                .map(AccountTransaction::getPhase, PhaseApiMapper::map, com.seb.account.api.AccountTransaction.Builder::setPhase)
                .map(AccountTransaction::getAmount, MoneyApiMapper::map, com.seb.account.api.AccountTransaction.Builder::setAmount)
                .build(com.seb.account.api.AccountTransaction.Builder::build);

    }



    public static com.seb.account.component.domain.CreateAccountTransactionRequest map(com.seb.account.api.CreateAccountTransactionRequest request){

        return Mapper.of(request,com.seb.account.component.domain.CreateAccountTransactionRequest::builder)
                .map(com.seb.account.api.CreateAccountTransactionRequest::getArrangementId,com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::withArrangementId)
                .map(com.seb.account.api.CreateAccountTransactionRequest::getBatchId,com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::withBatchId)
                .map(com.seb.account.api.CreateAccountTransactionRequest::getInsertionTimestamp, ValueMapper::map, com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::withInsertionTimestamp)
                .map(com.seb.account.api.CreateAccountTransactionRequest::getAddress, com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::withAddress)
                .map(com.seb.account.api.CreateAccountTransactionRequest::getPhase, PhaseApiMapper::map, com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::withPhase)
                .map(com.seb.account.api.CreateAccountTransactionRequest::getAmount, MoneyApiMapper::map, com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::withAmount)
                .build(com.seb.account.component.domain.CreateAccountTransactionRequest.Builder::build);

    }

    public static com.seb.account.api.CreateAccountTransactionRequest map(com.seb.account.component.domain.CreateAccountTransactionRequest request){

        return Mapper.of(request,com.seb.account.api.CreateAccountTransactionRequest::newBuilder)
                .map(com.seb.account.component.domain.CreateAccountTransactionRequest::getArrangementId,com.seb.account.api.CreateAccountTransactionRequest.Builder::setArrangementId)
                .map(com.seb.account.component.domain.CreateAccountTransactionRequest::getBatchId,com.seb.account.api.CreateAccountTransactionRequest.Builder::setBatchId)
                .map(com.seb.account.component.domain.CreateAccountTransactionRequest::getInsertionTimestamp, ValueMapper::map, com.seb.account.api.CreateAccountTransactionRequest.Builder::setInsertionTimestamp)
                .map(com.seb.account.component.domain.CreateAccountTransactionRequest::getAddress,com.seb.account.api.CreateAccountTransactionRequest.Builder::setAddress)
                .map(com.seb.account.component.domain.CreateAccountTransactionRequest::getPhase, PhaseApiMapper::map, com.seb.account.api.CreateAccountTransactionRequest.Builder::setPhase)
                .map(com.seb.account.component.domain.CreateAccountTransactionRequest::getAmount, MoneyApiMapper::map, com.seb.account.api.CreateAccountTransactionRequest.Builder::setAmount)
                .build(com.seb.account.api.CreateAccountTransactionRequest.Builder::build);

    }




}
