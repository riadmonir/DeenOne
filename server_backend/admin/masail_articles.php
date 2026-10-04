<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - MASAIL ARTICLES & RULINGS (মাসাইল ও ফতোয়া সহায়িকা)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Bilingual Topic mapping definition for 8 Masail Chapters (Strictly Rule 5 compliant - Zero mixed brackets)
$topicList = [
    1 => ['bn' => 'আজান', 'en' => 'Adhan'],
    2 => ['bn' => 'অজু', 'en' => 'Wudu'],
    3 => ['bn' => 'নামাজ', 'en' => 'Salah'],
    4 => ['bn' => 'তারাবীহ', 'en' => 'Tarabi'],
    5 => ['bn' => 'কাযা নামাজ', 'en' => 'Qadha Salah'],
    6 => ['bn' => 'সাহু সেজদাহ', 'en' => 'Sahu Sajdah'],
    7 => ['bn' => 'মসজিদ', 'en' => 'Masjid'],
    8 => ['bn' => 'জুম\'আ', 'en' => 'Jumu\'ah']
];

// Handle Action (Add, Edit, Delete, Toggle Status)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('masail_articles.php');
    }

    if ($action === 'create' || $action === 'update') {
        $topicId = (int)($_POST['topic_id'] ?? 1);
        $topicTitle = $topicList[$topicId]['bn'] ?? 'আজান';

        $titleBn = trim($_POST['title_bn'] ?? '');
        $titleEn = trim($_POST['title_en'] ?? '');
        $contentBn = trim($_POST['content_bn'] ?? '');
        $contentEn = trim($_POST['content_en'] ?? '');
        $displayOrder = (int)($_POST['display_order'] ?? 1);
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($titleBn) || empty($contentBn)) {
            setFlash('danger', __('আর্টিকেলের বাংলা শিরোনাম এবং মূল বিষয়বস্তু আবশ্যক।', 'Bengali title and content are required.'));
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO masail_articles (topic_id, topic_title, title_bn, title_en, content_bn, content_en, display_order, is_active) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$topicId, $topicTitle, $titleBn, $titleEn, $contentBn, $contentEn, $displayOrder, $isActive]);
                    $newId = $pdo->lastInsertId();
                    logAdminAction($pdo, 'CREATE_MASAIL_ARTICLE', 'masail_articles', (string)$newId, 'নতুন মাসাইল: ' . $titleBn);
                    setFlash('success', __('নতুন মাসাইল সফলভাবে যুক্ত হয়েছে!', 'New Masail article added successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE masail_articles SET topic_id = ?, topic_title = ?, title_bn = ?, title_en = ?, content_bn = ?, content_en = ?, display_order = ?, is_active = ? WHERE id = ?");
                    $stmt->execute([$topicId, $topicTitle, $titleBn, $titleEn, $contentBn, $contentEn, $displayOrder, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_MASAIL_ARTICLE', 'masail_articles', (string)$id, 'মাসাইল আপডেট: ' . $titleBn);
                    setFlash('success', __('মাসাইলের তথ্য সফলভাবে আপডেট হয়েছে!', 'Masail article updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('ডাটাবেস ত্রুটি: ', 'Database error: ') . $e->getMessage());
            }
        }
    } elseif ($action === 'toggle_status') {
        $id = (int)($_POST['id'] ?? 0);
        $status = (int)($_POST['status'] ?? 0);
        try {
            $stmt = $pdo->prepare("UPDATE masail_articles SET is_active = ? WHERE id = ?");
            $stmt->execute([$status, $id]);
            logAdminAction($pdo, 'TOGGLE_MASAIL_ARTICLE', 'masail_articles', (string)$id, 'স্ট্যাটাস পরিবর্তন: ' . ($status ? 'সক্রিয়' : 'নিষ্ক্রিয়'));
            setFlash('success', __('মাসাইলের স্ট্যাটাস পরিবর্তিত হয়েছে।', 'Article status updated successfully.'));
        } catch (Exception $e) {
            setFlash('danger', __('স্ট্যাটাস পরিবর্তনে ত্রুটি: ', 'Status update error: ') . $e->getMessage());
        }
    }
    redirect('masail_articles.php');
}

if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM masail_articles WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_MASAIL_ARTICLE', 'masail_articles', (string)$id, 'মাসাইল মুছে ফেলা হয়েছে');
        setFlash('success', __('মাসাইল সফলভাবে মুছে ফেলা হয়েছে!', 'Masail article deleted successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছে ফেলতে ত্রুটি: ', 'Delete error: ') . $e->getMessage());
    }
    redirect('masail_articles.php');
}

// Fetch Filter and List
$filterTopic = isset($_GET['topic_id']) ? (int)$_GET['topic_id'] : 0;
$search = trim($_GET['search'] ?? '');

$sql = "SELECT * FROM masail_articles WHERE 1=1";
$params = [];
if ($filterTopic > 0) {
    $sql .= " AND topic_id = ?";
    $params[] = $filterTopic;
}
if (!empty($search)) {
    $sql .= " AND (title_bn LIKE ? OR title_en LIKE ? OR content_bn LIKE ? OR content_en LIKE ?)";
    $params[] = "%$search%";
    $params[] = "%$search%";
    $params[] = "%$search%";
    $params[] = "%$search%";
}
$sql .= " ORDER BY topic_id ASC, display_order ASC, id ASC";

$stmt = $pdo->prepare($sql);
$stmt->execute($params);
$articles = $stmt->fetchAll();

// Fetch Edit Item if requested
$editItem = null;
if (isset($_GET['edit'])) {
    $editId = (int)$_GET['edit'];
    $stmt = $pdo->prepare("SELECT * FROM masail_articles WHERE id = ?");
    $stmt->execute([$editId]);
    $editItem = $stmt->fetch();
}

$pageTitle = __('মাসাইল', 'Masail');
require_once __DIR__ . '/header.php';
?>

<div class="row">
    <!-- Left Column: Add / Edit Form -->
    <div class="col-lg-5 mb-4">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                    <i class="fas fa-<?= $editItem ? 'edit' : 'plus-circle' ?> me-2"></i>
                    <?= $editItem ? __('মাসাইল সম্পাদনা', 'Edit Masail') : __('নতুন মাসাইল', 'New Masail') ?>
                </h5>
                <?php if ($editItem): ?>
                    <a href="masail_articles.php" class="btn btn-sm btn-light"><?= __('নতুন', 'New') ?></a>
                <?php endif; ?>
            </div>
            <div class="card-body">
                <form method="POST" action="masail_articles.php">
                    <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                    <input type="hidden" name="action" value="<?= $editItem ? 'update' : 'create' ?>">
                    <?php if ($editItem): ?>
                        <input type="hidden" name="id" value="<?= $editItem['id'] ?>">
                    <?php endif; ?>

                    <div class="mb-3">
                        <label class="form-label font-weight-bold"><?= __('অধ্যায়', 'Topic') ?> <span class="text-danger">*</span></label>
                        <select name="topic_id" class="form-select" required>
                            <?php foreach ($topicList as $tid => $tInfo): ?>
                                <option value="<?= $tid ?>" <?= ($editItem && $editItem['topic_id'] == $tid) ? 'selected' : '' ?>>
                                    <?= isEn() ? $tInfo['en'] : $tInfo['bn'] ?>
                                </option>
                            <?php endforeach; ?>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label font-weight-bold"><?= __('বাংলা শিরোনাম', 'Bengali Title') ?> <span class="text-danger">*</span></label>
                        <input type="text" name="title_bn" class="form-control" value="<?= htmlspecialchars($editItem['title_bn'] ?? '') ?>" placeholder="<?= __('উদাঃ আজানের শুরুতে দরুদ শরিফ পড়া', 'e.g. Reciting Durood before Adhan') ?>" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label font-weight-bold"><?= __('ইংরেজি শিরোনাম', 'English Title') ?></label>
                        <input type="text" name="title_en" class="form-control" value="<?= htmlspecialchars($editItem['title_en'] ?? '') ?>" placeholder="e.g. Reciting Durood Sharif Before the Adhan">
                    </div>

                    <div class="mb-3">
                        <label class="form-label font-weight-bold"><?= __('বাংলা বিবরণ', 'Bengali Content') ?> <span class="text-danger">*</span></label>
                        <textarea name="content_bn" class="form-control" rows="6" placeholder="<?= __('বিস্তারিত মাসআলা, দলিল ও হাদিস রেফারেন্স লিখুন...', 'Write detailed ruling, evidence and Hadith references...') ?>" required><?= htmlspecialchars($editItem['content_bn'] ?? '') ?></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label font-weight-bold"><?= __('ইংরেজি বিবরণ', 'English Content') ?></label>
                        <textarea name="content_en" class="form-control" rows="5" placeholder="Detailed rulings and authentic references in English..."><?= htmlspecialchars($editItem['content_en'] ?? '') ?></textarea>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label font-weight-bold"><?= __('ক্রম', 'Order') ?></label>
                            <input type="number" name="display_order" class="form-control" value="<?= $editItem['display_order'] ?? 1 ?>" min="1">
                        </div>
                        <div class="col-md-6 mb-3 d-flex align-items-center">
                            <div class="form-check mt-4">
                                <input class="form-check-input" type="checkbox" name="is_active" id="isActiveCheck" <?= (!$editItem || $editItem['is_active']) ? 'checked' : '' ?>>
                                <label class="form-check-label font-weight-bold" for="isActiveCheck"><?= __('সক্রিয়', 'Active') ?></label>
                            </div>
                        </div>
                    </div>

                    <div class="d-grid mt-3">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-save me-2"></i> <?= __('সংরক্ষণ করুন', 'Save') ?>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Right Column: Articles List -->
    <div class="col-lg-7">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-light d-flex flex-wrap justify-content-between align-items-center">
                <h5 class="mb-0 text-dark">
                    <i class="fas fa-book-open me-2 text-primary"></i> 
                    <?= __('মাসাইল তালিকা', 'Masail List') ?> <span class="badge bg-secondary ms-2"><?= toLangNum(count($articles)) ?></span>
                </h5>
            </div>
            <div class="card-body p-3">
                <!-- Filter bar -->
                <form method="GET" class="row g-2 mb-3">
                    <div class="col-md-5">
                        <select name="topic_id" class="form-select form-select-sm" onchange="this.form.submit()">
                            <option value="0"><?= __('সকল অধ্যায়', 'All Topics') ?></option>
                            <?php foreach ($topicList as $tid => $tInfo): ?>
                                <option value="<?= $tid ?>" <?= $filterTopic == $tid ? 'selected' : '' ?>>
                                    <?= isEn() ? $tInfo['en'] : $tInfo['bn'] ?>
                                </option>
                            <?php endforeach; ?>
                        </select>
                    </div>
                    <div class="col-md-5">
                        <input type="text" name="search" class="form-control form-control-sm" placeholder="<?= __('অনুসন্ধান করুন...', 'Search articles...') ?>" value="<?= htmlspecialchars($search) ?>">
                    </div>
                    <div class="col-md-2 d-grid">
                        <button type="submit" class="btn btn-sm btn-outline-primary"><?= __('ফিল্টার', 'Filter') ?></button>
                    </div>
                </form>

                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 50px;"><?= __('ক্রম', 'Order') ?></th>
                                <th><?= __('বিষয় ও শিরোনাম', 'Topic & Title') ?></th>
                                <th style="width: 80px;"><?= __('স্ট্যাটাস', 'Status') ?></th>
                                <th style="width: 120px;" class="text-end"><?= __('অ্যাকশন', 'Action') ?></th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php if (empty($articles)): ?>
                                <tr>
                                    <td colspan="4" class="text-center py-4 text-muted"><?= __('কোনো মাসাইল পাওয়া যায়নি।', 'No Masail articles found.') ?></td>
                                </tr>
                            <?php else: ?>
                                <?php foreach ($articles as $art): ?>
                                    <tr>
                                        <td><span class="badge bg-secondary"><?= toLangNum($art['display_order']) ?></span></td>
                                        <td>
                                            <?php
                                            $topicDisplay = htmlspecialchars($art['topic_title']);
                                            if (isset($topicList[$art['topic_id']])) {
                                                $topicDisplay = isEn() ? $topicList[$art['topic_id']]['en'] : $topicList[$art['topic_id']]['bn'];
                                            }
                                            ?>
                                            <span class="badge bg-info text-dark mb-1"><?= $topicDisplay ?></span><br>
                                            <strong><?= htmlspecialchars(isEn() && !empty($art['title_en']) ? $art['title_en'] : $art['title_bn']) ?></strong>
                                            <?php if (isEn() && !empty($art['title_bn'])): ?>
                                                <br><small class="text-muted"><?= htmlspecialchars($art['title_bn']) ?></small>
                                            <?php elseif (!isEn() && !empty($art['title_en'])): ?>
                                                <br><small class="text-muted"><?= htmlspecialchars($art['title_en']) ?></small>
                                            <?php endif; ?>
                                        </td>
                                        <td>
                                            <form method="POST" action="masail_articles.php" class="d-inline">
                                                <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                                                <input type="hidden" name="action" value="toggle_status">
                                                <input type="hidden" name="id" value="<?= $art['id'] ?>">
                                                <input type="hidden" name="status" value="<?= $art['is_active'] ? 0 : 1 ?>">
                                                <button type="submit" class="badge bg-<?= $art['is_active'] ? 'success' : 'danger' ?> border-0" style="cursor: pointer;">
                                                    <?= $art['is_active'] ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive') ?>
                                                </button>
                                            </form>
                                        </td>
                                        <td class="text-end">
                                            <a href="masail_articles.php?edit=<?= $art['id'] ?>" class="btn btn-sm btn-outline-primary me-1" title="<?= __('সম্পাদনা', 'Edit') ?>">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="masail_articles.php?delete=<?= $art['id'] ?>" class="btn btn-sm btn-outline-danger" onclick="return confirm('<?= __('আপনি কি নিশ্চিত যে এই মাসাইলটি মুছে ফেলতে চান?', 'Are you sure you want to delete this article?') ?>');" title="<?= __('মুছে ফেলুন', 'Delete') ?>">
                                                <i class="fas fa-trash-alt"></i>
                                            </a>
                                        </td>
                                    </tr>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
