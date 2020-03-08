/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : tape

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 08/03/2020 20:24:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority` VALUES (1, 'ROLE_USER');
INSERT INTO `authority` VALUES (2, 'ROLE_ADMIN');
INSERT INTO `authority` VALUES (3, 'ROLE_REVIEWER');

-- ----------------------------
-- Table structure for clip
-- ----------------------------
DROP TABLE IF EXISTS `clip`;
CREATE TABLE `clip`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `clip_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creator` int(255) NOT NULL,
  `is_transcoded` int(255) NULL DEFAULT NULL,
  `is_del` int(255) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_man` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_man` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of clip
-- ----------------------------
INSERT INTO `clip` VALUES (1, 'frozen', '19a0d4fc6485435fa18a2e2c6eef3cf7.mp4', '19a0d4fc6485435fa18a2e2c6eef3cf7.m3u8', '19a0d4fc6485435fa18a2e2c6eef3cf7.png', 1, NULL, 0, NULL, NULL, '2020-03-02 19:14:02', 1);
INSERT INTO `clip` VALUES (2, NULL, '58c80121794644cb928fd025650bcd6c.mp4', '58c80121794644cb928fd025650bcd6c.m3u8', '58c80121794644cb928fd025650bcd6c.png', 1, NULL, 0, NULL, NULL, '2020-03-02 19:15:58', 1);
INSERT INTO `clip` VALUES (3, 'wtf', 'e6b3c982f5f24e8aa73a325b00688f8a.mp4', 'e6b3c982f5f24e8aa73a325b00688f8a.m3u8', 'e6b3c982f5f24e8aa73a325b00688f8a.png', 1, NULL, 0, NULL, NULL, '2020-03-05 15:55:04', 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_man` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'menghe', '$2a$10$Q.k8xSlXr4etPkOeh9y5nOVtl/saUp3nXBp0AfvUaRnZqYnLREE9.', NULL, NULL, NULL, 'default.jpg', NULL, NULL, NULL, '2020-02-22 13:43:22', '0');
INSERT INTO `user` VALUES (2, 'event', '$2a$10$oJmLGxtymlMwVGViWtU4hey7H.Pt2rXizhsrdU3Jwxbma64KbGRV2', NULL, NULL, NULL, 'default.jpg', NULL, NULL, NULL, '2020-02-22 14:11:50', '0');
INSERT INTO `user` VALUES (3, 'menghehe', '$2a$10$/JlbahMKZMJQ5/LXEylwb.5MCrYGDXMvX./YINwKOU0w0MFTtVUMq', NULL, NULL, NULL, 'default.jpg', NULL, NULL, NULL, '2020-03-04 13:44:52', '0');

-- ----------------------------
-- Table structure for user_authority
-- ----------------------------
DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `authority` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_authority
-- ----------------------------
INSERT INTO `user_authority` VALUES (1, 1, 1);
INSERT INTO `user_authority` VALUES (2, 2, 1);
INSERT INTO `user_authority` VALUES (3, 3, 1);

SET FOREIGN_KEY_CHECKS = 1;
