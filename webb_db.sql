/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : webb_db

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 29/06/2025 15:36:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for audit_logs
-- ----------------------------
DROP TABLE IF EXISTS `audit_logs`;
CREATE TABLE `audit_logs`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志唯一ID',
  `user_id` int NULL DEFAULT NULL COMMENT '操作用户ID (可为空，如系统自动任务)',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户名',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作者IP地址',
  `action_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型 (例如: LOGIN_SUCCESS, LOGIN_FAILURE, VIEW_EMPLOYEES, ADD_DEPARTMENT)',
  `target_resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作对象描述 (例如: Employee ID: 123, Department Name: 销售部)',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作详情 (例如: 更新员工“张三”的手机号)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作状态 (SUCCESS, FAILURE)',
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志记录时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_log_time`(`log_time` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_action_type`(`action_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 598 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '安全审计日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of audit_logs
-- ----------------------------
INSERT INTO `audit_logs` VALUES (1, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 15:23:33');
INSERT INTO `audit_logs` VALUES (2, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-06 15:23:36');
INSERT INTO `audit_logs` VALUES (3, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-06 15:23:40');
INSERT INTO `audit_logs` VALUES (4, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-06 15:23:42');
INSERT INTO `audit_logs` VALUES (5, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-06 15:23:43');
INSERT INTO `audit_logs` VALUES (6, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-06 15:23:44');
INSERT INTO `audit_logs` VALUES (7, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 15:28:42');
INSERT INTO `audit_logs` VALUES (8, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-06 15:29:03');
INSERT INTO `audit_logs` VALUES (9, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-06 15:29:08');
INSERT INTO `audit_logs` VALUES (10, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-06 15:29:08');
INSERT INTO `audit_logs` VALUES (11, 1, 'admin', '0:0:0:0:0:0:0:1', '更新部门', '部门ID: 1', '成功更新部门: 销售部门', 'SUCCESS', '2025-06-06 15:29:13');
INSERT INTO `audit_logs` VALUES (12, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-06 15:29:13');
INSERT INTO `audit_logs` VALUES (13, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 15:55:20');
INSERT INTO `audit_logs` VALUES (14, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-06 15:55:22');
INSERT INTO `audit_logs` VALUES (15, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 15:55:25');
INSERT INTO `audit_logs` VALUES (16, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 15:55:31');
INSERT INTO `audit_logs` VALUES (17, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 15:57:58');
INSERT INTO `audit_logs` VALUES (18, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 15:58:07');
INSERT INTO `audit_logs` VALUES (19, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 15:58:09');
INSERT INTO `audit_logs` VALUES (20, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 16:00:45');
INSERT INTO `audit_logs` VALUES (21, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:00:47');
INSERT INTO `audit_logs` VALUES (22, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 16:11:14');
INSERT INTO `audit_logs` VALUES (23, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:11:18');
INSERT INTO `audit_logs` VALUES (24, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:11:24');
INSERT INTO `audit_logs` VALUES (25, 1, 'admin', '0:0:0:0:0:0:0:1', '添加用户', '新用户: rsgly', '成功添加系统用户。', 'SUCCESS', '2025-06-06 16:12:34');
INSERT INTO `audit_logs` VALUES (26, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:12:34');
INSERT INTO `audit_logs` VALUES (27, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-06 16:12:46');
INSERT INTO `audit_logs` VALUES (28, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-06 16:12:51');
INSERT INTO `audit_logs` VALUES (29, NULL, 'rsgly', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: rsgly', '登录失败原因: 账户未激活，请联系管理员。', 'FAILURE', '2025-06-06 16:13:04');
INSERT INTO `audit_logs` VALUES (30, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-06 16:13:10');
INSERT INTO `audit_logs` VALUES (31, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 16:13:14');
INSERT INTO `audit_logs` VALUES (32, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:13:16');
INSERT INTO `audit_logs` VALUES (33, 1, 'admin', '0:0:0:0:0:0:0:1', '更新用户', '用户ID: 2', '成功更新用户信息。', 'SUCCESS', '2025-06-06 16:13:23');
INSERT INTO `audit_logs` VALUES (34, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:13:23');
INSERT INTO `audit_logs` VALUES (35, 1, 'admin', '0:0:0:0:0:0:0:1', '添加用户', '新用户: cwgly', '成功添加系统用户。', 'SUCCESS', '2025-06-06 16:13:56');
INSERT INTO `audit_logs` VALUES (36, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:13:56');
INSERT INTO `audit_logs` VALUES (37, 1, 'admin', '0:0:0:0:0:0:0:1', '添加用户', '新用户: zjl', '成功添加系统用户。', 'SUCCESS', '2025-06-06 16:15:01');
INSERT INTO `audit_logs` VALUES (38, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:15:01');
INSERT INTO `audit_logs` VALUES (39, 1, 'admin', '0:0:0:0:0:0:0:1', '添加用户', '新用户: sjy', '成功添加系统用户。', 'SUCCESS', '2025-06-06 16:15:26');
INSERT INTO `audit_logs` VALUES (40, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:15:26');
INSERT INTO `audit_logs` VALUES (41, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:15:30');
INSERT INTO `audit_logs` VALUES (42, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-06 16:15:32');
INSERT INTO `audit_logs` VALUES (43, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: sjy', '用户登录成功。', 'SUCCESS', '2025-06-06 16:15:41');
INSERT INTO `audit_logs` VALUES (44, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登出', '用户: sjy', '用户主动登出。', 'SUCCESS', '2025-06-06 16:15:53');
INSERT INTO `audit_logs` VALUES (45, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl', '用户登录成功。', 'SUCCESS', '2025-06-06 16:16:08');
INSERT INTO `audit_logs` VALUES (46, 4, 'zjl', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-06 16:16:10');
INSERT INTO `audit_logs` VALUES (47, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl', '用户主动登出。', 'SUCCESS', '2025-06-06 16:16:18');
INSERT INTO `audit_logs` VALUES (48, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 16:16:24');
INSERT INTO `audit_logs` VALUES (49, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:16:27');
INSERT INTO `audit_logs` VALUES (50, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-06 16:19:08');
INSERT INTO `audit_logs` VALUES (51, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-06 16:19:14');
INSERT INTO `audit_logs` VALUES (52, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:19:15');
INSERT INTO `audit_logs` VALUES (53, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-06 16:19:22');
INSERT INTO `audit_logs` VALUES (54, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-06 16:19:32');
INSERT INTO `audit_logs` VALUES (55, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-06 16:19:33');
INSERT INTO `audit_logs` VALUES (56, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 13:54:40');
INSERT INTO `audit_logs` VALUES (57, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 13:54:42');
INSERT INTO `audit_logs` VALUES (58, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 13:54:43');
INSERT INTO `audit_logs` VALUES (59, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 13:54:50');
INSERT INTO `audit_logs` VALUES (60, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 13:54:51');
INSERT INTO `audit_logs` VALUES (61, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 13:54:53');
INSERT INTO `audit_logs` VALUES (62, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 13:56:03');
INSERT INTO `audit_logs` VALUES (63, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 13:56:05');
INSERT INTO `audit_logs` VALUES (64, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 13:56:07');
INSERT INTO `audit_logs` VALUES (65, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 13:56:09');
INSERT INTO `audit_logs` VALUES (66, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 13:58:34');
INSERT INTO `audit_logs` VALUES (67, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 13:58:36');
INSERT INTO `audit_logs` VALUES (68, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 13:58:42');
INSERT INTO `audit_logs` VALUES (69, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 13:58:46');
INSERT INTO `audit_logs` VALUES (70, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 13:58:48');
INSERT INTO `audit_logs` VALUES (71, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 13:58:51');
INSERT INTO `audit_logs` VALUES (72, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:10:13');
INSERT INTO `audit_logs` VALUES (73, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 14:10:23');
INSERT INTO `audit_logs` VALUES (74, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:10:25');
INSERT INTO `audit_logs` VALUES (75, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:10:26');
INSERT INTO `audit_logs` VALUES (76, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:10:27');
INSERT INTO `audit_logs` VALUES (77, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:10:28');
INSERT INTO `audit_logs` VALUES (78, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 14:10:30');
INSERT INTO `audit_logs` VALUES (79, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:10:32');
INSERT INTO `audit_logs` VALUES (80, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 14:20:01');
INSERT INTO `audit_logs` VALUES (81, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:20:03');
INSERT INTO `audit_logs` VALUES (82, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:20:04');
INSERT INTO `audit_logs` VALUES (83, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:20:05');
INSERT INTO `audit_logs` VALUES (84, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 14:20:10');
INSERT INTO `audit_logs` VALUES (85, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:20:12');
INSERT INTO `audit_logs` VALUES (86, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:21:23');
INSERT INTO `audit_logs` VALUES (87, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 14:21:30');
INSERT INTO `audit_logs` VALUES (88, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 14:21:36');
INSERT INTO `audit_logs` VALUES (89, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 14:21:41');
INSERT INTO `audit_logs` VALUES (90, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 14:21:43');
INSERT INTO `audit_logs` VALUES (91, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:21:45');
INSERT INTO `audit_logs` VALUES (92, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:21:49');
INSERT INTO `audit_logs` VALUES (93, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:21:56');
INSERT INTO `audit_logs` VALUES (94, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 3', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 14:21:58');
INSERT INTO `audit_logs` VALUES (95, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:21:59');
INSERT INTO `audit_logs` VALUES (96, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 14:22:01');
INSERT INTO `audit_logs` VALUES (97, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:22:03');
INSERT INTO `audit_logs` VALUES (98, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 14:22:06');
INSERT INTO `audit_logs` VALUES (99, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:22:08');
INSERT INTO `audit_logs` VALUES (100, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 2', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-09 14:22:09');
INSERT INTO `audit_logs` VALUES (101, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:22:11');
INSERT INTO `audit_logs` VALUES (102, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:22:12');
INSERT INTO `audit_logs` VALUES (103, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 14:22:14');
INSERT INTO `audit_logs` VALUES (104, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:22:19');
INSERT INTO `audit_logs` VALUES (105, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-09 14:22:21');
INSERT INTO `audit_logs` VALUES (106, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:22:28');
INSERT INTO `audit_logs` VALUES (107, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:24:26');
INSERT INTO `audit_logs` VALUES (108, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:24:27');
INSERT INTO `audit_logs` VALUES (109, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:25:25');
INSERT INTO `audit_logs` VALUES (110, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:25:26');
INSERT INTO `audit_logs` VALUES (111, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 14:30:44');
INSERT INTO `audit_logs` VALUES (112, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 14:30:46');
INSERT INTO `audit_logs` VALUES (113, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:30:50');
INSERT INTO `audit_logs` VALUES (114, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:30:51');
INSERT INTO `audit_logs` VALUES (115, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:30:55');
INSERT INTO `audit_logs` VALUES (116, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:30:59');
INSERT INTO `audit_logs` VALUES (117, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:31:04');
INSERT INTO `audit_logs` VALUES (118, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:31:05');
INSERT INTO `audit_logs` VALUES (119, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:31:07');
INSERT INTO `audit_logs` VALUES (120, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 2', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-09 14:31:08');
INSERT INTO `audit_logs` VALUES (121, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:31:10');
INSERT INTO `audit_logs` VALUES (122, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 14:31:11');
INSERT INTO `audit_logs` VALUES (123, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 2', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-09 14:31:12');
INSERT INTO `audit_logs` VALUES (124, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 14:41:25');
INSERT INTO `audit_logs` VALUES (125, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 14:41:26');
INSERT INTO `audit_logs` VALUES (126, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 14:41:27');
INSERT INTO `audit_logs` VALUES (127, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 14:41:32');
INSERT INTO `audit_logs` VALUES (128, 1, 'admin', '0:0:0:0:0:0:0:1', '添加角色', '新角色: 学生处处长', '成功添加新角色。', 'SUCCESS', '2025-06-09 14:41:37');
INSERT INTO `audit_logs` VALUES (129, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 14:41:37');
INSERT INTO `audit_logs` VALUES (130, 1, 'admin', '0:0:0:0:0:0:0:1', '删除角色', '角色ID: 6', '成功删除角色: 学生处处长', 'SUCCESS', '2025-06-09 14:41:41');
INSERT INTO `audit_logs` VALUES (131, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 14:41:41');
INSERT INTO `audit_logs` VALUES (132, 1, 'admin', '0:0:0:0:0:0:0:1', '添加角色', '新角色: 学生处管理员', '成功添加新角色。', 'SUCCESS', '2025-06-09 14:41:44');
INSERT INTO `audit_logs` VALUES (133, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 14:41:44');
INSERT INTO `audit_logs` VALUES (134, 1, 'admin', '0:0:0:0:0:0:0:1', '删除角色', '角色ID: 7', '成功删除角色: 学生处管理员', 'SUCCESS', '2025-06-09 14:41:47');
INSERT INTO `audit_logs` VALUES (135, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 14:41:47');
INSERT INTO `audit_logs` VALUES (136, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 14:41:54');
INSERT INTO `audit_logs` VALUES (137, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 14:41:55');
INSERT INTO `audit_logs` VALUES (138, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:07:18');
INSERT INTO `audit_logs` VALUES (139, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:12:47');
INSERT INTO `audit_logs` VALUES (140, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:12:49');
INSERT INTO `audit_logs` VALUES (141, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:12:50');
INSERT INTO `audit_logs` VALUES (142, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:12:53');
INSERT INTO `audit_logs` VALUES (143, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:12:53');
INSERT INTO `audit_logs` VALUES (144, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:12:54');
INSERT INTO `audit_logs` VALUES (145, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:14:09');
INSERT INTO `audit_logs` VALUES (146, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:14:19');
INSERT INTO `audit_logs` VALUES (147, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:18:40');
INSERT INTO `audit_logs` VALUES (148, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:18:42');
INSERT INTO `audit_logs` VALUES (149, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:18:54');
INSERT INTO `audit_logs` VALUES (150, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:18:55');
INSERT INTO `audit_logs` VALUES (151, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:18:55');
INSERT INTO `audit_logs` VALUES (152, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:19:00');
INSERT INTO `audit_logs` VALUES (153, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:19:36');
INSERT INTO `audit_logs` VALUES (154, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:20:16');
INSERT INTO `audit_logs` VALUES (155, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:22:26');
INSERT INTO `audit_logs` VALUES (156, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 员工工资表.xlsx', '导入处理完成。摘要: 总共处理 2 行数据。 成功导入并计算: 1 条 失败: 1 条  失败详情: 第 2 行处理失败: 数据库中不存在员工编号为 \'员工编号\' 的员工。 ', 'SUCCESS', '2025-06-09 15:22:37');
INSERT INTO `audit_logs` VALUES (157, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:22:37');
INSERT INTO `audit_logs` VALUES (158, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 员工工资表.xlsx', '导入处理完成。摘要: 总共处理 0 行数据。 成功导入并计算: 0 条 失败: 0 条  失败详情: ', 'SUCCESS', '2025-06-09 15:24:10');
INSERT INTO `audit_logs` VALUES (159, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:24:10');
INSERT INTO `audit_logs` VALUES (160, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:25:00');
INSERT INTO `audit_logs` VALUES (161, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:26:14');
INSERT INTO `audit_logs` VALUES (162, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:31:01');
INSERT INTO `audit_logs` VALUES (163, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:31:02');
INSERT INTO `audit_logs` VALUES (164, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:31:03');
INSERT INTO `audit_logs` VALUES (165, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:31:03');
INSERT INTO `audit_logs` VALUES (166, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:31:04');
INSERT INTO `audit_logs` VALUES (167, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 15:35:04');
INSERT INTO `audit_logs` VALUES (168, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:35:08');
INSERT INTO `audit_logs` VALUES (169, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:35:10');
INSERT INTO `audit_logs` VALUES (170, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:35:39');
INSERT INTO `audit_logs` VALUES (171, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:35:40');
INSERT INTO `audit_logs` VALUES (172, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:35:41');
INSERT INTO `audit_logs` VALUES (173, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:35:42');
INSERT INTO `audit_logs` VALUES (174, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:35:45');
INSERT INTO `audit_logs` VALUES (175, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:35:48');
INSERT INTO `audit_logs` VALUES (176, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:35:50');
INSERT INTO `audit_logs` VALUES (177, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:35:50');
INSERT INTO `audit_logs` VALUES (178, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:35:51');
INSERT INTO `audit_logs` VALUES (179, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:35:52');
INSERT INTO `audit_logs` VALUES (180, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:35:53');
INSERT INTO `audit_logs` VALUES (181, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:36:56');
INSERT INTO `audit_logs` VALUES (182, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:37:02');
INSERT INTO `audit_logs` VALUES (183, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:37:04');
INSERT INTO `audit_logs` VALUES (184, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:37:05');
INSERT INTO `audit_logs` VALUES (185, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:37:06');
INSERT INTO `audit_logs` VALUES (186, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:37:06');
INSERT INTO `audit_logs` VALUES (187, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:37:07');
INSERT INTO `audit_logs` VALUES (188, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 15:37:23');
INSERT INTO `audit_logs` VALUES (189, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-09 15:41:34');
INSERT INTO `audit_logs` VALUES (190, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl', '用户登录成功。', 'SUCCESS', '2025-06-09 15:41:45');
INSERT INTO `audit_logs` VALUES (191, 4, 'zjl', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 15:41:47');
INSERT INTO `audit_logs` VALUES (192, 4, 'zjl', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 15:41:54');
INSERT INTO `audit_logs` VALUES (193, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl', '用户主动登出。', 'SUCCESS', '2025-06-09 15:41:59');
INSERT INTO `audit_logs` VALUES (194, 2, 'rsgly', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: rsgly', '用户登录成功。', 'SUCCESS', '2025-06-09 15:42:12');
INSERT INTO `audit_logs` VALUES (195, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 15:42:13');
INSERT INTO `audit_logs` VALUES (196, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 15:42:14');
INSERT INTO `audit_logs` VALUES (197, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:42:15');
INSERT INTO `audit_logs` VALUES (198, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 15:42:16');
INSERT INTO `audit_logs` VALUES (199, 2, 'rsgly', '0:0:0:0:0:0:0:1', '用户登出', '用户: rsgly', '用户主动登出。', 'SUCCESS', '2025-06-09 15:42:18');
INSERT INTO `audit_logs` VALUES (200, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: sjy', '用户登录成功。', 'SUCCESS', '2025-06-09 15:42:27');
INSERT INTO `audit_logs` VALUES (201, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登出', '用户: sjy', '用户主动登出。', 'SUCCESS', '2025-06-09 15:42:36');
INSERT INTO `audit_logs` VALUES (202, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:42:40');
INSERT INTO `audit_logs` VALUES (203, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:42:42');
INSERT INTO `audit_logs` VALUES (204, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:42:44');
INSERT INTO `audit_logs` VALUES (205, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:42:45');
INSERT INTO `audit_logs` VALUES (206, 1, 'admin', '0:0:0:0:0:0:0:1', '添加用户', '新用户: zjl01', '成功添加系统用户。', 'SUCCESS', '2025-06-09 15:43:31');
INSERT INTO `audit_logs` VALUES (207, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:43:31');
INSERT INTO `audit_logs` VALUES (208, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-09 15:43:37');
INSERT INTO `audit_logs` VALUES (209, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 15:44:57');
INSERT INTO `audit_logs` VALUES (210, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 15:45:03');
INSERT INTO `audit_logs` VALUES (211, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 15:45:07');
INSERT INTO `audit_logs` VALUES (212, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 15:45:14');
INSERT INTO `audit_logs` VALUES (213, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 15:47:57');
INSERT INTO `audit_logs` VALUES (214, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-09 15:48:07');
INSERT INTO `audit_logs` VALUES (215, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 15:48:11');
INSERT INTO `audit_logs` VALUES (216, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:48:24');
INSERT INTO `audit_logs` VALUES (217, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:48:33');
INSERT INTO `audit_logs` VALUES (218, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:48:38');
INSERT INTO `audit_logs` VALUES (219, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:48:39');
INSERT INTO `audit_logs` VALUES (220, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 15:49:27');
INSERT INTO `audit_logs` VALUES (221, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 15:49:29');
INSERT INTO `audit_logs` VALUES (222, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:03:49');
INSERT INTO `audit_logs` VALUES (223, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 16:04:01');
INSERT INTO `audit_logs` VALUES (224, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:04:08');
INSERT INTO `audit_logs` VALUES (225, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 16:04:09');
INSERT INTO `audit_logs` VALUES (226, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:04:11');
INSERT INTO `audit_logs` VALUES (227, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 16:04:15');
INSERT INTO `audit_logs` VALUES (228, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 16:04:19');
INSERT INTO `audit_logs` VALUES (229, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 16:04:20');
INSERT INTO `audit_logs` VALUES (230, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:04:20');
INSERT INTO `audit_logs` VALUES (231, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:04:22');
INSERT INTO `audit_logs` VALUES (232, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-09 16:04:25');
INSERT INTO `audit_logs` VALUES (233, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-09 16:04:26');
INSERT INTO `audit_logs` VALUES (234, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-09 16:04:27');
INSERT INTO `audit_logs` VALUES (235, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 16:04:28');
INSERT INTO `audit_logs` VALUES (236, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 16:04:29');
INSERT INTO `audit_logs` VALUES (237, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:04:30');
INSERT INTO `audit_logs` VALUES (238, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-09 16:04:31');
INSERT INTO `audit_logs` VALUES (239, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:04:37');
INSERT INTO `audit_logs` VALUES (240, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-09 16:04:39');
INSERT INTO `audit_logs` VALUES (241, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:04:50');
INSERT INTO `audit_logs` VALUES (242, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:04:54');
INSERT INTO `audit_logs` VALUES (243, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:04:58');
INSERT INTO `audit_logs` VALUES (244, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:05:02');
INSERT INTO `audit_logs` VALUES (245, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 密码错误次数过多，账户已锁定 30 分钟。', 'FAILURE', '2025-06-09 16:05:19');
INSERT INTO `audit_logs` VALUES (246, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 账户已被锁定，请于 30 分钟后重试。', 'FAILURE', '2025-06-09 16:05:24');
INSERT INTO `audit_logs` VALUES (247, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 账户已被锁定，请于 30 分钟后重试。', 'FAILURE', '2025-06-09 16:05:35');
INSERT INTO `audit_logs` VALUES (248, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:05:44');
INSERT INTO `audit_logs` VALUES (249, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:05:48');
INSERT INTO `audit_logs` VALUES (250, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 16:06:16');
INSERT INTO `audit_logs` VALUES (251, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:06:17');
INSERT INTO `audit_logs` VALUES (252, 1, 'admin', '0:0:0:0:0:0:0:1', '解锁用户', '用户: zjl01', '成功解锁用户账户。', 'SUCCESS', '2025-06-09 16:06:22');
INSERT INTO `audit_logs` VALUES (253, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:06:22');
INSERT INTO `audit_logs` VALUES (254, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-09 16:06:24');
INSERT INTO `audit_logs` VALUES (255, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:06:38');
INSERT INTO `audit_logs` VALUES (256, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:06:39');
INSERT INTO `audit_logs` VALUES (257, 6, 'zjl01', '0:0:0:0:0:0:0:1', '修改个人密码', '用户: zjl01', '用户成功修改了自己的密码。', 'SUCCESS', '2025-06-09 16:07:05');
INSERT INTO `audit_logs` VALUES (258, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-09 16:07:07');
INSERT INTO `audit_logs` VALUES (259, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:07:18');
INSERT INTO `audit_logs` VALUES (260, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:07:24');
INSERT INTO `audit_logs` VALUES (261, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:07:25');
INSERT INTO `audit_logs` VALUES (262, NULL, 'zjl', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:07:28');
INSERT INTO `audit_logs` VALUES (263, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:07:39');
INSERT INTO `audit_logs` VALUES (264, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:11:00');
INSERT INTO `audit_logs` VALUES (265, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:11:01');
INSERT INTO `audit_logs` VALUES (266, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:11:07');
INSERT INTO `audit_logs` VALUES (267, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-09 16:11:10');
INSERT INTO `audit_logs` VALUES (268, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl', '用户登录成功。', 'SUCCESS', '2025-06-09 16:11:20');
INSERT INTO `audit_logs` VALUES (269, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl', '用户主动登出。', 'SUCCESS', '2025-06-09 16:11:22');
INSERT INTO `audit_logs` VALUES (270, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:12:59');
INSERT INTO `audit_logs` VALUES (271, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:13:01');
INSERT INTO `audit_logs` VALUES (272, 6, 'zjl01', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:13:05');
INSERT INTO `audit_logs` VALUES (273, NULL, '[未登录用户]', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:14:38');
INSERT INTO `audit_logs` VALUES (274, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:14:49');
INSERT INTO `audit_logs` VALUES (275, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-09 16:15:38');
INSERT INTO `audit_logs` VALUES (276, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:15:44');
INSERT INTO `audit_logs` VALUES (277, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:15:50');
INSERT INTO `audit_logs` VALUES (278, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:15:54');
INSERT INTO `audit_logs` VALUES (279, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:15:57');
INSERT INTO `audit_logs` VALUES (280, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 密码错误次数过多，账户已锁定 30 分钟。', 'FAILURE', '2025-06-09 16:16:02');
INSERT INTO `audit_logs` VALUES (281, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-09 16:16:59');
INSERT INTO `audit_logs` VALUES (282, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:17:00');
INSERT INTO `audit_logs` VALUES (283, 1, 'admin', '0:0:0:0:0:0:0:1', '解锁用户', '用户: zjl01', '成功解锁用户账户。', 'SUCCESS', '2025-06-09 16:17:03');
INSERT INTO `audit_logs` VALUES (284, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:17:03');
INSERT INTO `audit_logs` VALUES (285, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-09 16:17:10');
INSERT INTO `audit_logs` VALUES (286, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:29:42');
INSERT INTO `audit_logs` VALUES (287, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:29:44');
INSERT INTO `audit_logs` VALUES (288, 6, 'zjl01', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:29:49');
INSERT INTO `audit_logs` VALUES (289, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:29:57');
INSERT INTO `audit_logs` VALUES (290, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:29:59');
INSERT INTO `audit_logs` VALUES (291, 6, 'zjl01', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:30:01');
INSERT INTO `audit_logs` VALUES (292, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:30:02');
INSERT INTO `audit_logs` VALUES (293, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:30:58');
INSERT INTO `audit_logs` VALUES (294, 6, 'zjl01', '0:0:0:0:0:0:0:1', '修改个人密码', '用户: zjl01', '用户成功修改了自己的密码。', 'SUCCESS', '2025-06-09 16:31:20');
INSERT INTO `audit_logs` VALUES (295, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:33:23');
INSERT INTO `audit_logs` VALUES (296, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: Admin', '用户登录成功。', 'SUCCESS', '2025-06-09 16:37:49');
INSERT INTO `audit_logs` VALUES (297, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:38:00');
INSERT INTO `audit_logs` VALUES (298, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:38:04');
INSERT INTO `audit_logs` VALUES (299, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-09 16:38:06');
INSERT INTO `audit_logs` VALUES (300, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-09 16:38:07');
INSERT INTO `audit_logs` VALUES (301, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:38:16');
INSERT INTO `audit_logs` VALUES (302, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:38:24');
INSERT INTO `audit_logs` VALUES (303, 6, 'zjl01', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-09 16:38:26');
INSERT INTO `audit_logs` VALUES (304, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:39:27');
INSERT INTO `audit_logs` VALUES (305, 6, 'zjl01', '0:0:0:0:0:0:0:1', '修改个人密码', '用户: zjl01', '用户成功修改了自己的密码。', 'SUCCESS', '2025-06-09 16:40:00');
INSERT INTO `audit_logs` VALUES (306, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-09 16:40:22');
INSERT INTO `audit_logs` VALUES (307, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-09 16:40:32');
INSERT INTO `audit_logs` VALUES (308, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-09 16:40:33');
INSERT INTO `audit_logs` VALUES (309, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:15:36');
INSERT INTO `audit_logs` VALUES (310, NULL, 'admin、', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin、', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:15:40');
INSERT INTO `audit_logs` VALUES (311, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-26 17:15:52');
INSERT INTO `audit_logs` VALUES (312, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 17:16:06');
INSERT INTO `audit_logs` VALUES (313, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-26 17:16:32');
INSERT INTO `audit_logs` VALUES (314, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-26 17:16:42');
INSERT INTO `audit_logs` VALUES (315, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-26 17:16:52');
INSERT INTO `audit_logs` VALUES (316, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:17:03');
INSERT INTO `audit_logs` VALUES (317, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:17:12');
INSERT INTO `audit_logs` VALUES (318, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 17:17:18');
INSERT INTO `audit_logs` VALUES (319, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:17:19');
INSERT INTO `audit_logs` VALUES (320, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:17:23');
INSERT INTO `audit_logs` VALUES (321, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-26 17:17:28');
INSERT INTO `audit_logs` VALUES (322, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:17:32');
INSERT INTO `audit_logs` VALUES (323, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 17:17:36');
INSERT INTO `audit_logs` VALUES (324, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:17:37');
INSERT INTO `audit_logs` VALUES (325, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:17:48');
INSERT INTO `audit_logs` VALUES (326, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 17:17:49');
INSERT INTO `audit_logs` VALUES (327, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:17:51');
INSERT INTO `audit_logs` VALUES (328, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:17:53');
INSERT INTO `audit_logs` VALUES (329, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-26 17:18:06');
INSERT INTO `audit_logs` VALUES (330, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-26 17:18:54');
INSERT INTO `audit_logs` VALUES (331, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl', '用户登录成功。', 'SUCCESS', '2025-06-26 17:19:07');
INSERT INTO `audit_logs` VALUES (332, 4, 'zjl', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:19:10');
INSERT INTO `audit_logs` VALUES (333, 4, 'zjl', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-26 17:19:11');
INSERT INTO `audit_logs` VALUES (334, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl', '用户主动登出。', 'SUCCESS', '2025-06-26 17:19:20');
INSERT INTO `audit_logs` VALUES (335, NULL, 'sjy', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: sjy', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:19:29');
INSERT INTO `audit_logs` VALUES (336, NULL, 'sjy', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: sjy', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:19:34');
INSERT INTO `audit_logs` VALUES (337, NULL, 'sjy', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: sjy', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:19:55');
INSERT INTO `audit_logs` VALUES (338, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: sjy', '用户登录成功。', 'SUCCESS', '2025-06-26 17:20:06');
INSERT INTO `audit_logs` VALUES (339, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登出', '用户: sjy', '用户主动登出。', 'SUCCESS', '2025-06-26 17:20:25');
INSERT INTO `audit_logs` VALUES (340, 3, 'cwgly', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: cwgly', '用户登录成功。', 'SUCCESS', '2025-06-26 17:20:38');
INSERT INTO `audit_logs` VALUES (341, 3, 'cwgly', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:20:39');
INSERT INTO `audit_logs` VALUES (342, 3, 'cwgly', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-26 17:20:46');
INSERT INTO `audit_logs` VALUES (343, 3, 'cwgly', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:20:48');
INSERT INTO `audit_logs` VALUES (344, 3, 'cwgly', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 17:21:01');
INSERT INTO `audit_logs` VALUES (345, 3, 'cwgly', '0:0:0:0:0:0:0:1', '用户登出', '用户: cwgly', '用户主动登出。', 'SUCCESS', '2025-06-26 17:21:03');
INSERT INTO `audit_logs` VALUES (346, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-26 17:21:17');
INSERT INTO `audit_logs` VALUES (347, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 17:21:18');
INSERT INTO `audit_logs` VALUES (348, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 17:21:24');
INSERT INTO `audit_logs` VALUES (349, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-26 17:21:26');
INSERT INTO `audit_logs` VALUES (350, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-26 17:21:27');
INSERT INTO `audit_logs` VALUES (351, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:21:28');
INSERT INTO `audit_logs` VALUES (352, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 17:21:28');
INSERT INTO `audit_logs` VALUES (353, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-26 17:21:29');
INSERT INTO `audit_logs` VALUES (354, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 17:21:31');
INSERT INTO `audit_logs` VALUES (355, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-26 17:22:08');
INSERT INTO `audit_logs` VALUES (356, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:22:28');
INSERT INTO `audit_logs` VALUES (357, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-26 17:22:44');
INSERT INTO `audit_logs` VALUES (358, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-26 17:22:59');
INSERT INTO `audit_logs` VALUES (359, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-26 17:23:10');
INSERT INTO `audit_logs` VALUES (360, 6, 'zjl01', '0:0:0:0:0:0:0:1', '修改个人密码', '用户: zjl01', '用户成功修改了自己的密码。', 'SUCCESS', '2025-06-26 17:23:28');
INSERT INTO `audit_logs` VALUES (361, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:23:35');
INSERT INTO `audit_logs` VALUES (362, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:23:39');
INSERT INTO `audit_logs` VALUES (363, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:23:45');
INSERT INTO `audit_logs` VALUES (364, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:23:48');
INSERT INTO `audit_logs` VALUES (365, NULL, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: zjl01', '登录失败原因: 密码错误次数过多，账户已锁定 30 分钟。', 'FAILURE', '2025-06-26 17:23:52');
INSERT INTO `audit_logs` VALUES (366, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 17:24:54');
INSERT INTO `audit_logs` VALUES (367, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-26 17:25:09');
INSERT INTO `audit_logs` VALUES (368, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 17:25:11');
INSERT INTO `audit_logs` VALUES (369, 1, 'admin', '0:0:0:0:0:0:0:1', '解锁用户', '用户: zjl01', '成功解锁用户账户。', 'SUCCESS', '2025-06-26 17:25:15');
INSERT INTO `audit_logs` VALUES (370, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 17:25:15');
INSERT INTO `audit_logs` VALUES (371, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-26 21:17:34');
INSERT INTO `audit_logs` VALUES (372, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 21:21:52');
INSERT INTO `audit_logs` VALUES (373, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-26 21:23:38');
INSERT INTO `audit_logs` VALUES (374, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 21:24:14');
INSERT INTO `audit_logs` VALUES (375, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-26 21:25:09');
INSERT INTO `audit_logs` VALUES (376, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-26 21:25:59');
INSERT INTO `audit_logs` VALUES (377, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-26 21:26:35');
INSERT INTO `audit_logs` VALUES (378, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-26 21:27:35');
INSERT INTO `audit_logs` VALUES (379, NULL, 'rsgly', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: rsgly', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-26 21:27:52');
INSERT INTO `audit_logs` VALUES (380, 2, 'rsgly', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: rsgly', '用户登录成功。', 'SUCCESS', '2025-06-26 21:28:03');
INSERT INTO `audit_logs` VALUES (381, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-26 21:28:05');
INSERT INTO `audit_logs` VALUES (382, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 21:28:35');
INSERT INTO `audit_logs` VALUES (383, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 21:29:50');
INSERT INTO `audit_logs` VALUES (384, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 21:29:58');
INSERT INTO `audit_logs` VALUES (385, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-26 21:29:58');
INSERT INTO `audit_logs` VALUES (386, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 21:29:59');
INSERT INTO `audit_logs` VALUES (387, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 21:30:24');
INSERT INTO `audit_logs` VALUES (388, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 21:30:27');
INSERT INTO `audit_logs` VALUES (389, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-26 21:31:31');
INSERT INTO `audit_logs` VALUES (390, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-26 21:33:01');
INSERT INTO `audit_logs` VALUES (391, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-26 21:33:06');
INSERT INTO `audit_logs` VALUES (392, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 21:33:09');
INSERT INTO `audit_logs` VALUES (393, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 21:33:12');
INSERT INTO `audit_logs` VALUES (394, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 2', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-26 21:33:14');
INSERT INTO `audit_logs` VALUES (395, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-26 21:34:26');
INSERT INTO `audit_logs` VALUES (396, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-26 21:34:32');
INSERT INTO `audit_logs` VALUES (397, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-26 21:36:27');
INSERT INTO `audit_logs` VALUES (398, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 3', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-26 21:36:29');
INSERT INTO `audit_logs` VALUES (399, 2, 'rsgly', '0:0:0:0:0:0:0:1', '用户登出', '用户: rsgly', '用户主动登出。', 'SUCCESS', '2025-06-26 21:37:09');
INSERT INTO `audit_logs` VALUES (400, 3, 'cwgly', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: cwgly', '用户登录成功。', 'SUCCESS', '2025-06-26 21:37:20');
INSERT INTO `audit_logs` VALUES (401, 3, 'cwgly', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-26 21:37:25');
INSERT INTO `audit_logs` VALUES (402, 3, 'cwgly', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-26 21:39:08');
INSERT INTO `audit_logs` VALUES (403, 3, 'cwgly', '0:0:0:0:0:0:0:1', '用户登出', '用户: cwgly', '用户主动登出。', 'SUCCESS', '2025-06-26 21:43:39');
INSERT INTO `audit_logs` VALUES (404, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: sjy', '用户登录成功。', 'SUCCESS', '2025-06-26 21:43:51');
INSERT INTO `audit_logs` VALUES (405, NULL, 'system', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: system', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-27 13:47:07');
INSERT INTO `audit_logs` VALUES (406, NULL, 'system', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: system', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-27 13:49:48');
INSERT INTO `audit_logs` VALUES (407, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-27 13:50:01');
INSERT INTO `audit_logs` VALUES (408, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-27 13:50:13');
INSERT INTO `audit_logs` VALUES (409, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-27 13:50:32');
INSERT INTO `audit_logs` VALUES (410, 6, 'zjl01', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:50:35');
INSERT INTO `audit_logs` VALUES (411, 6, 'zjl01', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:50:36');
INSERT INTO `audit_logs` VALUES (412, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-27 13:50:39');
INSERT INTO `audit_logs` VALUES (413, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-27 13:51:57');
INSERT INTO `audit_logs` VALUES (414, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-27 13:52:14');
INSERT INTO `audit_logs` VALUES (415, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl01', '用户登录成功。', 'SUCCESS', '2025-06-27 13:52:24');
INSERT INTO `audit_logs` VALUES (416, 6, 'zjl01', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl01', '用户主动登出。', 'SUCCESS', '2025-06-27 13:52:29');
INSERT INTO `audit_logs` VALUES (417, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-27 13:53:07');
INSERT INTO `audit_logs` VALUES (418, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-27 13:53:09');
INSERT INTO `audit_logs` VALUES (419, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-27 13:53:13');
INSERT INTO `audit_logs` VALUES (420, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 13:53:15');
INSERT INTO `audit_logs` VALUES (421, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:53:16');
INSERT INTO `audit_logs` VALUES (422, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:53:47');
INSERT INTO `audit_logs` VALUES (423, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 13:54:57');
INSERT INTO `audit_logs` VALUES (424, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:55:45');
INSERT INTO `audit_logs` VALUES (425, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:55:48');
INSERT INTO `audit_logs` VALUES (426, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-27 13:55:51');
INSERT INTO `audit_logs` VALUES (427, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 13:55:55');
INSERT INTO `audit_logs` VALUES (428, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-27 13:55:56');
INSERT INTO `audit_logs` VALUES (429, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 13:55:57');
INSERT INTO `audit_logs` VALUES (430, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:55:57');
INSERT INTO `audit_logs` VALUES (431, 1, 'admin', '0:0:0:0:0:0:0:1', '添加员工', '员工编号: 003', '成功添加新员工: tom', 'SUCCESS', '2025-06-27 13:56:37');
INSERT INTO `audit_logs` VALUES (432, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:56:37');
INSERT INTO `audit_logs` VALUES (433, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 13:56:40');
INSERT INTO `audit_logs` VALUES (434, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:56:41');
INSERT INTO `audit_logs` VALUES (435, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:57:59');
INSERT INTO `audit_logs` VALUES (436, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 工资导入模板 (2).xlsx', '导入处理完成。摘要: 总共处理 0 行数据。 成功导入并计算: 0 条 失败: 0 条  失败详情: ', 'SUCCESS', '2025-06-27 13:58:09');
INSERT INTO `audit_logs` VALUES (437, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:58:09');
INSERT INTO `audit_logs` VALUES (438, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:58:21');
INSERT INTO `audit_logs` VALUES (439, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:58:25');
INSERT INTO `audit_logs` VALUES (440, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:58:31');
INSERT INTO `audit_logs` VALUES (441, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:58:39');
INSERT INTO `audit_logs` VALUES (442, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:58:44');
INSERT INTO `audit_logs` VALUES (443, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:58:57');
INSERT INTO `audit_logs` VALUES (444, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:58:58');
INSERT INTO `audit_logs` VALUES (445, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 5', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 13:59:01');
INSERT INTO `audit_logs` VALUES (446, 1, 'admin', '0:0:0:0:0:0:0:1', '保存年度专项扣除', '员工ID: 5, 年份: 2025', '添加员工ID 5 的 2025 年度专项附加扣除记录。', 'SUCCESS', '2025-06-27 13:59:25');
INSERT INTO `audit_logs` VALUES (447, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 5', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 13:59:25');
INSERT INTO `audit_logs` VALUES (448, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 13:59:26');
INSERT INTO `audit_logs` VALUES (449, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 13:59:45');
INSERT INTO `audit_logs` VALUES (450, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:59:46');
INSERT INTO `audit_logs` VALUES (451, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 工资导入模板 (2).xlsx', '导入处理完成。摘要: 总共处理 0 行数据。 成功导入并计算: 0 条 失败: 0 条  失败详情: ', 'SUCCESS', '2025-06-27 13:59:51');
INSERT INTO `audit_logs` VALUES (452, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 13:59:51');
INSERT INTO `audit_logs` VALUES (453, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 14:00:25');
INSERT INTO `audit_logs` VALUES (454, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 14:00:30');
INSERT INTO `audit_logs` VALUES (455, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 14:00:34');
INSERT INTO `audit_logs` VALUES (456, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 14:00:35');
INSERT INTO `audit_logs` VALUES (457, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 14:00:37');
INSERT INTO `audit_logs` VALUES (458, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 14:00:38');
INSERT INTO `audit_logs` VALUES (459, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 5', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-27 14:00:41');
INSERT INTO `audit_logs` VALUES (460, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 14:00:42');
INSERT INTO `audit_logs` VALUES (461, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 5', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 14:00:43');
INSERT INTO `audit_logs` VALUES (462, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 14:00:50');
INSERT INTO `audit_logs` VALUES (463, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 14:01:22');
INSERT INTO `audit_logs` VALUES (464, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 14:01:34');
INSERT INTO `audit_logs` VALUES (465, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 工资导入模板 (2).xlsx', '导入处理完成。摘要: 总共处理 0 行数据。 成功导入并计算: 0 条 失败: 0 条  失败详情: ', 'SUCCESS', '2025-06-27 14:01:41');
INSERT INTO `audit_logs` VALUES (466, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 14:01:41');
INSERT INTO `audit_logs` VALUES (467, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 14:02:00');
INSERT INTO `audit_logs` VALUES (468, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-27 18:04:54');
INSERT INTO `audit_logs` VALUES (469, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 18:04:56');
INSERT INTO `audit_logs` VALUES (470, 1, 'admin', '0:0:0:0:0:0:0:1', '添加部门', '部门名称: 111', '成功添加新部门。', 'SUCCESS', '2025-06-27 18:05:02');
INSERT INTO `audit_logs` VALUES (471, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 18:05:02');
INSERT INTO `audit_logs` VALUES (472, 1, 'admin', '0:0:0:0:0:0:0:1', '更新部门', '部门ID: 19', '成功更新部门: 111', 'SUCCESS', '2025-06-27 18:05:09');
INSERT INTO `audit_logs` VALUES (473, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 18:05:09');
INSERT INTO `audit_logs` VALUES (474, 1, 'admin', '0:0:0:0:0:0:0:1', '删除部门', '部门ID: 19', '成功删除部门: 111', 'SUCCESS', '2025-06-27 18:05:17');
INSERT INTO `audit_logs` VALUES (475, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 18:05:17');
INSERT INTO `audit_logs` VALUES (476, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-27 22:15:28');
INSERT INTO `audit_logs` VALUES (477, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-27 22:15:30');
INSERT INTO `audit_logs` VALUES (478, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-27 22:15:31');
INSERT INTO `audit_logs` VALUES (479, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 22:15:32');
INSERT INTO `audit_logs` VALUES (480, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:15:35');
INSERT INTO `audit_logs` VALUES (481, 1, 'admin', '0:0:0:0:0:0:0:1', '添加员工', '员工编号: 004', '成功添加新员工: 孙七', 'SUCCESS', '2025-06-27 22:16:37');
INSERT INTO `audit_logs` VALUES (482, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:16:37');
INSERT INTO `audit_logs` VALUES (483, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:16:51');
INSERT INTO `audit_logs` VALUES (484, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 6', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 22:16:54');
INSERT INTO `audit_logs` VALUES (485, 1, 'admin', '0:0:0:0:0:0:0:1', '保存年度专项扣除', '员工ID: 6, 年份: 2025', '添加员工ID 6 的 2025 年度专项附加扣除记录。', 'SUCCESS', '2025-06-27 22:17:27');
INSERT INTO `audit_logs` VALUES (486, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 6', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 22:17:27');
INSERT INTO `audit_logs` VALUES (487, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:17:36');
INSERT INTO `audit_logs` VALUES (488, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 22:17:39');
INSERT INTO `audit_logs` VALUES (489, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 6', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 22:17:42');
INSERT INTO `audit_logs` VALUES (490, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:17:43');
INSERT INTO `audit_logs` VALUES (491, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 22:17:44');
INSERT INTO `audit_logs` VALUES (492, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 6', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-27 22:17:45');
INSERT INTO `audit_logs` VALUES (493, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 6', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 22:17:48');
INSERT INTO `audit_logs` VALUES (494, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:17:49');
INSERT INTO `audit_logs` VALUES (495, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 6', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-27 22:17:51');
INSERT INTO `audit_logs` VALUES (496, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:17:53');
INSERT INTO `audit_logs` VALUES (497, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 22:17:58');
INSERT INTO `audit_logs` VALUES (498, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 6', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-27 22:18:00');
INSERT INTO `audit_logs` VALUES (499, 1, 'admin', '0:0:0:0:0:0:0:1', '保存被抚养人', '员工ID: 6', '添加员工ID 6 的被抚养人: 孙六', 'SUCCESS', '2025-06-27 22:18:55');
INSERT INTO `audit_logs` VALUES (500, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 6', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-27 22:18:55');
INSERT INTO `audit_logs` VALUES (501, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 22:18:57');
INSERT INTO `audit_logs` VALUES (502, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 22:19:02');
INSERT INTO `audit_logs` VALUES (503, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-27 22:19:22');
INSERT INTO `audit_logs` VALUES (504, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-27 22:19:25');
INSERT INTO `audit_logs` VALUES (505, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 22:19:26');
INSERT INTO `audit_logs` VALUES (506, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:19:27');
INSERT INTO `audit_logs` VALUES (507, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 22:20:43');
INSERT INTO `audit_logs` VALUES (508, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 工资导入模板 (3).xlsx', '导入处理完成。摘要: 总共处理 1 行数据。 成功导入并计算: 1 条 失败: 0 条  失败详情: ', 'SUCCESS', '2025-06-27 22:20:50');
INSERT INTO `audit_logs` VALUES (509, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 22:20:50');
INSERT INTO `audit_logs` VALUES (510, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-27 22:20:59');
INSERT INTO `audit_logs` VALUES (511, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 22:21:00');
INSERT INTO `audit_logs` VALUES (512, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 22:23:36');
INSERT INTO `audit_logs` VALUES (513, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 22:23:48');
INSERT INTO `audit_logs` VALUES (514, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-27 22:24:06');
INSERT INTO `audit_logs` VALUES (515, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-27 22:24:07');
INSERT INTO `audit_logs` VALUES (516, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-27 22:24:07');
INSERT INTO `audit_logs` VALUES (517, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 22:24:08');
INSERT INTO `audit_logs` VALUES (518, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-27 22:24:10');
INSERT INTO `audit_logs` VALUES (519, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-27 22:24:10');
INSERT INTO `audit_logs` VALUES (520, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-27 22:46:57');
INSERT INTO `audit_logs` VALUES (521, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-27 22:47:00');
INSERT INTO `audit_logs` VALUES (522, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 3', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-27 22:47:03');
INSERT INTO `audit_logs` VALUES (523, NULL, '001', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: 001', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-28 14:01:07');
INSERT INTO `audit_logs` VALUES (524, NULL, 'admin', '0:0:0:0:0:0:0:1', '用户登录失败', '用户: admin', '登录失败原因: 用户名或密码错误。', 'FAILURE', '2025-06-28 14:01:21');
INSERT INTO `audit_logs` VALUES (525, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-28 14:01:31');
INSERT INTO `audit_logs` VALUES (526, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-28 14:01:34');
INSERT INTO `audit_logs` VALUES (527, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-28 14:01:35');
INSERT INTO `audit_logs` VALUES (528, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-28 14:01:36');
INSERT INTO `audit_logs` VALUES (529, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:01:40');
INSERT INTO `audit_logs` VALUES (530, 1, 'admin', '0:0:0:0:0:0:0:1', '添加员工', '员工编号: 005', '成功添加新员工: 张三', 'SUCCESS', '2025-06-28 14:02:11');
INSERT INTO `audit_logs` VALUES (531, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:02:11');
INSERT INTO `audit_logs` VALUES (532, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-28 14:02:27');
INSERT INTO `audit_logs` VALUES (533, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:02:35');
INSERT INTO `audit_logs` VALUES (534, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:02:37');
INSERT INTO `audit_logs` VALUES (535, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 7', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-28 14:02:43');
INSERT INTO `audit_logs` VALUES (536, 1, 'admin', '0:0:0:0:0:0:0:1', '保存被抚养人', '员工ID: 7', '添加员工ID 7 的被抚养人: 孙六', 'SUCCESS', '2025-06-28 14:03:05');
INSERT INTO `audit_logs` VALUES (537, 1, 'admin', '0:0:0:0:0:0:0:1', '查看被抚养人', '员工ID: 7', '查看该员工的被抚养人列表。', 'SUCCESS', '2025-06-28 14:03:05');
INSERT INTO `audit_logs` VALUES (538, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:03:07');
INSERT INTO `audit_logs` VALUES (539, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 7', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-28 14:03:09');
INSERT INTO `audit_logs` VALUES (540, 1, 'admin', '0:0:0:0:0:0:0:1', '保存年度专项扣除', '员工ID: 7, 年份: 2025', '添加员工ID 7 的 2025 年度专项附加扣除记录。', 'SUCCESS', '2025-06-28 14:03:24');
INSERT INTO `audit_logs` VALUES (541, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 7', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-28 14:03:24');
INSERT INTO `audit_logs` VALUES (542, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:03:33');
INSERT INTO `audit_logs` VALUES (543, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:03:35');
INSERT INTO `audit_logs` VALUES (544, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-28 14:03:39');
INSERT INTO `audit_logs` VALUES (545, 1, 'admin', '0:0:0:0:0:0:0:1', '保存年度专项扣除', '员工ID: 2, 年份: 2000', '添加员工ID 2 的 2000 年度专项附加扣除记录。', 'SUCCESS', '2025-06-28 14:03:50');
INSERT INTO `audit_logs` VALUES (546, 1, 'admin', '0:0:0:0:0:0:0:1', '查看年度专项扣除', '员工ID: 2', '查看该员工的年度专项附加扣除列表。', 'SUCCESS', '2025-06-28 14:03:50');
INSERT INTO `audit_logs` VALUES (547, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-28 14:03:53');
INSERT INTO `audit_logs` VALUES (548, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-05', '查看了 2025-05 的月度工资列表。', 'SUCCESS', '2025-06-28 14:04:01');
INSERT INTO `audit_logs` VALUES (549, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-28 14:04:04');
INSERT INTO `audit_logs` VALUES (550, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-28 14:05:00');
INSERT INTO `audit_logs` VALUES (551, 1, 'admin', '0:0:0:0:0:0:0:1', '查看用户列表', '所有系统用户', '系统管理员查看了用户列表。', 'SUCCESS', '2025-06-28 14:06:16');
INSERT INTO `audit_logs` VALUES (552, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-28 14:06:20');
INSERT INTO `audit_logs` VALUES (553, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:06:21');
INSERT INTO `audit_logs` VALUES (554, 1, 'admin', '0:0:0:0:0:0:0:1', '添加员工', '员工编号: 005', '添加员工失败: 员工编号 \'005\' 已存在。', 'FAILURE', '2025-06-28 14:06:42');
INSERT INTO `audit_logs` VALUES (555, 1, 'admin', '0:0:0:0:0:0:0:1', '添加员工', '员工编号: 006', '成功添加新员工: 张四', 'SUCCESS', '2025-06-28 14:06:48');
INSERT INTO `audit_logs` VALUES (556, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:06:48');
INSERT INTO `audit_logs` VALUES (557, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-28 14:07:46');
INSERT INTO `audit_logs` VALUES (558, 1, 'admin', '0:0:0:0:0:0:0:1', '工资批量导入', '文件名: 工资导入模板.xlsx', '导入处理完成。摘要: 总共处理 2 行数据。 成功导入并计算: 2 条 失败: 0 条  失败详情: ', 'SUCCESS', '2025-06-28 14:07:55');
INSERT INTO `audit_logs` VALUES (559, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-28 14:07:55');
INSERT INTO `audit_logs` VALUES (560, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-28 14:07:58');
INSERT INTO `audit_logs` VALUES (561, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-28 14:08:09');
INSERT INTO `audit_logs` VALUES (562, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登出', '用户: admin', '用户主动登出。', 'SUCCESS', '2025-06-28 14:08:29');
INSERT INTO `audit_logs` VALUES (563, 2, 'rsgly', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: rsgly', '用户登录成功。', 'SUCCESS', '2025-06-28 14:08:43');
INSERT INTO `audit_logs` VALUES (564, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-28 14:08:44');
INSERT INTO `audit_logs` VALUES (565, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:08:45');
INSERT INTO `audit_logs` VALUES (566, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:08:45');
INSERT INTO `audit_logs` VALUES (567, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:08:46');
INSERT INTO `audit_logs` VALUES (568, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-28 14:08:46');
INSERT INTO `audit_logs` VALUES (569, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:08:47');
INSERT INTO `audit_logs` VALUES (570, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:08:48');
INSERT INTO `audit_logs` VALUES (571, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:08:48');
INSERT INTO `audit_logs` VALUES (572, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:08:51');
INSERT INTO `audit_logs` VALUES (573, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-28 14:08:52');
INSERT INTO `audit_logs` VALUES (574, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-28 14:08:56');
INSERT INTO `audit_logs` VALUES (575, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-28 14:08:58');
INSERT INTO `audit_logs` VALUES (576, 2, 'rsgly', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-28 14:08:59');
INSERT INTO `audit_logs` VALUES (577, 2, 'rsgly', '0:0:0:0:0:0:0:1', '用户登出', '用户: rsgly', '用户主动登出。', 'SUCCESS', '2025-06-28 14:09:04');
INSERT INTO `audit_logs` VALUES (578, 3, 'cwgly', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: cwgly', '用户登录成功。', 'SUCCESS', '2025-06-28 14:09:18');
INSERT INTO `audit_logs` VALUES (579, 3, 'cwgly', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-28 14:09:20');
INSERT INTO `audit_logs` VALUES (580, 3, 'cwgly', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-28 14:09:23');
INSERT INTO `audit_logs` VALUES (581, 3, 'cwgly', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-28 14:09:25');
INSERT INTO `audit_logs` VALUES (582, 3, 'cwgly', '0:0:0:0:0:0:0:1', '用户登出', '用户: cwgly', '用户主动登出。', 'SUCCESS', '2025-06-28 14:09:36');
INSERT INTO `audit_logs` VALUES (583, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: zjl', '用户登录成功。', 'SUCCESS', '2025-06-28 14:09:45');
INSERT INTO `audit_logs` VALUES (584, 4, 'zjl', '0:0:0:0:0:0:0:1', '用户登出', '用户: zjl', '用户主动登出。', 'SUCCESS', '2025-06-28 14:10:52');
INSERT INTO `audit_logs` VALUES (585, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: sjy', '用户登录成功。', 'SUCCESS', '2025-06-28 14:11:03');
INSERT INTO `audit_logs` VALUES (586, 5, 'sjy', '0:0:0:0:0:0:0:1', '用户登出', '用户: sjy', '用户主动登出。', 'SUCCESS', '2025-06-28 14:12:01');
INSERT INTO `audit_logs` VALUES (587, 1, 'admin', '0:0:0:0:0:0:0:1', '用户登录成功', '用户: admin', '用户登录成功。', 'SUCCESS', '2025-06-29 15:33:00');
INSERT INTO `audit_logs` VALUES (588, 1, 'admin', '0:0:0:0:0:0:0:1', '访问工资导入页面', 'N/A', '用户访问了工资批量导入页面。', 'SUCCESS', '2025-06-29 15:33:12');
INSERT INTO `audit_logs` VALUES (589, 1, 'admin', '0:0:0:0:0:0:0:1', '查看月度工资列表', '工资月份: 2025-06', '查看了 2025-06 的月度工资列表。', 'SUCCESS', '2025-06-29 15:33:15');
INSERT INTO `audit_logs` VALUES (590, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-29 15:33:16');
INSERT INTO `audit_logs` VALUES (591, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-29 15:33:17');
INSERT INTO `audit_logs` VALUES (592, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-29 15:33:19');
INSERT INTO `audit_logs` VALUES (593, 1, 'admin', '0:0:0:0:0:0:0:1', '查看角色列表', '所有角色', '系统管理员查看了角色列表。', 'SUCCESS', '2025-06-29 15:33:23');
INSERT INTO `audit_logs` VALUES (594, 1, 'admin', '0:0:0:0:0:0:0:1', '查看部门列表', '所有部门', '用户查看了部门列表。', 'SUCCESS', '2025-06-29 15:33:25');
INSERT INTO `audit_logs` VALUES (595, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-29 15:33:27');
INSERT INTO `audit_logs` VALUES (596, 1, 'admin', '0:0:0:0:0:0:0:1', '查看专项扣除员工选择列表', '所有员工', '用户进入专项附加扣除模块，查看员工选择列表。', 'SUCCESS', '2025-06-29 15:33:31');
INSERT INTO `audit_logs` VALUES (597, 1, 'admin', '0:0:0:0:0:0:0:1', '查看员工列表', '所有员工', '用户查看了员工列表。', 'SUCCESS', '2025-06-29 15:33:33');

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `parent_dept_id` int NULL DEFAULT NULL COMMENT '上级部门ID (用于支持层级结构)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dept_name`(`dept_name` ASC) USING BTREE,
  INDEX `parent_dept_id`(`parent_dept_id` ASC) USING BTREE,
  CONSTRAINT `departments_ibfk_1` FOREIGN KEY (`parent_dept_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '销售部门', NULL, '2025-06-03 17:40:58', '2025-06-06 15:29:13');
INSERT INTO `departments` VALUES (2, '客服部门', NULL, '2025-06-03 17:41:13', '2025-06-05 19:08:46');
INSERT INTO `departments` VALUES (3, '单证部门', NULL, '2025-06-03 17:41:25', '2025-06-05 19:08:48');
INSERT INTO `departments` VALUES (4, '财务部门', NULL, '2025-06-03 17:41:38', '2025-06-05 19:08:49');
INSERT INTO `departments` VALUES (5, '配载部门', NULL, '2025-06-03 17:41:49', '2025-06-05 19:08:50');
INSERT INTO `departments` VALUES (6, '箱管部门', NULL, '2025-06-03 17:42:03', '2025-06-05 19:08:52');
INSERT INTO `departments` VALUES (7, '码头与堆场部门', NULL, '2025-06-03 17:42:34', '2025-06-05 19:08:53');
INSERT INTO `departments` VALUES (8, '计划班期部门', NULL, '2025-06-03 17:42:49', '2025-06-05 19:08:54');
INSERT INTO `departments` VALUES (9, '供应部门', NULL, '2025-06-03 17:42:59', '2025-06-05 19:08:55');
INSERT INTO `departments` VALUES (10, '运价控制部门', NULL, '2025-06-03 17:43:13', '2025-06-05 19:09:00');
INSERT INTO `departments` VALUES (11, '舱位控制部门', NULL, '2025-06-03 17:43:31', '2025-06-05 19:09:01');
INSERT INTO `departments` VALUES (12, '船舶安全管理部', NULL, '2025-06-03 17:43:56', '2025-06-05 19:10:45');

-- ----------------------------
-- Table structure for employee_annual_deductions
-- ----------------------------
DROP TABLE IF EXISTS `employee_annual_deductions`;
CREATE TABLE `employee_annual_deductions`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL COMMENT '关联的员工ID',
  `year` int NOT NULL COMMENT '扣除所属年度 (例如: 2023, 2024)',
  `children_education_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '子女教育年度扣除总额',
  `continuing_education_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '继续教育年度扣除总额',
  `serious_illness_medical_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '大病医疗年度扣除总额',
  `housing_loan_interest_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '住房贷款利息年度扣除总额',
  `housing_rent_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '住房租金年度扣除总额',
  `elderly_care_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '赡养老人年度扣除总额',
  `infant_care_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '3岁以下婴幼儿照护年度扣除总额',
  `total_annual_deduction` decimal(12, 2) GENERATED ALWAYS AS (((((((`children_education_amount` + `continuing_education_amount`) + `serious_illness_medical_amount`) + `housing_loan_interest_amount`) + `housing_rent_amount`) + `elderly_care_amount`) + `infant_care_amount`)) STORED COMMENT '年度专项附加扣除总计 (计算列)' NULL,
  `monthly_deduction_calculated` decimal(10, 2) GENERATED ALWAYS AS ((((((((`children_education_amount` + `continuing_education_amount`) + `serious_illness_medical_amount`) + `housing_loan_interest_amount`) + `housing_rent_amount`) + `elderly_care_amount`) + `infant_care_amount`) / 12)) STORED COMMENT '月度平均扣除额 (计算列, 用于个税计算)' NULL,
  `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_employee_year`(`employee_id` ASC, `year` ASC) USING BTREE,
  CONSTRAINT `employee_annual_deductions_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工年度专项附加扣除总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee_annual_deductions
-- ----------------------------
INSERT INTO `employee_annual_deductions` VALUES (1, 2, 2025, 1000.00, 100.00, 100.00, 100.00, 1000.00, 1000.00, 100.00, DEFAULT, DEFAULT, '', '2025-06-05 14:16:35', '2025-06-05 14:16:35');
INSERT INTO `employee_annual_deductions` VALUES (2, 5, 2025, 100.00, 200.00, 0.00, 400.00, 400.00, 100.00, 300.00, DEFAULT, DEFAULT, '', '2025-06-27 13:59:25', '2025-06-27 13:59:25');
INSERT INTO `employee_annual_deductions` VALUES (3, 6, 2025, 2000.00, 100.00, 0.00, 4000.00, 3000.00, 2000.00, 0.00, DEFAULT, DEFAULT, '', '2025-06-27 22:17:27', '2025-06-27 22:17:27');
INSERT INTO `employee_annual_deductions` VALUES (4, 7, 2025, 1222.00, 11.00, 0.00, 0.00, 0.00, 0.00, 11.00, DEFAULT, DEFAULT, '', '2025-06-28 14:03:24', '2025-06-28 14:03:24');
INSERT INTO `employee_annual_deductions` VALUES (5, 2, 2000, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, DEFAULT, DEFAULT, '', '2025-06-28 14:03:50', '2025-06-28 14:03:50');

-- ----------------------------
-- Table structure for employee_dependents
-- ----------------------------
DROP TABLE IF EXISTS `employee_dependents`;
CREATE TABLE `employee_dependents`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL COMMENT '关联的员工ID',
  `annual_deduction_id` int NULL DEFAULT NULL COMMENT '关联的年度专项附加扣除总表ID (可选, 如果需要更直接关联到某年的申报)',
  `dependent_name_encrypted` varbinary(255) NOT NULL COMMENT '被抚养人姓名 (SM4加密)',
  `dependent_id_card_encrypted` varbinary(255) NOT NULL COMMENT '被抚养人身份证号 (SM4加密)',
  `relationship` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '与员工关系 (例如: 子女, 父亲, 母亲, 配偶的父亲, 配偶的母亲)',
  `deduction_type_involved` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '涉及的扣除项目 (可逗号分隔存储多个，如: 子女教育,婴幼儿照护; 或设计成更规范的多对多关系)',
  `birth_date` date NULL DEFAULT NULL COMMENT '被抚养人出生日期 (例如用于判断子女年龄)',
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_employee_dependent_idcard`(`employee_id` ASC, `dependent_id_card_encrypted`(64) ASC) USING BTREE,
  INDEX `annual_deduction_id`(`annual_deduction_id` ASC) USING BTREE,
  CONSTRAINT `employee_dependents_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `employee_dependents_ibfk_2` FOREIGN KEY (`annual_deduction_id`) REFERENCES `employee_annual_deductions` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工被抚养人信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee_dependents
-- ----------------------------
INSERT INTO `employee_dependents` VALUES (1, 2, 1, 0x47B6164D8CF52D62B81DBE47D7D2EB62, 0x046E695AE1D761C99634FA780659EE840203D9227E3CF7B15641D520BB1DEC09, '父亲', '子女教育,继续教育,赡养老人', '1975-09-11', '', '2025-06-05 14:18:27', '2025-06-05 19:35:12');
INSERT INTO `employee_dependents` VALUES (2, 6, 3, 0x0A4C99F44FC192CE7CC36CB5AA03B48D, 0x046E695AE1D761C99634FA780659EE846D3A576FDAC05EE8CB1B9EC587D0375B, '父亲', '赡养老人', '1975-09-11', '', '2025-06-27 22:18:55', '2025-06-27 22:18:55');
INSERT INTO `employee_dependents` VALUES (3, 7, NULL, 0x0A4C99F44FC192CE7CC36CB5AA03B48D, 0x046E695AE1D761C99634FA780659EE84989933A5DC030ED948F175F02AD63CA7, '父亲', '子女教育', '2025-06-11', '', '2025-06-28 14:03:05', '2025-06-28 14:03:05');

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工编号',
  `name_encrypted` varbinary(255) NOT NULL COMMENT '姓名 (SM4加密)',
  `id_card_number_encrypted` varbinary(255) NOT NULL COMMENT '身份证号 (SM4加密)',
  `phone_number_encrypted` varbinary(128) NULL DEFAULT NULL COMMENT '手机号 (SM4加密)',
  `address_encrypted` varbinary(512) NULL DEFAULT NULL COMMENT '住址 (SM4加密)',
  `department_id` int NOT NULL COMMENT '所属部门ID',
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '岗位',
  `job_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职务',
  `hire_date` date NULL DEFAULT NULL COMMENT '入职日期',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'active' COMMENT '员工状态 (例如: active, resigned, on_leave)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `employee_number`(`employee_number` ASC) USING BTREE,
  UNIQUE INDEX `id_card_number_encrypted`(`id_card_number_encrypted` ASC) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employees
-- ----------------------------
INSERT INTO `employees` VALUES (2, '001', 0xDECCC91394BABDDD0947495A201AFD3D, 0xC7AD7CB90EC0962AD42B50D7F1B2CCE748C2B6A89821589AAFDB86871C0C2FAA, 0x1FE6D443525ED385B8FB1FE23E6A6807, 0xE76CDC4B8546D0E6B30E0F06F36CE59961378892EFEC5B4F30D8F524282DBA7F, 1, '维修协调', '学生', '2025-06-10', 'active', '2025-06-03 19:16:35', '2025-06-05 19:33:22');
INSERT INTO `employees` VALUES (3, '002', 0x83D4C8A56058A5C2F47685949EC1AB00, 0xC7AD7CB90EC0962AD42B50D7F1B2CCE71FC83E95651EDEDCBB22F760A6B383AC, 0x59D8D86639193EE5CD65786E16E9C694, 0xE76CDC4B8546D0E6B30E0F06F36CE59961378892EFEC5B4F30D8F524282DBA7F, 10, '学生', '学生', '2025-06-11', 'active', '2025-06-05 14:15:07', '2025-06-05 19:33:52');
INSERT INTO `employees` VALUES (5, '003', 0x33F285F02A9663ED099AAC52FB5CEA14, 0x110DC39559788FABB47A069C6470E1DE8A50103AADADC9F8A8DCC2988225EF37, 0x6F68AC1FEC2D1EFA871C95B47DBC9960, NULL, 1, '', '', '2025-06-18', 'active', '2025-06-27 13:56:37', '2025-06-27 13:56:37');
INSERT INTO `employees` VALUES (6, '004', 0x11218DF338BD5411AA19E0C121B8F5A3, 0xB92492953659759E07D58C786ADA232F440BC3479C80A683BE3FA409189A2F11, 0x6EFBA966F2D2B3ADEB99603951255A1D, 0xE76CDC4B8546D0E6B30E0F06F36CE59961378892EFEC5B4F30D8F524282DBA7F, 3, '', '', '2025-06-26', 'active', '2025-06-27 22:16:37', '2025-06-27 22:16:37');
INSERT INTO `employees` VALUES (7, '005', 0x177FEC330D5DB6739FA59A94F5893C96, 0xC7AD7CB90EC0962AD42B50D7F1B2CCE7A4C6731837A4ADAA471C67D6E0F78430, NULL, NULL, 6, '', '', '2025-06-19', 'active', '2025-06-28 14:02:11', '2025-06-28 14:02:11');
INSERT INTO `employees` VALUES (8, '006', 0x0C54AF17EA656C14919FB94DF124218E, 0xC7AD7CB90EC0962AD42B50D7F1B2CCE75AC406B97F59D81B53D3F8D179444965, NULL, NULL, 4, '', '', '2025-06-17', 'active', '2025-06-28 14:06:48', '2025-06-28 14:06:48');

-- ----------------------------
-- Table structure for monthly_salaries
-- ----------------------------
DROP TABLE IF EXISTS `monthly_salaries`;
CREATE TABLE `monthly_salaries`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工资记录唯一ID',
  `employee_id` int NOT NULL COMMENT '关联的员工ID',
  `year_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工资所属月份 (格式: YYYY-MM, 例如: 2023-01)',
  `should_attend_days` decimal(5, 1) NULL DEFAULT 0.0 COMMENT '本月应出勤天数',
  `actual_attend_days` decimal(5, 1) NULL DEFAULT 0.0 COMMENT '实际出勤天数',
  `basic_salary` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '基本工资',
  `post_allowance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '岗位津贴',
  `lunch_subsidy` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '午餐补贴',
  `overtime_pay` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '加班工资',
  `attendance_bonus` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '全勤工资',
  `other_earnings` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '其他应发款项/奖金',
  `social_security_personal` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '个人承担社保总额 (养老,医疗,失业)',
  `provident_fund_personal` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '个人承担住房公积金',
  `special_additional_deduction_monthly` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '本月专项附加扣除总额 (从年度申报计算得出)',
  `enterprise_annuity_personal` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '个人承担企业年金 (如果适用)',
  `other_pre_tax_deductions` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '其他税前扣除项 (如特定商业健康险)',
  `taxable_income_before_threshold` decimal(12, 2) NULL DEFAULT NULL COMMENT '未减除起征点的应纳税所得额 (总应发 - 总税前扣除)',
  `tax_threshold_amount` decimal(10, 2) NULL DEFAULT 5000.00 COMMENT '个税起征点/基本减除费用 (当前标准为5000元/月)',
  `taxable_income` decimal(12, 2) NULL DEFAULT NULL COMMENT '应纳税所得额 (taxable_income_before_threshold - tax_threshold_amount, 若小于0则为0)',
  `personal_income_tax` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '个人所得税',
  `late_leave_deduction` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '迟到/早退/请假扣款 (需明确是税前调整还是税后扣)',
  `other_post_tax_deductions` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '其他税后扣除项',
  `total_earnings_manual` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应发工资合计 (手动计算或应用层计算)',
  `total_pre_tax_deductions_manual` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '税前扣除总额 (手动计算或应用层计算)',
  `net_salary_manual` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '实发工资 (手动计算或应用层计算)',
  `salary_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'DRAFT' COMMENT '工资状态 (DRAFT草稿, PENDING_APPROVAL待审核, APPROVED已审核, PAID已发放, REJECTED已驳回)',
  `calculation_method_notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '税款计算方法备注 (例如：月度，累计预扣)',
  `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工资备注',
  `created_by_user_id` int NULL DEFAULT NULL COMMENT '创建人用户ID',
  `approved_by_user_id` int NULL DEFAULT NULL COMMENT '审核人用户ID',
  `paid_by_user_id` int NULL DEFAULT NULL COMMENT '发放操作人用户ID',
  `paid_date` datetime NULL DEFAULT NULL COMMENT '发放日期',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_employee_year_month`(`employee_id` ASC, `year_month` ASC) USING BTREE,
  INDEX `created_by_user_id`(`created_by_user_id` ASC) USING BTREE,
  INDEX `approved_by_user_id`(`approved_by_user_id` ASC) USING BTREE,
  INDEX `paid_by_user_id`(`paid_by_user_id` ASC) USING BTREE,
  CONSTRAINT `monthly_salaries_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `monthly_salaries_ibfk_2` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `monthly_salaries_ibfk_3` FOREIGN KEY (`approved_by_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `monthly_salaries_ibfk_4` FOREIGN KEY (`paid_by_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '月度工资明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of monthly_salaries
-- ----------------------------
INSERT INTO `monthly_salaries` VALUES (1, 2, '2025-06', 0.0, 0.0, 5000.00, 1000.00, 1000.00, 2000.00, 1000.00, 500.00, 100.00, 300.00, 283.33, 200.00, -0.01, 9616.68, 5000.00, 4616.68, 251.67, 30.00, -0.01, 10500.00, 883.32, 9818.34, 'APPROVED', '简化月度计算法 (非标，仅供演示)', '', NULL, NULL, NULL, NULL, '2025-06-09 14:23:50', '2025-06-09 14:24:10');
INSERT INTO `monthly_salaries` VALUES (2, 3, '2025-06', 0.0, 0.0, 4000.00, 1100.00, 1000.00, 1000.00, 1000.00, 0.00, 100.00, 500.00, 0.00, 0.00, 0.00, 7500.00, 5000.00, 2500.00, 75.00, 50.00, 0.00, 8100.00, 600.00, 7375.00, 'PAID', '简化月度计算法 (非标，仅供演示)', '', NULL, NULL, NULL, NULL, '2025-06-09 15:22:37', '2025-06-09 15:37:23');
INSERT INTO `monthly_salaries` VALUES (3, 5, '2025-06', 0.0, 0.0, 3000.00, 100.00, 100.00, 200.00, 300.00, 400.00, 100.00, 200.00, 125.00, 200.00, 0.00, 3475.00, 5000.00, 0.00, 0.00, 0.00, 0.00, 4100.00, 625.00, 3800.00, 'DRAFT', '简化月度计算法 (非标，仅供演示)', '', NULL, NULL, NULL, NULL, '2025-06-27 14:01:22', '2025-06-27 14:01:22');
INSERT INTO `monthly_salaries` VALUES (4, 6, '2025-06', 0.0, 0.0, 6998.00, 200.00, 300.00, 2000.00, 1000.00, 0.00, 100.00, 200.00, 925.00, 0.00, 0.00, 9273.00, 5000.00, 4273.00, 217.30, 0.00, 0.00, 10498.00, 1225.00, 9980.70, 'REJECTED', '简化月度计算法 (非标，仅供演示)', '', NULL, NULL, NULL, NULL, '2025-06-27 22:20:50', '2025-06-27 22:23:48');
INSERT INTO `monthly_salaries` VALUES (5, 7, '2025-06', 0.0, 0.0, 12222.00, 233.00, 1333.00, 100.00, 2000.00, 0.00, 300.00, 100.00, 103.67, 0.00, 0.00, 15384.33, 5000.00, 10384.33, 828.43, 100.00, 0.00, 15888.00, 503.67, 14559.57, 'DRAFT', '简化月度计算法 (非标，仅供演示)', NULL, NULL, NULL, NULL, NULL, '2025-06-28 14:07:55', '2025-06-28 14:07:55');
INSERT INTO `monthly_salaries` VALUES (6, 8, '2025-06', 0.0, 0.0, 12222.00, 233.00, 1333.00, 100.00, 2000.00, 0.00, 300.00, 100.00, 0.00, 0.00, 0.00, 15488.00, 5000.00, 10488.00, 838.80, 100.00, 0.00, 15888.00, 400.00, 14549.20, 'DRAFT', '简化月度计算法 (非标，仅供演示)', NULL, NULL, NULL, NULL, NULL, '2025-06-28 14:07:55', '2025-06-28 14:07:55');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称 (人事管理员, 财务管理员, 总经理, 系统管理员, 审计员)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, '系统管理员', '管理系统用户、角色权限等');
INSERT INTO `roles` VALUES (2, '人事管理员', '管理部门、人员信息、专项附加扣除');
INSERT INTO `roles` VALUES (3, '财务管理员', '管理工资条目、工资计算与发放');
INSERT INTO `roles` VALUES (4, '总经理', '查看各类报表和信息，审批权限');
INSERT INTO `roles` VALUES (5, '审计员', '查看系统操作日志');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户名',
  `password_hash` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SM3加密后的密码哈希',
  `role_id` int NOT NULL COMMENT '角色ID',
  `employee_id` int NULL DEFAULT NULL COMMENT '关联的员工ID (如果用户是员工, 后续添加employees表后建立外键)',
  `last_password_change_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后密码修改日期',
  `failed_login_attempts` int NULL DEFAULT 0 COMMENT '连续登录失败次数',
  `lockout_until` timestamp NULL DEFAULT NULL COMMENT '锁定截止时间',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '账户是否激活',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `employee_id`(`employee_id` ASC) USING BTREE,
  INDEX `role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', 'c4039a5810e67a8b1eb72cc2b9d9f2e55ca0e2fd2b84f55e212ad3a23aec89bc', 1, NULL, '2025-06-03 17:14:42', 0, NULL, '2025-06-29 15:33:00', 1, '2025-06-03 17:14:42', '2025-06-29 15:33:00');
INSERT INTO `users` VALUES (2, 'rsgly', '7cfd14cda64781170a78650da1eaa92581d37f1121b2a59a80ffa8ae5524d5fe', 2, NULL, '2025-06-06 08:12:34', 0, NULL, '2025-06-28 14:08:43', 1, '2025-06-06 16:12:34', '2025-06-28 14:08:43');
INSERT INTO `users` VALUES (3, 'cwgly', '1eeee12860a1573ab79079bcbf59c99dcefdc64b313e4664c30120038982e3eb', 3, NULL, '2025-06-06 08:13:57', 0, NULL, '2025-06-28 14:09:18', 1, '2025-06-06 16:13:56', '2025-06-28 14:09:18');
INSERT INTO `users` VALUES (4, 'zjl', '83b95dedb44ad69cb710e8ee56ac436817f2e3adf63e0af5d678aaa0cfbbdfb1', 4, NULL, '2025-06-06 08:15:02', 0, NULL, '2025-06-28 14:09:45', 1, '2025-06-06 16:15:01', '2025-06-28 14:09:45');
INSERT INTO `users` VALUES (5, 'sjy', 'fa2b8620e9a8d9d941db627eac0103d80dcdc760c1417a7eb2b6efa0158ad239', 5, NULL, '2025-06-06 08:15:27', 0, NULL, '2025-06-28 14:11:03', 1, '2025-06-06 16:15:26', '2025-06-28 14:11:03');
INSERT INTO `users` VALUES (6, 'zjl01', '83b95dedb44ad69cb710e8ee56ac436817f2e3adf63e0af5d678aaa0cfbbdfb1', 4, NULL, '2025-06-26 17:23:28', 0, NULL, '2025-06-27 13:52:24', 1, '2025-06-09 15:43:31', '2025-06-27 13:52:24');

SET FOREIGN_KEY_CHECKS = 1;
