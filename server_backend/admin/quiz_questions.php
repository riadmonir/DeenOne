<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - QUIZ & KNOWLEDGE BATTLE QUESTION BANK (LEVEL 2)
 * Clean, Compact, Interactive Expandable / Flip Accordion Design
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

/**
 * Server-Side Fast Translation from Bengali to English
 */
function autoTranslateServerBnToEn($text) {
    $text = trim($text ?? '');
    if (empty($text)) return '';
    
    // Strategy 1: Google Dict Client API
    try {
        $url = 'https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=bn&tl=en&q=' . urlencode($text);
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_TIMEOUT, 4);
        curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)');
        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        if ($httpCode === 200 && !empty($response)) {
            $json = json_decode($response, true);
            if (is_array($json) && !empty($json[0])) {
                $res = is_array($json[0]) ? implode(' ', $json[0]) : (string)$json[0];
                if (!empty(trim($res))) return trim($res);
            }
        }
    } catch(Throwable $e) {}

    // Strategy 2: MyMemory API Fallback
    try {
        $url = 'https://api.mymemory.translated.net/get?q=' . urlencode($text) . '&langpair=bn|en';
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_TIMEOUT, 4);
        $response = curl_exec($ch);
        curl_close($ch);
        if (!empty($response)) {
            $json = json_decode($response, true);
            if (!empty($json['responseData']['translatedText'])) {
                return trim($json['responseData']['translatedText']);
            }
        }
    } catch(Throwable $e) {}

    return $text;
}

// Handle Action (Add, Edit, Delete)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('quiz_questions.php');
    }

    if ($action === 'create' || $action === 'update') {
        $categoryId = trim($_POST['category_id'] ?? 'general_knowledge');
        $questionBn = trim($_POST['question_bn'] ?? '');
        $opt1 = trim($_POST['option_1'] ?? '');
        $opt2 = trim($_POST['option_2'] ?? '');
        $opt3 = trim($_POST['option_3'] ?? '');
        $opt4 = trim($_POST['option_4'] ?? '');
        $correctOpt = (int)($_POST['correct_option'] ?? 0);
        $explanation = trim($_POST['explanation'] ?? '');
        $reference = trim($_POST['reference'] ?? '');
        $difficulty = 'MEDIUM'; // Universal default
        $id = (int)($_POST['id'] ?? 0);

        if (empty($questionBn) || empty($opt1) || empty($opt2) || empty($opt3) || empty($opt4)) {
            setFlash('danger', __('প্রশ্ন এবং ৪টি অপশন অবশ্যই পূরণ করতে হবে।', 'Question and all 4 options are required.'));
        } else {
            // Duplicate Question Prevention across system
            $dupStmt = $pdo->prepare("SELECT id FROM quiz_questions WHERE TRIM(question_bn) = ? " . ($action === 'update' ? "AND id != ?" : ""));
            if ($action === 'update') {
                $dupStmt->execute([$questionBn, $id]);
            } else {
                $dupStmt->execute([$questionBn]);
            }
            if ($dupStmt->fetch()) {
                setFlash('danger', __('এই প্রশ্নটি ইতিমধ্যে সিস্টেমে বিদ্যমান রয়েছে! একই প্রশ্ন পুনরাবৃত্তি করা যাবে না।', 'This question already exists in the system! Duplicate questions are not allowed.'));
                $retCat = !empty($_POST['category_id']) ? '?category=' . urlencode($_POST['category_id']) : '';
                redirect('quiz_questions.php' . $retCat);
            }

            // Real-time server background translation from Bengali to English
            $questionEn = autoTranslateServerBnToEn($questionBn);
            $opt1En = autoTranslateServerBnToEn($opt1);
            $opt2En = autoTranslateServerBnToEn($opt2);
            $opt3En = autoTranslateServerBnToEn($opt3);
            $opt4En = autoTranslateServerBnToEn($opt4);
            $explanationEn = !empty($explanation) ? autoTranslateServerBnToEn($explanation) : '';
            $referenceEn = !empty($reference) ? autoTranslateServerBnToEn($reference) : '';

            try {
                if ($action === 'create') {
                    $uid = trim($_POST['question_uid'] ?? '');
                    if (empty($uid)) {
                        $uid = 'Q_' . strtoupper(substr(preg_replace('/[^a-zA-Z0-9]/', '', $categoryId), 0, 3)) . '_' . strtoupper(bin2hex(random_bytes(3)));
                    }
                    $stmt = $pdo->prepare("INSERT INTO quiz_questions (question_uid, category_id, question_bn, question_en, option_1, option_1_en, option_2, option_2_en, option_3, option_3_en, option_4, option_4_en, correct_option, explanation, explanation_en, reference, reference_en, difficulty) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$uid, $categoryId, $questionBn, $questionEn, $opt1, $opt1En, $opt2, $opt2En, $opt3, $opt3En, $opt4, $opt4En, $correctOpt, $explanation, $explanationEn, $reference, $referenceEn, $difficulty]);
                    logAdminAction($pdo, 'CREATE_QUESTION', 'quiz_questions', $uid, 'প্রশ্ন তৈরি: ' . mb_substr($questionBn, 0, 30));
                    setFlash('success', __('নতুন ইসলামিক প্রশ্ন সফলভাবে যোগ করা হয়েছে ও ব্যাকএন্ডে স্বয়ংক্রিয়ভাবে অনুবাদ হয়েছে!', 'New Islamic question added and translated successfully!'));
                } else {
                    $stmt = $pdo->prepare("UPDATE quiz_questions SET category_id = ?, question_bn = ?, question_en = ?, option_1 = ?, option_1_en = ?, option_2 = ?, option_2_en = ?, option_3 = ?, option_3_en = ?, option_4 = ?, option_4_en = ?, correct_option = ?, explanation = ?, explanation_en = ?, reference = ?, reference_en = ?, difficulty = ? WHERE id = ?");
                    $stmt->execute([$categoryId, $questionBn, $questionEn, $opt1, $opt1En, $opt2, $opt2En, $opt3, $opt3En, $opt4, $opt4En, $correctOpt, $explanation, $explanationEn, $reference, $referenceEn, $difficulty, $id]);
                    logAdminAction($pdo, 'UPDATE_QUESTION', 'quiz_questions', (string)$id, 'প্রশ্ন আপডেট: ' . mb_substr($questionBn, 0, 30));
                    setFlash('success', __('প্রশ্ন তথ্য সফলভাবে আপডেট করা হয়েছে!', 'Question updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('ডাটাবেস ত্রুটি: ', 'Database error: ') . $e->getMessage());
            }
        }
    }
    $retCat = !empty($_POST['category_id']) ? '?category=' . urlencode($_POST['category_id']) : '';
    redirect('quiz_questions.php' . $retCat);
}

if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM quiz_questions WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_QUESTION', 'quiz_questions', (string)$id, 'প্রশ্ন মুছে ফেলা হয়েছে');
        setFlash('success', __('প্রশ্ন সফলভাবে মুছে ফেলা হয়েছে।', 'Question deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
    }
    $retCat = !empty($_GET['category']) ? '?category=' . urlencode($_GET['category']) : '';
    redirect('quiz_questions.php' . $retCat);
}

// Fetch all categories for modal & dropdown
$categories = [];
$totalGlobalQuestions = 0;
try {
    $cStmt = $pdo->query("SELECT qc.*, COUNT(qq.id) as question_count 
                          FROM quiz_categories qc 
                          LEFT JOIN quiz_questions qq ON qc.category_id = qq.category_id 
                          GROUP BY qc.id 
                          ORDER BY qc.display_order ASC, qc.id ASC");
    $categories = $cStmt->fetchAll();
    foreach ($categories as $cat) {
        $totalGlobalQuestions += (int)$cat['question_count'];
    }
} catch (Exception $e) {}

// Filter & Pagination logic
$selectedCategory = trim($_GET['category'] ?? '');
$search = trim($_GET['search'] ?? '');
$page = max(1, (int)($_GET['page'] ?? 1));
$perPage = 25;
$offset = ($page - 1) * $perPage;

// Resolve Current Category details
$currentCatTitle = '';
if (!empty($selectedCategory)) {
    foreach ($categories as $c) {
        if ($c['category_id'] === $selectedCategory) {
            $currentCatTitle = isEn() ? ($c['title_en'] ?: $c['title_bn']) : $c['title_bn'];
            break;
        }
    }
}

$whereClauses = [];
$params = [];

if (!empty($selectedCategory)) {
    $whereClauses[] = "qq.category_id = ?";
    $params[] = $selectedCategory;
}
if (!empty($search)) {
    $whereClauses[] = "(qq.question_bn LIKE ? OR qq.reference LIKE ? OR qq.explanation LIKE ? OR qq.option_1 LIKE ? OR qq.option_2 LIKE ? OR qq.option_3 LIKE ? OR qq.option_4 LIKE ?)";
    $term = "%$search%";
    for ($i = 0; $i < 7; $i++) {
        $params[] = $term;
    }
}

$whereSql = !empty($whereClauses) ? "WHERE " . implode(" AND ", $whereClauses) : "";

$totalFilteredQuestions = 0;
$questions = [];
try {
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM quiz_questions qq $whereSql");
    $countStmt->execute($params);
    $totalFilteredQuestions = (int)$countStmt->fetchColumn();

    $sql = "SELECT qq.*, qc.title_bn as cat_title, qc.title_en as cat_title_en 
            FROM quiz_questions qq 
            LEFT JOIN quiz_categories qc ON qq.category_id = qc.category_id 
            $whereSql 
            ORDER BY qq.id DESC 
            LIMIT $perPage OFFSET $offset";
    $qStmt = $pdo->prepare($sql);
    $qStmt->execute($params);
    $questions = $qStmt->fetchAll();
} catch (Exception $e) {}

$totalPages = max(1, ceil($totalFilteredQuestions / $perPage));

$pageHeadingText = !empty($currentCatTitle) ? $currentCatTitle : __('কুইজ প্রশ্নমালা', 'Quiz Questions');
$pageTitle = $pageHeadingText;
$activeNav = 'quiz_questions';
require_once __DIR__ . '/header.php';
?>

<style>
/* Flip / Accordion Question Card Styling */
.question-flip-item {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  margin-bottom: 12px;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
  overflow: hidden;
}

.question-flip-item:hover {
  border-color: var(--primary);
  box-shadow: 0 4px 14px rgba(0,0,0,0.04);
}

.question-flip-header {
  padding: 14px 18px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  cursor: pointer;
  user-select: none;
  background: var(--bg-card);
  transition: background 0.15s ease;
}

.question-flip-header:hover {
  background: var(--hover-bg);
}

.question-flip-title {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.question-flip-badge {
  font-size: 11px;
  font-weight: 800;
  padding: 3px 8px;
  border-radius: 6px;
  background: var(--hover-bg);
  border: 1px solid var(--border-color);
  color: var(--text-muted);
  white-space: nowrap;
}

.question-flip-text {
  font-size: 14.5px;
  font-weight: 700;
  color: var(--text-heading);
  line-height: 1.5;
}

.question-flip-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.category-pill-single {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(2, 132, 199, 0.08);
  border: 1px solid rgba(2, 132, 199, 0.2);
  color: #0284c7;
  border-radius: 9999px;
  font-size: 11.5px;
  font-weight: 700;
  white-space: nowrap;
}

.dark .category-pill-single {
  background: rgba(56, 189, 248, 0.12);
  border-color: rgba(56, 189, 248, 0.25);
  color: #38bdf8;
}

.chevron-icon {
  font-size: 12px;
  color: var(--text-muted);
  transition: transform 0.25s ease;
}

.question-flip-item.open .chevron-icon {
  transform: rotate(180deg);
  color: var(--primary);
}

.question-flip-body {
  display: none;
  padding: 16px 18px 18px;
  background: var(--hover-bg);
  border-top: 1px solid var(--border-color);
}

.question-flip-item.open .question-flip-body {
  display: block;
  animation: fadeInDown 0.2s ease-out;
}

@keyframes fadeInDown {
  from { opacity: 0; transform: translateY(-6px); }
  to { opacity: 1; transform: translateY(0); }
}

.options-flip-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 12px;
}

@media (max-width: 640px) {
  .options-flip-grid {
    grid-template-columns: 1fr;
  }
}

.option-flip-box {
  padding: 10px 14px;
  border-radius: 8px;
  font-size: 13px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  color: var(--text-main);
}

.option-flip-box.correct {
  background: rgba(16, 185, 129, 0.08);
  border-color: var(--primary);
  color: var(--text-heading);
  font-weight: 700;
}

.dark .option-flip-box.correct {
  background: rgba(16, 185, 129, 0.15);
}

.option-num-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  font-size: 10.5px;
  font-weight: 800;
  background: var(--hover-bg);
  border: 1px solid var(--border-color);
  color: var(--text-muted);
}

.option-flip-box.correct .option-num-badge {
  background: var(--primary);
  border-color: var(--primary);
  color: #fff;
}
</style>

<!-- Single Clean Page Header -->
<div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px; margin-bottom: 20px;">
  <div>
    <h2 style="font-size: 20px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
      <span style="color: #0284c7; font-weight: 900;">/</span>
      <span><?php echo htmlspecialchars($pageHeadingText); ?></span>
    </h2>
  </div>
  <div>
    <button type="button" class="btn btn-primary btn-sm" onclick="openAddQuestionModal('<?php echo htmlspecialchars($selectedCategory, ENT_QUOTES); ?>')" style="font-weight: 700; padding: 8px 18px;">
      <i class="fa-solid fa-circle-plus"></i>
      <span><?php echo __('নতুন প্রশ্ন', 'New Question'); ?></span>
    </button>
  </div>
</div>

<!-- Search & Category Filter Bar -->
<div class="card mb-5">
  <div class="card-body p-4">
    <form method="GET" action="quiz_questions.php" style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
      <div style="flex: 1; min-width: 220px; position: relative;">
        <i class="fa-solid fa-magnifying-glass" style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); color: var(--text-muted); font-size: 13px;"></i>
        <input type="text" name="search" class="form-control" style="padding-left: 34px; font-size: 13px;" placeholder="<?php echo __('অনুসন্ধান...', 'Search...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
      </div>

      <div style="width: 240px;">
        <select name="category" class="form-control font-semibold" style="font-size: 13px;" onchange="this.form.submit()">
          <option value=""><?php echo __('— সকল ক্যাটাগরি —', '— All Categories —'); ?></option>
          <?php foreach ($categories as $c): ?>
            <option value="<?php echo htmlspecialchars($c['category_id']); ?>" <?php echo $selectedCategory === $c['category_id'] ? 'selected' : ''; ?>>
              <?php echo htmlspecialchars(isEn() ? ($c['title_en'] ?: $c['title_bn']) : $c['title_bn']); ?>
            </option>
          <?php endforeach; ?>
        </select>
      </div>

      <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700;">
        <i class="fa-solid fa-magnifying-glass"></i>
        <span><?php echo __('খুঁজুন', 'Search'); ?></span>
      </button>

      <?php if (!empty($search) || !empty($selectedCategory)): ?>
        <a href="quiz_questions.php" class="btn btn-secondary btn-sm" title="<?php echo __('ফিল্টার রিসেট করুন', 'Reset Filter'); ?>">
          <i class="fa-solid fa-rotate-left"></i>
          <span><?php echo __('রিসেট', 'Reset'); ?></span>
        </a>
      <?php endif; ?>
    </form>
  </div>
</div>

<!-- Expandable / Flip Questions List -->
<div>
  <?php if (empty($questions)): ?>
    <div class="card p-12 text-center text-slate-400">
      <div class="text-4xl mb-3">❓</div>
      <div class="font-bold text-base text-slate-600 dark:text-slate-300"><?php echo __('কোনো প্রশ্ন পাওয়া যায়নি', 'No questions found'); ?></div>
      <p class="text-sm mt-1">
        <?php echo __('নতুন প্রশ্ন যুক্ত করতে উপরের বাটনে ক্লিক করুন।', 'Click the button above to add a new question.'); ?>
      </p>
    </div>
  <?php else: ?>
    <?php foreach ($questions as $key => $q): ?>
      <?php 
        $itemSeqNum = $offset + $key + 1;
        $catDisplay = isEn() ? ($q['cat_title_en'] ?: $q['cat_title'] ?: $q['category_id']) : ($q['cat_title'] ?: $q['category_id']);
        $displayQuestion = (isEn() && !empty($q['question_en'])) ? $q['question_en'] : $q['question_bn'];
        $displayRef = (isEn() && !empty($q['reference_en'])) ? $q['reference_en'] : $q['reference'];
        $displayExp = (isEn() && !empty($q['explanation_en'])) ? $q['explanation_en'] : $q['explanation'];
      ?>
      <div class="question-flip-item" id="qItem_<?php echo $q['id']; ?>">
        <!-- Flip Card Header (Click to toggle) -->
        <div class="question-flip-header" onclick="toggleQuestionFlip(<?php echo $q['id']; ?>)">
          <div class="question-flip-title">
            <span class="question-flip-badge">#<?php echo toLangNum($itemSeqNum); ?></span>
            <span class="question-flip-text"><?php echo htmlspecialchars($displayQuestion); ?></span>
          </div>

          <div class="question-flip-meta" onclick="event.stopPropagation()">
            <span class="category-pill-single">
              <i class="fa-solid fa-layer-group" style="font-size: 10px;"></i>
              <span><?php echo htmlspecialchars($catDisplay); ?></span>
            </span>

            <button type="button" class="btn btn-secondary btn-sm" style="padding: 5px 8px;"
                    data-item="<?php echo htmlspecialchars(json_encode($q, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP), ENT_QUOTES, 'UTF-8'); ?>"
                    onclick="editQuestion(JSON.parse(this.dataset.item))" 
                    title="<?php echo __('সম্পাদনা', 'Edit'); ?>">
              <i class="fa-solid fa-pen-to-square text-blue-500"></i>
            </button>
            <a href="quiz_questions.php?delete=<?php echo $q['id']; ?><?php echo !empty($selectedCategory) ? '&category=' . urlencode($selectedCategory) : ''; ?>" 
               class="btn btn-danger btn-sm" style="padding: 5px 8px;" title="<?php echo __('মুছুন', 'Delete'); ?>" 
               onclick="return confirmAction(event, this.href, '<?php echo __('আপনি কি নিশ্চিতভাবে এই প্রশ্নটি মুছে ফেলতে চান?', 'Are you sure you want to delete this question?'); ?>');">
              <i class="fa-solid fa-trash-can"></i>
            </a>

            <div onclick="toggleQuestionFlip(<?php echo $q['id']; ?>)" style="cursor: pointer; padding: 4px 6px;">
              <i class="fa-solid fa-chevron-down chevron-icon"></i>
            </div>
          </div>
        </div>

        <!-- Flip Card Body (Revealed on Click) -->
        <div class="question-flip-body">
          <div class="options-flip-grid">
            <?php for ($optIdx = 1; $optIdx <= 4; $optIdx++): ?>
              <?php 
                $isCorrect = ((int)$q['correct_option'] === ($optIdx - 1));
                $optText = (isEn() && !empty($q['option_' . $optIdx . '_en'])) ? $q['option_' . $optIdx . '_en'] : ($q['option_' . $optIdx] ?? '');
              ?>
              <div class="option-flip-box <?php echo $isCorrect ? 'correct' : ''; ?>">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <span class="option-num-badge"><?php echo toLangNum($optIdx); ?></span>
                  <span><?php echo htmlspecialchars($optText); ?></span>
                </div>
                <?php if ($isCorrect): ?>
                  <span style="font-size: 11px; font-weight: 800; color: #10b981; display: inline-flex; align-items: center; gap: 4px;">
                    <i class="fa-solid fa-circle-check"></i> <?php echo __('সঠিক উত্তর', 'Correct'); ?>
                  </span>
                <?php endif; ?>
              </div>
            <?php endfor; ?>
          </div>

          <?php if (!empty($displayRef) || !empty($displayExp)): ?>
            <div style="display: flex; flex-direction: column; gap: 6px; padding-top: 8px; border-top: 1px dashed var(--border-color); font-size: 12.5px;">
              <?php if (!empty($displayRef)): ?>
                <div style="color: #d97706; font-weight: 700; display: flex; align-items: center; gap: 6px;">
                  <i class="fa-solid fa-book-open"></i> <span><?php echo htmlspecialchars($displayRef); ?></span>
                </div>
              <?php endif; ?>
              <?php if (!empty($displayExp)): ?>
                <div style="color: var(--text-muted); line-height: 1.5;">
                  💡 <strong><?php echo __('ব্যাখ্যা:', 'Explanation:'); ?></strong> <?php echo htmlspecialchars($displayExp); ?>
                </div>
              <?php endif; ?>
            </div>
          <?php endif; ?>
        </div>
      </div>
    <?php endforeach; ?>
  <?php endif; ?>
</div>

<!-- Pagination Bar -->
<?php if ($totalPages > 1): ?>
  <div class="p-4 border-t border-slate-200 dark:border-slate-800 flex items-center justify-between flex-wrap gap-3 mt-4">
    <span class="text-xs text-slate-500">
      <?php echo __('পৃষ্ঠা ', 'Page ') . toLangNum($page) . __(' এর ', ' of ') . toLangNum($totalPages) . ' (' . __('মোট ', 'Total ') . toLangNum($totalFilteredQuestions) . __('টি প্রশ্ন', ' Questions') . ')'; ?>
    </span>
    <div class="flex items-center gap-1.5">
      <?php if ($page > 1): ?>
        <a href="quiz_questions.php?page=<?php echo $page - 1; ?><?php echo !empty($selectedCategory) ? '&category=' . urlencode($selectedCategory) : ''; ?><?php echo !empty($search) ? '&search=' . urlencode($search) : ''; ?>" 
           class="btn btn-secondary btn-sm">
          <i class="fa-solid fa-chevron-left"></i> <span><?php echo __('পূর্ববর্তী', 'Previous'); ?></span>
        </a>
      <?php endif; ?>

      <?php for ($p = max(1, $page - 2); $p <= min($totalPages, $page + 2); $p++): ?>
        <a href="quiz_questions.php?page=<?php echo $p; ?><?php echo !empty($selectedCategory) ? '&category=' . urlencode($selectedCategory) : ''; ?><?php echo !empty($search) ? '&search=' . urlencode($search) : ''; ?>" 
           class="btn <?php echo $p === $page ? 'btn-primary' : 'btn-secondary'; ?> btn-sm px-3">
          <?php echo toLangNum($p); ?>
        </a>
      <?php endfor; ?>

      <?php if ($page < $totalPages): ?>
        <a href="quiz_questions.php?page=<?php echo $page + 1; ?><?php echo !empty($selectedCategory) ? '&category=' . urlencode($selectedCategory) : ''; ?><?php echo !empty($search) ? '&search=' . urlencode($search) : ''; ?>" 
           class="btn btn-secondary btn-sm">
          <span><?php echo __('পরবর্তী', 'Next'); ?></span> <i class="fa-solid fa-chevron-right"></i>
        </a>
      <?php endif; ?>
    </div>
  </div>
<?php endif; ?>

<!-- Modal Add/Edit Question -->
<div class="modal-backdrop" id="questionModal">
  <div class="modal-window" style="max-width: 680px;">
    <div class="modal-header">
      <div class="modal-title" id="qModalTitle">
        <i class="fa-solid fa-circle-question text-emerald-500 mr-2"></i>
        <span><?php echo __('নতুন প্রশ্ন', 'New Question'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('questionModal')">&times;</button>
    </div>
    <form method="POST" action="quiz_questions.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="qAction" value="create">
      <input type="hidden" name="id" id="qId" value="">

      <div class="modal-body" style="max-height: 75vh; overflow-y: auto;">
        <div class="form-group">
          <label class="form-label font-bold"><?php echo __('ক্যাটাগরি', 'Category'); ?> <span class="text-rose-500">*</span></label>
          <select name="category_id" id="inputQCat" class="form-control font-bold" required>
            <?php foreach ($categories as $c): ?>
              <option value="<?php echo htmlspecialchars($c['category_id']); ?>" <?php echo $selectedCategory === $c['category_id'] ? 'selected' : ''; ?>>
                <?php echo htmlspecialchars(isEn() ? ($c['title_en'] ?: $c['title_bn']) : $c['title_bn']); ?>
              </option>
            <?php endforeach; ?>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label font-bold"><?php echo __('প্রশ্ন', 'Question'); ?> <span class="text-rose-500">*</span></label>
          <textarea name="question_bn" id="inputQText" class="form-control text-base" rows="3" placeholder="<?php echo __('যেমন: ইসলামের পাঁচটি মৌলিক স্তম্ভের মধ্যে দ্বিতীয় স্তম্ভ কোনটি?', 'e.g. What is the second pillar among the five pillars of Islam?'); ?>" required></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label font-bold"><?php echo __('অপশন ১', 'Option 1'); ?> <span class="text-rose-500">*</span></label>
            <input type="text" name="option_1" id="inputQOpt1" class="form-control" placeholder="<?php echo __('যেমন: সালাত', 'e.g. Salah'); ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label font-bold"><?php echo __('অপশন ২', 'Option 2'); ?> <span class="text-rose-500">*</span></label>
            <input type="text" name="option_2" id="inputQOpt2" class="form-control" placeholder="<?php echo __('যেমন: যাকাত', 'e.g. Zakat'); ?>" required>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label font-bold"><?php echo __('অপশন ৩', 'Option 3'); ?> <span class="text-rose-500">*</span></label>
            <input type="text" name="option_3" id="inputQOpt3" class="form-control" placeholder="<?php echo __('যেমন: সাওম', 'e.g. Sawm'); ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label font-bold"><?php echo __('অপশন ৪', 'Option 4'); ?> <span class="text-rose-500">*</span></label>
            <input type="text" name="option_4" id="inputQOpt4" class="form-control" placeholder="<?php echo __('যেমন: হজ্জ', 'e.g. Hajj'); ?>" required>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label font-bold text-emerald-700 dark:text-emerald-300">
            <i class="fa-solid fa-circle-check"></i> <?php echo __('সঠিক উত্তর', 'Correct Option'); ?> <span class="text-rose-500">*</span>
          </label>
          <select name="correct_option" id="inputQCorrect" class="form-control font-bold" style="border-color: var(--primary);">
            <option value="0"><?php echo __('অপশন ১', 'Option 1'); ?></option>
            <option value="1"><?php echo __('অপশন ২', 'Option 2'); ?></option>
            <option value="2"><?php echo __('অপশন ৩', 'Option 3'); ?></option>
            <option value="3"><?php echo __('অপশন ৪', 'Option 4'); ?></option>
          </select>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('রেফারেন্স', 'Reference'); ?></label>
            <input type="text" name="reference" id="inputQRef" class="form-control" placeholder="<?php echo __('যেমন: সহীহ বুখারী: ৮ অথবা সূরা বাকারা: ২৮২', 'e.g. Sahih Bukhari: 8 or Surah Al-Baqarah: 282'); ?>">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ব্যাখ্যা', 'Explanation'); ?></label>
            <input type="text" name="explanation" id="inputQExp" class="form-control" placeholder="<?php echo __('যেমন: ঈমানের পর ইসলামের দ্বিতীয় স্তম্ভ হলো দৈনিক পাঁচ ওয়াক্ত সালাত।', 'e.g. After Iman, the second pillar of Islam is the five daily prayers.'); ?>">
          </div>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('questionModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary" id="btnQSubmit">
          <i class="fa-solid fa-floppy-disk"></i>
          <span><?php echo __('সংরক্ষণ করুন', 'Save'); ?></span>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
const isEnglish = <?php echo isEn() ? 'true' : 'false'; ?>;

function toggleQuestionFlip(id) {
  const item = document.getElementById('qItem_' + id);
  if (item) {
    item.classList.toggle('open');
  }
}

function openAddQuestionModal(defaultCat = '') {
  document.getElementById('qModalTitle').innerHTML = '<i class="fa-solid fa-circle-question text-emerald-500 mr-2"></i><span>' + (isEnglish ? 'New Question' : 'নতুন প্রশ্ন') + '</span>';
  document.getElementById('qAction').value = 'create';
  document.getElementById('qId').value = '';
  
  const targetCat = defaultCat || '<?php echo !empty($selectedCategory) ? htmlspecialchars($selectedCategory, ENT_QUOTES) : "general_knowledge"; ?>';
  if (targetCat) {
    document.getElementById('inputQCat').value = targetCat;
  }
  
  document.getElementById('inputQText').value = '';
  document.getElementById('inputQOpt1').value = '';
  document.getElementById('inputQOpt2').value = '';
  document.getElementById('inputQOpt3').value = '';
  document.getElementById('inputQOpt4').value = '';
  document.getElementById('inputQCorrect').value = '0';
  document.getElementById('inputQRef').value = '';
  document.getElementById('inputQExp').value = '';
  document.getElementById('btnQSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span>' + (isEnglish ? 'Save' : 'সংরক্ষণ করুন') + '</span>';
  openModal('questionModal');
}

function editQuestion(q) {
  document.getElementById('qModalTitle').innerHTML = '<i class="fa-solid fa-pen-to-square text-emerald-500 mr-2"></i><span>' + (isEnglish ? 'Edit Question #' : 'প্রশ্ন সম্পাদনা #') + q.id + '</span>';
  document.getElementById('qAction').value = 'update';
  document.getElementById('qId').value = q.id;
  document.getElementById('inputQCat').value = q.category_id;
  document.getElementById('inputQText').value = q.question_bn;
  document.getElementById('inputQOpt1').value = q.option_1;
  document.getElementById('inputQOpt2').value = q.option_2;
  document.getElementById('inputQOpt3').value = q.option_3;
  document.getElementById('inputQOpt4').value = q.option_4;
  document.getElementById('inputQCorrect').value = q.correct_option;
  document.getElementById('inputQRef').value = q.reference || '';
  document.getElementById('inputQExp').value = q.explanation || '';
  document.getElementById('btnQSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span>' + (isEnglish ? 'Save' : 'সংরক্ষণ করুন') + '</span>';
  openModal('questionModal');
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
