<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - ISLAMIC BOOKS & LIBRARY (ইসলামিক বই ও কিতাবসমূহ)
 * Complete CRUD Management, Moderation & Real-Time Sync
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Auto create table if not exists
try {
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS `islamic_books` (
          `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
          `book_uid` VARCHAR(60) NOT NULL UNIQUE,
          `title_bn` VARCHAR(200) NOT NULL,
          `title_en` VARCHAR(200) DEFAULT NULL,
          `title_ar` VARCHAR(200) DEFAULT NULL,
          `author_bn` VARCHAR(150) NOT NULL,
          `author_en` VARCHAR(150) DEFAULT NULL,
          `category_bn` VARCHAR(100) DEFAULT 'সাধারণ ইসলামিক',
          `category_en` VARCHAR(100) DEFAULT 'General Islamic',
          `description_bn` TEXT DEFAULT NULL,
          `description_en` TEXT DEFAULT NULL,
          `cover_image_url` VARCHAR(255) DEFAULT NULL,
          `pdf_url` VARCHAR(255) NOT NULL,
          `file_size_bytes` BIGINT UNSIGNED DEFAULT 0,
          `total_pages` INT UNSIGNED DEFAULT 0,
          `download_count` INT UNSIGNED DEFAULT 0,
          `read_count` INT UNSIGNED DEFAULT 0,
          `is_active` TINYINT(1) DEFAULT 1,
          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          INDEX `idx_book_cat` (`category_bn`),
          INDEX `idx_book_active` (`is_active`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    ");
} catch (Exception $e) {}

// Handle Action (Add, Edit, Delete, Toggle)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('islamic_books.php');
    }

    if ($action === 'create' || $action === 'update') {
        $titleBn = trim($_POST['title_bn'] ?? '');
        $titleEn = trim($_POST['title_en'] ?? '');
        $authorBn = trim($_POST['author_bn'] ?? '');
        $authorEn = trim($_POST['author_en'] ?? '');
        $categoryBn = trim($_POST['category_bn'] ?? 'সাধারণ ইসলামিক');
        $categoryEn = trim($_POST['category_en'] ?? 'General Islamic');
        $descBn = trim($_POST['description_bn'] ?? '');
        $coverUrl = trim($_POST['cover_image_url'] ?? '');
        $pdfUrl = trim($_POST['pdf_url'] ?? '');
        $totalPages = (int)($_POST['total_pages'] ?? 0);
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($titleBn) || empty($authorBn) || empty($pdfUrl)) {
            setFlash('danger', __('বইয়ের বাংলা শিরোনাম, লেখকের নাম এবং পিডিএফ লিংক আবশ্যক।', 'Book title in Bengali, author name, and PDF URL are required.'));
        } else {
            try {
                if ($action === 'create') {
                    $uid = trim($_POST['book_uid'] ?? '');
                    if (empty($uid)) {
                        $uid = 'BOOK_' . strtoupper(bin2hex(random_bytes(4)));
                    }
                    $stmt = $pdo->prepare("INSERT INTO islamic_books (book_uid, title_bn, title_en, author_bn, author_en, category_bn, category_en, description_bn, cover_image_url, pdf_url, total_pages, is_active) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$uid, $titleBn, $titleEn, $authorBn, $authorEn, $categoryBn, $categoryEn, $descBn, $coverUrl, $pdfUrl, $totalPages, $isActive]);
                    logAdminAction($pdo, 'CREATE_BOOK', 'islamic_books', $uid, 'নতুন কিতাব: ' . $titleBn);
                    setFlash('success', __('নতুন ইসলামিক কিতাব সফলভাবে সংরক্ষিত হয়েছে!', 'New Islamic book added successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE islamic_books SET title_bn = ?, title_en = ?, author_bn = ?, author_en = ?, category_bn = ?, category_en = ?, description_bn = ?, cover_image_url = ?, pdf_url = ?, total_pages = ?, is_active = ? WHERE id = ?");
                    $stmt->execute([$titleBn, $titleEn, $authorBn, $authorEn, $categoryBn, $categoryEn, $descBn, $coverUrl, $pdfUrl, $totalPages, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_BOOK', 'islamic_books', (string)$id, 'কিতাব আপডেট: ' . $titleBn);
                    setFlash('success', __('কিতাবের তথ্য সফলভাবে আপডেট হয়েছে!', 'Book details updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('ডাটাবেস ত্রুটি: ', 'Database error: ') . $e->getMessage());
            }
        }
    }
    redirect('islamic_books.php');
}

if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM islamic_books WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_BOOK', 'islamic_books', (string)$id, 'কিতাব মুছে ফেলা হয়েছে');
        setFlash('success', __('কিতাব সফলভাবে মুছে ফেলা হয়েছে।', 'Book deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
    }
    redirect('islamic_books.php');
}

if (isset($_GET['toggle'])) {
    $id = (int)$_GET['toggle'];
    try {
        $stmt = $pdo->prepare("UPDATE islamic_books SET is_active = IF(is_active=1, 0, 1) WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'TOGGLE_BOOK', 'islamic_books', (string)$id, 'স্ট্যাটাস টগল');
        setFlash('success', __('কিতাবের প্রদর্শন স্ট্যাটাস পরিবর্তন করা হয়েছে।', 'Book visibility status toggled.'));
    } catch (Exception $e) {
        setFlash('danger', __('স্ট্যাটাস পরিবর্তন ব্যর্থ: ', 'Toggle failed: ') . $e->getMessage());
    }
    redirect('islamic_books.php');
}

// Filtering & Search
$selectedCat = trim($_GET['category'] ?? '');
$searchQuery = trim($_GET['q'] ?? '');

$where = [];
$params = [];

if (!empty($selectedCat) && $selectedCat !== 'all') {
    $where[] = "category_bn = ?";
    $params[] = $selectedCat;
}

if (!empty($searchQuery)) {
    $where[] = "(title_bn LIKE ? OR author_bn LIKE ? OR description_bn LIKE ?)";
    $term = "%$searchQuery%";
    $params = array_merge($params, [$term, $term, $term]);
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

// Fetch distinct categories
$distinctCategories = [];
try {
    $catStmt = $pdo->query("SELECT DISTINCT category_bn FROM islamic_books WHERE category_bn IS NOT NULL AND category_bn != ''");
    $distinctCategories = $catStmt->fetchAll(PDO::FETCH_COLUMN);
} catch (Exception $e) {}

// Fetch Books
$books = [];
try {
    $stmt = $pdo->prepare("SELECT * FROM islamic_books $whereSql ORDER BY id DESC LIMIT 100");
    $stmt->execute($params);
    $books = $stmt->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('ইসলামিক বই', 'Islamic Books');
$activeNav = 'islamic_books';
require_once __DIR__ . '/header.php';
?>

<!-- Content Header -->
<div class="content-header" style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px; margin-bottom:24px;">
    <div>
        <h1 class="page-title" style="margin:0 0 6px 0; font-size:24px; font-weight:700; color:var(--text-heading, #0f172a); display:flex; align-items:center; gap:10px;">
            <i class="fa-solid fa-book" style="color:var(--primary, #10b981);"></i><?php echo __('ইসলামিক বই', 'Islamic Books'); ?>
        </h1>
    </div>
    <div>
        <button type="button" class="btn btn-primary" onclick="openAddModal()" style="display:flex; align-items:center; gap:8px; padding:10px 20px; font-weight:600; border-radius:10px;">
            <i class="fa-solid fa-plus"></i><?php echo __('নতুন বই', 'Add Book'); ?>
        </button>
    </div>
</div>

<!-- Filters Bar -->
<div class="card mb-4 p-3" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px;">
    <form method="GET" action="islamic_books.php" class="flex-wrap gap-3" style="display:flex; align-items:center;">
        <div style="flex: 1; min-width: 220px;">
            <input type="text" name="q" value="<?php echo htmlspecialchars($searchQuery); ?>" 
                   placeholder="<?php echo __('বইয়ের নাম বা লেখক খুঁজুন...', 'Search by title or author...'); ?>" 
                   class="form-control">
        </div>
        <div style="min-width: 180px;">
            <select name="category" class="form-control" onchange="this.form.submit()">
                <option value="all"><?php echo __('সকল ক্যাটাগরি', 'All Categories'); ?></option>
                <?php foreach ($distinctCategories as $c): ?>
                    <option value="<?php echo htmlspecialchars($c); ?>" <?php echo $selectedCat === $c ? 'selected' : ''; ?>>
                        <?php echo htmlspecialchars($c); ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </div>
        <button type="submit" class="btn btn-secondary">
            <i class="fa-solid fa-filter mr-1"></i><?php echo __('ফিল্টার', 'Filter'); ?>
        </button>
        <?php if (!empty($searchQuery) || !empty($selectedCat)): ?>
            <a href="islamic_books.php" class="btn btn-outline"><?php echo __('রিসেট', 'Reset'); ?></a>
        <?php endif; ?>
    </form>
</div>

<!-- Books Table -->
<div class="card p-0" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px; overflow: hidden;">
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th style="width: 50px;">#</th>
                    <th style="width: 70px;"><?php echo __('কভার', 'Cover'); ?></th>
                    <th><?php echo __('বই ও লেখক', 'Title & Author'); ?></th>
                    <th><?php echo __('ক্যাটাগরি', 'Category'); ?></th>
                    <th style="width: 100px;"><?php echo __('পৃষ্ঠা', 'Pages'); ?></th>
                    <th style="width: 100px;"><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
                    <th style="width: 150px; text-align: right;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
                </tr>
            </thead>
            <tbody>
                <?php if (empty($books)): ?>
                    <tr>
                        <td colspan="7" class="text-center py-5 text-muted">
                            <i class="fa-solid fa-book-open fa-2x mb-2" style="opacity: 0.5;"></i><br>
                            <?php echo __('কোনো ইসলামিক কিতাব পাওয়া যায়নি।', 'No Islamic books found.'); ?>
                        </td>
                    </tr>
                <?php else: ?>
                    <?php foreach ($books as $idx => $b): ?>
                        <tr>
                            <td><?php echo $idx + 1; ?></td>
                            <td>
                                <?php if (!empty($b['cover_image_url'])): ?>
                                    <img src="<?php echo htmlspecialchars($b['cover_image_url']); ?>" alt="Cover" style="width: 44px; height: 58px; object-fit: cover; border-radius: 6px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                <?php else: ?>
                                    <div style="width: 44px; height: 58px; background: rgba(16,185,129,0.1); border-radius: 6px; display: flex; align-items: center; justify-content: center; color: #10B981;">
                                        <i class="fa-solid fa-book"></i>
                                    </div>
                                <?php endif; ?>
                            </td>
                            <td>
                                <div style="font-weight: 600; color: var(--text-primary); font-size: 15px;">
                                    <?php echo htmlspecialchars($b['title_bn']); ?>
                                </div>
                                <?php if (!empty($b['title_en'])): ?>
                                    <div class="text-muted" style="font-size: 12px;"><?php echo htmlspecialchars($b['title_en']); ?></div>
                                <?php endif; ?>
                                <div class="text-muted mt-1" style="font-size: 13px;">
                                    <i class="fa-solid fa-feather mr-1" style="color: var(--primary);"></i><?php echo htmlspecialchars($b['author_bn']); ?>
                                </div>
                            </td>
                            <td>
                                <span class="badge" style="background: rgba(16,185,129,0.15); color: #10B981; font-weight: 600; padding: 4px 10px; border-radius: 6px;">
                                    <?php echo htmlspecialchars($b['category_bn']); ?>
                                </span>
                            </td>
                            <td><?php echo toLangNum($b['total_pages']); ?></td>
                            <td>
                                <a href="islamic_books.php?toggle=<?php echo $b['id']; ?>" class="badge <?php echo $b['is_active'] ? 'badge-success' : 'badge-danger'; ?>" title="<?php echo __('ক্লিক করে স্ট্যাটাস পরিবর্তন করুন', 'Click to toggle'); ?>" style="text-decoration: none; padding: 4px 8px; border-radius: 6px;">
                                    <?php echo $b['is_active'] ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
                                </a>
                            </td>
                            <td style="text-align: right;">
                                <div style="display: flex; justify-content: flex-end; gap: 6px;">
                                    <a href="<?php echo htmlspecialchars($b['pdf_url']); ?>" target="_blank" class="btn btn-sm btn-outline" title="<?php echo __('পিডিএফ দেখুন', 'View PDF'); ?>" style="padding: 6px 10px;">
                                        <i class="fa-solid fa-file-pdf text-red-500"></i>
                                    </a>
                                    <button class="btn btn-sm btn-secondary" onclick='openEditModal(<?php echo json_encode($b); ?>)' title="<?php echo __('সম্পাদনা', 'Edit'); ?>" style="padding: 6px 10px;">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                    <a href="islamic_books.php?delete=<?php echo $b['id']; ?>" class="btn btn-sm btn-danger" onclick="return confirm('<?php echo __('আপনি কি নিশ্চিত যে এই কিতাবটি মুছে ফেলতে চান?', 'Are you sure you want to delete this book?'); ?>');" title="<?php echo __('মুছে ফেলুন', 'Delete'); ?>" style="padding: 6px 10px;">
                                        <i class="fa-solid fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                <?php endif; ?>
            </tbody>
        </table>
    </div>
</div>

<!-- Add / Edit Modal -->
<div id="bookModal" class="modal" style="display:none; position: fixed; z-index: 99999; left: 0; top: 0; width: 100%; height: 100%; overflow-x: hidden; overflow-y: auto; background-color: rgba(15,23,42,0.65); backdrop-filter: blur(4px); -webkit-backdrop-filter: blur(4px); justify-content: center; align-items: center; padding: 20px;" onclick="if(event.target===this)closeModal()">
    <div class="modal-content" style="background: var(--bg-card); border-radius: 16px; width: 100%; max-width: 680px; max-height: 90vh; display: flex; flex-direction: column; border: 1px solid var(--border-color); box-shadow: 0 25px 50px -12px rgba(0,0,0,0.35); overflow: hidden; margin: auto;" onclick="event.stopPropagation()">
        
        <!-- Modal Header: Left Title, Right Close (X) Icon -->
        <div class="modal-header" style="display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--border-color); flex-shrink: 0; background: var(--bg-card);">
            <h3 id="modalTitle" style="margin: 0; font-size: 18px; font-weight: 700; color: var(--text-heading); display: flex; align-items: center; gap: 10px;">
                <i class="fa-solid fa-book-open" style="color: var(--primary, #10b981);"></i>
                <span id="modalTitleText"><?php echo __('নতুন বই', 'New Book'); ?></span>
            </h3>
            <button type="button" class="modal-close-btn" onclick="closeModal()" title="<?php echo __('বন্ধ করুন', 'Close'); ?>" style="background: rgba(0,0,0,0.05); border: 1px solid var(--border-color); width: 34px; height: 34px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 20px; color: var(--text-muted); cursor: pointer; line-height: 1; transition: all 0.2s;">&times;</button>
        </div>

        <!-- Modal Body: Fully Scrollable from Top to Bottom -->
        <div class="modal-body" style="padding: 22px 24px; overflow-y: auto; flex: 1 1 auto; max-height: calc(90vh - 140px);">
            <form method="POST" action="islamic_books.php" id="bookForm">
                <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
                <input type="hidden" name="action" id="formAction" value="create">
                <input type="hidden" name="id" id="bookId" value="0">

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('বাংলা নাম *', 'Bengali Title *'); ?></label>
                    <input type="text" name="title_bn" id="bookTitleBn" required class="form-control" placeholder="উদাঃ রিয়াযুস স্বা-লিহীন">
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('ইংরেজি নাম', 'English Title'); ?></label>
                    <input type="text" name="title_en" id="bookTitleEn" class="form-control" placeholder="e.g. Riyad as-Salihin">
                </div>

                <div class="grid grid-cols-2 gap-3 mb-3" style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                    <div class="form-group">
                        <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('বাংলা লেখক *', 'Bengali Author *'); ?></label>
                        <input type="text" name="author_bn" id="bookAuthorBn" required class="form-control" placeholder="উদাঃ ইমাম আন-নববী">
                    </div>
                    <div class="form-group">
                        <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('ইংরেজি লেখক', 'English Author'); ?></label>
                        <input type="text" name="author_en" id="bookAuthorEn" class="form-control" placeholder="e.g. Imam An-Nawawi">
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-3 mb-3" style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                    <div class="form-group">
                        <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('ক্যাটাগরি', 'Category'); ?></label>
                        <input type="text" name="category_bn" id="bookCategoryBn" class="form-control" value="সহীহ হাদিস ও আখলাক">
                    </div>
                    <div class="form-group">
                        <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('মোট পৃষ্ঠা', 'Total Pages'); ?></label>
                        <input type="number" name="total_pages" id="bookPages" class="form-control" value="0">
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('পিডিএফ ডাউনলোড লিংক *', 'PDF Download URL *'); ?></label>
                    <input type="url" name="pdf_url" id="bookPdfUrl" required class="form-control" placeholder="https://example.com/books/sample.pdf">
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('কভার ছবি লিংক', 'Cover Image URL'); ?></label>
                    <input type="url" name="cover_image_url" id="bookCoverUrl" class="form-control" placeholder="https://example.com/covers/sample.jpg">
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('সংক্ষিপ্ত বিবরণ', 'Description'); ?></label>
                    <textarea name="description_bn" id="bookDescBn" rows="3" class="form-control" placeholder="বইয়ের সংক্ষিপ্ত বিষয়বস্তু ও উপকারিতা..."></textarea>
                </div>

                <div class="form-group mb-2" style="display: flex; align-items: center; gap: 10px;">
                    <input type="checkbox" name="is_active" id="bookIsActive" value="1" checked style="width: 18px; height: 18px; accent-color: var(--primary);">
                    <label for="bookIsActive" style="margin: 0; cursor: pointer; font-weight: 500;"><?php echo __('অ্যাপে প্রদর্শন করুন', 'Show in App'); ?></label>
                </div>
            </form>
        </div>

        <!-- Modal Footer: Cancel and Save Buttons ON THE RIGHT SIDE TOGETHER -->
        <div class="modal-footer" style="display: flex; justify-content: flex-end; align-items: center; gap: 12px; padding: 16px 24px; border-top: 1px solid var(--border-color); background: var(--bg-card); flex-shrink: 0;">
            <button type="button" class="btn btn-outline" onclick="closeModal()" style="padding: 9px 18px; border-radius: 8px; font-weight: 600;"><?php echo __('বাতিল', 'Cancel'); ?></button>
            <button type="submit" form="bookForm" class="btn btn-primary" style="padding: 9px 22px; border-radius: 8px; font-weight: 600; display: flex; align-items: center; gap: 8px;">
                <i class="fa-solid fa-save"></i><?php echo __('সংরক্ষণ করুন', 'Save'); ?>
            </button>
        </div>
    </div>
</div>

<script>
function openAddModal() {
    var modal = document.getElementById('bookModal');
    var titleText = document.getElementById('modalTitleText');
    if (titleText) titleText.innerText = '<?php echo __('নতুন বই', 'New Book'); ?>';
    document.getElementById('formAction').value = 'create';
    document.getElementById('bookId').value = '0';
    document.getElementById('bookTitleBn').value = '';
    document.getElementById('bookTitleEn').value = '';
    document.getElementById('bookAuthorBn').value = '';
    document.getElementById('bookAuthorEn').value = '';
    document.getElementById('bookCategoryBn').value = 'সাধারণ ইসলামিক';
    document.getElementById('bookPages').value = '0';
    document.getElementById('bookPdfUrl').value = '';
    document.getElementById('bookCoverUrl').value = '';
    document.getElementById('bookDescBn').value = '';
    document.getElementById('bookIsActive').checked = true;
    if (modal) {
        modal.style.display = 'flex';
    }
}
window.openAddModal = openAddModal;

function openEditModal(book) {
    var modal = document.getElementById('bookModal');
    var titleText = document.getElementById('modalTitleText');
    if (titleText) titleText.innerText = '<?php echo __('বই সম্পাদনা', 'Edit Book'); ?>';
    document.getElementById('formAction').value = 'update';
    document.getElementById('bookId').value = book.id;
    document.getElementById('bookTitleBn').value = book.title_bn || '';
    document.getElementById('bookTitleEn').value = book.title_en || '';
    document.getElementById('bookAuthorBn').value = book.author_bn || '';
    document.getElementById('bookAuthorEn').value = book.author_en || '';
    document.getElementById('bookCategoryBn').value = book.category_bn || 'সাধারণ ইসলামিক';
    document.getElementById('bookPages').value = book.total_pages || 0;
    document.getElementById('bookPdfUrl').value = book.pdf_url || '';
    document.getElementById('bookCoverUrl').value = book.cover_image_url || '';
    document.getElementById('bookDescBn').value = book.description_bn || '';
    document.getElementById('bookIsActive').checked = (book.is_active == 1);
    if (modal) {
        modal.style.display = 'flex';
    }
}
window.openEditModal = openEditModal;

function closeModal() {
    var modal = document.getElementById('bookModal');
    if (modal) {
        modal.style.display = 'none';
    }
}
window.closeModal = closeModal;

// ESC key to close modal
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeModal();
    }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>

