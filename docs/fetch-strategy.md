# Stratégie de chargement (fetch strategy)

Toutes les associations sont en `FetchType.LAZY` pour éviter de charger des données inutiles et limiter les requêtes SQL (problème N+1 et jointures coûteuses). Les données associées ne sont lues que lorsqu'on y accède.

| Classe | Association | Type | Fetch |
|---|---|---|---|
| Vehicule | agence | @ManyToOne | LAZY |
| Vehicule | equipements | @ManyToMany | LAZY |
| Vehicule | maintenances | @OneToMany | LAZY |
| Agence | vehicules, employes | @OneToMany | LAZY |
| Employe | agence | @ManyToOne | LAZY |
| Maintenance | vehicule | @ManyToOne | LAZY |
| Client | reservations | @OneToMany | LAZY |
| Reservation | client, vehicule | @ManyToOne | LAZY |
| Reservation | contrat | @OneToOne | LAZY |
| Contrat | reservation | @OneToOne | LAZY |
| Contrat | paiements | @OneToMany | LAZY |
| Paiement | contrat | @ManyToOne | LAZY |

## Justification

- `@ManyToOne` et `@OneToOne` sont EAGER par défaut dans JPA, on les passe donc explicitement en LAZY.
- `@OneToMany` et `@ManyToMany` sont LAZY par défaut, mais on l'écrit explicitement pour que ce soit visible.
- Si une association est nécessaire, on la charge à la demande (par exemple avec `JOIN FETCH`).