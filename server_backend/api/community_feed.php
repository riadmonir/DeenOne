<?php
/**
 * ==============================================================================
 * DEEN ONE API - COMMUNITY FEED, COMMENTS, REPLIES, LIKES & REPORTS
 * Offline-First & Public Browsing Architecture
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

// --- 0. Self-Healing Schema Migration ---
try {
    // 1. community_posts
    $pdo->exec("CREATE TABLE IF NOT EXISTS `community_posts` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `user_id` VARCHAR(100) DEFAULT NULL,
      `author_name` VARCHAR(150) NOT NULL,
      `author_avatar` VARCHAR(500) DEFAULT 'avatar_1',
      `author_location` VARCHAR(100) DEFAULT 'বাংলাদেশ',
      `title` VARCHAR(255) DEFAULT '',
      `category` VARCHAR(60) DEFAULT 'dua_request',
      `content` TEXT NOT NULL,
      `image_url` VARCHAR(255) DEFAULT NULL,
      `likes_count` INT UNSIGNED NOT NULL DEFAULT 0,
      `comments_count` INT UNSIGNED NOT NULL DEFAULT 0,
      `is_pinned` TINYINT(1) NOT NULL DEFAULT 0,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_comm_pin` (`is_pinned`),
      INDEX `idx_comm_cat` (`category`),
      INDEX `idx_comm_created` (`created_at` DESC)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    $postCols = $pdo->query("SHOW COLUMNS FROM community_posts")->fetchAll(PDO::FETCH_COLUMN);
    if (!in_array('title', $postCols)) {
        $pdo->exec("ALTER TABLE community_posts ADD COLUMN `title` VARCHAR(255) DEFAULT '' AFTER `author_avatar`");
    }
    if (!in_array('category', $postCols)) {
        $pdo->exec("ALTER TABLE community_posts ADD COLUMN `category` VARCHAR(60) DEFAULT 'dua_request' AFTER `title`");
    }
    if (!in_array('author_location', $postCols)) {
        $pdo->exec("ALTER TABLE community_posts ADD COLUMN `author_location` VARCHAR(100) DEFAULT 'বাংলাদেশ' AFTER `category`");
    }
    $pdo->exec("ALTER TABLE community_posts MODIFY COLUMN `author_avatar` VARCHAR(500) DEFAULT 'avatar_1'");

    // 2. community_comments
    $pdo->exec("CREATE TABLE IF NOT EXISTS `community_comments` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `post_id` INT UNSIGNED NOT NULL,
      `parent_comment_id` INT UNSIGNED DEFAULT NULL,
      `reply_to_author` VARCHAR(150) DEFAULT NULL,
      `user_id` VARCHAR(100) DEFAULT NULL,
      `author_name` VARCHAR(150) NOT NULL,
      `author_avatar` VARCHAR(500) DEFAULT 'avatar_1',
      `comment_text` TEXT NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_comment_post` (`post_id`),
      INDEX `idx_comment_parent` (`parent_comment_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    $commentCols = $pdo->query("SHOW COLUMNS FROM community_comments")->fetchAll(PDO::FETCH_COLUMN);
    if (!in_array('parent_comment_id', $commentCols)) {
        $pdo->exec("ALTER TABLE community_comments ADD COLUMN `parent_comment_id` INT UNSIGNED DEFAULT NULL AFTER `post_id`");
    }
    if (!in_array('reply_to_author', $commentCols)) {
        $pdo->exec("ALTER TABLE community_comments ADD COLUMN `reply_to_author` VARCHAR(150) DEFAULT NULL AFTER `parent_comment_id`");
    }
    $pdo->exec("ALTER TABLE community_comments MODIFY COLUMN `author_avatar` VARCHAR(500) DEFAULT 'avatar_1'");

    // 3. community_likes
    $pdo->exec("CREATE TABLE IF NOT EXISTS `community_likes` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `post_id` INT UNSIGNED NOT NULL,
      `user_id` VARCHAR(100) NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      UNIQUE KEY `idx_post_user` (`post_id`, `user_id`),
      INDEX `idx_likes_post` (`post_id`),
      INDEX `idx_likes_user` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    // 4. community_reports
    $pdo->exec("CREATE TABLE IF NOT EXISTS `community_reports` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `post_id` INT UNSIGNED DEFAULT NULL,
      `comment_id` INT UNSIGNED DEFAULT NULL,
      `reporter_user_id` VARCHAR(100) DEFAULT NULL,
      `reporter_name` VARCHAR(100) DEFAULT 'বেনামী ইউজার',
      `reason` VARCHAR(255) NOT NULL,
      `details` TEXT DEFAULT NULL,
      `status` ENUM('PENDING','RESOLVED','DISMISSED') NOT NULL DEFAULT 'PENDING',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_rep_status` (`status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
} catch (Exception $e) {
    // Continue execution
}

// Helper to clean post ID strings like "post_1712345" or "p_1" -> int 1
function parseCleanId($idStr) {
    if (is_numeric($idStr)) {
        return (int)$idStr;
    }
    if (preg_match('/_(\d+)$/', $idStr, $m)) {
        return (int)$m[1];
    }
    return (int)preg_replace('/\D/', '', (string)$idStr);
}

// --- 1. Handle POST Actions (Authenticated Only) ---
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = getRequestData();
    $action = $data['action'] ?? 'create_post';
    $userId = trim($data['user_id'] ?? '');

    if ($action !== 'toggle_like') {
        if (empty($userId) || $userId === 'guest' || $userId === 'anonymous' || (is_string($userId) && strpos($userId, 'dev_') === 0)) {
            sendJsonResponse(['success' => false, 'error' => 'Login is required to participate in community.'], 401);
        }
    } else {
        if (empty($userId) || $userId === 'guest' || $userId === 'anonymous') {
            $ip = $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
            $ua = $_SERVER['HTTP_USER_AGENT'] ?? 'DeenOne';
            $userId = 'dev_' . substr(md5($ip . $ua), 0, 16);
        }
    }

    if ($action === 'create_post') {
        $authorName = trim($data['author_name'] ?? '');
        $avatar = trim($data['author_avatar'] ?? 'avatar_1');
        $location = trim($data['author_location'] ?? 'বাংলাদেশ');
        $category = trim($data['category'] ?? 'dua_request');
        $title = trim($data['title'] ?? '');
        $content = trim($data['content'] ?? '');
        $mediaUrl = trim($data['media_url'] ?? '');

        if (empty($authorName)) {
            $authorName = 'সহযাত্রী';
        }

        if (empty($content) && empty($title)) {
            sendJsonResponse(['success' => false, 'error' => 'Post content cannot be empty.'], 400);
        }

        // Auto-split title and content if content contains newline and title is blank
        if (empty($title) && strpos($content, "\n") !== false) {
            $parts = explode("\n", $content, 2);
            $title = trim($parts[0]);
            $content = trim($parts[1]);
        } elseif (empty($title)) {
            $title = mb_substr($content, 0, 40) . '...';
        }

        syncUser($pdo, $userId, $authorName, $avatar);

        try {
            $stmt = $pdo->prepare("INSERT INTO community_posts 
                (user_id, author_name, author_avatar, author_location, title, category, content, image_url, likes_count, comments_count, is_pinned, created_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, NOW())");
            $stmt->execute([$userId, $authorName, $avatar, $location, $title, $category, $content, !empty($mediaUrl) ? $mediaUrl : null]);
            $newId = (int)$pdo->lastInsertId();

            sendJsonResponse([
                'success' => true,
                'post_id' => $newId,
                'message' => 'Post published successfully.'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'add_comment') {
        $rawPostId = $data['post_id'] ?? 0;
        $postId = parseCleanId($rawPostId);
        $parentCommentId = !empty($data['parent_comment_id']) ? parseCleanId($data['parent_comment_id']) : null;
        $replyToAuthor = !empty($data['reply_to_author']) ? trim($data['reply_to_author']) : null;
        $authorName = trim($data['author_name'] ?? 'সহযাত্রী');
        $avatar = trim($data['author_avatar'] ?? 'avatar_1');
        $comment = trim($data['comment_text'] ?? $data['content'] ?? '');

        if ($postId <= 0 || empty($comment)) {
            sendJsonResponse(['success' => false, 'error' => 'Invalid post or comment text.'], 400);
        }

        syncUser($pdo, $userId, $authorName, $avatar);

        try {
            $stmt = $pdo->prepare("INSERT INTO community_comments 
                (post_id, parent_comment_id, reply_to_author, user_id, author_name, author_avatar, comment_text, created_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW())");
            $stmt->execute([$postId, $parentCommentId > 0 ? $parentCommentId : null, $replyToAuthor, $userId, $authorName, $avatar, $comment]);
            $newCommentId = (int)$pdo->lastInsertId();

            // Recalculate and update exact comments count
            $cntStmt = $pdo->prepare("SELECT COUNT(*) FROM community_comments WHERE post_id = ?");
            $cntStmt->execute([$postId]);
            $newCommentsCount = (int)$cntStmt->fetchColumn();

            $pdo->prepare("UPDATE community_posts SET comments_count = ? WHERE id = ?")->execute([$newCommentsCount, $postId]);

            // Dispatch Real-time Push Notification to Target Author
            try {
                if ($parentCommentId > 0) {
                    $pStmt = $pdo->prepare("SELECT user_id, author_name FROM community_comments WHERE id = ?");
                    $pStmt->execute([$parentCommentId]);
                    $parentComment = $pStmt->fetch();
                    if ($parentComment && !empty($parentComment['user_id']) && $parentComment['user_id'] !== $userId) {
                        $notifTitle = "💬 আপনার মন্তব্যে নতুন উত্তর!";
                        $notifBody = $authorName . " আপনার মন্তব্যে উত্তর দিয়েছেন: \"" . (mb_strlen($comment) > 50 ? mb_substr($comment, 0, 47) . '...' : $comment) . "\"";
                        $targetAction = 'feature_community:post_id:' . $postId;
                        $nStmt = $pdo->prepare("INSERT INTO push_notifications (title, body, notification_type, target_action, target_audience, priority, is_active) VALUES (?, ?, 'COMMUNITY_REPLY', ?, ?, 'HIGH', 1)");
                        $nStmt->execute([$notifTitle, $notifBody, $targetAction, $parentComment['user_id']]);
                    }
                } else {
                    $postStmt = $pdo->prepare("SELECT user_id, author_name, content FROM community_posts WHERE id = ?");
                    $postStmt->execute([$postId]);
                    $targetPost = $postStmt->fetch();
                    if ($targetPost && !empty($targetPost['user_id']) && $targetPost['user_id'] !== $userId) {
                        $notifTitle = "💬 আপনার পোস্টে নতুন মন্তব্য!";
                        $notifBody = $authorName . " আপনার পোস্টে মন্তব্য করেছেন: \"" . (mb_strlen($comment) > 50 ? mb_substr($comment, 0, 47) . '...' : $comment) . "\"";
                        $targetAction = 'feature_community:post_id:' . $postId;
                        $nStmt = $pdo->prepare("INSERT INTO push_notifications (title, body, notification_type, target_action, target_audience, priority, is_active) VALUES (?, ?, 'COMMUNITY_COMMENT', ?, ?, 'HIGH', 1)");
                        $nStmt->execute([$notifTitle, $notifBody, $targetAction, $targetPost['user_id']]);
                    }
                }
            } catch (Exception $notifEx) {
                // Ignore notification insert error
            }

            sendJsonResponse([
                'success' => true, 
                'comment_id' => $newCommentId,
                'parent_comment_id' => $parentCommentId,
                'comments_count' => $newCommentsCount,
                'message' => 'Comment added successfully.'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'toggle_like') {
        $rawPostId = $data['post_id'] ?? 0;
        $postId = parseCleanId($rawPostId);
        $authorName = trim($data['author_name'] ?? 'সহযাত্রী');

        if ($postId <= 0) {
            sendJsonResponse(['success' => false, 'error' => 'Invalid post ID.'], 400);
        }

        try {
            $check = $pdo->prepare("SELECT 1 FROM community_likes WHERE post_id = ? AND user_id = ?");
            $check->execute([$postId, $userId]);
            $alreadyLiked = (bool)$check->fetchColumn();

            $shouldLike = isset($data['liked']) ? (bool)$data['liked'] : !$alreadyLiked;

            if ($shouldLike && !$alreadyLiked) {
                $pdo->prepare("INSERT IGNORE INTO community_likes (post_id, user_id) VALUES (?, ?)")->execute([$postId, $userId]);
                $pdo->prepare("UPDATE community_posts SET likes_count = likes_count + 1 WHERE id = ?")->execute([$postId]);

                // Push Notification
                try {
                    $postStmt = $pdo->prepare("SELECT user_id, author_name, content FROM community_posts WHERE id = ?");
                    $postStmt->execute([$postId]);
                    $targetPost = $postStmt->fetch();
                    if ($targetPost && !empty($targetPost['user_id']) && $targetPost['user_id'] !== $userId) {
                        $notifTitle = "❤️ আপনার পোস্টে নতুন লাইক এসেছে!";
                        $preview = !empty($targetPost['content']) ? (mb_strlen($targetPost['content']) > 40 ? mb_substr($targetPost['content'], 0, 37) . '...' : $targetPost['content']) : 'পোস্টে';
                        $notifBody = $authorName . " আপনার \"" . $preview . "\" পোস্টে লাইক করেছেন।";
                        $targetAction = 'feature_community:post_id:' . $postId;
                        $nStmt = $pdo->prepare("INSERT INTO push_notifications (title, body, notification_type, target_action, target_audience, priority, is_active) VALUES (?, ?, 'COMMUNITY_LIKE', ?, ?, 'NORMAL', 1)");
                        $nStmt->execute([$notifTitle, $notifBody, $targetAction, $targetPost['user_id']]);
                    }
                } catch (Exception $notifEx) {}
            } elseif (!$shouldLike && $alreadyLiked) {
                $pdo->prepare("DELETE FROM community_likes WHERE post_id = ? AND user_id = ?")->execute([$postId, $userId]);
                $pdo->prepare("UPDATE community_posts SET likes_count = GREATEST(0, likes_count - 1) WHERE id = ?")->execute([$postId]);
            }

            // Fetch latest count
            $cntStmt = $pdo->prepare("SELECT likes_count FROM community_posts WHERE id = ?");
            $cntStmt->execute([$postId]);
            $newLikesCount = (int)$cntStmt->fetchColumn();

            sendJsonResponse([
                'success' => true, 
                'liked' => $shouldLike, 
                'likes_count' => $newLikesCount
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'edit_post') {
        $rawPostId = $data['post_id'] ?? 0;
        $postId = parseCleanId($rawPostId);
        $title = trim($data['title'] ?? '');
        $content = trim($data['content'] ?? '');
        $category = trim($data['category'] ?? '');

        if ($postId <= 0 || (empty($title) && empty($content))) {
            sendJsonResponse(['success' => false, 'error' => 'পোস্ট আইডি বা কন্টেন্ট খালি হতে পারে না।'], 400);
        }

        try {
            // Verify author ownership
            $chkStmt = $pdo->prepare("SELECT user_id, title, content, category FROM community_posts WHERE id = ?");
            $chkStmt->execute([$postId]);
            $existingPost = $chkStmt->fetch(PDO::FETCH_ASSOC);

            if (!$existingPost) {
                sendJsonResponse(['success' => false, 'error' => 'পোস্টটি পাওয়া যায়নি।'], 404);
            }

            if ($existingPost['user_id'] !== $userId && $userId !== 'admin_official') {
                sendJsonResponse(['success' => false, 'error' => 'শুধুমাত্র পোস্টের লেখক এটি এডিট করতে পারবেন।'], 403);
            }

            if (empty($title) && strpos($content, "\n") !== false) {
                $parts = explode("\n", $content, 2);
                $title = trim($parts[0]);
                $content = trim($parts[1]);
            } elseif (empty($title)) {
                $title = mb_substr($content, 0, 40) . '...';
            }

            $catToUpdate = !empty($category) ? $category : $existingPost['category'];

            $updStmt = $pdo->prepare("UPDATE community_posts SET title = ?, content = ?, category = ? WHERE id = ?");
            $updStmt->execute([$title, $content, $catToUpdate, $postId]);

            sendJsonResponse([
                'success' => true,
                'post_id' => $postId,
                'title' => $title,
                'content' => $content,
                'category' => $catToUpdate,
                'message' => 'পোস্ট সফলভাবে এডিট ও আপডেট করা হয়েছে।'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'ডাটাবেজ ত্রুটি: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'delete_post') {
        $rawPostId = $data['post_id'] ?? 0;
        $postId = parseCleanId($rawPostId);
        if ($postId <= 0) {
            sendJsonResponse(['success' => false, 'error' => 'অকার্যকর পোস্ট আইডি।'], 400);
        }
        try {
            // Verify author ownership
            $chkStmt = $pdo->prepare("SELECT user_id FROM community_posts WHERE id = ?");
            $chkStmt->execute([$postId]);
            $existingPost = $chkStmt->fetch(PDO::FETCH_ASSOC);

            if (!$existingPost) {
                sendJsonResponse(['success' => true, 'message' => 'পোস্টটি ইতিমধ্যে মুছে ফেলা হয়েছে।']);
            }

            if ($existingPost['user_id'] !== $userId && $userId !== 'admin_official') {
                sendJsonResponse(['success' => false, 'error' => 'শুধুমাত্র পোস্টের লেখক এটি মুছতে পারবেন।'], 403);
            }

            $pdo->prepare("DELETE FROM community_posts WHERE id = ?")->execute([$postId]);
            $pdo->prepare("DELETE FROM community_comments WHERE post_id = ?")->execute([$postId]);
            $pdo->prepare("DELETE FROM community_likes WHERE post_id = ?")->execute([$postId]);
            $pdo->prepare("DELETE FROM community_reports WHERE post_id = ?")->execute([$postId]);
            sendJsonResponse(['success' => true, 'message' => 'পোস্ট সফলভাবে মুছে ফেলা হয়েছে।']);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => $e->getMessage()], 500);
        }
    }

    if ($action === 'delete_comment') {
        $rawCommentId = $data['comment_id'] ?? 0;
        $commentId = parseCleanId($rawCommentId);
        $rawPostId = $data['post_id'] ?? 0;
        $postId = parseCleanId($rawPostId);
        if ($commentId > 0) {
            try {
                $pdo->prepare("DELETE FROM community_comments WHERE id = ? AND user_id = ?")->execute([$commentId, $userId]);
                if ($postId > 0) {
                    $cntStmt = $pdo->prepare("SELECT COUNT(*) FROM community_comments WHERE post_id = ?");
                    $cntStmt->execute([$postId]);
                    $cnt = (int)$cntStmt->fetchColumn();
                    $pdo->prepare("UPDATE community_posts SET comments_count = ? WHERE id = ?")->execute([$cnt, $postId]);
                }
                sendJsonResponse(['success' => true, 'message' => 'Comment deleted.']);
            } catch (Exception $e) {
                sendJsonResponse(['success' => false, 'error' => $e->getMessage()], 500);
            }
        }
    }

    if ($action === 'report_post') {
        $rawPostId = $data['post_id'] ?? 0;
        $postId = parseCleanId($rawPostId);
        $reason = trim($data['reason'] ?? 'আপত্তিকর বা ক্ষতিকর বিষয়বস্তু');
        $details = trim($data['details'] ?? '');
        $reporterName = trim($data['reporter_name'] ?? '');
        if (empty($reporterName)) {
            $reporterName = !empty($userId) ? 'ইউজার (' . substr($userId, 0, 8) . ')' : 'সহযাত্রী';
        }

        if ($postId <= 0) {
            sendJsonResponse(['success' => false, 'error' => 'অকার্যকর পোস্ট আইডি।'], 400);
        }

        try {
            $stmt = $pdo->prepare("INSERT INTO community_reports (post_id, reporter_user_id, reporter_name, reason, details, status) VALUES (?, ?, ?, ?, ?, 'PENDING')");
            $stmt->execute([$postId, $userId, $reporterName, $reason, $details]);
            sendJsonResponse(['success' => true, 'message' => 'আপনার রিপোর্টটি সফলভাবে পর্যালোচনার জন্য জমা নেওয়া হয়েছে।']);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'ডাটাবেজ ত্রুটি: ' . $e->getMessage()], 500);
        }
    }

    sendJsonResponse(['success' => false, 'error' => 'Unsupported action.'], 400);
}

// --- 2. Handle GET (Public Feed or Comments - Open to All Users) ---
$action = trim($_GET['action'] ?? 'feed');

// 2.1 Fetch Comments for a specific post
if ($action === 'comments') {
    $rawPostId = $_GET['post_id'] ?? 0;
    $postId = parseCleanId($rawPostId);
    if ($postId <= 0) {
        sendJsonResponse(['success' => false, 'error' => 'Invalid post_id.'], 400);
    }
    try {
        $stmt = $pdo->prepare("SELECT c.id, c.post_id, c.parent_comment_id, c.reply_to_author, c.user_id, c.author_name, 
                                      COALESCE(NULLIF(u.avatar, ''), NULLIF(c.author_avatar, ''), 'avatar_1') as author_avatar, 
                                      c.comment_text, c.created_at 
                               FROM community_comments c
                               LEFT JOIN users u ON c.user_id = u.user_id
                               WHERE c.post_id = ? 
                               ORDER BY c.created_at ASC");
        $stmt->execute([$postId]);
        $comments = $stmt->fetchAll();
        sendJsonResponse(['success' => true, 'count' => count($comments), 'comments' => $comments]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Failed to load comments: ' . $e->getMessage()], 500);
    }
}

// 2.2 Fetch Public Community Feed (Open to All, Cached for Offline)
$page = max(1, (int)($_GET['page'] ?? 1));
$limit = max(1, min(100, (int)($_GET['limit'] ?? 50)));
$offset = ($page - 1) * $limit;
$currentUserId = trim($_GET['user_id'] ?? '');
$category = trim($_GET['category'] ?? 'all');

try {
    $whereClause = "";
    $params = [$currentUserId];

    if (!empty($category) && $category !== 'all') {
        $whereClause = "WHERE cp.category = ?";
        $params[] = $category;
    }

    $sql = "SELECT cp.id, cp.user_id, cp.author_name, 
                   COALESCE(NULLIF(u.avatar, ''), NULLIF(cp.author_avatar, ''), 'avatar_1') as author_avatar, 
                   IFNULL(cp.author_location, 'বাংলাদেশ') as author_location,
                   IFNULL(cp.title, '') as title,
                   IFNULL(cp.category, 'dua_request') as category,
                   cp.content, cp.image_url, cp.likes_count, cp.comments_count, cp.is_pinned, cp.created_at,
                   EXISTS(SELECT 1 FROM community_likes cl WHERE cl.post_id = cp.id AND cl.user_id = ?) as user_has_liked
            FROM community_posts cp
            LEFT JOIN users u ON cp.user_id = u.user_id
            $whereClause
            ORDER BY cp.is_pinned DESC, cp.created_at DESC
            LIMIT $limit OFFSET $offset";

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $posts = $stmt->fetchAll();

    // Eagerly attach comments for each post so mobile clients can browse completely offline
    $postIds = array_column($posts, 'id');
    $commentsByPost = [];

    if (!empty($postIds)) {
        $inPlaceholders = implode(',', array_fill(0, count($postIds), '?'));
        $cStmt = $pdo->prepare("SELECT c.id, c.post_id, c.parent_comment_id, c.reply_to_author, c.user_id, c.author_name, 
                                       COALESCE(NULLIF(u.avatar, ''), NULLIF(c.author_avatar, ''), 'avatar_1') as author_avatar, 
                                       c.comment_text, c.created_at 
                                FROM community_comments c
                                LEFT JOIN users u ON c.user_id = u.user_id
                                WHERE c.post_id IN ($inPlaceholders) 
                                ORDER BY c.created_at ASC");
        $cStmt->execute($postIds);
        while ($c = $cStmt->fetch()) {
            $commentsByPost[$c['post_id']][] = $c;
        }
    }

    foreach ($posts as &$p) {
        $p['user_has_liked'] = (bool)$p['user_has_liked'];
        $p['is_pinned'] = (bool)$p['is_pinned'];
        $p['likes_count'] = (int)$p['likes_count'];
        $p['comments_count'] = (int)$p['comments_count'];

        // If title is blank, extract from content
        if (empty($p['title'])) {
            $contentLines = explode("\n", $p['content'], 2);
            $p['title'] = trim($contentLines[0]);
            if (isset($contentLines[1])) {
                $p['content'] = trim($contentLines[1]);
            }
        }

        $p['comments'] = $commentsByPost[$p['id']] ?? [];
    }

    sendJsonResponse([
        'success' => true,
        'count' => count($posts),
        'page' => $page,
        'posts' => $posts
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load community posts: ' . $e->getMessage()
    ], 500);
}
