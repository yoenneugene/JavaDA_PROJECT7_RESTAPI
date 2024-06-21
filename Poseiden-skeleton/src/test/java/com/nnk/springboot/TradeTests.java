package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		Trade trade = new Trade();

		// Utilisation des setters pour définir les valeurs des champs
		trade.setTradeId(1);
		trade.setAccount("Trade Account");
		trade.setType("Buy");
		trade.setBuyQuantity(100.0);
		trade.setSellQuantity(0.0);
		trade.setBuyPrice(10.5);
		trade.setSellPrice(0.0);
		trade.setBenchmark("BenchmarkXYZ");
		trade.setTradeDate(new Timestamp(System.currentTimeMillis()));
		trade.setSecurity("SecurityXYZ");
		trade.setStatus("Open");
		trade.setTrader("John Doe");
		trade.setBook("BookABC");
		trade.setCreationName("Admin");
		trade.setCreationDate(new Timestamp(System.currentTimeMillis()));
		trade.setRevisionName("Manager");
		trade.setRevisionDate(new Timestamp(System.currentTimeMillis()));
		trade.setDealName("Deal123");
		trade.setDealType("New");
		trade.setSourceListId("Source456");
		trade.setSide("Buy");

		// Save
		trade = tradeRepository.save(trade);
		Assert.assertNotNull(trade.getTradeId());
		Assert.assertTrue(trade.getAccount().equals("Trade Account"));

		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
		Assert.assertTrue(trade.getAccount().equals("Trade Account Update"));

		// Find
		List<Trade> listResult = tradeRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		Assert.assertFalse(tradeList.isPresent());
	}
}
