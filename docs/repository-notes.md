# Notes sur la couche Repository

## Choix d'interface

Toutes les interfaces étendent `JpaRepository<Entité, Long>`, retenue pour AutoLoc : CRUD, listes, tri, pagination et flush dans une seule interface.

| Interface | Étend | Justification |
|---|---|---|
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Créé pour lire les paiements ; la création et le retrait passent par le Contrat (composition). |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet, findAll renvoie une List, tri et pagination. |

Attention : les suppressions en lot (deleteAllInBatch, deleteAllByIdInBatch) contournent le contexte de persistance, donc la cascade et l'orphanRemoval ne s'appliquent pas. On les évite sur Contrat.

## Anomalies SonarQube for IDE corrigées

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Import inutilisé `java.util.Set` dans Agence.java | java:S1128 : un import non utilisé alourdit le code (code mort). | Suppression de l'import. |
| Import inutilisé `java.util.ArrayList` dans Paiement.java | java:S1128 : la classe n'utilise pas ArrayList. | Suppression de l'import. |
| Import inutilisé `java.util.List` dans Paiement.java | java:S1128 : la classe n'utilise pas List. | Suppression de l'import. |