package service;

import dao.AccountDAO;
import dao.impl.AccountDAOIml;
import domain.Account;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAOIml();

    public Account getAccount(int accountId) {
        return accountDAO.get(accountId);
    }

}
