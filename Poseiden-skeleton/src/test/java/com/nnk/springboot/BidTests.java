package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidTests {

	@Autowired
	private BidListRepository bidListRepository;

	@Test
	public void bidListTest() {
		BidList bid = new BidList();
		bid.setBidListId(1);
		bid.setAccount("Account123");
		bid.setType("TypeXYZ");
		bid.setBidQuantity(10.0);
		bid.setAskQuantity(50.0);
		bid.setBid(10.0);
		bid.setAsk(11.0);
		bid.setBenchmark("BenchmarkXYZ");
		bid.setBidListDate(new Timestamp(System.currentTimeMillis()));
		bid.setCommentary("Comment on bid list");
		bid.setSecurity("SecurityXYZ");
		bid.setStatus("Open");
		bid.setTrader("John Doe");
		bid.setBook("BookABC");
		bid.setCreationName("Admin");
		bid.setCreationDate(new Timestamp(System.currentTimeMillis()));
		bid.setRevisionName("Manager");
		bid.setRevisionDate(new Timestamp(System.currentTimeMillis()));
		bid.setDealName("Deal123");
		bid.setDealType("New");
		bid.setSourceListId("Source456");
		bid.setSide("Buy");

		// Save
		bid = bidListRepository.save(bid);
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(bid.getBidQuantity(), 10d, 10d);

		// Update
		bid.setBidQuantity(20d);
		bid = bidListRepository.save(bid);
		Assert.assertEquals(bid.getBidQuantity(), 20d, 20d);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assert.assertFalse(bidList.isPresent());
	}
}
