-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2022-01-16 15:15:10
-- 伺服器版本： 10.4.14-MariaDB
-- PHP 版本： 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `test`
--

-- --------------------------------------------------------

--
-- 資料表結構 `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `email` varchar(64) NOT NULL,
  `account` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `admin`
--

INSERT INTO `admin` (`admin_id`, `email`, `account`, `password`, `created_at`) VALUES
(1, 'admintest1@g.ncu.edu.tw', 'admin001', 'admintest1', '2021-01-01 12:00:05'),
(2, 'admintest2@g.ncu.edu.tw', 'admin002', 'admintest2', '2021-02-21 10:00:01'),
(3, 'admintest3@g.ncu.edu.tw', 'admin003', 'admintest3', '2021-12-11 23:20:00');

-- --------------------------------------------------------

--
-- 資料表結構 `bargain`
--

CREATE TABLE `bargain` (
  `bargain_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `lower_limit` int(10) UNSIGNED NOT NULL,
  `upper_limit` int(10) UNSIGNED NOT NULL,
  `final_price` int(10) UNSIGNED DEFAULT NULL,
  `is_rejected` char(1) DEFAULT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='		';

--
-- 傾印資料表的資料 `bargain`
--

INSERT INTO `bargain` (`bargain_id`, `customer_id`, `supplier_id`, `product_id`, `lower_limit`, `upper_limit`, `final_price`, `is_rejected`, `created_at`) VALUES
(1, 1, 2, 1, 50, 150, 150, 'n', '2021-12-12 23:20:01'),
(2, 1, 3, 3, 300, 450, 400, 'n', '2021-12-12 23:20:10'),
(3, 4, 2, 1, 50, 150, 100, 'n', '2021-12-13 23:20:01'),
(4, 2, 3, 3, 200, 400, 350, 'n', '2021-12-13 23:20:02'),
(5, 4, 3, 3, 100, 250, NULL, 'y', '2021-12-14 23:20:03'),
(6, 5, 3, 3, 300, 400, NULL, 'n', '2021-12-15 23:20:04'),
(7, 5, 3, 4, 50, 100, 80, 'n', '2021-12-15 23:20:05'),
(8, 1, 3, 4, 20, 70, NULL, 'y', '2021-12-15 23:20:06'),
(9, 3, 2, 1, 100, 150, NULL, 'n', '2021-12-15 23:20:07'),
(10, 9, 3, 3, 300, 400, NULL, 'n', '2021-12-15 23:20:08');

-- --------------------------------------------------------

--
-- 資料表結構 `cart`
--

CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_num` int(10) UNSIGNED NOT NULL,
  `product_price` int(10) UNSIGNED NOT NULL,
  `is_bargained` char(1) DEFAULT 'n',
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `cart`
--

INSERT INTO `cart` (`cart_id`, `customer_id`, `product_id`, `product_num`, `product_price`, `is_bargained`, `created_at`) VALUES
(1, 4, 1, 2, 200, 'n', '2021-12-13 23:20:03'),
(2, 4, 5, 1, 50, 'n', '2021-12-13 23:20:04'),
(3, 1, 2, 1, 300, 'n', '2021-12-13 23:20:05'),
(4, 1, 1, 1, 100, 'y', '2021-12-14 23:20:08'),
(5, 2, 4, 3, 100, 'n', '2021-12-14 23:20:09'),
(6, 2, 3, 1, 350, 'y', '2021-12-14 23:20:10');

-- --------------------------------------------------------

--
-- 資料表結構 `member`
--

CREATE TABLE `member` (
  `member_id` int(11) NOT NULL,
  `email` varchar(64) NOT NULL,
  `account` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `status` char(1) NOT NULL DEFAULT 'n',
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `member`
--

INSERT INTO `member` (`member_id`, `email`, `account`, `password`, `name`, `phone`, `status`, `created_at`) VALUES
(1, 'membertest1@g.ncu.edu.tw', '108999001', 'membertest1', 'member1', '0900000001', 'y', '2021-01-01 12:00:05'),
(2, 'membertest2@g.ncu.edu.tw', '108999002', 'membertest2', 'member2', '0900000002', 'y', '2021-07-14 14:40:20'),
(3, 'membertest3@g.ncu.edu.tw', '108999003', 'membertest3', 'member3', '0900000003', 'y', '2021-12-11 23:20:01'),
(4, 'membertest4@g.ncu.edu.tw', '108999004', 'membertest4', 'member4', '0900000004', 'y', '2021-12-12 23:20:02'),
(5, 'membertest5@g.ncu.edu.tw', '108999005', 'membertest5', 'member5', '0900000005', 'y', '2021-12-14 23:20:03'),
(6, 'membertest6@g.ncu.edu.tw', '108999006', 'membertest6', 'member6', '0900000006', 'n', '2021-12-14 23:20:04'),
(7, 'membertest7@g.ncu.edu.tw', '108999007', 'membertest7', 'member7', '0900000007', 'n', '2021-12-14 23:20:05'),
(8, 'membertest8@g.ncu.edu.tw', '108999008', 'membertest8', 'member8', '0900000008', 'n', '2021-12-14 23:20:06'),
(9, 'membertest9@g.ncu.edu.tw', '108999009', 'membertest9', 'member9', '0900000009', 'y', '2021-12-14 23:20:07'),
(10, 'membertest10@g.ncu.edu.tw', '108999010', 'membertest10', 'member10', '0900000010', 'n', '2022-01-14 21:52:01'),
(11, 'membertest11@g.ncu.edu.tw', '108999011', 'membertest11', 'member11', '0900000011', 'n', '2022-01-15 21:53:15');

-- --------------------------------------------------------

--
-- 資料表結構 `order`
--

CREATE TABLE `order` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `address` varchar(64) NOT NULL,
  `totalPrice` int(10) UNSIGNED NOT NULL,
  `shipping_code` varchar(64) DEFAULT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `order`
--

INSERT INTO `order` (`order_id`, `customer_id`, `status`, `address`, `totalPrice`, `shipping_code`, `created_at`) VALUES
(1, 1, 4, '中壢中央一店(松果)', 1250, 'S0000001', '2021-12-21 03:20:09'),
(2, 4, 4, '中壢中央二店(男九)', 150, 'S0000002', '2021-12-21 03:20:12'),
(3, 5, 2, '中壢中央三店(松苑)', 250, 'S0000003', '2021-12-21 03:20:14');

-- --------------------------------------------------------

--
-- 資料表結構 `order_details`
--

CREATE TABLE `order_details` (
  `order_details_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_num` int(10) UNSIGNED NOT NULL,
  `product_price` int(10) UNSIGNED NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `order_details`
--

INSERT INTO `order_details` (`order_details_id`, `order_id`, `customer_id`, `supplier_id`, `product_id`, `product_num`, `product_price`, `created_at`) VALUES
(1, 1, 1, 2, 1, 1, 150, '2021-12-21 03:20:09'),
(2, 1, 1, 2, 2, 1, 300, '2021-12-21 03:20:09'),
(3, 1, 1, 3, 3, 2, 400, '2021-12-21 03:20:09'),
(4, 2, 4, 1, 5, 1, 50, '2021-12-21 03:20:12'),
(5, 2, 4, 3, 4, 1, 100, '2021-12-21 03:20:12'),
(6, 3, 5, 1, 5, 1, 50, '2021-12-21 03:20:14'),
(7, 3, 5, 2, 2, 2, 300, '2021-12-21 03:20:14');

-- --------------------------------------------------------

--
-- 資料表結構 `photo`
--

CREATE TABLE `photo` (
  `photo_id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `imgName` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `photo`
--

INSERT INTO `photo` (`photo_id`, `member_id`, `product_id`, `imgName`, `created_at`) VALUES
(1, 1, NULL, 'photo1.jpeg', '2021-12-20 23:20:00'),
(2, NULL, 1, 'p00011.jpg', '2021-12-20 23:20:01'),
(3, NULL, 1, 'p00012.jpg', '2021-12-20 23:20:02'),
(4, NULL, 2, 'p00021.jpg', '2021-12-20 23:20:03'),
(5, NULL, 2, 'p00022.jpg', '2021-12-20 23:20:04'),
(6, 3, NULL, 'photo6.png', '2021-12-20 23:20:05'),
(7, NULL, 3, 'p00031.jpg', '2021-12-20 23:20:06'),
(8, NULL, 3, 'p00032.jpg', '2021-12-20 23:20:07'),
(9, NULL, 4, 'p00041.jpg', '2021-12-20 23:20:08'),
(10, NULL, 4, 'p00042.jpg', '2021-12-20 23:20:09'),
(11, 9, NULL, 'photo11.png', '2021-12-20 23:20:10'),
(12, NULL, 5, 'p00051.jpg', '2022-01-05 14:18:40'),
(13, NULL, 5, 'p00052.jpg', '2022-01-05 14:18:40'),
(14, 10, NULL, 'cardtest1.jfif', '2022-01-21 21:54:17'),
(15, 11, NULL, 'cardtest2.jfif', '2022-01-15 21:54:56');

-- --------------------------------------------------------

--
-- 資料表結構 `product`
--

CREATE TABLE `product` (
  `product_id` int(11) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `price` int(10) UNSIGNED NOT NULL,
  `stock_num` int(10) UNSIGNED NOT NULL,
  `author` varchar(64) NOT NULL,
  `condition` int(11) NOT NULL,
  `is_bargain` char(1) NOT NULL DEFAULT 'n',
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `product`
--

INSERT INTO `product` (`product_id`, `supplier_id`, `name`, `price`, `stock_num`, `author`, `condition`, `is_bargain`, `created_at`) VALUES
(1, 2, '合境平安\r\n', 200, 6, '楊富閔\r\n', 2, 'y', '2021-12-12 23:20:01'),
(2, 2, '擊敗渣男\r\n', 300, 5, '吳孟玲\r\n', 2, 'n', '2021-12-12 23:20:02'),
(3, 3, '擊退奧客\r\n', 500, 4, '東弘樹\r\n', 3, 'y', '2021-12-12 23:20:03'),
(4, 3, '台北多謝', 100, 5, '男子的日常生活\r\n', 1, 'y', '2021-12-12 23:20:04'),
(5, 1, '愛上名為自己的風景\r\n', 50, 3, '李秉律\r\n', 0, 'n', '2021-12-12 23:20:05');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `admin_id_UNIQUE` (`admin_id`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`),
  ADD UNIQUE KEY `account_UNIQUE` (`account`);

--
-- 資料表索引 `bargain`
--
ALTER TABLE `bargain`
  ADD PRIMARY KEY (`bargain_id`),
  ADD UNIQUE KEY `bargain_id_UNIQUE` (`bargain_id`),
  ADD KEY `tblBargain_tblMember_customertId_fk_idx` (`customer_id`),
  ADD KEY `tblOrderDetails_tblProduct_productId_fk_idx` (`product_id`),
  ADD KEY `tblBargain_tblMember_supplierIds_fk_idx` (`supplier_id`);

--
-- 資料表索引 `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `tblCart_tbProduct_productId_fk_idx` (`product_id`),
  ADD KEY `tblCart_tblMember_customerId_fk_idx` (`customer_id`);

--
-- 資料表索引 `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`member_id`),
  ADD UNIQUE KEY `member_id_UNIQUE` (`member_id`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`),
  ADD UNIQUE KEY `account_UNIQUE` (`account`),
  ADD UNIQUE KEY `phone_UNIQUE` (`phone`);

--
-- 資料表索引 `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`order_id`),
  ADD UNIQUE KEY `order_id_UNIQUE` (`order_id`),
  ADD UNIQUE KEY `shipping_code_UNIQUE` (`shipping_code`),
  ADD KEY `tblOrder_tblMember_customerId_fk_idx` (`customer_id`);

--
-- 資料表索引 `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`order_details_id`,`order_id`),
  ADD UNIQUE KEY `order_details_id_UNIQUE` (`order_details_id`),
  ADD KEY `tblOrderDetails_tblOrder_orderId_fk_idx` (`order_id`),
  ADD KEY `tblOrderDetails_tblMember_customerId_fk_idx` (`customer_id`),
  ADD KEY `tblOrderDetails_tblProduct_productId_fk_idx` (`product_id`),
  ADD KEY `tblOrderDetails_tblMember_supplierId_fk_idx` (`supplier_id`);

--
-- 資料表索引 `photo`
--
ALTER TABLE `photo`
  ADD PRIMARY KEY (`photo_id`),
  ADD UNIQUE KEY `photo_id_UNIQUE` (`photo_id`),
  ADD UNIQUE KEY `imgName_UNIQUE` (`imgName`),
  ADD KEY `tblPhoto_tbMember_memberId_fk_idx` (`member_id`),
  ADD KEY `tblPhoto_tblProduct_productId_fk_idx` (`product_id`);

--
-- 資料表索引 `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`,`supplier_id`),
  ADD UNIQUE KEY `product_id_UNIQUE` (`product_id`),
  ADD KEY `supplier_id_idx` (`supplier_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `bargain`
--
ALTER TABLE `bargain`
  MODIFY `bargain_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `member`
--
ALTER TABLE `member`
  MODIFY `member_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `order`
--
ALTER TABLE `order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `order_details`
--
ALTER TABLE `order_details`
  MODIFY `order_details_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `photo`
--
ALTER TABLE `photo`
  MODIFY `photo_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `product`
--
ALTER TABLE `product`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `bargain`
--
ALTER TABLE `bargain`
  ADD CONSTRAINT `tblBargain_tblMember_customertId_fk` FOREIGN KEY (`customer_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblBargain_tblMember_supplierIds_fk` FOREIGN KEY (`supplier_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblBargain_tblProduct_productId_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- 資料表的限制式 `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `tblCart_tblMember_customerId_fk` FOREIGN KEY (`customer_id`) REFERENCES `member` (`member_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblCart_tblProduct_productId_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- 資料表的限制式 `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `tblOrder_tblMember_customerId_fk` FOREIGN KEY (`customer_id`) REFERENCES `member` (`member_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- 資料表的限制式 `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `tblOrderDetails_tblMember_customerId_fk` FOREIGN KEY (`customer_id`) REFERENCES `member` (`member_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblOrderDetails_tblMember_supplierId_fk` FOREIGN KEY (`supplier_id`) REFERENCES `member` (`member_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblOrderDetails_tblOrder_orderId_fk` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblOrderDetails_tblProduct_productId_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- 資料表的限制式 `photo`
--
ALTER TABLE `photo`
  ADD CONSTRAINT `tblPhoto_tblMember_memberId_fk` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tblPhoto_tblProduct_productId_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `tblProduct_tblMember_supplierId_fk` FOREIGN KEY (`supplier_id`) REFERENCES `member` (`member_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
