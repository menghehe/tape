/*
 Navicat Premium Data Transfer

 Source Server         : 本地docker
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : tape

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 14/10/2020 23:16:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tape_authority
-- ----------------------------
DROP TABLE IF EXISTS `tape_authority`;
CREATE TABLE `tape_authority`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `authority` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_authority
-- ----------------------------
INSERT INTO `tape_authority` VALUES (1, 'ROLE_USER');
INSERT INTO `tape_authority` VALUES (2, 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tape_clip
-- ----------------------------
DROP TABLE IF EXISTS `tape_clip`;
CREATE TABLE `tape_clip`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `source_file` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传源文件存放路径',
  `clip_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频存放路径',
  `cover_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面存放路径',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_man` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_man` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0否，1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_clip
-- ----------------------------

-- ----------------------------
-- Table structure for tape_comment
-- ----------------------------
DROP TABLE IF EXISTS `tape_comment`;
CREATE TABLE `tape_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `to_id` bigint NULL DEFAULT NULL COMMENT '被评论人ID',
  `from_id` bigint NULL DEFAULT NULL COMMENT '评论人ID',
  `clip_id` bigint NULL DEFAULT NULL COMMENT '视频ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_man` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_man` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0否，1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_comment
-- ----------------------------

-- ----------------------------
-- Table structure for tape_friend
-- ----------------------------
DROP TABLE IF EXISTS `tape_friend`;
CREATE TABLE `tape_friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `follower` bigint NULL DEFAULT NULL COMMENT '粉丝ID',
  `following` bigint NULL DEFAULT NULL COMMENT '被关注ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_man` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_man` binary(1) NULL DEFAULT NULL COMMENT '创建人ID',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0否，1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_friend
-- ----------------------------

-- ----------------------------
-- Table structure for tape_like
-- ----------------------------
DROP TABLE IF EXISTS `tape_like`;
CREATE TABLE `tape_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `like_type` int NULL DEFAULT NULL COMMENT '点赞类型 1视频，2评论',
  `from_id` bigint NULL DEFAULT NULL COMMENT '点赞人ID',
  `to_id` bigint NULL DEFAULT NULL COMMENT '被点赞人ID',
  `target_id` bigint NULL DEFAULT NULL COMMENT '视频或评论ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_man` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_man` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0否，1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_like
-- ----------------------------

-- ----------------------------
-- Table structure for tape_user
-- ----------------------------
DROP TABLE IF EXISTS `tape_user`;
CREATE TABLE `tape_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像存放路径',
  `signature` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名',
  `is_locked` tinyint NULL DEFAULT NULL COMMENT '是否封禁 1 是 0否',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_man` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_man` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0否,1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_user
-- ----------------------------

-- ----------------------------
-- Table structure for tape_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `tape_user_authority`;
CREATE TABLE `tape_user_authority`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `authority_id` int NULL DEFAULT NULL COMMENT '权限ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_man` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_man` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0否,1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tape_user_authority
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
