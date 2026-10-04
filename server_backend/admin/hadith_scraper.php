<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - SMART HADITH SCRAPER & PENDING STAGING QUEUE HUB
 * High-Speed Web Scraper, Auto-Translator & 1-Click Approval Pipeline
 * ==============================================================================
 */

require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();

$pdo = getDbConnection();

// Fetch all books for dropdowns
$booksStmt = $pdo->query("SELECT id, book_slug, name_bn, name_en FROM hadith_books WHERE is_active = 1 ORDER BY display_order ASC, id ASC");
$books = $booksStmt->fetchAll();

// Default book & chapter filter (Support 'all' books and '0' all chapters)
$selectedBook = trim($_GET['book'] ?? 'all');
$selectedChapter = isset($_GET['chapter']) ? (int)$_GET['chapter'] : 0;
$selectedStatus = trim($_GET['status'] ?? 'pending');
$search = trim($_GET['search'] ?? '');
$currentTab = trim($_GET['tab'] ?? ($selectedStatus === 'approved' || isset($_GET['status']) || $selectedBook !== 'all' || $selectedChapter > 0 || !empty($search) ? 'pending' : 'scraper'));

// Fetch chapters for selected book (if specific book chosen)
$chapters = [];
if (!empty($selectedBook) && $selectedBook !== 'all') {
    $chapStmt = $pdo->prepare("SELECT chapter_number, title_bn, title_en, hadith_range_bn FROM hadith_chapters WHERE book_slug = ? ORDER BY chapter_number ASC");
    $chapStmt->execute([$selectedBook]);
    $chapters = $chapStmt->fetchAll();
}

// All chapters map for client-side instant book-chapter dropdown sync
$allChaptersStmt = $pdo->query("SELECT book_slug, chapter_number, title_bn, title_en, hadith_range_bn FROM hadith_chapters ORDER BY book_slug, chapter_number ASC");
$allChaptersGrouped = [];
while ($cr = $allChaptersStmt->fetch()) {
    $allChaptersGrouped[$cr['book_slug']][] = $cr;
}

// Global Stats calculation
$totalScraped = (int)$pdo->query("SELECT COUNT(*) FROM hadith_scraped_staging")->fetchColumn();
$totalPending = (int)$pdo->query("SELECT COUNT(*) FROM hadith_scraped_staging WHERE status = 'pending'")->fetchColumn();
$totalApproved = (int)$pdo->query("SELECT COUNT(*) FROM hadith_scraped_staging WHERE status = 'approved'")->fetchColumn();
$totalLiveHadithsGlobal = 0;
try {
    $totalLiveHadithsGlobal = (int)$pdo->query("SELECT COUNT(*) FROM hadith_items")->fetchColumn();
} catch (Exception $e) {}

// Fetch staged hadiths based on filter
$where = [];
$params = [];

if (!empty($selectedBook) && $selectedBook !== 'all') {
    $where[] = "s.book_slug = ?";
    $params[] = $selectedBook;
}

if ($selectedChapter > 0) {
    $where[] = "s.chapter_number = ?";
    $params[] = $selectedChapter;
}

if (!empty($selectedStatus) && $selectedStatus !== 'all') {
    $where[] = "s.status = ?";
    $params[] = $selectedStatus;
}

if (!empty($search)) {
    $where[] = "(s.bangla_text LIKE ? OR s.arabic_text LIKE ? OR s.narrator_bn LIKE ? OR s.hadith_number = ? OR s.hadith_number_bn LIKE ? OR s.reference LIKE ?)";
    $sParam = "%$search%";
    $params[] = $sParam;
    $params[] = $sParam;
    $params[] = $sParam;
    $params[] = is_numeric($search) ? (int)$search : 0;
    $params[] = $sParam;
    $params[] = $sParam;
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

$stagedQuery = "SELECT s.*, b.name_bn AS book_name_bn, b.name_en AS book_name_en 
                FROM hadith_scraped_staging s 
                LEFT JOIN hadith_books b ON b.book_slug = s.book_slug 
                $whereSql 
                ORDER BY s.id DESC";

$stmt = $pdo->prepare($stagedQuery);
$stmt->execute($params);
$stagedItems = $stmt->fetchAll();

$pageTitle = __('হাদিস স্ক্র্যাপার', 'Hadith Scraper');
$activeNav = 'hadith_scraper';
require_once __DIR__ . '/header.php';
?>

<style>
.staged-hadith-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 22px;
  margin-bottom: 20px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.04);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.staged-hadith-card:hover {
  box-shadow: 0 8px 24px rgba(16, 185, 129, 0.08);
}
.stat-card-clickable {
  text-decoration: none;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease, border-color 0.2s ease;
  position: relative;
  overflow: hidden;
}
.stat-card-clickable:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}
.stat-card-clickable.active-stat {
  border-width: 2px !important;
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.2), 0 8px 20px rgba(0, 0, 0, 0.08);
}
.tab-btn {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 700;
  border-radius: var(--radius-md);
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}
.tab-btn.active {
  background: var(--primary);
  color: #ffffff;
}
.tab-btn:not(.active) {
  background: var(--hover-bg);
  color: var(--text-muted);
  border-color: var(--border-color);
}
</style>

<div class="content-body">

  <!-- Interactive Clickable Stats Grid -->
  <div class="stats-grid" style="grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); margin-bottom: 24px;">
    
    <!-- Card 1: Total Scraped -->
    <a href="hadith_scraper.php?tab=pending&book=all&chapter=0&status=all" class="stat-card stat-card-clickable <?php echo ($currentTab === 'pending' && $selectedStatus === 'all') ? 'active-stat' : ''; ?>" style="border-color: rgba(16, 185, 129, 0.4);" title="<?php echo __('সকল স্ক্র্যাপড হাদিস দেখুন', 'View All Scraped'); ?>">
      <div class="stat-icon-wrap emerald">
        <i class="fa-solid fa-cloud-arrow-down"></i>
      </div>
      <div class="stat-content">
        <h3><?php echo toLangNum($totalScraped); ?></h3>
        <p><?php echo __('মোট স্ক্র্যাপড', 'Total Scraped'); ?></p>
        <span style="font-size: 11px; color: var(--primary); font-weight: 700; display: inline-flex; align-items: center; gap: 4px; margin-top: 4px;">
          <i class="fa-solid fa-arrow-right"></i> <?php echo __('সকল ডাটা', 'All Records'); ?>
        </span>
      </div>
    </a>

    <!-- Card 2: Pending Review -->
    <a href="hadith_scraper.php?tab=pending&book=all&chapter=0&status=pending" class="stat-card stat-card-clickable <?php echo ($currentTab === 'pending' && $selectedStatus === 'pending') ? 'active-stat' : ''; ?>" style="border-color: rgba(245, 158, 11, 0.45);" title="<?php echo __('অনুমোদনের অপেক্ষায় থাকা হাদিসসমূহ দেখুন', 'View Pending Hadiths'); ?>">
      <div class="stat-icon-wrap amber">
        <i class="fa-solid fa-clock-rotate-left"></i>
      </div>
      <div class="stat-content">
        <h3 style="color: #d97706;"><?php echo toLangNum($totalPending); ?></h3>
        <p><?php echo __('পেন্ডিং রিভিউ', 'Pending Review'); ?></p>
        <span style="font-size: 11px; color: #d97706; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; margin-top: 4px;">
          <i class="fa-solid fa-filter"></i> <?php echo __('অনুমোদন কিউ', 'Approval Queue'); ?>
        </span>
      </div>
    </a>

    <!-- Card 3: Approved Staging -->
    <a href="hadith_scraper.php?tab=pending&book=all&chapter=0&status=approved" class="stat-card stat-card-clickable <?php echo ($currentTab === 'pending' && $selectedStatus === 'approved') ? 'active-stat' : ''; ?>" style="border-color: rgba(16, 185, 129, 0.45);" title="<?php echo __('অনুমোদিত স্ক্র্যাপড হাদিসসমূহ দেখুন', 'View Approved Hadiths'); ?>">
      <div class="stat-icon-wrap emerald">
        <i class="fa-solid fa-circle-check"></i>
      </div>
      <div class="stat-content">
        <h3 style="color: #059669;"><?php echo toLangNum($totalApproved); ?></h3>
        <p><?php echo __('অনুমোদিত', 'Approved'); ?></p>
        <span style="font-size: 11px; color: #059669; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; margin-top: 4px;">
          <i class="fa-solid fa-check-double"></i> <?php echo __('অনুমোদিত তালিকা', 'Approved List'); ?>
        </span>
      </div>
    </a>

    <!-- Card 4: Live Hadiths in App -->
    <a href="hadiths.php" class="stat-card stat-card-clickable" style="border-color: rgba(2, 132, 199, 0.45);" title="<?php echo __('মূল লাইভ হাদিস ডিরেক্টরিতে যান', 'Go to Live Hadith Directory'); ?>">
      <div class="stat-icon-wrap blue">
        <i class="fa-solid fa-book-open"></i>
      </div>
      <div class="stat-content">
        <h3 style="color: #0284c7;"><?php echo toLangNum($totalLiveHadithsGlobal); ?></h3>
        <p><?php echo __('লাইভ হাদিস', 'Live Hadiths'); ?></p>
        <span style="font-size: 11px; color: #0284c7; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; margin-top: 4px;">
          <i class="fa-solid fa-arrow-up-right-from-square"></i> <?php echo __('লাইভ ডিরেক্টরি', 'Live Directory'); ?>
        </span>
      </div>
    </a>

  </div>

  <!-- Main Navigation Tabs -->
  <div style="display: flex; gap: 12px; margin-bottom: 24px;">
    <button type="button" class="tab-btn <?php echo ($currentTab === 'scraper') ? 'active' : ''; ?>" onclick="switchTab('scraper')">
      <i class="fa-solid fa-wand-magic-sparkles"></i>
      <span><?php echo __('স্ক্র্যাপার ও ইমপোর্টার', 'Scraper & Importer'); ?></span>
    </button>
    <button type="button" class="tab-btn <?php echo ($currentTab === 'pending') ? 'active' : ''; ?>" onclick="switchTab('pending')">
      <i class="fa-solid fa-list-check"></i>
      <span><?php echo __('পেন্ডিং রিভিউ', 'Pending Review'); ?></span>
      <?php if ($totalPending > 0): ?>
        <span class="badge badge-warning" style="background: #f59e0b; color: #fff; font-size: 11px; padding: 2px 8px; border-radius: 999px;"><?php echo toLangNum($totalPending); ?></span>
      <?php endif; ?>
    </button>
  </div>

  <!-- =========================================================================
       TAB 1: SMART SCRAPER & IMPORTER
       ========================================================================= -->
  <div id="tabScraperSection" style="<?php echo ($currentTab === 'pending') ? 'display: none;' : ''; ?>">
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(460px, 1fr)); gap: 24px;">
      
      <!-- Option A: HadithBD / Web Direct HTML Extractor -->
      <div class="card" style="margin-bottom: 0;">
        <div class="card-header" style="background: linear-gradient(135deg, rgba(16,185,129,0.08), rgba(6,78,59,0.15));">
          <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-link text-emerald-500"></i>
            <span><?php echo __('হাদিসবিডি পার্সার', 'HadithBD Parser'); ?></span>
          </div>
        </div>
        <div class="card-body" style="padding: 22px;">
          <form id="formHadithBdParser" onsubmit="handleHadithBdScrape(event)">
            <div class="form-row">
              <div class="form-group">
                <label class="form-label"><?php echo __('হাদিস গ্রন্থ', 'Hadith Book'); ?> <span class="text-rose-500">*</span></label>
                <select name="book_slug" id="scrapeBookSlug" class="form-control" required onchange="onBookChangeForScrape(this.value)">
                  <?php foreach ($books as $b): ?>
                    <option value="<?php echo htmlspecialchars($b['book_slug']); ?>" <?php echo ($selectedBook === $b['book_slug'] || ($selectedBook === 'all' && $b['book_slug'] === 'bukhari')) ? 'selected' : ''; ?>>
                      <?php echo htmlspecialchars(isEn() ? ($b['name_en'] ?: $b['name_bn']) : $b['name_bn']); ?>
                    </option>
                  <?php endforeach; ?>
                </select>
              </div>
              <div class="form-group">
                <label class="form-label"><?php echo __('অধ্যায়', 'Chapter'); ?> <span class="text-rose-500">*</span></label>
                <input type="number" name="chapter_number" id="scrapeChapterNum" class="form-control font-bold" min="1" value="<?php echo $selectedChapter > 0 ? $selectedChapter : 1; ?>" required>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label"><?php echo __('সোর্স লিংক', 'Source URL'); ?></label>
              <input type="url" name="source_url" id="scrapeSourceUrl" class="form-control" placeholder="https://www.hadithbd.com/hadith/" value="https://www.hadithbd.com/hadith/" readonly style="background: var(--hover-bg); cursor: default;">
            </div>

            <div class="form-group">
              <label class="form-label">
                <span><?php echo __('পেজের কন্টেন্ট বা HTML', 'Content / HTML'); ?> <span class="text-rose-500">*</span></span>
              </label>
              <textarea name="raw_content" id="scrapeRawContent" class="form-control" rows="8" placeholder="https://www.hadithbd.com/hadith/ থেকে কপি করা হাদিস টেক্সট বা সম্পূর্ণ HTML এখানে পেস্ট করুন..." required></textarea>
            </div>

            <div style="display: flex; align-items: center; justify-content: flex-end; gap: 12px; margin-top: 18px;">
              <button type="submit" class="btn btn-primary" id="btnScrapeSubmit" style="padding: 10px 22px;">
                <i class="fa-solid fa-spider"></i>
                <span id="btnScrapeSubmitText"><?php echo __('পেন্ডিং কিউতে যোগ করুন', 'Add to Pending'); ?></span>
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- Option B: 1-Click HadithBD Auto Chapter Scraper -->
      <div class="card" style="margin-bottom: 0;">
        <div class="card-header" style="background: linear-gradient(135deg, rgba(2,132,199,0.08), rgba(30,58,138,0.15)); display: flex; align-items: center; justify-content: space-between;">
          <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-bolt text-sky-500"></i>
            <span><?php echo __('হাদিসবিডি স্বয়ংক্রিয় অধ্যায় স্ক্র্যাপার', 'HadithBD Auto Chapter Scraper'); ?></span>
          </div>
          <span class="badge badge-primary" style="font-size: 11.5px; background: rgba(14,165,233,0.15); color: #0284c7; border: 1px solid rgba(14,165,233,0.3);">
            <i class="fa-solid fa-link"></i> hadithbd.com/hadith/
          </span>
        </div>
        <div class="card-body" style="padding: 22px;">
          <form id="formAutoChapterCrawler" onsubmit="handleAutoChapterCrawl(event)">
            <div class="form-group">
              <label class="form-label"><?php echo __('হাদিস গ্রন্থ', 'Hadith Book'); ?> <span class="text-rose-500">*</span></label>
              <select name="crawl_book_slug" id="crawlBookSlug" class="form-control" required onchange="onBookChangeForCrawl(this.value)">
                <?php foreach ($books as $b): ?>
                  <option value="<?php echo htmlspecialchars($b['book_slug']); ?>" <?php echo ($selectedBook === $b['book_slug'] || ($selectedBook === 'all' && $b['book_slug'] === 'bukhari')) ? 'selected' : ''; ?>>
                    <?php echo htmlspecialchars(isEn() ? ($b['name_en'] ?: $b['name_bn']) : $b['name_bn']); ?>
                  </option>
                <?php endforeach; ?>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label"><?php echo __('অধ্যায়', 'Chapter'); ?> <span class="text-rose-500">*</span></label>
              <div style="display: flex; gap: 10px;">
                <input type="number" name="crawl_chapter_number" id="crawlChapterNum" class="form-control font-bold" min="1" value="<?php echo $selectedChapter > 0 ? $selectedChapter : 1; ?>" style="max-width: 140px;" required placeholder="<?php echo __('অধ্যায় নং', 'Chapter No'); ?>" oninput="updateChapterPreview(this.value)">
                <div style="flex: 1; display: flex; align-items: center; background: var(--hover-bg); padding: 0 14px; border: 1px solid var(--border-color); border-radius: var(--radius-md); font-size: 13px; color: var(--text-muted);" id="displayChapterTitlePreview">
                  <?php 
                    $previewBook = ($selectedBook !== 'all') ? $selectedBook : 'bukhari';
                    $curChapObj = null;
                    if (!empty($allChaptersGrouped[$previewBook])) {
                      foreach ($allChaptersGrouped[$previewBook] as $c) {
                        if ((int)$c['chapter_number'] === ($selectedChapter > 0 ? $selectedChapter : 1)) { $curChapObj = $c; break; }
                      }
                    }
                    echo htmlspecialchars($curChapObj ? ($curChapObj['title_bn'] . ' (' . $curChapObj['hadith_range_bn'] . ')') : ('অধ্যায় #' . ($selectedChapter > 0 ? $selectedChapter : 1)));
                  ?>
                </div>
              </div>
            </div>

            <button type="submit" class="btn btn-primary" id="btnCrawlSubmit" style="width: 100%; padding: 12px; justify-content: center; margin-top: 14px;">
              <i class="fa-solid fa-cloud-arrow-down"></i>
              <span id="btnCrawlSubmitText"><?php echo __('হাদিসবিডি থেকে স্ক্র্যাপ শুরু করুন', 'Scrape from HadithBD'); ?></span>
            </button>
          </form>
        </div>
      </div>

    </div>
  </div>

  <!-- =========================================================================
       TAB 2: PENDING REVIEW & 1-CLICK APPROVAL QUEUE
       ========================================================================= -->
  <div id="tabPendingSection" style="<?php echo ($currentTab !== 'pending') ? 'display: none;' : ''; ?>">
    
    <!-- Filter & Search Bar -->
    <div class="card" style="margin-bottom: 20px;">
      <div class="card-body" style="padding: 16px 20px;">
        <form method="GET" action="hadith_scraper.php" id="filterPendingForm" style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 14px;">
          <input type="hidden" name="tab" value="pending">

          <div style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap; flex: 1;">
            <!-- Book Filter -->
            <select name="book" id="filterBookSelect" class="form-control" style="width: auto; min-width: 180px;" onchange="onFilterBookChange(this.value)">
              <option value="all" <?php echo $selectedBook === 'all' ? 'selected' : ''; ?>><?php echo __('সকল গ্রন্থ', 'All Books'); ?></option>
              <?php foreach ($books as $b): ?>
                <option value="<?php echo htmlspecialchars($b['book_slug']); ?>" <?php echo $selectedBook === $b['book_slug'] ? 'selected' : ''; ?>>
                  <?php echo htmlspecialchars(isEn() ? ($b['name_en'] ?: $b['name_bn']) : $b['name_bn']); ?>
                </option>
              <?php endforeach; ?>
            </select>

            <!-- Chapter Filter -->
            <select name="chapter" id="filterChapterSelect" class="form-control" style="width: auto; min-width: 160px;" onchange="this.form.submit()">
              <option value="0" <?php echo $selectedChapter === 0 ? 'selected' : ''; ?>><?php echo __('সকল অধ্যায়', 'All Chapters'); ?></option>
              <?php if (!empty($chapters)): ?>
                <?php foreach ($chapters as $c): ?>
                  <option value="<?php echo $c['chapter_number']; ?>" <?php echo $selectedChapter === (int)$c['chapter_number'] ? 'selected' : ''; ?>>
                    <?php echo __('অধ্যায় ', 'Chapter ') . toLangNum($c['chapter_number']) . ': ' . htmlspecialchars($c['title_bn']); ?>
                  </option>
                <?php endforeach; ?>
              <?php endif; ?>
            </select>

            <!-- Status Filter -->
            <select name="status" class="form-control" style="width: auto; min-width: 140px;" onchange="this.form.submit()">
              <option value="pending" <?php echo $selectedStatus === 'pending' ? 'selected' : ''; ?>><?php echo __('পেন্ডিং', 'Pending'); ?></option>
              <option value="approved" <?php echo $selectedStatus === 'approved' ? 'selected' : ''; ?>><?php echo __('অনুমোদিত', 'Approved'); ?></option>
              <option value="all" <?php echo $selectedStatus === 'all' ? 'selected' : ''; ?>><?php echo __('সকল স্ট্যাটাস', 'All Statuses'); ?></option>
            </select>

            <!-- Search -->
            <div style="position: relative; flex: 1; min-width: 220px;">
              <input type="text" name="search" class="form-control" placeholder="<?php echo __('হাদিস নম্বর, বাংলা বা আরবি টেক্সট দিয়ে খুঁজুন...', 'Search by hadith text or number...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
            </div>

            <button type="submit" class="btn btn-secondary"><?php echo __('ফিল্টার', 'Filter'); ?></button>
          </div>

          <!-- Bulk Action Buttons -->
          <?php if (!empty($stagedItems) && $selectedStatus === 'pending' && $selectedBook !== 'all' && $selectedChapter > 0): ?>
            <div style="display: inline-flex; align-items: center; gap: 8px;">
              <button type="button" class="btn btn-primary" onclick="bulkApproveCurrentChapter('<?php echo htmlspecialchars($selectedBook); ?>', <?php echo $selectedChapter; ?>)" style="background: linear-gradient(135deg, #10b981, #059669); border: none; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);">
                <i class="fa-solid fa-check-double"></i>
                <span><?php echo __('একসাথে অনুমোদন', 'Bulk Approve'); ?> (<?php echo count($stagedItems); ?>)</span>
              </button>
              <button type="button" class="btn btn-danger" onclick="bulkDeleteCurrentChapter('<?php echo htmlspecialchars($selectedBook); ?>', <?php echo $selectedChapter; ?>)" style="box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25);" title="<?php echo __('পেন্ডিং কিউ খালি করুন', 'Clear Pending'); ?>">
                <i class="fa-solid fa-trash-can"></i>
                <span><?php echo __('পেন্ডিং খালি করুন', 'Clear Pending'); ?></span>
              </button>
            </div>
          <?php endif; ?>

        </form>
      </div>
    </div>

    <!-- Staged Items List -->
    <?php if (empty($stagedItems)): ?>
      <div class="card" style="text-align: center; padding: 50px 20px;">
        <div style="font-size: 48px; margin-bottom: 12px;">⏳</div>
        <h3 style="font-size: 18px; font-weight: 700; color: var(--text-heading); margin-bottom: 6px;">
          <?php echo __('কোনো স্ক্র্যাপড হাদিস নেই', 'No Hadiths Found'); ?>
        </h3>
        <p style="font-size: 13.5px; color: var(--text-muted); margin-bottom: 20px;">
          <?php echo __('নির্বাচিত ফিল্টারে কোনো হাদিস পাওয়া যায়নি।', 'No hadiths found for this filter.'); ?>
        </p>
        <button type="button" class="btn btn-primary" onclick="switchTab('scraper')">
          <i class="fa-solid fa-spider"></i> <?php echo __('স্ক্র্যাপার', 'Scraper'); ?>
        </button>
      </div>
    <?php else: ?>
      <div style="display: flex; flex-direction: column; gap: 16px;">
        <?php foreach ($stagedItems as $item): ?>
          <div class="staged-hadith-card" id="stagedRow_<?php echo $item['id']; ?>" style="<?php echo $item['status'] === 'approved' ? 'border-left: 4px solid #10b981;' : 'border-left: 4px solid #f59e0b;'; ?>">
            
            <!-- Card Header: Badges & Actions -->
            <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px; margin-bottom: 14px; padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
              <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
                <span class="badge badge-primary" style="font-size: 13px; font-weight: 800; padding: 5px 12px; background: rgba(16,185,129,0.15); color: var(--primary-dark); border: 1px solid rgba(16,185,129,0.3);">
                  <i class="fa-solid fa-book-bookmark"></i> <?php echo htmlspecialchars($item['book_name_bn'] ?: $item['book_slug']); ?> : নং <?php echo toLangNum($item['hadith_number']); ?>
                </span>
                <span class="badge badge-secondary" style="font-size: 12px;">
                  <i class="fa-solid fa-folder-tree"></i> <?php echo __('অধ্যায় ', 'Chapter ') . toLangNum($item['chapter_number']) . ($item['chapter_title_bn'] ? ': ' . htmlspecialchars($item['chapter_title_bn']) : ''); ?>
                </span>
                <span class="badge badge-success" style="font-size: 12px; font-weight: 700;">
                  <?php echo htmlspecialchars($item['grade'] ?: (isEn() ? 'Sahih' : 'সহীহ')); ?>
                </span>
                <?php if ($item['status'] === 'pending'): ?>
                  <span class="badge badge-warning" style="background: rgba(245, 158, 11, 0.15); color: #d97706; border: 1px solid rgba(245, 158, 11, 0.35); font-weight: 700;">
                    <i class="fa-solid fa-hourglass-half"></i> <?php echo __('পেন্ডিং রিভিউ', 'Pending Review'); ?>
                  </span>
                <?php else: ?>
                  <span class="badge badge-success" style="background: rgba(16, 185, 129, 0.15); color: #059669; border: 1px solid rgba(16, 185, 129, 0.35); font-weight: 700;">
                    <i class="fa-solid fa-circle-check"></i> <?php echo __('অনুমোদিত ও সক্রিয়', 'Approved & Active'); ?>
                  </span>
                <?php endif; ?>
              </div>

              <!-- Action Buttons -->
              <div style="display: inline-flex; align-items: center; gap: 8px;">
                <?php if ($item['status'] === 'pending'): ?>
                  <button type="button" class="btn btn-primary btn-sm" onclick="approveSingleHadith(<?php echo $item['id']; ?>)" style="background: #10b981; border-color: #10b981; padding: 7px 16px; font-weight: 700; box-shadow: 0 2px 8px rgba(16,185,129,0.3);">
                    <i class="fa-solid fa-check"></i> <?php echo __('অনুমোদন', 'Approve'); ?>
                  </button>
                <?php else: ?>
                  <a href="hadiths.php?book=<?php echo urlencode($item['book_slug']); ?>&chapter=<?php echo $item['chapter_number']; ?>&search=<?php echo urlencode($item['hadith_number']); ?>" target="_blank" class="btn btn-primary btn-sm" style="background: linear-gradient(135deg, #0284c7, #0369a1); border: none; font-weight: 700; padding: 7px 14px; display: inline-flex; align-items: center; gap: 6px; box-shadow: 0 2px 8px rgba(2,132,199,0.3);" title="<?php echo __('মূল লাইভ হাদিস ডিরেক্টরিতে এই হাদিসটি দেখুন', 'View in Live Hadiths Directory'); ?>">
                    <i class="fa-solid fa-arrow-up-right-from-square"></i>
                    <span><?php echo __('লাইভ', 'Live'); ?></span>
                  </a>
                <?php endif; ?>

                <button type="button" class="btn btn-secondary btn-sm" onclick="openEditStagedModal(<?php echo htmlspecialchars(json_encode($item)); ?>)" title="<?php echo __('সম্পাদনা', 'Edit'); ?>">
                  <i class="fa-solid fa-pen-to-square text-blue-500"></i>
                </button>

                <button type="button" class="btn btn-danger btn-sm" onclick="deleteStagedHadith(<?php echo $item['id']; ?>)" title="<?php echo __('মুছুন', 'Delete'); ?>">
                  <i class="fa-solid fa-trash-can"></i>
                </button>
              </div>
            </div>

            <!-- Mapping Box -->
            <div style="background: <?php echo $item['status'] === 'approved' ? 'rgba(16, 185, 129, 0.08)' : 'rgba(245, 158, 11, 0.08)'; ?>; border: 1px solid <?php echo $item['status'] === 'approved' ? 'rgba(16, 185, 129, 0.25)' : 'rgba(245, 158, 11, 0.25)'; ?>; border-radius: var(--radius-md); padding: 10px 14px; margin-bottom: 14px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px; font-size: 13px;">
              <div>
                <?php if ($item['status'] === 'approved'): ?>
                  <span style="color: #059669; font-weight: 700;">
                    <i class="fa-solid fa-circle-check"></i> <?php echo __('ম্যাপিং:', 'Mapping:'); ?>
                  </span>
                  <span style="color: var(--text-heading); font-weight: 600; margin-left: 4px;">
                    <?php echo __('গ্রন্থ: ', 'Book: '); ?><strong><?php echo htmlspecialchars($item['book_name_bn'] ?: $item['book_slug']); ?></strong> | 
                    <?php echo __('অধ্যায়: ', 'Chapter: '); ?><strong><?php echo toLangNum($item['chapter_number']); ?></strong> | 
                    <?php echo __('হাদিস নং: ', 'Hadith No: '); ?><strong>#<?php echo toLangNum($item['hadith_number']); ?></strong>
                  </span>
                <?php else: ?>
                  <span style="color: #d97706; font-weight: 700;">
                    <i class="fa-solid fa-bullseye"></i> <?php echo __('লাইভ অবস্থান:', 'Destination:'); ?>
                  </span>
                  <span style="color: var(--text-heading); font-weight: 600; margin-left: 4px;">
                    <?php echo __('গ্রন্থ: ', 'Book: '); ?><strong><?php echo htmlspecialchars($item['book_name_bn'] ?: $item['book_slug']); ?></strong> → 
                    <?php echo __('অধ্যায়: ', 'Chapter: '); ?><strong><?php echo toLangNum($item['chapter_number']); ?></strong> → 
                    <?php echo __('হাদিস নং: ', 'Hadith No: '); ?><strong>#<?php echo toLangNum($item['hadith_number']); ?></strong>
                  </span>
                <?php endif; ?>
              </div>
              
              <?php if ($item['status'] === 'approved'): ?>
                <a href="hadiths.php?book=<?php echo urlencode($item['book_slug']); ?>&chapter=<?php echo $item['chapter_number']; ?>&search=<?php echo urlencode($item['hadith_number']); ?>" target="_blank" style="color: #0284c7; font-weight: 700; text-decoration: underline; font-size: 12.5px; display: inline-flex; align-items: center; gap: 4px;">
                  <i class="fa-solid fa-arrow-up-right-from-square"></i> <?php echo __('লাইভ দেখুন', 'View Live'); ?>
                </a>
              <?php endif; ?>
            </div>

            <!-- Narrators -->
            <?php if (!empty($item['narrator_bn']) || !empty($item['narrator_en'])): ?>
              <div style="margin-bottom: 14px; background: var(--hover-bg); padding: 10px 14px; border-radius: var(--radius-md); border-left: 3px solid var(--primary);">
                <div style="font-weight: 700; color: var(--primary-dark); font-size: 13.5px;">
                  <i class="fa-solid fa-feather mr-1.5"></i> <?php echo htmlspecialchars($item['narrator_bn']); ?>
                </div>
                <?php if (!empty($item['narrator_en'])): ?>
                  <div style="font-size: 12px; color: var(--text-muted); margin-top: 3px;">
                    <?php echo htmlspecialchars($item['narrator_en']); ?>
                  </div>
                <?php endif; ?>
              </div>
            <?php endif; ?>

            <!-- Arabic Text -->
            <?php if (!empty($item['arabic_text'])): ?>
              <div style="font-family: 'Amiri', 'Traditional Arabic', serif; font-size: 19px; line-height: 1.8; color: var(--accent-gold); direction: rtl; text-align: right; margin-bottom: 16px; padding: 12px 16px; background: var(--hover-bg); border-radius: var(--radius-md);">
                <?php echo htmlspecialchars($item['arabic_text']); ?>
              </div>
            <?php endif; ?>

            <!-- Bangla Text -->
            <div style="font-size: 14.5px; line-height: 1.65; color: var(--text-heading); margin-bottom: 14px;">
              <strong style="color: var(--primary);"><?php echo __('অনুবাদ: ', 'Translation: '); ?></strong>
              <?php echo nl2br(htmlspecialchars($item['bangla_text'])); ?>
            </div>

            <!-- English Text -->
            <?php if (!empty($item['english_text'])): ?>
              <div style="font-size: 13px; line-height: 1.55; color: var(--text-muted); margin-bottom: 14px; font-style: italic; background: rgba(16,185,129,0.04); padding: 10px 14px; border-radius: var(--radius-sm); border: 1px solid rgba(16,185,129,0.15);">
                <strong style="color: var(--primary-dark); font-style: normal;"><?php echo __('English: ', 'English: '); ?></strong>
                <?php echo nl2br(htmlspecialchars($item['english_text'])); ?>
              </div>
            <?php endif; ?>

            <!-- Footnotes & Reference -->
            <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px; font-size: 12px; color: var(--text-dim); padding-top: 10px; border-top: 1px dashed var(--border-color);">
              <div>
                <?php if (!empty($item['footnote_bn'])): ?>
                  <span><i class="fa-solid fa-asterisk text-amber-500"></i> <?php echo htmlspecialchars($item['footnote_bn']); ?></span>
                <?php endif; ?>
              </div>
              <div>
                <?php if (!empty($item['reference'])): ?>
                  <span><i class="fa-solid fa-bookmark text-emerald-500"></i> <?php echo htmlspecialchars($item['reference']); ?></span>
                <?php endif; ?>
              </div>
            </div>

          </div>
        <?php endforeach; ?>
      </div>
    <?php endif; ?>

  </div>

</div>

<!-- =========================================================================
     MODAL: EDIT STAGED HADITH
     ========================================================================= -->
<div class="modal-backdrop" id="editStagedModal">
  <div class="modal-window" style="max-width: 840px;">
    <div class="modal-header" style="background: linear-gradient(135deg, rgba(16, 185, 129, 0.12), rgba(6, 78, 59, 0.2));">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-pen-to-square text-emerald-500"></i>
        <span id="stagedEditTitle"><?php echo __('হাদিস সম্পাদনা', 'Edit Hadith'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('editStagedModal')">&times;</button>
    </div>

    <form id="formEditStaged" onsubmit="handleSaveEditStaged(event)">
      <input type="hidden" name="id" id="editStagedId" value="">

      <div class="modal-body" style="max-height: 75vh; overflow-y: auto; padding: 20px;">
        
        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('বর্ণনাকারী', 'Narrator'); ?></label>
            <input type="text" name="narrator_bn" id="editHNarratorBn" class="form-control">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ইংরেজি বর্ণনাকারী', 'English Narrator'); ?></label>
            <input type="text" name="narrator_en" id="editHNarratorEn" class="form-control">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('আরবি পাঠ', 'Arabic Text'); ?></label>
          <textarea name="arabic_text" id="editHArabic" class="form-control arabic-input" rows="4" style="font-size: 18px; line-height: 1.8; font-family: 'Amiri', 'Traditional Arabic', serif;" required></textarea>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('অনুবাদ', 'Translation'); ?></label>
          <textarea name="bangla_text" id="editHBangla" class="form-control" rows="4" required></textarea>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('ইংরেজি অনুবাদ', 'English Translation'); ?></label>
          <textarea name="english_text" id="editHEnglish" class="form-control" rows="4"></textarea>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('ফুটনোট ও তাখরীজ', 'Footnote & Takhrij'); ?></label>
            <textarea name="footnote_bn" id="editHFootnoteBn" class="form-control" rows="2"></textarea>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ইংরেজি ফুটনোট', 'English Footnote'); ?></label>
            <textarea name="footnote_en" id="editHFootnoteEn" class="form-control" rows="2"></textarea>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px; margin-bottom: 0;">
          <div class="form-group">
            <label class="form-label"><?php echo __('হাদিসের মান', 'Grade'); ?></label>
            <input type="text" name="grade" id="editHGrade" class="form-control" value="সহীহ">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('রেফারেন্স', 'Reference'); ?></label>
            <input type="text" name="reference" id="editHRef" class="form-control">
          </div>
        </div>

      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('editStagedModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary px-6">
          <i class="fa-solid fa-floppy-disk"></i>
          <span><?php echo __('সংরক্ষণ', 'Save'); ?></span>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
const allChaptersGrouped = <?php echo json_encode($allChaptersGrouped); ?>;

// Real-time live auto-translation on edit modal
document.addEventListener('DOMContentLoaded', function() {
  if (typeof setupLiveTranslate === 'function') {
    setupLiveTranslate('editHNarratorBn', 'editHNarratorEn');
    setupLiveTranslate('editHBangla', 'editHEnglish');
    setupLiveTranslate('editHFootnoteBn', 'editHFootnoteEn');
  }
});

function switchTab(tab) {
  if (tab === 'scraper') {
    document.getElementById('tabScraperSection').style.display = 'block';
    document.getElementById('tabPendingSection').style.display = 'none';
  } else {
    document.getElementById('tabScraperSection').style.display = 'none';
    document.getElementById('tabPendingSection').style.display = 'block';
  }
  const url = new URL(window.location.href);
  url.searchParams.set('tab', tab);
  window.history.replaceState({}, '', url);
}

function onFilterBookChange(bookSlug) {
  const chapSelect = document.getElementById('filterChapterSelect');
  chapSelect.innerHTML = '<option value="0"><?php echo __('সকল অধ্যায়', 'All Chapters'); ?></option>';
  
  if (bookSlug !== 'all' && allChaptersGrouped[bookSlug]) {
    allChaptersGrouped[bookSlug].forEach(c => {
      const opt = document.createElement('option');
      opt.value = c.chapter_number;
      opt.innerText = 'অধ্যায় ' + c.chapter_number + ': ' + c.title_bn;
      chapSelect.appendChild(opt);
    });
  }
  document.getElementById('filterPendingForm').submit();
}

function onBookChangeForScrape(bookSlug) {
  const chaps = allChaptersGrouped[bookSlug] || [];
  if (chaps.length > 0) {
    document.getElementById('scrapeChapterNum').value = chaps[0].chapter_number;
  }
}

function onBookChangeForCrawl(bookSlug) {
  const chaps = allChaptersGrouped[bookSlug] || [];
  if (chaps.length > 0) {
    document.getElementById('crawlChapterNum').value = chaps[0].chapter_number;
    document.getElementById('displayChapterTitlePreview').innerText = chaps[0].title_bn + ' (' + chaps[0].hadith_range_bn + ')';
  }
}

function updateChapterPreview(num) {
  const bookSlug = document.getElementById('crawlBookSlug').value;
  const chaps = allChaptersGrouped[bookSlug] || [];
  const chapObj = chaps.find(c => parseInt(c.chapter_number) === parseInt(num));
  if (chapObj) {
    document.getElementById('displayChapterTitlePreview').innerText = chapObj.title_bn + ' (' + chapObj.hadith_range_bn + ')';
  } else {
    document.getElementById('displayChapterTitlePreview').innerText = 'অধ্যায় #' + num;
  }
}

// 1. Handle HadithBD Content Scraping
async function handleHadithBdScrape(e) {
  e.preventDefault();
  const btnText = document.getElementById('btnScrapeSubmitText');
  const original = btnText.innerText;
  btnText.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> পার্স ও অনুবাদ হচ্ছে...';

  const formData = new FormData(document.getElementById('formHadithBdParser'));
  formData.append('action', 'parse_raw_content');

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: formData });
    const data = await res.json();
    if (data.success) {
      alert(data.message);
      window.location.href = 'hadith_scraper.php?tab=pending&book=' + encodeURIComponent(formData.get('book_slug')) + '&chapter=' + encodeURIComponent(formData.get('chapter_number'));
    } else {
      alert('ত্রুটি: ' + (data.error || 'স্ক্র্যাপ সম্পন্ন হতে পারেনি।'));
      btnText.innerText = original;
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার রিকোয়েস্ট ব্যর্থ হয়েছে।');
    btnText.innerText = original;
  }
}

// 2. Handle 1-Click Auto Chapter Crawler
async function handleAutoChapterCrawl(e) {
  e.preventDefault();
  const btnText = document.getElementById('btnCrawlSubmitText');
  const original = btnText.innerText;
  btnText.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> অধ্যায় ক্রল ও প্রসেসিং হচ্ছে...';

  const formData = new FormData(document.getElementById('formAutoChapterCrawler'));
  const bookSlug = formData.get('crawl_book_slug');
  const chapNum = formData.get('crawl_chapter_number');

  const postData = new FormData();
  postData.append('action', 'crawl_online_chapter');
  postData.append('book_slug', bookSlug);
  postData.append('chapter_number', chapNum);

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: postData });
    const data = await res.json();
    if (data.success) {
      alert(data.message);
      window.location.href = 'hadith_scraper.php?tab=pending&book=' + encodeURIComponent(bookSlug) + '&chapter=' + encodeURIComponent(chapNum);
    } else {
      alert('ত্রুটি: ' + (data.error || 'ক্রলিং ব্যর্থ হয়েছে।'));
      btnText.innerText = original;
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার রিকোয়েস্ট ব্যর্থ হয়েছে।');
    btnText.innerText = original;
  }
}

// 3. Single Hadith Approval
async function approveSingleHadith(stagingId) {
  if (!confirm('আপনি কি এই হাদিসটি অনুমোদন করে দীন ওয়ান মূল লাইভ ডাটাবেজে (hadith_items) যুক্ত করতে চান?')) return;

  const fd = new FormData();
  fd.append('action', 'approve_single');
  fd.append('staging_id', stagingId);

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: fd });
    const data = await res.json();
    if (data.success) {
      const liveTargetUrl = data.live_url || ('hadiths.php?book=' + encodeURIComponent(data.book_slug) + '&chapter=' + data.chapter_number + '&search=' + encodeURIComponent(data.hadith_number));
      if (confirm('✓ ' + data.message + '\n\nআপনি কি এখনই মূল লাইভ হাদিস ডিরেক্টরিতে গিয়ে এটি যাচাই করতে চান?')) {
        window.location.href = liveTargetUrl;
      } else {
        window.location.reload();
      }
    } else {
      alert('অনুমোদন ব্যর্থ: ' + data.error);
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার এরর');
  }
}

// 4. Bulk Approve Full Chapter
async function bulkApproveCurrentChapter(bookSlug, chapterNum) {
  if (!confirm('আপনি কি নিশ্চিত যে এই অধ্যায়ের সকল পেন্ডিং হাদিস একসাথে অনুমোদন করে দীন ওয়ান লাইভ ডাটাবেজে যুক্ত করতে চান?')) return;

  const fd = new FormData();
  fd.append('action', 'bulk_approve_chapter');
  fd.append('book_slug', bookSlug);
  fd.append('chapter_number', chapterNum);

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: fd });
    const data = await res.json();
    if (data.success) {
      const liveTargetUrl = data.live_url || ('hadiths.php?book=' + encodeURIComponent(data.book_slug) + '&chapter=' + data.chapter_number);
      if (confirm('✓ ' + data.message + '\n\nআপনি কি এখনই লাইভ হাদিস ডিরেক্টরিতে গিয়ে সম্পূর্ণ অধ্যায়টি দেখতে চান?')) {
        window.location.href = liveTargetUrl;
      } else {
        window.location.reload();
      }
    } else {
      alert('বাল্ক অনুমোদন ব্যর্থ: ' + data.error);
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার এরর');
  }
}

// 4.1 Bulk Clear Pending for Current Chapter
async function bulkDeleteCurrentChapter(bookSlug, chapterNum) {
  if (!confirm('আপনি কি নিশ্চিত যে এই অধ্যায়ের সকল পেন্ডিং হাদিস মুছে ফেলে কিউ খালি করতে চান?')) return;

  const fd = new FormData();
  fd.append('action', 'bulk_delete_chapter');
  fd.append('book_slug', bookSlug);
  fd.append('chapter_number', chapterNum);

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: fd });
    const data = await res.json();
    if (data.success) {
      alert('✓ ' + data.message);
      window.location.reload();
    } else {
      alert('মুছতে ব্যর্থ: ' + data.error);
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার এরর');
  }
}

// 5. Edit Staged Hadith Modal
function openEditStagedModal(item) {
  document.getElementById('editStagedId').value = item.id;
  document.getElementById('stagedEditTitle').innerText = 'স্ক্র্যাপড হাদিস #' + item.hadith_number + ' সম্পাদনা';
  document.getElementById('editHNarratorBn').value = item.narrator_bn || '';
  document.getElementById('editHNarratorEn').value = item.narrator_en || '';
  document.getElementById('editHArabic').value = item.arabic_text || '';
  document.getElementById('editHBangla').value = item.bangla_text || '';
  document.getElementById('editHEnglish').value = item.english_text || '';
  document.getElementById('editHFootnoteBn').value = item.footnote_bn || '';
  document.getElementById('editHFootnoteEn').value = item.footnote_en || '';
  document.getElementById('editHGrade').value = item.grade || '<?php echo isEn() ? "Sahih" : "সহীহ"; ?>';
  document.getElementById('editHRef').value = item.reference || '';

  openModal('editStagedModal');
}

async function handleSaveEditStaged(e) {
  e.preventDefault();
  const fd = new FormData(document.getElementById('formEditStaged'));
  fd.append('action', 'update_staged');

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: fd });
    const data = await res.json();
    if (data.success) {
      alert(data.message);
      closeModal('editStagedModal');
      window.location.reload();
    } else {
      alert('আপডেট ব্যর্থ: ' + data.error);
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার এরর');
  }
}

// 6. Delete Staged Hadith
async function deleteStagedHadith(stagingId) {
  if (!confirm('আপনি কি নিশ্চিত যে এই স্ক্র্যাপড হাদিসটি মুছে ফেলতে চান?')) return;

  const fd = new FormData();
  fd.append('action', 'delete_staged');
  fd.append('staging_id', stagingId);

  try {
    const res = await fetch('api_hadith_scraper.php', { method: 'POST', body: fd });
    const data = await res.json();
    if (data.success) {
      const row = document.getElementById('stagedRow_' + stagingId);
      if (row) row.remove();
    } else {
      alert('মুছতে ব্যর্থ: ' + data.error);
    }
  } catch (err) {
    console.error(err);
    alert('সার্ভার এরর');
  }
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
