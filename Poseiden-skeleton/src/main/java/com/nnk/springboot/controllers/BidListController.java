package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class BidListController {
    // TODO: Inject Bid service
    @Autowired
    private BidListRepository bidListRepository;

    @RequestMapping("/bidList/list")
    public String home( Model model)
    {
        // TODO: call service find all bids to show to the view
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ajouter l'utilisateur connecté au modèle
        model.addAttribute("username", username);
        model.addAttribute( "bidLists", bidListRepository.findAll());

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {

        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return bid list
        if (result.hasErrors()) {
            return "bidList/add";
        }

        // Enregistrer la nouvelle bid dans la base de données
        bidListRepository.save(bid);

        // Rediriger vers la liste des bid après l'ajout réussi
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));

        model.addAttribute("bid", bid);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            bidList.setBidListId(id); // Restaurer l'ID dans le modèle
            return "bidList/update";
        }

        BidList existingBid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));

        existingBid.setAccount(bidList.getAccount());
        existingBid.setType(bidList.getType());
        existingBid.setBidQuantity(bidList.getBidQuantity());
        existingBid.setAskQuantity(bidList.getAskQuantity());
        existingBid.setBid(bidList.getBid());
        existingBid.setAsk(bidList.getAsk());
        existingBid.setBenchmark(bidList.getBenchmark());
        existingBid.setCommentary(bidList.getCommentary());
        existingBid.setSecurity(bidList.getSecurity());
        existingBid.setStatus(bidList.getStatus());
        existingBid.setTrader(bidList.getTrader());
        existingBid.setBook(bidList.getBook());
        existingBid.setCreationName(bidList.getCreationName());
        existingBid.setCreationDate(bidList.getCreationDate());
        existingBid.setRevisionName(bidList.getRevisionName());
        existingBid.setRevisionDate(bidList.getRevisionDate());
        existingBid.setDealName(bidList.getDealName());
        existingBid.setDealType(bidList.getDealType());
        existingBid.setSourceListId(bidList.getSourceListId());
        existingBid.setSide(bidList.getSide());

        // Enregistrer les modifications dans la base de données
        bidListRepository.save(existingBid);// TODO: check required fields, if valid call service to update Bid and return list Bid
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));

        bidListRepository.delete(bid);
        return "redirect:/bidList/list";
    }
}
