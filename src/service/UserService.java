package service;

import java.util.Iterator;
import java.util.Set;

import dao.ComputerDAO;
import dao.TradeDAO;
import dao.TradeItemDAO;
import dao.UserDAO;
import dao.impl.ComputerDAOImpl;
import dao.impl.TradeDAOImpl;
import dao.impl.TradeItemDAOImpl;
import dao.impl.UserDAOImpl;
import domain.Trade;
import domain.TradeItem;
import domain.User;

public class UserService {

    private UserDAO userDAO = new UserDAOImpl();

    public User getUserByUserName(String username) {
        return userDAO.getUser(username);
    }

    private TradeDAO tradeDAO = new TradeDAOImpl();
    private TradeItemDAO tradeItemDAO = new TradeItemDAOImpl();
    private ComputerDAO computerDAO = new ComputerDAOImpl();

    public User getUserWithTrades(String username) {

//		���� UserDAO �ķ�����ȡ User ����
        User user = userDAO.getUser(username);
        if (user == null) {
            return null;
        }

//		���� TradeDAO �ķ�����ȡ Trade �ļ��ϣ�����װ��Ϊ User ������
        int userId = user.getUserId();

//		���� TradeItemDAO �ķ�����ȡÿһ�� Trade �е� TradeItem �ļ��ϣ�������װ��Ϊ Trade ������
        Set<Trade> trades = tradeDAO.getTradesWithUserId(userId);

        if (trades != null) {
            Iterator<Trade> tradeIt = trades.iterator();

            while (tradeIt.hasNext()) {
                Trade trade = tradeIt.next();

                int tradeId = trade.getTradeId();
                Set<TradeItem> items = tradeItemDAO.getTradeItemsWithTradeId(tradeId);

                if (items != null) {
                    for (TradeItem item : items) {
                        item.setComputer(computerDAO.getComputer(item.getComputerId()));
                    }

                    if (items != null && items.size() != 0) {
                        trade.setItems(items);
                    }
                }

                if (items == null || items.size() == 0) {
                    tradeIt.remove();
                }

            }
        }

        if (trades != null && trades.size() != 0) {
            user.setTrades(trades);
        }

        return user;
    }

}
