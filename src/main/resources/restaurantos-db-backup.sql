-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2022 at 10:03 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurantos-db`
--

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE `food` (
  `food_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `allergens` varchar(10) NOT NULL,
  `cost` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`food_id`, `type_id`, `name`, `description`, `allergens`, `cost`) VALUES
(1, 0, 'Pizza', 'Best Italian food', 'ABCD', 180),
(2, 0, 'Lasagna', 'Second Italian food', 'ABCD', 210),
(3, 0, 'Pasta', NULL, 'ABCD', 210),
(4, 1, 'CocaCola', NULL, 'A', 30),
(19, 0, 'Ravioli', 'The word ravioli denotes various kinds of pasta ma', 'ABCD', 120),
(20, 0, 'Burrito', 'Burrito is a dish consisting of a wheat flour tort', 'ABCD', 100),
(21, 0, 'Fried Chicken', '', 'ABCD', 70),
(22, 0, 'Pasta carbonara', 'The carbonara we know today is prepared by simply ', 'ABCD', 110),
(23, 0, 'Gyoza', 'The famous Japanese gyoza are crescent-shaped dump', 'ABCD', 90),
(24, 0, 'Cupcake', 'A cupcake is a tiny cake that is baked in a thin p', 'ABCD', 200),
(25, 0, 'Risotto', 'This widely popular and extremely versatile group ', 'ABCD', 150),
(26, 0, 'Mochi', 'Mochi, the tiny cakes made out of glutinous rice. ', 'ABCD', 60);

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `menu_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `created_date` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`menu_id`, `date`, `created_date`) VALUES
(1, '2022-11-17', '2022-11-17'),
(2, '2022-11-19', '2022-11-17'),
(3, '2022-11-20', '2022-11-17'),
(4, '2022-11-21', '2022-11-17'),
(5, '2022-12-04', '2022-12-04'),
(14, '2022-12-05', '2022-12-05');

-- --------------------------------------------------------

--
-- Table structure for table `menu_item`
--

CREATE TABLE `menu_item` (
  `menu_item_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `cost` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `menu_item`
--

INSERT INTO `menu_item` (`menu_item_id`, `menu_id`, `food_id`, `count`, `cost`) VALUES
(1, 1, 1, 40, 0),
(2, 1, 2, 30, 0),
(3, 1, 3, 10, 0),
(5, 2, 1, 40, 0),
(6, 2, 2, 30, 0),
(7, 2, 3, 10, 0),
(9, 3, 1, 40, 0),
(10, 3, 2, 30, 0),
(12, 3, 4, 80, 0),
(13, 4, 1, 40, 0),
(14, 4, 2, 30, 0),
(15, 4, 3, 10, 0),
(28, 5, 3, 7, 0),
(31, 5, 1, 12, 0),
(42, 5, 4, 30, 0),
(58, 5, 26, 28, 0),
(59, 5, 23, 30, 0),
(60, 5, 25, 20, 0),
(61, 5, 20, 31, 0),
(62, 5, 21, 11, 0),
(63, 5, 2, 22, 0),
(64, 5, 19, 11, 0),
(65, 5, 24, 11, 0),
(66, 5, 22, 11, 0),
(71, 14, 19, 18, 120),
(72, 14, 22, 34, 110),
(73, 14, 1, 11, 180),
(74, 14, 26, 36, 60);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `order_id` int(11) NOT NULL,
  `table_id` int(11) NOT NULL,
  `created_date` date NOT NULL DEFAULT current_timestamp(),
  `paid` tinyint(1) DEFAULT 0,
  `created_by` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`order_id`, `table_id`, `created_date`, `paid`, `created_by`) VALUES
(6, 2, '2022-11-17', 0, 0),
(7, 3, '2022-11-17', 0, 0),
(8, 4, '2022-11-17', 0, 0),
(9, 1, '2022-11-17', 0, 0),
(11, 1, '2022-11-20', 0, 0),
(12, 2, '2022-11-22', 0, 0),
(13, 1, '2022-11-20', 0, 0),
(14, 1, '2022-11-20', 0, 0),
(32, 1, '2022-11-20', 0, 0),
(33, 1, '2022-11-20', 0, 0),
(34, 1, '2022-11-20', 0, 0),
(35, 1, '2022-11-20', 0, 0),
(36, 1, '2022-11-20', 0, 0),
(37, 1, '2022-11-22', 0, 0),
(38, 1, '2022-11-22', 0, 0),
(39, 1, '2022-11-22', 0, 0),
(40, 1, '2022-11-23', 0, 0),
(41, 1, '2022-11-23', 0, 0),
(42, 1, '2022-11-23', 0, 0),
(43, 1, '2022-11-23', 0, 0),
(44, 1, '2022-11-23', 0, 0),
(45, 1, '2022-11-30', 0, 0),
(46, 1, '2022-12-04', 1, 0),
(47, 1, '2022-12-04', 1, 0),
(48, 1, '2022-12-04', 1, 0),
(49, 1, '2022-12-04', 1, 0),
(50, 1, '2022-12-04', 1, 0),
(51, 1, '2022-12-04', 1, 0),
(52, 1, '2022-12-04', 0, 0),
(53, 1, '2022-12-04', 1, 0),
(54, 1, '2022-12-04', 1, 0),
(55, 1, '2022-12-04', 1, 0),
(149, 1, '2022-12-04', 1, 0),
(150, 1, '2022-12-04', 1, 0),
(151, 1, '2022-12-04', 1, 0),
(152, 1, '2022-12-04', 0, 19),
(153, 1, '2022-12-04', 1, 0),
(154, 1, '2022-12-04', 0, 0),
(155, 1, '2022-12-04', 0, 0),
(156, 1, '2022-12-05', 1, 0),
(157, 1, '2022-12-05', 1, 19),
(158, 1, '2022-12-05', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `order_item`
--

CREATE TABLE `order_item` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `menu_item_id` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT 0,
  `state` varchar(10) NOT NULL DEFAULT 'Ordered',
  `cooked_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_item`
--

INSERT INTO `order_item` (`order_item_id`, `order_id`, `menu_item_id`, `count`, `state`, `cooked_by`) VALUES
(3, 7, 6, 3, 'Served', NULL),
(4, 7, 6, 3, 'Served', NULL),
(5, 7, 5, 1, 'Served', NULL),
(6, 7, 6, 3, 'Served', NULL),
(7, 8, 9, 1, 'Canceled', NULL),
(8, 9, 12, 3, 'Canceled', NULL),
(9, 14, 3, 4, 'Preparing', NULL),
(10, 14, 2, 3, 'Prepared', NULL),
(11, 14, 3, 3, 'Preparing', NULL),
(12, 12, 2, 4, 'Ordered', NULL),
(13, 12, 1, 3, 'Ordered', NULL),
(14, 12, 2, 2, 'Ordered', NULL),
(15, 12, 1, 3, 'Ordered', NULL),
(16, 12, 3, 2, 'Ordered', NULL),
(17, 12, 2, 2, 'Ordered', NULL),
(18, 12, 3, 3, 'Preparing', NULL),
(19, 12, 3, 3, 'Ordered', NULL),
(20, 12, 3, 2, 'Preparing', NULL),
(21, 12, 2, 2, 'Ordered', NULL),
(22, 12, 2, 3, 'Ordered', NULL),
(23, 12, 2, 3, 'Ordered', NULL),
(24, 12, 3, 3, 'Ordered', NULL),
(25, 12, 1, 2, 'Ordered', NULL),
(26, 12, 1, 2, 'Ordered', NULL),
(27, 8, 1, 1, 'Prepared', NULL),
(28, 8, 2, 4, 'Preparing', NULL),
(29, 14, 2, 2, 'Preparing', NULL),
(30, 11, 2, 1, 'Preparing', NULL),
(31, 11, 3, 3, 'Preparing', NULL),
(32, 11, 3, 3, 'Preparing', NULL),
(33, 11, 2, 4, 'Preparing', NULL),
(34, 11, 3, 3, 'Preparing', NULL),
(35, 8, 1, 3, 'Preparing', NULL),
(36, 9, 1, 3, 'Served', NULL),
(37, 9, 3, 4, 'Served', NULL),
(38, 13, 1, 4, 'Prepared', NULL),
(39, 13, 1, 3, 'Preparing', NULL),
(40, 6, 1, 1, 'Served', NULL),
(41, 32, 2, 2, 'Served', NULL),
(42, 32, 1, 2, 'Served', NULL),
(43, 32, 1, 3, 'Served', NULL),
(44, 33, 1, 1, 'Served', NULL),
(45, 33, 1, 4, 'Served', NULL),
(46, 33, 2, 1, 'Served', NULL),
(47, 33, 1, 3, 'Served', NULL),
(48, 33, 2, 3, 'Served', NULL),
(49, 34, 3, 2, 'Served', NULL),
(50, 34, 2, 2, 'Served', NULL),
(51, 34, 1, 3, 'Served', NULL),
(52, 14, 1, 1, 'Ordered', NULL),
(53, 35, 3, 2, 'Ordered', NULL),
(54, 35, 2, 1, 'Ordered', NULL),
(55, 35, 2, 2, 'Ordered', NULL),
(56, 35, 3, 2, 'Ordered', NULL),
(57, 35, 1, 4, 'Ordered', NULL),
(58, 35, 1, 2, 'Ordered', NULL),
(59, 35, 3, 3, 'Ordered', NULL),
(60, 35, 3, 4, 'Ordered', NULL),
(61, 35, 1, 4, 'Ordered', NULL),
(62, 35, 1, 3, 'Ordered', NULL),
(63, 35, 2, 3, 'Ordered', NULL),
(64, 35, 1, 1, 'Ordered', NULL),
(65, 35, 1, 4, 'Ordered', NULL),
(66, 36, 3, 2, 'Ordered', NULL),
(67, 36, 2, 4, 'Ordered', NULL),
(68, 37, 2, 2, 'Ordered', NULL),
(69, 37, 2, 3, 'Ordered', NULL),
(70, 37, 2, 3, 'Preparing', NULL),
(71, 37, 2, 2, 'Preparing', NULL),
(72, 37, 2, 3, 'Preparing', NULL),
(73, 37, 2, 2, 'Preparing', NULL),
(74, 37, 2, 3, 'Ordered', NULL),
(75, 37, 2, 3, 'Preparing', NULL),
(76, 37, 1, 3, 'Preparing', NULL),
(77, 37, 1, 2, 'Preparing', NULL),
(78, 37, 2, 3, 'Ordered', NULL),
(79, 37, 2, 2, 'Ordered', NULL),
(80, 37, 2, 3, 'Ordered', NULL),
(81, 37, 2, 2, 'Ordered', NULL),
(82, 37, 2, 3, 'Ordered', NULL),
(83, 38, 1, 4, 'Canceled', NULL),
(84, 38, 3, 1, 'Canceled', NULL),
(85, 38, 2, 2, 'Canceled', NULL),
(86, 38, 2, 2, 'Canceled', NULL),
(87, 38, 1, 4, 'Canceled', NULL),
(88, 38, 3, 1, 'Canceled', NULL),
(89, 38, 2, 2, 'Canceled', NULL),
(90, 38, 2, 2, 'Canceled', NULL),
(91, 38, 1, 2, 'Canceled', NULL),
(92, 38, 3, 3, 'Canceled', NULL),
(93, 38, 2, 2, 'Canceled', NULL),
(94, 38, 1, 1, 'Canceled', NULL),
(95, 38, 1, 4, 'Canceled', NULL),
(96, 38, 2, 2, 'Canceled', NULL),
(97, 38, 1, 1, 'Canceled', NULL),
(98, 38, 1, 4, 'Canceled', NULL),
(99, 38, 2, 1, 'Canceled', NULL),
(101, 39, 2, 1, 'Canceled', NULL),
(102, 39, 1, 3, 'Canceled', NULL),
(103, 39, 3, 1, 'Canceled', NULL),
(104, 39, 1, 4, 'Canceled', NULL),
(105, 39, 1, 3, 'Canceled', NULL),
(106, 39, 3, 1, 'Preparing', NULL),
(107, 39, 2, 2, 'Preparing', NULL),
(108, 39, 3, 2, 'Preparing', NULL),
(109, 40, 3, 2, 'Preparing', NULL),
(110, 40, 3, 2, 'Ordered', NULL),
(111, 40, 1, 1, 'Ordered', NULL),
(112, 41, 3, 3, 'Served', NULL),
(113, 41, 3, 4, 'Served', NULL),
(114, 41, 2, 1, 'Served', NULL),
(115, 42, 3, 4, 'Prepared', NULL),
(116, 43, 1, 1, 'Prepared', NULL),
(117, 43, 2, 4, 'Prepared', NULL),
(118, 43, 1, 4, 'Prepared', NULL),
(119, 44, 1, 4, 'Canceled', NULL),
(120, 44, 1, 3, 'Canceled', NULL),
(121, 44, 1, 1, 'Canceled', NULL),
(122, 44, 3, 1, 'Canceled', NULL),
(123, 45, 3, 3, 'Prepared', NULL),
(124, 45, 1, 3, 'Preparing', NULL),
(125, 53, 3, 4, 'Paid', NULL),
(126, 53, 1, 4, 'Paid', NULL),
(127, 53, 2, 2, 'Paid', NULL),
(128, 53, 1, 4, 'Paid', NULL),
(129, 53, 1, 3, 'Paid', NULL),
(130, 53, 1, 2, 'Paid', NULL),
(131, 53, 3, 1, 'Paid', NULL),
(132, 53, 1, 3, 'Paid', NULL),
(133, 53, 1, 3, 'Paid', NULL),
(134, 53, 3, 3, 'Paid', NULL),
(135, 53, 1, 1, 'Paid', NULL),
(136, 53, 1, 4, 'Paid', NULL),
(137, 46, 3, 3, 'Paid', NULL),
(138, 46, 1, 3, 'Paid', NULL),
(139, 46, 2, 3, 'Paid', NULL),
(140, 46, 2, 3, 'Paid', NULL),
(141, 46, 1, 1, 'Paid', NULL),
(142, 47, 1, 1, 'Paid', NULL),
(143, 47, 1, 2, 'Paid', NULL),
(144, 47, 2, 4, 'Paid', NULL),
(145, 48, 1, 3, 'Paid', NULL),
(146, 48, 1, 1, 'Paid', NULL),
(147, 48, 1, 4, 'Paid', NULL),
(148, 48, 3, 4, 'Paid', NULL),
(149, 48, 3, 2, 'Paid', NULL),
(150, 48, 1, 4, 'Paid', NULL),
(151, 48, 1, 2, 'Paid', NULL),
(152, 48, 3, 2, 'Paid', NULL),
(153, 48, 2, 1, 'Paid', NULL),
(154, 48, 2, 4, 'Paid', NULL),
(155, 48, 3, 3, 'Paid', NULL),
(156, 48, 3, 3, 'Paid', NULL),
(157, 48, 3, 3, 'Paid', NULL),
(158, 48, 1, 2, 'Paid', NULL),
(159, 52, 1, 1, 'Paid', NULL),
(160, 52, 1, 2, 'Paid', NULL),
(161, 52, 1, 2, 'Paid', NULL),
(162, 52, 3, 4, 'Paid', NULL),
(163, 52, 3, 1, 'Paid', NULL),
(164, 52, 3, 4, 'Paid', NULL),
(165, 52, 1, 3, 'Paid', NULL),
(166, 52, 1, 4, 'Paid', NULL),
(167, 52, 3, 3, 'Paid', NULL),
(168, 52, 3, 3, 'Paid', NULL),
(169, 52, 2, 4, 'Paid', NULL),
(170, 49, 1, 4, 'Paid', NULL),
(171, 49, 2, 2, 'Paid', NULL),
(172, 49, 3, 3, 'Paid', NULL),
(173, 50, 2, 3, 'Paid', NULL),
(174, 50, 3, 4, 'Paid', NULL),
(175, 50, 2, 4, 'Paid', NULL),
(176, 50, 3, 4, 'Paid', NULL),
(177, 50, 2, 1, 'Paid', NULL),
(178, 54, 42, 3, 'Paid', NULL),
(179, 54, 42, 2, 'Paid', NULL),
(180, 54, 31, 1, 'Paid', NULL),
(181, 54, 31, 3, 'Paid', NULL),
(182, 54, 28, 2, 'Paid', NULL),
(183, 54, 31, 3, 'Paid', NULL),
(184, 55, 28, 1, 'Paid', NULL),
(185, 55, 28, 3, 'Paid', NULL),
(186, 55, 42, 1, 'Paid', NULL),
(187, 55, 31, 1, 'Paid', NULL),
(188, 51, 42, 4, 'Paid', NULL),
(189, 51, 31, 2, 'Paid', NULL),
(190, 51, 28, 3, 'Paid', NULL),
(191, 149, 31, 2, 'Paid', NULL),
(192, 149, 31, 2, 'Paid', NULL),
(193, 150, 64, 1, 'Paid', NULL),
(194, 150, 62, 2, 'Paid', NULL),
(195, 150, 28, 2, 'Paid', NULL),
(196, 150, 61, 4, 'Paid', NULL),
(197, 150, 64, 2, 'Paid', NULL),
(198, 150, 62, 2, 'Paid', NULL),
(199, 151, 61, 2, 'Paid', NULL),
(200, 151, 58, 2, 'Paid', NULL),
(201, 151, 62, 1, 'Paid', NULL),
(202, 151, 65, 3, 'Paid', NULL),
(203, 151, 63, 3, 'Paid', NULL),
(204, 152, 62, 2, 'Canceled', NULL),
(205, 152, 61, 2, 'Canceled', NULL),
(206, 152, 64, 4, 'Canceled', NULL),
(207, 153, 65, 1, 'Paid', NULL),
(208, 153, 66, 4, 'Paid', NULL),
(209, 153, 59, 3, 'Paid', NULL),
(210, 154, 28, 2, 'Served', NULL),
(211, 154, 42, 2, 'Served', NULL),
(212, 154, 59, 3, 'Served', NULL),
(213, 155, 66, 2, 'Prepared', NULL),
(214, 155, 28, 3, 'Ordered', NULL),
(215, 155, 31, 4, 'Preparing', NULL),
(220, 156, 74, 1, 'Paid', NULL),
(221, 156, 71, 4, 'Paid', NULL),
(222, 156, 72, 4, 'Paid', NULL),
(223, 156, 72, 3, 'Paid', NULL),
(224, 156, 72, 3, 'Paid', NULL),
(225, 156, 71, 3, 'Paid', NULL),
(226, 156, 72, 2, 'Paid', NULL),
(227, 156, 74, 1, 'Paid', NULL),
(228, 156, 73, 3, 'Paid', NULL),
(229, 156, 73, 2, 'Paid', NULL),
(230, 156, 73, 3, 'Paid', NULL),
(231, 156, 73, 1, 'Paid', NULL),
(232, 156, 74, 1, 'Paid', NULL),
(233, 156, 72, 4, 'Paid', NULL),
(234, 157, 72, 3, 'Paid', NULL),
(235, 157, 71, 4, 'Paid', NULL),
(236, 157, 71, 4, 'Paid', NULL),
(237, 158, 72, 1, 'Paid', NULL),
(238, 158, 71, 4, 'Paid', NULL),
(239, 158, 73, 3, 'Paid', NULL),
(240, 158, 73, 2, 'Paid', NULL),
(241, 158, 74, 2, 'Paid', NULL),
(242, 158, 73, 4, 'Paid', NULL),
(243, 158, 74, 3, 'Paid', NULL),
(244, 158, 74, 2, 'Paid', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `name` varchar(10) NOT NULL,
  `description` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `name`, `description`) VALUES
(0, 'Manager', 'Main role of DB and App'),
(1, 'Service', 'People who serve food'),
(2, 'Chef', 'Creates food for customers');

-- --------------------------------------------------------

--
-- Table structure for table `table`
--

CREATE TABLE `table` (
  `table_id` int(11) NOT NULL,
  `capacity` int(11) NOT NULL DEFAULT 0,
  `reserved` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `table`
--

INSERT INTO `table` (`table_id`, `capacity`, `reserved`) VALUES
(1, 4, 0),
(2, 3, 0),
(3, 4, 0),
(4, 6, 0),
(5, 8, 0);

-- --------------------------------------------------------

--
-- Table structure for table `type`
--

CREATE TABLE `type` (
  `type_id` int(11) NOT NULL,
  `name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `type`
--

INSERT INTO `type` (`type_id`, `name`) VALUES
(0, 'Food'),
(1, 'Drink');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(10) NOT NULL,
  `last_name` varchar(10) NOT NULL,
  `born_date` date NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL
) ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `first_name`, `last_name`, `born_date`, `email`, `password`, `role_id`) VALUES
(0, 'Tomáš', 'Sobota', '2002-01-01', 'manager@gmail.com', '$s0$41010$qxQSi8DYWPIbJeltM0ddFQ==$aSb/EeeBMu/DKTOZcCMzqv5lb11SnQCnlvvqpC5slhY=', 0),
(7, 'Ludvik', 'Trusty', '2001-09-11', 'chef@gmail.com', '$s0$41010$Yt/7O3oYebRzlmOPzNidPg==$aoZyGqohn2JWdf3NQ6+sja3XT2avusPUanX5RO7hz8A=', 2),
(8, 'Klára', 'Strong', '2000-01-01', 'service@gmail.com', '$s0$41010$AQxwGF5ne4x+YFpFtyO2MA==$mXDJV+mFSpDuhDROGqGjiZgKRxBRCikf9KGkIryGOJM=', 1),
(19, 'Laura', 'New', '2000-12-10', 'laura@gmail.com', '$s0$41010$HIF4UEcNqzvNrQbVH/9R0A==$EdGDBJelyix5NfxlktDE3/SKQC2MpWr61BbVQNC9Iwg=', 1),
(21, 'Emma', 'Nobody', '2001-01-01', 'emma64@gmail.com', '$s0$41010$5LBeTDUbLzCOGY5374484Q==$CUhdO98J6z9H2LlnnuZvw1fO0bnO055+DSopSN5a8NY=', 1),
(25, 'Mirek', 'Luboš', '2000-12-24', 'lubos@gmail.com', '$s0$41010$Y0jU1UIN5seXzOpHhjiFnw==$xd9Lp3M/vKeTc3DcGS2Yg4+GuDFBhVCoJHuuno/P/y4=', 2),
(26, 'Marek', 'Kuchař', '2000-01-01', 'marek@gmail.com', '$s0$41010$HTvzuYSyVVuipfUK1FcMCA==$emUwHU37zAQ1pBODlVxl2NlHjj9atDFsNN9YUI9vE3s=', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`food_id`),
  ADD KEY `c_type_id` (`type_id`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- Indexes for table `menu_item`
--
ALTER TABLE `menu_item`
  ADD PRIMARY KEY (`menu_item_id`),
  ADD KEY `c_food_id` (`food_id`),
  ADD KEY `c_menu_id` (`menu_id`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `fk_created_by` (`created_by`),
  ADD KEY `tf_table_id` (`table_id`);

--
-- Indexes for table `order_item`
--
ALTER TABLE `order_item`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `fk_cooked_by` (`cooked_by`),
  ADD KEY `fk_menu_item_id` (`menu_item_id`),
  ADD KEY `fk_order_id` (`order_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `table`
--
ALTER TABLE `table`
  ADD PRIMARY KEY (`table_id`);

--
-- Indexes for table `type`
--
ALTER TABLE `type`
  ADD PRIMARY KEY (`type_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `c_role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `food`
--
ALTER TABLE `food`
  MODIFY `food_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `menu_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `menu_item`
--
ALTER TABLE `menu_item`
  MODIFY `menu_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=159;

--
-- AUTO_INCREMENT for table `order_item`
--
ALTER TABLE `order_item`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=245;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `table`
--
ALTER TABLE `table`
  MODIFY `table_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `type`
--
ALTER TABLE `type`
  MODIFY `type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `food`
--
ALTER TABLE `food`
  ADD CONSTRAINT `c_type_id` FOREIGN KEY (`type_id`) REFERENCES `type` (`type_id`);

--
-- Constraints for table `menu_item`
--
ALTER TABLE `menu_item`
  ADD CONSTRAINT `c_food_id` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`),
  ADD CONSTRAINT `c_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `fk_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `tf_table_id` FOREIGN KEY (`table_id`) REFERENCES `table` (`table_id`);

--
-- Constraints for table `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `fk_cooked_by` FOREIGN KEY (`cooked_by`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `fk_menu_item_id` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_item` (`menu_item_id`),
  ADD CONSTRAINT `fk_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `c_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
