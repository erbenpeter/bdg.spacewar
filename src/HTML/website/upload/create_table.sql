-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Hoszt: 127.0.0.1
-- Létrehozás ideje: 2014. jún. 11. 11:33
-- Szerver verzió: 5.0.45
-- PHP verzió: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Adatbázis: `bdgspacewar`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet: `uploaded_games`
--

CREATE TABLE IF NOT EXISTS `uploaded_games` (
  `id` int(64) NOT NULL auto_increment,
  `player1` text character set utf8 collate utf8_unicode_ci NOT NULL,
  `player2` text character set utf8 collate utf8_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `ip` varchar(23) character set utf8 collate utf8_unicode_ci NOT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- A tábla adatainak kiíratása `uploaded_games`
--

