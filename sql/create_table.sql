CREATE DATABASE `intelligent_bi`;

USE `intelligent_bi`;

CREATE TABLE `chart` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `user_id` bigint(20) DEFAULT NULL COMMENT '用户Id',
     `chart_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
     `goal` text COLLATE utf8mb4_unicode_ci COMMENT '分析目标',
     `chart_data` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原始数据表名',
     `chart_type` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图表类型',
     `generate_chart` text COLLATE utf8mb4_unicode_ci COMMENT '生成的图表数据',
     `generate_result` text COLLATE utf8mb4_unicode_ci COMMENT '生成的分析结论',
     `status` varchar(50) NOT NULL DEFAULT 'wait' COMMENT 'wait-[等待中], running-[执行中], succeed-[已完成], failed-[执行失败]',
     `exec_message` varchar(128) NULL COMMENT '执行信息',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1662806896488230914 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图表信息';

CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_account` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
    `user_password` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `user_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
    `user_avatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
    `user_role` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_account` (`user_account`)
) ENGINE=InnoDB AUTO_INCREMENT=1652621656956338179 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
