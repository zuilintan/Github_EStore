package service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;


import dao.AccountDAO;
import dao.ComputerDAO;
import dao.TradeDAO;
import dao.TradeItemDAO;
import dao.UserDAO;
import dao.impl.AccountDAOIml;
import dao.impl.ComputerDAOImpl;
import dao.impl.TradeDAOImpl;
import dao.impl.TradeItemDAOImpl;
import dao.impl.UserDAOImpl;
import domain.Computer;
import domain.ShoppingCart;
import domain.ShoppingCartItem;
import domain.Trade;
import domain.TradeItem;
import web.CriteriaComputer;
import web.Page;

public class ComputerService {
    private ComputerDAO computerDAO = new ComputerDAOImpl();
    private AccountDAO accountDAO = new AccountDAOIml();
    private TradeDAO tradeDAO = new TradeDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private TradeItemDAO tradeItemDAO = new TradeItemDAOImpl();

    public Page<Computer> getPage(CriteriaComputer cc) {
        return computerDAO.getPage(cc);
    }

    public Computer getComputer(int id) {
        // TODO Auto-generated method stub
        return computerDAO.getComputer(id);
    }

    public boolean addToCart(int id, ShoppingCart sc) {
        // TODO Auto-generated method stub
        Computer computer = computerDAO.getComputer(id);

        if (computer != null) {
            sc.addComputer(computer);
            return true;
        }
        return false;


    }

    //业务方法.
    public void cash(ShoppingCart shoppingCart, String username,
                     String accountId) {

        //1. 更新 mycomputers 数据表相关记录的 salesamount 和 storenumber
        computerDAO.batchUpdateStoreNumberAndSalesAmount(shoppingCart.getItems());

        int i = 10;

        //2. 更新 account 数据表的 balance
        accountDAO.updateBalance(Integer.parseInt(accountId), shoppingCart.getTotalMoney());

        //3. 向 trade 数据表插入一条记录
        Trade trade = new Trade();
        trade.setTradeTime(new Date(new java.util.Date().getTime()));
        trade.setUserId(userDAO.getUser(username).getUserId());
        tradeDAO.insert(trade);

        //4. 向 tradeitem 数据表插入 n 条记录
        Collection<TradeItem> items = new ArrayList<>();
        for (ShoppingCartItem sci : shoppingCart.getItems()) {
            TradeItem tradeItem = new TradeItem();

            tradeItem.setComputerId(sci.getComputer().getId());
            tradeItem.setQuantity(sci.getQuantity());
            tradeItem.setTradeId(trade.getTradeId());

            items.add(tradeItem);
        }
        tradeItemDAO.batchSave(items);

        //5. 清空购物车
        shoppingCart.clear();
    }


    public void updateItemQuantity(ShoppingCart sc, int id, int quantity) {
        sc.updateItemQuantity(id, quantity);
    }

    public void removeItemFromShoppingCart(ShoppingCart sc, int id) {
        sc.removeItem(id);
    }

    public void clearShoppingCart(ShoppingCart sc) {
        // TODO Auto-generated method stub
        sc.clear();
    }
}
