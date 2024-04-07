package net.dstone.sample.market.mart;

import java.util.List;

import net.dstone.sample.market.buy.actor.Buyer;
import net.dstone.sample.market.item.Item;
import net.dstone.sample.market.sell.actor.Seller;

public interface Market {
	public boolean trade(Buyer buyer, Seller seller, Item item,  int itemCnt, int money);
	public void trade(String buyerId, String itemId, int itemTradeCnt) throws Exception;
	public Buyer setBuyerForTrade(String buyerId, int pocketMoney) throws Exception;
	public Seller setSellerForTrade(String itemId, int itemPrice, int itemStorageCnt) throws Exception;
}
