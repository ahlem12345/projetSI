package tn.esprit.ahlembensalem4cce11.service;

import java.util.List;
import tn.esprit.ahlembensalem4cce11.domaine.Agence;

public interface IAgenceService {

    Agence create(Agence agence);

    Agence findById(Long id);

    List<Agence> findAll();

    Agence update(Long id, Agence agence);

    void deleteById(Long id);
}