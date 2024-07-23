package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurveController {

    // Injecter le service Curve Point
    @Autowired
    private CurvePointRepository curvePointRepository;

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ajouter l'utilisateur connecté au modèle
        model.addAttribute("username", username);
        // Récupérer tous les Curve Points et les ajouter au modèle
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        // Retourner le formulaire d'ajout de Curve Point
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        // Vérifier la validité des données du formulaire et enregistrer en BD
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, retour au formulaire d'ajout avec les erreurs
            return "curvePoint/add";
        }

        // Enregistrer le nouveau Curve Point dans la base de données
        curvePointRepository.save(curvePoint);

        // Rediriger vers la liste des Curve Points après l'ajout réussi
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // Récupérer le Curve Point par son ID et l'ajouter au modèle pour le formulaire de mise à jour
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid curve point Id:" + id));

        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                   BindingResult result, Model model) {
        // Vérifier la validité des données du formulaire et mettre à jour en BD
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, retour au formulaire de mise à jour avec les erreurs
            curvePoint.setId(id); // Restaurer l'ID dans le modèle
            return "curvePoint/update";
        }

        CurvePoint existingCurvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid curve point Id:" + id));

        // Mettre à jour les champs du Curve Point existant
        existingCurvePoint.setCurveId(curvePoint.getCurveId());
        existingCurvePoint.setAsOfDate(curvePoint.getAsOfDate());
        existingCurvePoint.setTerm(curvePoint.getTerm());
        existingCurvePoint.setValue(curvePoint.getValue());
        existingCurvePoint.setCreationDate(curvePoint.getCreationDate());

        // Enregistrer les modifications dans la base de données
        curvePointRepository.save(existingCurvePoint);

        // Rediriger vers la liste des Curve Points après la mise à jour réussie
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        // Récupérer le Curve Point par son ID et le supprimer
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid curve point Id:" + id));

        curvePointRepository.delete(curvePoint);

        // Rediriger vers la liste des Curve Points après la suppression réussie
        return "redirect:/curvePoint/list";
    }
}

