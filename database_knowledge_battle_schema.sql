-- =========================================================================
-- DEENONE KNOWLEDGE BATTLE (নলেজ ব্যাটেল) PRODUCTION MYSQL DATABASE SCHEMA
-- Compatible with MySQL 5.7+ / MySQL 8.0+ / MariaDB 10.3+
-- Charset: utf8mb4 (Full Bengali and Arabic Unicode Support)
-- =========================================================================

CREATE DATABASE IF NOT EXISTS `deenone_battle_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `deenone_battle_db`;

-- -------------------------------------------------------------------------
-- 1. TABLE: users (User accounts & Profile Information)
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` VARCHAR(64) NOT NULL,
    `username` VARCHAR(100) NOT NULL,
    `profile_information` JSON NULL,
    `account_status` ENUM('ACTIVE', 'SUSPENDED', 'BANNED') NOT NULL DEFAULT 'ACTIVE',
    `total_xp` INT UNSIGNED NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_account_status` (`account_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------------------
-- 2. TABLE: battles (Battle Rooms & Match Settings)
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `battles` (
    `battle_id` VARCHAR(64) NOT NULL,
    `battle_code` VARCHAR(10) NOT NULL,
    `creator_id` VARCHAR(64) NOT NULL,
    `mode` ENUM('1v1', '1v2', '1v3', 'custom') NOT NULL DEFAULT '1v1',
    `max_players` TINYINT UNSIGNED NOT NULL DEFAULT 2,
    `question_count` SMALLINT UNSIGNED NOT NULL DEFAULT 10,
    `question_time` SMALLINT UNSIGNED NOT NULL DEFAULT 15,
    `category_id` VARCHAR(64) NOT NULL,
    `difficulty` ENUM('EASY', 'MEDIUM', 'HARD', 'MIXED') NOT NULL DEFAULT 'MEDIUM',
    `status` ENUM(
        'CREATED',
        'WAITING_FOR_PLAYERS',
        'PLAYERS_JOINED',
        'READY_CHECK',
        'COUNTDOWN',
        'LIVE',
        'COMPLETED',
        'RESULT',
        'EXPIRED'
    ) NOT NULL DEFAULT 'CREATED',
    `negative_marking` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `started_at` TIMESTAMP NULL DEFAULT NULL,
    `completed_at` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`battle_id`),
    UNIQUE KEY `uk_battle_code` (`battle_code`),
    INDEX `idx_creator` (`creator_id`),
    INDEX `idx_category` (`category_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    CONSTRAINT `fk_battles_creator` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------------------
-- 3. TABLE: battle_players (Participant Live State, Scores & Connection)
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `battle_players` (
    `battle_player_id` VARCHAR(64) NOT NULL,
    `battle_id` VARCHAR(64) NOT NULL,
    `user_id` VARCHAR(64) NOT NULL,
    `joined_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ready_status` ENUM('READY', 'NOT_READY') NOT NULL DEFAULT 'READY',
    `connection_status` ENUM('JOINED', 'READY', 'NOT_READY', 'DISCONNECTED', 'RECONNECTED') NOT NULL DEFAULT 'JOINED',
    `score` INT NOT NULL DEFAULT 0,
    `correct_answers` SMALLINT UNSIGNED NOT NULL DEFAULT 0,
    `wrong_answers` SMALLINT UNSIGNED NOT NULL DEFAULT 0,
    `completion_status` ENUM('IN_PROGRESS', 'FINISHED', 'ABANDONED') NOT NULL DEFAULT 'IN_PROGRESS',
    `session_token` VARCHAR(64) NULL,
    `last_heartbeat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`battle_player_id`),
    UNIQUE KEY `uk_battle_user` (`battle_id`, `user_id`),
    INDEX `idx_bp_battle` (`battle_id`),
    INDEX `idx_bp_user` (`user_id`),
    INDEX `idx_bp_connection` (`connection_status`),
    CONSTRAINT `fk_bp_battle` FOREIGN KEY (`battle_id`) REFERENCES `battles` (`battle_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_bp_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------------------
-- 4. TABLE: questions (Verified Question Bank with Dalil & Metadata)
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `questions` (
    `question_id` VARCHAR(64) NOT NULL,
    `category_id` VARCHAR(64) NOT NULL,
    `question_text` TEXT NOT NULL,
    `options` JSON NOT NULL,
    `correct_answer` TINYINT UNSIGNED NOT NULL,
    `difficulty` ENUM('EASY', 'MEDIUM', 'HARD', 'MIXED') NOT NULL DEFAULT 'MEDIUM',
    `source` VARCHAR(255) NOT NULL,
    `source_id` VARCHAR(100) NULL,
    `explanation` TEXT NULL,
    `language` VARCHAR(10) NOT NULL DEFAULT 'bn',
    `status` ENUM('ACTIVE', 'VERIFIED', 'ARCHIVED') NOT NULL DEFAULT 'VERIFIED',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`question_id`),
    INDEX `idx_q_category` (`category_id`),
    INDEX `idx_q_difficulty` (`difficulty`),
    INDEX `idx_q_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------------------
-- 5. TABLE: battle_questions (Questions Allocated to a Specific Match)
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `battle_questions` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `battle_id` VARCHAR(64) NOT NULL,
    `question_id` VARCHAR(64) NOT NULL,
    `question_order` SMALLINT UNSIGNED NOT NULL,
    `started_at` TIMESTAMP NULL DEFAULT NULL,
    `ended_at` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_battle_q_order` (`battle_id`, `question_order`),
    INDEX `idx_bq_battle` (`battle_id`),
    INDEX `idx_bq_question` (`question_id`),
    CONSTRAINT `fk_bq_battle` FOREIGN KEY (`battle_id`) REFERENCES `battles` (`battle_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_bq_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`question_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------------------
-- 6. TABLE: answers (Authoritative Player Submissions & Time Log)
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `answers` (
    `answer_id` VARCHAR(64) NOT NULL,
    `battle_id` VARCHAR(64) NOT NULL,
    `question_id` VARCHAR(64) NOT NULL,
    `user_id` VARCHAR(64) NOT NULL,
    `selected_answer` TINYINT NOT NULL,
    `is_correct` TINYINT(1) NOT NULL,
    `response_time_ms` INT UNSIGNED NOT NULL DEFAULT 0,
    `points_delta` INT NOT NULL DEFAULT 0,
    `submitted_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`answer_id`),
    UNIQUE KEY `uk_user_battle_question` (`battle_id`, `question_id`, `user_id`),
    INDEX `idx_ans_battle` (`battle_id`),
    INDEX `idx_ans_user` (`user_id`),
    INDEX `idx_ans_question` (`question_id`),
    CONSTRAINT `fk_ans_battle` FOREIGN KEY (`battle_id`) REFERENCES `battles` (`battle_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ans_question` FOREIGN KEY (`question_id`) REFERENCES `questions` (`question_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ans_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
