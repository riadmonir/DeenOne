    </main>
  </div>
</div>

<!-- Modern Enterprise JS Libraries -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/canvas-confetti@1.9.2/dist/confetti.browser.min.js"></script>

<!-- DeenOne Core Admin JavaScript -->
<?php 
  $rawBase = dirname($_SERVER['SCRIPT_NAME'] ?? '/admin');
  $adminBase = rtrim(str_replace('\\', '/', $rawBase), '/');
  if ($adminBase === '' || $adminBase === '.') $adminBase = '';
  $jsFile = __DIR__ . '/assets/js/admin.js';
  $jsVersion = file_exists($jsFile) ? filemtime($jsFile) : time();
  $basePrefix = ($adminBase !== '' ? $adminBase : '.');
?>
<script src="<?php echo htmlspecialchars($basePrefix); ?>/assets/js/admin.js?v=<?php echo $jsVersion; ?>"></script>
<script src="<?php echo htmlspecialchars($basePrefix); ?>/assets/js/auto_translate.js?v=<?php echo $jsVersion; ?>"></script>
</body>
</html>

