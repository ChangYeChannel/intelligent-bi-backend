-- 创建库
create database if not exists intelligent_bi;

-- 切换库
use intelligent_bi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    user_name     varchar(256)                           null comment '用户昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    index idx_user_account (user_account)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表信息表
create table if not exists chart
(
    id         bigint          auto_increment          comment 'id' primary key,
    user_id    bigint                             null comment '用户Id',
    goal       text                               null comment '分析目标',
    chart_data text                               null comment '分析数据',
    chart_type varchar(128)                       null comment '图表类型',
    generate_chart  text                          null comment '生成的图表数据',
    generate_result text                          null comment '生成的分析结论',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除'
) comment '图表信息' collate = utf8mb4_unicode_ci;
