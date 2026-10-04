<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - QURAN JOURNEY (কুরআন যাত্রা পরিচালনা)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Handle Action (Add, Edit, Delete, Toggle)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর।');
        redirect('quran_journey.php');
    }

    if ($action === 'create' || $action === 'update') {
        $dayNumber = max(1, (int)($_POST['day_number'] ?? 1));
        $surahNumber = max(1, (int)($_POST['surah_number'] ?? 1));
        $ayahNumber = max(1, (int)($_POST['ayah_number'] ?? 1));
        $surahNameBn = trim($_POST['surah_name_bn'] ?? '');
        $arabicAyah = trim($_POST['arabic_ayah'] ?? '');
        $meaningBn = trim($_POST['meaning_bn'] ?? '');
        $audioUrl = trim($_POST['audio_url'] ?? '');
        $quizChallenge = trim($_POST['quiz_challenge'] ?? '');
        $correctOption = trim($_POST['correct_option'] ?? '');
        $lessonBn = trim($_POST['lesson_bn'] ?? '');
        $amolBn = trim($_POST['amol_bn'] ?? '');
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($arabicAyah) || empty($meaningBn)) {
            setFlash('danger', 'আরবি আয়াত ও বাংলা অর্থ উভয়ই আবশ্যক।');
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO quran_journey_lessons 
                        (day_number, surah_number, ayah_number, surah_name_bn, arabic_ayah, meaning_bn, audio_url, quiz_challenge, correct_option, lesson_bn, amol_bn, is_active)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$dayNumber, $surahNumber, $ayahNumber, $surahNameBn, $arabicAyah, $meaningBn, $audioUrl, $quizChallenge, $correctOption, $lessonBn, $amolBn, $isActive]);
                    logAdminAction($pdo, 'CREATE_QURAN_LESSON', 'quran_journey_lessons', (string)$pdo->lastInsertId(), "দিন #$dayNumber কুরআন পাঠ তৈরি");
                    setFlash('success', 'নতুন কুরআন পাঠ সফলভাবে যোগ হয়েছে!');
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE quran_journey_lessons SET 
                        day_number = ?, surah_number = ?, ayah_number = ?, surah_name_bn = ?, arabic_ayah = ?, meaning_bn = ?, audio_url = ?, quiz_challenge = ?, correct_option = ?, lesson_bn = ?, amol_bn = ?, is_active = ?
                        WHERE id = ?");
                    $stmt->execute([$dayNumber, $surahNumber, $ayahNumber, $surahNameBn, $arabicAyah, $meaningBn, $audioUrl, $quizChallenge, $correctOption, $lessonBn, $amolBn, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_QURAN_LESSON', 'quran_journey_lessons', (string)$id, "দিন #$dayNumber কুরআন পাঠ আপডেট");
                    setFlash('success', 'কুরআন পাঠ সফলভাবে আপডেট করা হয়েছে!');
                }
            } catch (Exception $e) {
                setFlash('danger', 'অপারেশন ব্যর্থ: ' . $e->getMessage());
            }
        }
    }
    redirect('quran_journey.php');
}

if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM quran_journey_lessons WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_QURAN_LESSON', 'quran_journey_lessons', (string)$id, 'কুরআন পাঠ মুছে ফেলা হয়েছে');
        setFlash('success', 'পাঠ মুছে ফেলা হয়েছে।');
    } catch (Exception $e) {
        setFlash('danger', 'মুছতে ব্যর্থ: ' . $e->getMessage());
    }
    redirect('quran_journey.php');
}

if (isset($_GET['toggle_status'])) {
    $id = (int)$_GET['toggle_status'];
    try {
        $stmt = $pdo->prepare("UPDATE quran_journey_lessons SET is_active = IF(is_active=1, 0, 1) WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'TOGGLE_QURAN_LESSON', 'quran_journey_lessons', (string)$id, 'কুরআন পাঠ সক্রিয়তা পরিবর্তন');
        setFlash('success', 'কুরআন পাঠের সক্রিয়তা স্ট্যাটাস পরিবর্তিত হয়েছে।');
    } catch (Exception $e) {
        setFlash('danger', 'স্ট্যাটাস পরিবর্তন ব্যর্থ: ' . $e->getMessage());
    }
    redirect('quran_journey.php');
}

$lessons = $pdo->query("SELECT * FROM quran_journey_lessons ORDER BY day_number ASC")->fetchAll(PDO::FETCH_ASSOC);
$userCount = 0;
try {
    $userCount = (int)$pdo->query("SELECT COUNT(DISTINCT user_id) FROM user_quran_journey_records")->fetchColumn();
} catch (Exception $e) {}
$totalLearners = max(1, $userCount);

$pageTitle = __('কুরআন যাত্রা', 'Quran Journey');
$activeNav = 'quran_journey';
require_once __DIR__ . '/header.php';
?>

<div class="content-header d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="page-title"><i class="fa-solid fa-book-quran me-2 text-success"></i> <?= __('কুরআন যাত্রা', 'Quran Journey') ?></h1>
    </div>
    <div class="d-flex align-items-center gap-2">
        <span class="badge bg-success py-2 px-3 fs-6"><?= __('শিক্ষার্থী', 'Learners') ?>: <?= toLangNum($totalLearners) ?></span>
        <button type="button" class="btn btn-primary" onclick="openAddLessonModal()">
            <i class="fa-solid fa-plus me-1"></i> <?= __('নতুন পাঠ', 'New Lesson') ?>
        </button>
    </div>
</div>

<!-- Lessons Table Card -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
        <h6 class="m-0 font-weight-bold text-primary"><?= __('পাঠ তালিকা', 'Lessons') ?> <span class="badge bg-secondary ms-2"><?= toLangNum(count($lessons)) ?></span></h6>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th><?= __('দিন', 'Day') ?></th>
                        <th><?= __('সূরা ও আয়াত', 'Surah & Ayah') ?></th>
                        <th><?= __('আরবি আয়াত', 'Arabic Ayah') ?></th>
                        <th><?= __('অর্থ', 'Meaning') ?></th>
                        <th><?= __('আমল', 'Lesson') ?></th>
                        <th><?= __('স্ট্যাটাস', 'Status') ?></th>
                        <th class="text-end"><?= __('অ্যাকশন', 'Action') ?></th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (empty($lessons)): ?>
                        <tr><td colspan="7" class="text-center py-4 text-muted"><?= __('কোনো পাঠ পাওয়া যায়নি।', 'No lessons found.') ?></td></tr>
                    <?php else: foreach ($lessons as $l): ?>
                        <tr>
                            <td><span class="badge bg-info"><?= toLangNum($l['day_number']) ?> <?= __('দিন', 'Day') ?></span></td>
                            <td><strong><?= htmlspecialchars($l['surah_name_bn']) ?></strong> (<?= toLangNum($l['surah_number']) ?>:<?= toLangNum($l['ayah_number']) ?>)</td>
                            <td class="font-arabic" style="font-size: 1.15rem; max-width: 260px;"><?= htmlspecialchars(mb_substr($l['arabic_ayah'], 0, 70)) . (mb_strlen($l['arabic_ayah']) > 70 ? '...' : '') ?></td>
                            <td style="max-width: 240px;"><?= htmlspecialchars(mb_substr($l['meaning_bn'], 0, 80)) . (mb_strlen($l['meaning_bn']) > 80 ? '...' : '') ?></td>
                            <td>
                                <small class="text-muted"><?= htmlspecialchars(mb_substr($l['lesson_bn'] ?? '', 0, 50)) ?>...</small>
                            </td>
                            <td>
                                <a href="quran_journey.php?toggle_status=<?= $l['id'] ?>" class="badge bg-<?= $l['is_active'] ? 'success' : 'secondary' ?>" style="text-decoration: none; cursor: pointer;" title="<?= __('স্ট্যাটাস পরিবর্তন', 'Toggle status') ?>">
                                    <?= $l['is_active'] ? __('✓ সক্রিয়', '✓ Active') : __('✕ নিষ্ক্রিয়', '✕ Inactive') ?>
                                </a>
                            </td>
                            <td class="text-end" style="white-space: nowrap;">
                                <button type="button" class="btn btn-sm btn-outline-primary me-1" onclick='openEditLessonModal(<?= json_encode($l, JSON_HEX_APOS | JSON_HEX_QUOT) ?>)' title="<?= __('সম্পাদনা', 'Edit') ?>">
                                    <i class="fa-solid fa-pen-to-square"></i>
                                </button>
                                <a href="quran_journey.php?delete=<?= $l['id'] ?>" class="btn btn-sm btn-outline-danger" onclick="return confirmAction(event, this.href, '<?= __('আপনি কি নিশ্চিত যে এই পাঠটি মুছে ফেলতে চান?', 'Are you sure you want to delete this lesson?') ?>');" title="<?= __('মুছে ফেলুন', 'Delete') ?>">
                                    <i class="fa-solid fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    <?php endforeach; endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Add / Edit Lesson Modal -->
<div class="modal fade" id="lessonModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="lessonModalTitle"><?= __('নতুন পাঠ', 'New Lesson') ?></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form method="POST" action="quran_journey.php" id="lessonForm">
                <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                <input type="hidden" name="action" id="formAction" value="create">
                <input type="hidden" name="id" id="lessonId" value="">

                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label fw-bold"><?= __('দিন', 'Day') ?> *</label>
                            <input type="number" name="day_number" id="inpDayNumber" class="form-control" min="1" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold"><?= __('সূরা নম্বর', 'Surah No') ?> *</label>
                            <input type="number" name="surah_number" id="inpSurahNumber" class="form-control" min="1" max="114" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold"><?= __('আয়াত নম্বর', 'Ayah No') ?> *</label>
                            <input type="number" name="ayah_number" id="inpAyahNumber" class="form-control" min="1" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold"><?= __('সূরা নাম', 'Surah Name') ?> *</label>
                            <input type="text" name="surah_name_bn" id="inpSurahName" class="form-control" placeholder="যেমন: আল-ফাতিহা" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label"><?= __('অডিও লিংক', 'Audio URL') ?></label>
                            <input type="url" name="audio_url" id="inpAudioUrl" class="form-control" placeholder="https://cdn.islamicapi.org/recitation.mp3">
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold"><?= __('আরবি আয়াত', 'Arabic Ayah') ?> *</label>
                            <textarea name="arabic_ayah" id="inpArabicAyah" class="form-control font-arabic" rows="3" required placeholder="بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"></textarea>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold"><?= __('বাংলা অর্থ', 'Bengali Meaning') ?> *</label>
                            <textarea name="meaning_bn" id="inpMeaningBn" class="form-control" rows="3" required placeholder="পরম করুণাময় অতি দয়ালু আল্লাহর নামে শুরু..."></textarea>
                        </div>
                        <div class="col-12">
                            <label class="form-label"><?= __('শিক্ষা', 'Lesson') ?></label>
                            <textarea name="lesson_bn" id="inpLessonBn" class="form-control" rows="2" placeholder="এই আয়াত থেকে আমাদের মূল শিক্ষা..."></textarea>
                        </div>
                        <div class="col-12">
                            <label class="form-label"><?= __('আমল', 'Action') ?></label>
                            <textarea name="amol_bn" id="inpAmolBn" class="form-control" rows="2" placeholder="আজকের নির্ধারিত আমল..."></textarea>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label"><?= __('কুইজ', 'Quiz') ?></label>
                            <input type="text" name="quiz_challenge" id="inpQuiz" class="form-control" placeholder="আয়াত ভিত্তিক একটি সহজ প্রশ্ন">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label"><?= __('উত্তর', 'Answer') ?></label>
                            <input type="text" name="correct_option" id="inpCorrectOption" class="form-control" placeholder="কুইজের সঠিক উত্তর">
                        </div>
                        <div class="col-12">
                            <div class="form-check form-switch mt-2">
                                <input class="form-check-input" type="checkbox" name="is_active" id="inpIsActive" value="1" checked>
                                <label class="form-check-label fw-bold" for="inpIsActive"><?= __('সক্রিয় রাখুন', 'Keep Active') ?></label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><?= __('বাতিল', 'Cancel') ?></button>
                    <button type="submit" class="btn btn-primary" id="btnLessonSubmit"><?= __('সংরক্ষণ করুন', 'Save') ?></button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
let lessonModalInstance = null;
document.addEventListener('DOMContentLoaded', () => {
    const modalEl = document.getElementById('lessonModal');
    if (modalEl && typeof bootstrap !== 'undefined') {
        lessonModalInstance = new bootstrap.Modal(modalEl);
    }
});

function openAddLessonModal() {
    document.getElementById('lessonModalTitle').textContent = '<?= __('নতুন পাঠ', 'New Lesson') ?>';
    document.getElementById('formAction').value = 'create';
    document.getElementById('lessonId').value = '';
    document.getElementById('lessonForm').reset();
    document.getElementById('inpDayNumber').value = '<?= count($lessons) + 1 ?>';
    document.getElementById('inpIsActive').checked = true;
    document.getElementById('btnLessonSubmit').textContent = '<?= __('সংরক্ষণ করুন', 'Save') ?>';
    if (lessonModalInstance) {
        lessonModalInstance.show();
    } else {
        openModal('lessonModal');
    }
}

function openEditLessonModal(l) {
    document.getElementById('lessonModalTitle').textContent = '<?= __('পাঠ সম্পাদনা', 'Edit Lesson') ?>';
    document.getElementById('formAction').value = 'update';
    document.getElementById('lessonId').value = l.id;
    document.getElementById('inpDayNumber').value = l.day_number || 1;
    document.getElementById('inpSurahNumber').value = l.surah_number || 1;
    document.getElementById('inpAyahNumber').value = l.ayah_number || 1;
    document.getElementById('inpSurahName').value = l.surah_name_bn || '';
    document.getElementById('inpArabicAyah').value = l.arabic_ayah || '';
    document.getElementById('inpMeaningBn').value = l.meaning_bn || '';
    document.getElementById('inpAudioUrl').value = l.audio_url || '';
    document.getElementById('inpLessonBn').value = l.lesson_bn || '';
    document.getElementById('inpAmolBn').value = l.amol_bn || '';
    document.getElementById('inpQuiz').value = l.quiz_challenge || '';
    document.getElementById('inpCorrectOption').value = l.correct_option || '';
    document.getElementById('inpIsActive').checked = (parseInt(l.is_active) === 1);
    document.getElementById('btnLessonSubmit').textContent = '<?= __('সংরক্ষণ করুন', 'Save') ?>';
    if (lessonModalInstance) {
        lessonModalInstance.show();
    } else {
        openModal('lessonModal');
    }
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
