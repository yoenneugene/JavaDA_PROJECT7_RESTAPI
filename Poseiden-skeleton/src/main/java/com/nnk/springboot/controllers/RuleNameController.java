package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
public class RuleNameController {

    // Injecter le service RuleName
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ajouter l'utilisateur connecté au modèle
        model.addAttribute("username", username);
        // Récupérer tous les RuleNames et les ajouter au modèle
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
        // Retourner le formulaire d'ajout de RuleName
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        // Vérifier la validité des données du formulaire et enregistrer en BD
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, retour au formulaire d'ajout avec les erreurs
            return "ruleName/add";
        }

        // Enregistrer le nouveau RuleName dans la base de données
        ruleNameRepository.save(ruleName);

        // Rediriger vers la liste des RuleNames après l'ajout réussi
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // Récupérer le RuleName par son ID et l'ajouter au modèle pour le formulaire de mise à jour
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));

        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        // Vérifier la validité des données du formulaire et mettre à jour en BD
        if (result.hasErrors()) {
            // Si des erreurs sont présentes, retour au formulaire de mise à jour avec les erreurs
            ruleName.setId(id); // Restaurer l'ID dans le modèle
            return "ruleName/update";
        }

        RuleName existingRuleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));

        // Mettre à jour les champs du RuleName existant
        existingRuleName.setName(ruleName.getName());
        existingRuleName.setDescription(ruleName.getDescription());
        existingRuleName.setJson(ruleName.getJson());
        existingRuleName.setTemplate(ruleName.getTemplate());
        existingRuleName.setSqlStr(ruleName.getSqlStr());
        existingRuleName.setSqlPart(ruleName.getSqlPart());

        // Enregistrer les modifications dans la base de données
        ruleNameRepository.save(existingRuleName);

        // Rediriger vers la liste des RuleNames après la mise à jour réussie
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        // Récupérer le RuleName par son ID et le supprimer
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));

        ruleNameRepository.delete(ruleName);

        // Rediriger vers la liste des RuleNames après la suppression réussie
        return "redirect:/ruleName/list";
    }
}