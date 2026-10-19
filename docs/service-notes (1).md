# Atelier 4 - Notes sur la couche Service

## A. Modes d'injection comparés (étape 2)

| Critère | Constructeur (`@RequiredArgsConstructor` + `final`) | Attribut (`@Autowired` sur le champ) |
|---|---|---|
| Champ `final` possible ? | Oui | Non |
| Dépendance visible dans la signature de la classe ? | Oui (paramètre du constructeur) | Non (dépendance cachée) |
| Utilisable avec `new` hors conteneur sans dépendance `null` ? | Oui, on est obligé de fournir le repository | Non, `contratRepository` reste `null` |
| Signalé par SonarQube for IDE ? | Non | Oui : `java:S6813` « Remove this field injection and use constructor injection instead. » |

Constat : l'application démarre dans les deux cas, mais seule l'injection par constructeur garantit qu'un service n'existe jamais dans un état incomplet. C'est la convention retenue pour les neuf services.

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Champ `final`, dépendance typée par interface, règle : valeur numérique non négative |
| AgenceServiceImpl | IAgenceRepository | Constructeur | Idem ; règle : attribut obligatoire non vide |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Idem ; règle : attribut obligatoire non vide |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Idem ; règle : libellé obligatoire (pas d'attribut quantité dans le modèle) |
| ClientServiceImpl | IClientRepository | Constructeur | Idem ; règle : attribut d'identification non vide |
| ReservationServiceImpl | IReservationRepository | Constructeur | Idem ; règle : date de fin non antérieure à la date de début |
| ContratServiceImpl | IContratRepository | Constructeur | Idem ; règle : montant total non négatif ; la collection `paiements` n'est jamais touchée par `update` (cascade + `orphanRemoval`) |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule (`findById`, `findAll`) : création, modification et suppression passent par le Contrat |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Idem ; règle : date de fin non antérieure à la date de début (pas de champ coût dans le modèle) |

## C. Messages d'erreur du conteneur (étape 3)

**Message 1 - `ContratController` ne trouve pas `IContratService`**
- Exception : `NoSuchBeanDefinitionException`, enveloppée dans `UnsatisfiedDependencyException`.
- Bean concerné : le paramètre 0 du constructeur de `ContratController`.
- Causes probables : (1) `@Service` oublié sur `ContratServiceImpl` ; (2) classe placée hors du package racine scanné par `@SpringBootApplication`.
- Correction : ajouter `@Service` ; vérifier que la classe est dans un sous-package du package racine.

**Message 2 - deux beans pour `NotificationController`**
- Exception : `NoUniqueBeanDefinitionException` (beans `emailNotificateur` et `smsNotificateur`).
- Correction 1 : `@Primary` sur la **classe** du bean par défaut (ex. `EmailNotificateur`).
- Correction 2 : `@Qualifier("smsNotificateur")` sur le **paramètre du constructeur** du contrôleur.

**Message 3 - cycle `clientServiceImpl` <-> `reservationServiceImpl`**
- Exception : dépendance circulaire (`BeanCurrentlyInCreationException`).
- Avec l'injection par constructeur, chaque bean doit être entièrement construit avant d'être fourni à l'autre : aucun des deux ne peut être créé en premier, donc le cycle est impossible à contourner.
- Piste de conception : extraire la responsabilité commune dans un troisième service, ou faire dépendre l'un des services du repository de l'autre plutôt que de son service.

## D. Anomalies SonarQube for IDE (étape 4)

| # | Règle | Explication | Correction apportée |
|---|---|---|---|
| 1 | `java:S6813` | Injection par attribut (`@Autowired` sur le champ) dans `ContratServiceImpl` : dépendance cachée, champ non `final`. | `@RequiredArgsConstructor` + `private final IContratRepository` |
| 2 | `java:S112` | `throw new RuntimeException(...)` dans `ContratServiceImpl.findById` : exception générique, impossible à intercepter précisément. | Remplacée par `ResourceNotFoundException("Contrat", id)` |
| 3 | `java:S1117` | Champ `vehicule` dupliqué dans `Maintenance` : un champ en masque un autre. | Doublon supprimé, un seul `private Vehicule vehicule;` |

## E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` et qui l'exécute ?** Il n'y en a pas dans notre code : c'est le conteneur Spring qui instancie la classe au démarrage, grâce à `@Service` et au scan, et qui lui fournit le proxy de `IContratRepository` par le constructeur.
2. **Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?** Pour dépendre d'une abstraction : on peut changer ou remplacer l'implémentation sans toucher au contrôleur, et on réduit le couplage.
3. **Pourquoi un service singleton doit-il rester sans état ?** Une seule instance est partagée par tous les threads. Exemple de bug : un attribut « contrat courant » écrit par une requête A puis écrasé par une requête B ; A travaille alors sur le mauvais contrat.
4. **Pourquoi `update` charge-t-il l'existant avant `save` ?** Passer l'objet reçu à `save` écraserait toutes les colonnes (champs absents mis à `null`) et, avec `orphanRemoval`, pourrait vider la collection de paiements. On charge, on recopie les attributs modifiables, puis on sauvegarde.
5. **Différences.**
   - `@Component` se pose sur une classe que l'on écrit ; `@Bean` se pose sur une méthode d'une classe `@Configuration` pour déclarer un bean explicitement (classe de bibliothèque ou construction particulière).
   - `@Primary` désigne le candidat par défaut, posé sur la classe ; `@Qualifier` est un choix explicite, posé à l'injection.
   - `IPaiementService` n'expose ni création, ni modification, ni suppression : un paiement n'existe qu'au sein d'un contrat (cascade et `orphanRemoval`), donc ces opérations passent par le Contrat.
