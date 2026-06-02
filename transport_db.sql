-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3307
-- Généré le : mar. 02 juin 2026 à 15:32
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `transport_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `passager`
--

CREATE TABLE `passager` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `piece_identite` varchar(50) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `passager`
--

INSERT INTO `passager` (`id`, `nom`, `prenom`, `telephone`, `piece_identite`, `email`) VALUES
(1, 'ATANGANA', 'Marie', '691234567', 'CNI-001234', 'marie@gmail.com'),
(2, 'BEYALA', 'Jean', '692345678', 'CNI-001235', 'jean@gmail.com'),
(3, 'nganfang', 'kengni', '656106891', NULL, 'merveillenganfang88@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `reservation_transport`
--

CREATE TABLE `reservation_transport` (
  `id` bigint(20) NOT NULL,
  `passager_id` bigint(20) NOT NULL,
  `voyage_id` bigint(20) NOT NULL,
  `nb_places` int(11) NOT NULL DEFAULT 1,
  `montant_total` decimal(10,2) NOT NULL,
  `numero_billet` varchar(20) NOT NULL,
  `statut` enum('CONFIRMEE','ANNULEE','EMBARQUEE') DEFAULT 'CONFIRMEE',
  `date_reservation` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservation_transport`
--

INSERT INTO `reservation_transport` (`id`, `passager_id`, `voyage_id`, `nb_places`, `montant_total`, `numero_billet`, `statut`, `date_reservation`) VALUES
(1, 1, 1, 2, 10000.00, 'BIL-20250001', 'CONFIRMEE', '2026-05-15 17:02:40'),
(2, 2, 2, 1, 15000.00, 'BIL-20250002', 'CONFIRMEE', '2026-05-15 17:02:40'),
(3, 1, 1, 1, 5000.00, 'BIL-081F2901', 'CONFIRMEE', '2026-05-26 07:08:11'),
(4, 1, 1, 1, 5000.00, 'BIL-E944F42A', 'CONFIRMEE', '2026-05-27 05:24:01'),
(5, 1, 1, 1, 5000.00, 'BIL-D75BE104', 'CONFIRMEE', '2026-05-27 05:24:23'),
(6, 3, 1, 1, 5000.00, 'BIL-FBCC424F', 'CONFIRMEE', '2026-05-27 05:26:22');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` bigint(20) NOT NULL,
  `login` varchar(80) NOT NULL,
  `mdp_hash` varchar(255) NOT NULL,
  `role` enum('ADMIN','AGENT','CLIENT') NOT NULL,
  `actif` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `login`, `mdp_hash`, `role`, `actif`) VALUES
(1, 'admin', '$2a$10$vytycPhdgSAot/GausFHpevq5HwHKsMIOpowXDQnmdtYdW/NCRTEq', 'ADMIN', 1),
(2, 'agent1', '$2a$10$uskeVe3nF5RPDp9.SwUg5uhjF6NX1Yhs1CgTVL.96H63x0QdlocbG', 'AGENT', 1),
(3, 'client1', '$2a$10$v5pfbMx1sZiEKPGi9fLOnubJVoxZTr1tfHW9N6K2QPfgb8TEerM42', 'CLIENT', 1),
(4, 'nganfang', '$2a$10$7ug/RKYlw3UZtuo9Ch9wqecv1RhO8PWOZm6hueqjs6qTjWIsLW7ey', 'CLIENT', 1);

-- --------------------------------------------------------

--
-- Structure de la table `vehicule`
--

CREATE TABLE `vehicule` (
  `id` bigint(20) NOT NULL,
  `immatriculation` varchar(20) NOT NULL,
  `marque` varchar(80) NOT NULL,
  `modele` varchar(80) NOT NULL,
  `capacite` int(11) NOT NULL,
  `statut` enum('DISPONIBLE','EN_ROUTE','EN_MAINTENANCE','RETIRE') DEFAULT 'DISPONIBLE',
  `date_derniere_revision` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `vehicule`
--

INSERT INTO `vehicule` (`id`, `immatriculation`, `marque`, `modele`, `capacite`, `statut`, `date_derniere_revision`) VALUES
(1, 'LT-001-AB', 'Toyota', 'Hiace', 15, 'DISPONIBLE', NULL),
(2, 'LT-002-CD', 'Mercedes', 'Sprinter', 20, 'DISPONIBLE', NULL),
(3, 'LT-003-EF', 'Hyundai', 'Universe', 45, 'DISPONIBLE', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `voyage`
--

CREATE TABLE `voyage` (
  `id` bigint(20) NOT NULL,
  `vehicule_id` bigint(20) NOT NULL,
  `chauffeur` varchar(100) NOT NULL,
  `ville_depart` varchar(80) NOT NULL,
  `ville_arrivee` varchar(80) NOT NULL,
  `date_heure_depart` datetime NOT NULL,
  `date_heure_arrivee_prevue` datetime DEFAULT NULL,
  `prix_place_fcfa` decimal(8,2) NOT NULL,
  `nb_places_dispo` int(11) NOT NULL,
  `statut` enum('PROGRAMME','EN_COURS','ARRIVE','ANNULE') DEFAULT 'PROGRAMME'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `voyage`
--

INSERT INTO `voyage` (`id`, `vehicule_id`, `chauffeur`, `ville_depart`, `ville_arrivee`, `date_heure_depart`, `date_heure_arrivee_prevue`, `prix_place_fcfa`, `nb_places_dispo`, `statut`) VALUES
(1, 1, 'Jean NOMO', 'Douala', 'Yaoundé', '2025-02-15 07:00:00', NULL, 5000.00, 11, 'PROGRAMME'),
(2, 2, 'Paul BIKOKO', 'Yaoundé', 'Garoua', '2025-02-15 08:00:00', NULL, 15000.00, 20, 'PROGRAMME');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `passager`
--
ALTER TABLE `passager`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reservation_transport`
--
ALTER TABLE `reservation_transport`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_billet` (`numero_billet`),
  ADD KEY `passager_id` (`passager_id`),
  ADD KEY `voyage_id` (`voyage_id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- Index pour la table `vehicule`
--
ALTER TABLE `vehicule`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `immatriculation` (`immatriculation`);

--
-- Index pour la table `voyage`
--
ALTER TABLE `voyage`
  ADD PRIMARY KEY (`id`),
  ADD KEY `vehicule_id` (`vehicule_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `passager`
--
ALTER TABLE `passager`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `reservation_transport`
--
ALTER TABLE `reservation_transport`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `vehicule`
--
ALTER TABLE `vehicule`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `voyage`
--
ALTER TABLE `voyage`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `reservation_transport`
--
ALTER TABLE `reservation_transport`
  ADD CONSTRAINT `reservation_transport_ibfk_1` FOREIGN KEY (`passager_id`) REFERENCES `passager` (`id`),
  ADD CONSTRAINT `reservation_transport_ibfk_2` FOREIGN KEY (`voyage_id`) REFERENCES `voyage` (`id`);

--
-- Contraintes pour la table `voyage`
--
ALTER TABLE `voyage`
  ADD CONSTRAINT `voyage_ibfk_1` FOREIGN KEY (`vehicule_id`) REFERENCES `vehicule` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
