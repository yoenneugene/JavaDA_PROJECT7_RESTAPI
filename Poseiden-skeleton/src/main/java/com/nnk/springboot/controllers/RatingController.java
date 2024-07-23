package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
public class RatingController {
    // TODO: Inject Rating service
    @Autowired
     RatingRepository ratingRepository ;
    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ajouter l'utilisateur connecté au modèle
        model.addAttribute("username", username);
        // TODO: find all Rating, add to model
        model.addAttribute("ratings", ratingRepository.findAll());
                return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Rating list
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, retour à la vue du formulaire avec les erreurs
            return "rating/add";
        }

        // Si aucune erreur de validation, enregistrement de l'entité Rating dans la base de données
        ratingRepository.save(rating);

        // Ajout de la liste des ratings dans le modèle pour l'affichage
        model.addAttribute("ratings", ratingRepository.findAll());
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Rating by Id and to model then show to the form
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));

        // Ajouter le Rating au modèle pour le formulaire de mise à jour
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Rating and return Rating list
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, retourner au formulaire de mise à jour avec les erreurs
            model.addAttribute("ratings", rating);
            return "rating/update";
        }
        Rating existingRating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
        existingRating.setMoodysRating(rating.getMoodysRating());
        existingRating.setSandPRating(rating.getSandPRating());
        existingRating.setFitchRating(rating.getFitchRating());
        existingRating.setOrderNumber(rating.getOrderNumber());
        ratingRepository.save(existingRating);

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));

        // Supprimer l'entité Rating
        ratingRepository.delete(rating);
        return "redirect:/rating/list";
    }
}
