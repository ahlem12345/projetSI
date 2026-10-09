package tn.esprit.ahlembensalem4cce11.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ahlembensalem4cce11.domaine.Contrat;

public interface IContratRepository extends JpaRepository<Contrat, Long> {
}