<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - HAJJ JOURNEY (পবিত্র হজ যাত্রা পরিচালনা)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Dynamic Base URL
$protocol = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on') ? 'https' : 'http';
$host = $_SERVER['HTTP_HOST'] ?? '127.0.0.1:8000';
$scriptDir = dirname(dirname($_SERVER['SCRIPT_NAME'] ?? ''));
$scriptDir = str_replace('\\', '/', $scriptDir);
if ($scriptDir === '/' || $scriptDir === '.' || strpos($scriptDir, ':') !== false) {
    $scriptDir = '';
}
$baseUrl = $protocol . '://' . $host . ($scriptDir !== '' ? ('/' . ltrim($scriptDir, '/')) : '');

// Handle Action (Add, Edit, Delete, Toggle)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর।');
        redirect('hajj_journey.php');
    }

    if ($action === 'create' || $action === 'update') {
        $stageNumber = max(1, (int)($_POST['stage_number'] ?? 1));
        $stageNumberBn = trim($_POST['stage_number_bn'] ?? '');
        $stageNumberEn = trim($_POST['stage_number_en'] ?? '');
        $titleBn = trim($_POST['title_bn'] ?? '');
        $titleEn = trim($_POST['title_en'] ?? '');
        $descriptionBn = trim($_POST['description_bn'] ?? '');
        $descriptionEn = trim($_POST['description_en'] ?? '');
        $imageUrl = trim($_POST['image_url'] ?? '');
        $starColorHex = trim($_POST['star_color_hex'] ?? '#2FB68E');
        $detailsBn = trim($_POST['details_bn'] ?? '');
        $detailsEn = trim($_POST['details_en'] ?? '');
        $duaArabic = trim($_POST['dua_arabic'] ?? '');
        $duaPronunciationBn = trim($_POST['dua_pronunciation_bn'] ?? '');
        $duaPronunciationEn = trim($_POST['dua_pronunciation_en'] ?? '');
        $duaMeaningBn = trim($_POST['dua_meaning_bn'] ?? '');
        $duaMeaningEn = trim($_POST['dua_meaning_en'] ?? '');
        $reference = trim($_POST['reference'] ?? '');
        $isActive = isset($_POST['is_active']) ? 1 : 0;
        $displayOrder = (int)($_POST['display_order'] ?? $stageNumber);

        // Handle Image Upload if provided
        if (isset($_FILES['stage_image']) && $_FILES['stage_image']['error'] === UPLOAD_ERR_OK) {
            $file = $_FILES['stage_image'];
            $ext = strtolower(pathinfo($file['name'], PATHINFO_EXTENSION));
            if (in_array($ext, ['png', 'jpg', 'jpeg', 'webp'])) {
                $uploadDir = __DIR__ . '/../uploads/hajj_journey/';
                if (!is_dir($uploadDir)) {
                    mkdir($uploadDir, 0777, true);
                }
                $newFileName = 'img_hajj_journey_stage_' . $stageNumber . '.' . $ext;
                $targetPath = $uploadDir . $newFileName;
                if (move_uploaded_file($file['tmp_name'], $targetPath)) {
                    $imageUrl = 'uploads/hajj_journey/' . $newFileName;
                }
            }
        }

        if (empty($titleBn) || empty($descriptionBn)) {
            setFlash('danger', 'শিরোনাম ও বিবরণ উভয়ই আবশ্যক।');
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO hajj_journey_stages 
                        (stage_number, stage_number_bn, stage_number_en, title_bn, title_en, description_bn, description_en, image_url, star_color_hex, details_bn, details_en, dua_arabic, dua_pronunciation_bn, dua_pronunciation_en, dua_meaning_bn, dua_meaning_en, reference, is_active, display_order)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([
                        $stageNumber, $stageNumberBn, $stageNumberEn, $titleBn, $titleEn, $descriptionBn, $descriptionEn, $imageUrl, $starColorHex, $detailsBn, $detailsEn, $duaArabic, $duaPronunciationBn, $duaPronunciationEn, $duaMeaningBn, $duaMeaningEn, $reference, $isActive, $displayOrder
                    ]);
                    logAdminAction($pdo, 'CREATE_HAJJ_STAGE', 'hajj_journey_stages', (string)$pdo->lastInsertId(), "ধাপ #$stageNumber ($titleBn) তৈরি");
                    setFlash('success', 'নতুন হজ ধাপ সফলভাবে যুক্ত হয়েছে!');
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE hajj_journey_stages SET 
                        stage_number = ?, stage_number_bn = ?, stage_number_en = ?, title_bn = ?, title_en = ?, description_bn = ?, description_en = ?, image_url = ?, star_color_hex = ?, details_bn = ?, details_en = ?, dua_arabic = ?, dua_pronunciation_bn = ?, dua_pronunciation_en = ?, dua_meaning_bn = ?, dua_meaning_en = ?, reference = ?, is_active = ?, display_order = ?
                        WHERE id = ?");
                    $stmt->execute([
                        $stageNumber, $stageNumberBn, $stageNumberEn, $titleBn, $titleEn, $descriptionBn, $descriptionEn, $imageUrl, $starColorHex, $detailsBn, $detailsEn, $duaArabic, $duaPronunciationBn, $duaPronunciationEn, $duaMeaningBn, $duaMeaningEn, $reference, $isActive, $displayOrder, $id
                    ]);
                    logAdminAction($pdo, 'UPDATE_HAJJ_STAGE', 'hajj_journey_stages', (string)$id, "ধাপ #$stageNumber ($titleBn) আপডেট");
                    setFlash('success', 'হজ ধাপ সফলভাবে আপডেট করা হয়েছে!');
                }
            } catch (Exception $e) {
                setFlash('danger', 'অপারেশন ব্যর্থ: ' . $e->getMessage());
            }
        }
    }
    redirect('hajj_journey.php');
}

if (isset($_GET['toggle_status'])) {
    $id = (int)$_GET['toggle_status'];
    try {
        $stmt = $pdo->prepare("UPDATE hajj_journey_stages SET is_active = IF(is_active=1, 0, 1) WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'TOGGLE_HAJJ_STAGE', 'hajj_journey_stages', (string)$id, 'হজ ধাপের সক্রিয়তা পরিবর্তন');
        setFlash('success', 'হজ ধাপের সক্রিয়তা স্ট্যাটাস পরিবর্তিত হয়েছে।');
    } catch (Exception $e) {
        setFlash('danger', 'স্ট্যাটাস পরিবর্তন ব্যর্থ: ' . $e->getMessage());
    }
    redirect('hajj_journey.php');
}

$stages = $pdo->query("SELECT * FROM hajj_journey_stages ORDER BY display_order ASC, stage_number ASC")->fetchAll(PDO::FETCH_ASSOC);

$pageTitle = __('হজ যাত্রা পরিচালনা', 'Hajj Journey Management');
$activeNav = 'hajj_journey';
require_once __DIR__ . '/header.php';
?>

<div class="content-header d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="page-title"><i class="fa-solid fa-kaaba me-2 text-teal" style="color: #1E8787;"></i> <?= __('পবিত্র হজ যাত্রা', 'Hajj Journey Guide') ?></h1>
    </div>
    <div class="d-flex align-items-center gap-2">
        <span class="badge bg-teal py-2 px-3 fs-6" style="background-color: #1E8787 !important; color: white;">
            <?= __('মোট ধাপ', 'Total Stages') ?>: <?= toLangNum(count($stages)) ?>
        </span>
        <button type="button" class="btn btn-primary" onclick="openAddStageModal()">
            <i class="fa-solid fa-plus me-1"></i> <?= __('নতুন ধাপ', 'New Stage') ?>
        </button>
    </div>
</div>

<!-- Stages Table Card -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
        <h6 class="m-0 font-weight-bold text-primary"><?= __('হজের ১২টি প্রধান ধাপসমূহ', 'The 12 Sacred Hajj Stages') ?></h6>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th style="width: 70px;"><?= __('ধাপ', 'Stage') ?></th>
                        <th style="width: 90px;"><?= __('ছবি', 'Image') ?></th>
                        <th><?= __('শিরোনাম ও বিবরণ', 'Title & Description') ?></th>
                        <th><?= __('দোয়া ও আরবি', 'Dua & Arabic') ?></th>
                        <th style="width: 110px;"><?= __('কালার', 'Color') ?></th>
                        <th style="width: 100px;"><?= __('স্ট্যাটাস', 'Status') ?></th>
                        <th class="text-end" style="width: 100px;"><?= __('অ্যাকশন', 'Action') ?></th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($stages)): ?>
                        <tr><td colspan="7" class="text-center py-4 text-muted"><?= __('কোনো ধাপ পাওয়া যায়নি।', 'No stages found.') ?></td></tr>
                    <?php else: foreach ($stages as $s): 
                        $img = $s['image_url'];
                        $fullImg = (!empty($img) && !preg_match('/^https?:\/\//i', $img)) ? ($baseUrl . '/' . ltrim($img, '/')) : $img;
                    ?>
                        <tr>
                            <td>
                                <span class="badge" style="background-color: <?= htmlspecialchars($s['star_color_hex']) ?>; color: #1e293b; font-weight: bold; font-size: 0.95rem;">
                                    <?= toLangNum($s['stage_number']) ?>
                                </span>
                            </td>
                            <td>
                                <?php if (!empty($img)): ?>
                                    <img src="<?= htmlspecialchars($fullImg) ?>" alt="<?= htmlspecialchars($s['title_bn']) ?>" class="rounded shadow-sm" style="width: 60px; height: 60px; object-fit: contain; background: #f8fafc; border: 1px solid #e2e8f0; padding: 2px;">
                                <?php else: ?>
                                    <div class="rounded d-flex align-items-center justify-content-center bg-light text-muted" style="width: 60px; height: 60px;">-</div>
                                <?php endif; ?>
                            </td>
                            <td>
                                <div class="fw-bold text-dark fs-6"><?= htmlspecialchars($s['title_bn']) ?> <span class="text-muted fw-normal">(<?= htmlspecialchars($s['title_en']) ?>)</span></div>
                                <small class="text-muted d-block mt-1" style="max-width: 380px;"><?= htmlspecialchars(mb_substr($s['description_bn'], 0, 110)) ?>...</small>
                            </td>
                            <td>
                                <?php if (!empty($s['dua_arabic'])): ?>
                                    <div class="font-arabic" style="font-size: 1.05rem; direction: rtl; max-width: 280px;"><?= htmlspecialchars(mb_substr($s['dua_arabic'], 0, 60)) ?>...</div>
                                    <small class="text-muted d-block"><?= htmlspecialchars($s['reference'] ?? '') ?></small>
                                <?php else: ?>
                                    <span class="text-muted">-</span>
                                <?php endif; ?>
                            </td>
                            <td>
                                <div class="d-flex align-items-center gap-2">
                                    <span style="display:inline-block; width:22px; height:22px; border-radius:4px; background:<?= htmlspecialchars($s['star_color_hex']) ?>; border:1px solid #cbd5e1;"></span>
                                    <code style="font-size: 0.8rem;"><?= htmlspecialchars($s['star_color_hex']) ?></code>
                                </div>
                            </td>
                            <td>
                                <a href="hajj_journey.php?toggle_status=<?= $s['id'] ?>" class="badge bg-<?= $s['is_active'] ? 'success' : 'secondary' ?>" style="text-decoration: none;" title="<?= __('স্ট্যাটাস পরিবর্তন', 'Toggle status') ?>">
                                    <?= $s['is_active'] ? __('✓ সক্রিয়', '✓ Active') : __('✕ নিষ্ক্রিয়', '✕ Inactive') ?>
                                </a>
                            </td>
                            <td class="text-end">
                                <button type="button" class="btn btn-sm btn-outline-primary" onclick='openEditStageModal(<?= json_encode($s, JSON_HEX_APOS | JSON_HEX_QUOT | JSON_UNESCAPED_UNICODE) ?>)' title="<?= __('সম্পাদনা', 'Edit') ?>">
                                    <i class="fa-solid fa-pen-to-square"></i>
                                </button>
                            </td>
                        </tr>
                    <?php endforeach; endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Modal: Add / Edit Stage -->
<div class="modal fade" id="stageModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <form method="POST" enctype="multipart/form-data" class="modal-content">
            <input type="hidden" name="csrf_token" value="<?= generateCsrfToken() ?>">
            <input type="hidden" name="action" id="modalAction" value="create">
            <input type="hidden" name="id" id="modalId" value="">

            <div class="modal-header">
                <h5 class="modal-title" id="stageModalTitle"><?= __('হজ ধাপ যোগ / সম্পাদনা', 'Add / Edit Hajj Stage') ?></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="row g-3">
                    <div class="col-md-3">
                        <label class="form-label"><?= __('ধাপ নং', 'Stage Number') ?> *</label>
                        <input type="number" name="stage_number" id="stageNumber" class="form-control" required min="1" max="50">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label"><?= __('ধাপ নং (বাংলা)', 'Stage Num (BN)') ?> *</label>
                        <input type="text" name="stage_number_bn" id="stageNumberBn" class="form-control" required placeholder="০১">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label"><?= __('ধাপ নং (English)', 'Stage Num (EN)') ?> *</label>
                        <input type="text" name="stage_number_en" id="stageNumberEn" class="form-control" required placeholder="01">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label"><?= __('স্টার কালার (HEX)', 'Star Color HEX') ?></label>
                        <input type="text" name="star_color_hex" id="starColorHex" class="form-control" value="#2FB68E" placeholder="#2FB68E">
                    </div>

                    <div class="col-md-6">
                        <label class="form-label"><?= __('শিরোনাম (বাংলা)', 'Title (BN)') ?> *</label>
                        <input type="text" name="title_bn" id="titleBn" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><?= __('শিরোনাম (English)', 'Title (EN)') ?> *</label>
                        <input type="text" name="title_en" id="titleEn" class="form-control" required>
                    </div>

                    <div class="col-12">
                        <label class="form-label"><?= __('সংক্ষিপ্ত বিবরণ (বাংলা)', 'Short Description (BN)') ?> *</label>
                        <textarea name="description_bn" id="descriptionBn" class="form-control" rows="2" required></textarea>
                    </div>
                    <div class="col-12">
                        <label class="form-label"><?= __('সংক্ষিপ্ত বিবরণ (English)', 'Short Description (EN)') ?> *</label>
                        <textarea name="description_en" id="descriptionEn" class="form-control" rows="2" required></textarea>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label"><?= __('বর্তমান ইমেজ পাথ (URL)', 'Image URL / Path') ?></label>
                        <input type="text" name="image_url" id="imageUrl" class="form-control" placeholder="uploads/hajj_journey/img_hajj_journey_stage_1.png">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><?= __('নতুন ছবি আপলোড (PNG/JPG)', 'Upload New Image') ?></label>
                        <input type="file" name="stage_image" class="form-control" accept="image/*">
                    </div>

                    <div class="col-12">
                        <label class="form-label"><?= __('বিস্তারিত আহকাম ও নিয়মাবলী (বাংলা)', 'Detailed Rituals (BN)') ?></label>
                        <textarea name="details_bn" id="detailsBn" class="form-control" rows="3"></textarea>
                    </div>
                    <div class="col-12">
                        <label class="form-label"><?= __('বিস্তারিত আহকাম ও নিয়মাবলী (English)', 'Detailed Rituals (EN)') ?></label>
                        <textarea name="details_en" id="detailsEn" class="form-control" rows="3"></textarea>
                    </div>

                    <div class="col-12">
                        <label class="form-label"><?= __('দোয়া (আরবি)', 'Dua (Arabic)') ?></label>
                        <textarea name="dua_arabic" id="duaArabic" class="form-control font-arabic" rows="2" dir="rtl"></textarea>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><?= __('উচ্চারণ (বাংলা)', 'Pronunciation (BN)') ?></label>
                        <input type="text" name="dua_pronunciation_bn" id="duaPronunciationBn" class="form-control">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><?= __('উচ্চারণ (English)', 'Pronunciation (EN)') ?></label>
                        <input type="text" name="dua_pronunciation_en" id="duaPronunciationEn" class="form-control">
                    </div>

                    <div class="col-md-6">
                        <label class="form-label"><?= __('অর্থ (বাংলা)', 'Meaning (BN)') ?></label>
                        <textarea name="dua_meaning_bn" id="duaMeaningBn" class="form-control" rows="2"></textarea>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><?= __('অর্থ (English)', 'Meaning (EN)') ?></label>
                        <textarea name="dua_meaning_en" id="duaMeaningEn" class="form-control" rows="2"></textarea>
                    </div>

                    <div class="col-md-8">
                        <label class="form-label"><?= __('রেফারেন্স / হাদিস সূত্র', 'Reference') ?></label>
                        <input type="text" name="reference" id="reference" class="form-control" placeholder="সহীহ বুখারী: ১৫৪৯">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label"><?= __('ডিসপ্লে অর্ডার', 'Display Order') ?></label>
                        <input type="number" name="display_order" id="displayOrder" class="form-control" value="0">
                    </div>

                    <div class="col-12">
                        <div class="form-check form-switch mt-2">
                            <input class="form-check-input" type="checkbox" name="is_active" id="isActive" value="1" checked>
                            <label class="form-check-label" for="isActive"><?= __('ধাপটি অ্যাপে সক্রিয় থাকবে', 'Active in DeenOne App') ?></label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><?= __('বাতিল', 'Cancel') ?></button>
                <button type="submit" class="btn btn-primary"><?= __('সংরক্ষণ করুন', 'Save Stage') ?></button>
            </div>
        </form>
    </div>
</div>

<script>
function openAddStageModal() {
    document.getElementById('modalAction').value = 'create';
    document.getElementById('modalId').value = '';
    document.getElementById('stageModalTitle').innerText = '<?= __('নতুন হজ ধাপ যোগ করুন', 'Add New Hajj Stage') ?>';
    document.getElementById('stageNumber').value = '<?= count($stages) + 1 ?>';
    document.getElementById('stageNumberBn').value = '';
    document.getElementById('stageNumberEn').value = '';
    document.getElementById('titleBn').value = '';
    document.getElementById('titleEn').value = '';
    document.getElementById('descriptionBn').value = '';
    document.getElementById('descriptionEn').value = '';
    document.getElementById('imageUrl').value = 'uploads/hajj_journey/img_hajj_journey_stage_<?= count($stages) + 1 ?>.png';
    document.getElementById('starColorHex').value = '#2FB68E';
    document.getElementById('detailsBn').value = '';
    document.getElementById('detailsEn').value = '';
    document.getElementById('duaArabic').value = '';
    document.getElementById('duaPronunciationBn').value = '';
    document.getElementById('duaPronunciationEn').value = '';
    document.getElementById('duaMeaningBn').value = '';
    document.getElementById('duaMeaningEn').value = '';
    document.getElementById('reference').value = '';
    document.getElementById('displayOrder').value = '<?= count($stages) + 1 ?>';
    document.getElementById('isActive').checked = true;
    new bootstrap.Modal(document.getElementById('stageModal')).show();
}

function openEditStageModal(data) {
    document.getElementById('modalAction').value = 'update';
    document.getElementById('modalId').value = data.id || '';
    document.getElementById('stageModalTitle').innerText = '<?= __('হজ ধাপ সম্পাদনা', 'Edit Hajj Stage') ?> - ' + (data.title_bn || '');
    document.getElementById('stageNumber').value = data.stage_number || 1;
    document.getElementById('stageNumberBn').value = data.stage_number_bn || '';
    document.getElementById('stageNumberEn').value = data.stage_number_en || '';
    document.getElementById('titleBn').value = data.title_bn || '';
    document.getElementById('titleEn').value = data.title_en || '';
    document.getElementById('descriptionBn').value = data.description_bn || '';
    document.getElementById('descriptionEn').value = data.description_en || '';
    document.getElementById('imageUrl').value = data.image_url || '';
    document.getElementById('starColorHex').value = data.star_color_hex || '#2FB68E';
    document.getElementById('detailsBn').value = data.details_bn || '';
    document.getElementById('detailsEn').value = data.details_en || '';
    document.getElementById('duaArabic').value = data.dua_arabic || '';
    document.getElementById('duaPronunciationBn').value = data.dua_pronunciation_bn || '';
    document.getElementById('duaPronunciationEn').value = data.dua_pronunciation_en || '';
    document.getElementById('duaMeaningBn').value = data.dua_meaning_bn || '';
    document.getElementById('duaMeaningEn').value = data.dua_meaning_en || '';
    document.getElementById('reference').value = data.reference || '';
    document.getElementById('displayOrder').value = data.display_order || data.stage_number || 0;
    document.getElementById('isActive').checked = (data.is_active == 1);
    new bootstrap.Modal(document.getElementById('stageModal')).show();
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
