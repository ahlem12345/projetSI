
package tn.esprit.ahlembensalem4cce11.domaine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    private String libelle;
    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    Set<Vehicule> vehicule=new HashSet<>();

}