package tn.esprit.ahlembensalem4cce11.service;

import java.util.List;
import tn.esprit.ahlembensalem4cce11.domaine.Vehicule;

public interface IVehiculeService {

    Vehicule create(Vehicule vehicule);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);
}