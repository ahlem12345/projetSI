package tn.esprit.ahlembensalem4cce11.service;

import java.util.List;
import tn.esprit.ahlembensalem4cce11.domaine.Paiement;

public interface IPaiementService {

    Paiement findById(Long id);

    List<Paiement> findAll();
}