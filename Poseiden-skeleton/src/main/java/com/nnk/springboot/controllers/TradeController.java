package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class TradeController {
    // TODO: Inject Trade service
    @Autowired
    private TradeRepository tradeRepository;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ajouter l'utilisateur connecté au modèle
        model.addAttribute("username", username);
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        // Vérifier si le formulaire contient des erreurs de validation
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, renvoyer le formulaire pour correction
            return "trade/add";
        }

        // Si les données sont valides, sauvegarder l'objet Trade dans la base de données
        tradeRepository.save(trade);

        // Ajouter la liste des Trade au modèle pour affichage
        model.addAttribute("trades", tradeRepository.findAll());

        // Rediriger vers la liste des Trade (vue affichant la liste des trades)
        return "trade/list";

    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));

        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            trade.setTradeId(id); // Restaurer l'ID dans le modèle
            return "trade/update";
        }

        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));

        existingTrade.setAccount(trade.getAccount());
        existingTrade.setType(trade.getType());
        existingTrade.setBuyQuantity(trade.getBuyQuantity());
        existingTrade.setSellQuantity(trade.getSellQuantity());
        existingTrade.setBuyPrice(trade.getBuyPrice());
        existingTrade.setSellPrice(trade.getSellPrice());
        existingTrade.setBenchmark(trade.getBenchmark());
        existingTrade.setTradeDate(trade.getTradeDate());
        existingTrade.setSecurity(trade.getSecurity());
        existingTrade.setStatus(trade.getStatus());
        existingTrade.setTrader(trade.getTrader());
        existingTrade.setBook(trade.getBook());
        existingTrade.setCreationName(trade.getCreationName());
        existingTrade.setCreationDate(trade.getCreationDate());
        existingTrade.setRevisionName(trade.getRevisionName());
        existingTrade.setRevisionDate(trade.getRevisionDate());
        existingTrade.setDealName(trade.getDealName());
        existingTrade.setDealType(trade.getDealType());
        existingTrade.setSourceListId(trade.getSourceListId());
        existingTrade.setSide(trade.getSide());

        // Enregistrer les modifications dans la base de données
        tradeRepository.save(existingTrade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));

        tradeRepository.delete(trade);
        return "redirect:/trade/list";
    }
}
