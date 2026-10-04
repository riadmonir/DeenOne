/**
 * ==============================================================================
 * DEEN ONE ADMIN - ADVANCED DYNAMIC JAVASCRIPT & MICRO-INTERACTIONS
 * Integrated with SweetAlert2, Chart.js, Canvas Confetti & Modern Utilities
 * ==============================================================================
 */

document.addEventListener('DOMContentLoaded', () => {
  // 0. Initialize Theme & Font Mode
  initTheme();
  initAdminFont();

  // 1. Mobile Sidebar Toggle & Backdrop Dismiss
  const toggleBtn = document.querySelector('.btn-mobile-toggle');
  const sidebar = document.querySelector('.sidebar');
  const backdrop = document.getElementById('sidebarBackdrop');

  const closeSidebar = () => {
    if (sidebar) sidebar.classList.remove('open');
    if (backdrop) backdrop.classList.remove('open');
    document.body.classList.remove('sidebar-drawer-active');
  };

  const openSidebar = () => {
    if (sidebar) sidebar.classList.add('open');
    if (backdrop) backdrop.classList.add('open');
    document.body.classList.add('sidebar-drawer-active');
  };

  if (toggleBtn && sidebar) {
    toggleBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      if (sidebar.classList.contains('open')) {
        closeSidebar();
      } else {
        openSidebar();
      }
    });

    if (backdrop) {
      backdrop.addEventListener('click', closeSidebar);
    }

    document.addEventListener('click', (e) => {
      if (!sidebar.contains(e.target) && !toggleBtn.contains(e.target) && sidebar.classList.contains('open')) {
        closeSidebar();
      }
    });
  }

  // 2. Ultra-Smooth Live Dynamic Clock with Client Location
  const clockEl = document.getElementById('liveClock');
  if (clockEl) {
    const bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    const toBnDigits = (str) => str.replace(/[0-9]/g, d => bnDigits[d]);
    
    // Resolve client timezone & friendly location
    let userTz = 'Asia/Dhaka';
    try {
      userTz = Intl.DateTimeFormat().resolvedOptions().timeZone || 'Asia/Dhaka';
    } catch (e) {}

    let rawCity = userTz.split('/').pop().replace(/_/g, ' ');
    const tzCityBnMap = {
      'Riyadh': 'রিয়াদ',
      'Jeddah': 'জেদ্দা',
      'Makkah': 'মক্কা',
      'Medina': 'মদিনা',
      'Dhaka': 'ঢাকা',
      'Dubai': 'দুবাই',
      'Qatar': 'কাতার',
      'Kuwait': 'কুয়েত',
      'Bahrain': 'বাহরাইন',
      'London': 'লন্ডন',
      'New York': 'নিউ ইয়র্ক',
      'Kolkata': 'কলকাতা',
      'Karachi': 'করাচি',
      'Kuala Lumpur': 'কুয়ালালামপুর',
      'Singapore': 'সিঙ্গাপুর'
    };
    const cityBn = tzCityBnMap[rawCity] || rawCity;

    const updateClock = () => {
      const now = new Date();
      const isEn = document.documentElement.lang === 'en';
      const timeStr = now.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true
      });
      if (isEn) {
        clockEl.textContent = `${timeStr} (${rawCity})`;
      } else {
        clockEl.textContent = `${toBnDigits(timeStr)} (${cityBn})`;
      }
    };
    updateClock();
    setInterval(updateClock, 1000);
  }

  // 3. Alert auto-dismiss with smooth slide-fade
  document.querySelectorAll('.alert-close').forEach(btn => {
    btn.addEventListener('click', function() {
      const alert = this.closest('.alert');
      if (alert) {
        alert.style.transition = 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
        alert.style.opacity = '0';
        alert.style.transform = 'translateY(-10px)';
        setTimeout(() => alert.remove(), 300);
      }
    });
  });

  // 4. Auto-resize Bengali & Arabic Textareas
  document.querySelectorAll('textarea').forEach(tx => {
    tx.addEventListener('input', function() {
      this.style.height = 'auto';
      this.style.height = (this.scrollHeight + 4) + 'px';
    });
  });

  // 5. Global Keyboard Shortcuts (Ctrl + K Spotlight Search)
  document.addEventListener('keydown', (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
      e.preventDefault();
      openCommandPalette();
    } else if (e.key === 'Escape') {
      closeCommandPalette();
    }
  });

  // 6. Command Palette Live Filter
  const paletteInput = document.getElementById('paletteSearchInput');
  const paletteResults = document.getElementById('paletteResults');
  if (paletteInput && paletteResults) {
    paletteInput.addEventListener('input', () => {
      const query = paletteInput.value.toLowerCase().trim();
      const items = paletteResults.querySelectorAll('.palette-item');
      items.forEach(item => {
        const text = item.textContent.toLowerCase();
        if (text.includes(query)) {
          item.style.display = 'flex';
        } else {
          item.style.display = 'none';
        }
      });
    });
  }

  // 7. Animated Number Counters (Count-up easing effect)
  const counters = document.querySelectorAll('.counter-number');
  counters.forEach(counter => {
    const target = +counter.getAttribute('data-target') || 0;
    if (target === 0) return;
    const duration = 1200; // ms
    const stepTime = 20;
    const steps = duration / stepTime;
    const increment = target / steps;
    let current = 0;

    const timer = setInterval(() => {
      current += increment;
      if (current >= target) {
        counter.textContent = Number(target).toLocaleString('en-US');
        clearInterval(timer);
      } else {
        counter.textContent = Math.floor(current).toLocaleString('en-US');
      }
    }, stepTime);
  });
});

// Command Palette (Spotlight Search) Controls
function openCommandPalette() {
  const modal = document.getElementById('commandPaletteModal');
  const input = document.getElementById('paletteSearchInput');
  if (modal) {
    modal.style.display = 'flex';
    modal.classList.add('open');
    if (input) {
      input.value = '';
      setTimeout(() => input.focus(), 50);
    }
    document.body.style.overflow = 'hidden';
  }
}

function closeCommandPalette() {
  const modal = document.getElementById('commandPaletteModal');
  if (modal) {
    modal.style.display = 'none';
    modal.classList.remove('open');
    document.body.style.overflow = '';
  }
}

// Modal Management Helpers
function openModal(modalId) {
  if (!modalId) return;
  const modal = typeof modalId === 'string' ? document.getElementById(modalId) : modalId;
  if (!modal) return;

  if (typeof bootstrap !== 'undefined' && modal.classList.contains('modal') && !modal.classList.contains('modal-backdrop')) {
    try {
      let inst = bootstrap.Modal.getInstance(modal);
      if (!inst) inst = new bootstrap.Modal(modal);
      inst.show();
      return;
    } catch (e) {}
  }

  modal.classList.add('open');
  if (modal.classList.contains('modal')) {
    modal.style.display = 'block';
  }
  document.body.style.overflow = 'hidden';
}

function closeModal(modalId) {
  if (!modalId) {
    document.querySelectorAll('.modal-backdrop.open, .modal.open, .modal[style*="display: block"]').forEach(m => {
      m.classList.remove('open');
      if (m.classList.contains('modal') && !m.classList.contains('modal-backdrop')) {
        m.style.display = 'none';
      }
    });
    document.body.style.overflow = '';
    return;
  }
  const modal = typeof modalId === 'string' ? document.getElementById(modalId) : modalId;
  if (!modal) return;

  if (typeof bootstrap !== 'undefined' && modal.classList.contains('modal') && !modal.classList.contains('modal-backdrop')) {
    try {
      const inst = bootstrap.Modal.getInstance(modal);
      if (inst) {
        inst.hide();
        return;
      }
    } catch (e) {}
  }

  modal.classList.remove('open');
  if (modal.classList.contains('modal') && !modal.classList.contains('modal-backdrop')) {
    modal.style.display = 'none';
  }
  document.body.style.overflow = '';
}

// Close modal when clicking backdrop
document.addEventListener('click', (e) => {
  if (e.target.classList.contains('modal-backdrop') || e.target.classList.contains('command-palette-backdrop')) {
    e.target.classList.remove('open');
    if (e.target.classList.contains('modal') && !e.target.classList.contains('modal-backdrop')) {
      e.target.style.display = 'none';
    }
    document.body.style.overflow = '';
  }
});

// Enterprise SweetAlert2 Confirmation Dialog
function confirmAction(event, url, message = 'আপনি কি এই কাজটি সম্পন্ন করতে নিশ্চিত?') {
  if (event) event.preventDefault();

  if (typeof Swal !== 'undefined') {
    Swal.fire({
      title: 'নিশ্চিতকরণ',
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#10b981',
      cancelButtonColor: '#ef4444',
      confirmButtonText: 'হ্যাঁ, নিশ্চিত!',
      cancelButtonText: 'বাতিল',
      background: '#132247',
      color: '#f8fafc',
      customClass: {
        popup: 'animated fadeInDown'
      }
    }).then((result) => {
      if (result.isConfirmed) {
        if (url) {
          window.location.href = url;
        } else if (event && event.target && event.target.closest('form')) {
          event.target.closest('form').submit();
        }
      }
    });
    return false;
  } else {
    if (confirm(message)) {
      if (url) window.location.href = url;
      return true;
    }
    return false;
  }
}

// Quick Delete Confirmation Dialog
function confirmDelete(url, message = 'আপনি কি নিশ্চিত যে এই রেকর্ডটি মুছে ফেলতে চান?') {
  confirmAction(null, url, message);
}

// Live Real-Time Table Search Filter with Highlighting
function filterTable(inputId, tableId) {
  const input = document.getElementById(inputId);
  const table = document.getElementById(tableId);
  if (!input || !table) return;

  const filter = input.value.toLowerCase().trim();
  const rows = table.getElementsByTagName('tr');

  for (let i = 1; i < rows.length; i++) {
    const row = rows[i];
    const text = row.textContent || row.innerText;
    if (text.toLowerCase().indexOf(filter) > -1) {
      row.style.display = '';
    } else {
      row.style.display = 'none';
    }
  }
}

// Universal CSV Data Export Tool
function exportTableToCSV(tableId, filename = 'export_data.csv') {
  const table = document.getElementById(tableId);
  if (!table) return;

  let csv = [];
  const rows = table.querySelectorAll('tr');

  rows.forEach(row => {
    let rowData = [];
    const cols = row.querySelectorAll('td, th');
    cols.forEach(col => {
      // Clean inner text and escape quotes
      let text = col.innerText.replace(/"/g, '""').trim();
      rowData.push('"' + text + '"');
    });
    csv.push(rowData.join(','));
  });

  const csvFile = new Blob(['\uFEFF' + csv.join('\n')], { type: 'text/csv;charset=utf-8;' });
  const downloadLink = document.createElement('a');
  downloadLink.download = filename;
  downloadLink.href = window.URL.createObjectURL(csvFile);
  downloadLink.style.display = 'none';
  document.body.appendChild(downloadLink);
  downloadLink.click();
  document.body.removeChild(downloadLink);

  showToast('📥 ডাটা সফলভাবে CSV ফাইলে ডাউনলোড হয়েছে!', 'success');
  if (typeof confetti !== 'undefined') {
    confetti({ particleCount: 60, spread: 60, origin: { y: 0.8 } });
  }
}

// Toast Notification System
function showToast(message, type = 'success') {
  const container = document.getElementById('toastContainer');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = 'toast-message';
  const icon = type === 'success' ? '✨' : (type === 'danger' ? '⚠️' : '🔔');
  toast.innerHTML = `<span>${icon}</span><span>${message}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.transition = 'all 0.3s ease';
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(-10px)';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

// Copy Text to Clipboard with Instant Feedback
function copyToClipboard(text, successMsg = 'সফলভাবে ক্লিপবোর্ডে কপি হয়েছে!') {
  if (navigator.clipboard) {
    navigator.clipboard.writeText(text).then(() => {
      showToast(successMsg, 'success');
    });
  } else {
    const tempInput = document.createElement('input');
    tempInput.value = text;
    document.body.appendChild(tempInput);
    tempInput.select();
    document.execCommand('copy');
    document.body.removeChild(tempInput);
    showToast(successMsg, 'success');
  }
}

// ==============================================================================
// 8. THEME MODE CONTROLS (LIGHT MODE DEFAULT <-> DARK MODE TOGGLE - SYNCED WITH LANDING PAGE)
// ==============================================================================
function getThemeCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

function initTheme() {
  const currentTheme = localStorage.getItem('deenone_admin_theme') || 
                       localStorage.getItem('deenone_theme') || 
                       getThemeCookie('deenone_admin_theme') || 
                       getThemeCookie('deenone_theme') || 
                       'light';
  applyTheme(currentTheme === 'dark');
}

function toggleTheme() {
  const isDark = document.documentElement.classList.contains('dark') || 
                 document.documentElement.getAttribute('data-theme') === 'dark';
  const targetDark = !isDark;
  applyTheme(targetDark);
  const themeVal = targetDark ? 'dark' : 'light';
  localStorage.setItem('deenone_admin_theme', themeVal);
  localStorage.setItem('deenone_theme', themeVal);
  document.cookie = `deenone_admin_theme=${themeVal};path=/;max-age=31536000`;
  document.cookie = `deenone_theme=${themeVal};path=/;max-age=31536000`;
}

function applyTheme(isDark) {
  const themeVal = isDark ? 'dark' : 'light';
  document.documentElement.setAttribute('data-theme', themeVal);
  if (isDark) {
    document.documentElement.classList.add('dark');
    if (document.body) document.body.classList.add('theme-dark');
  } else {
    document.documentElement.classList.remove('dark');
    if (document.body) document.body.classList.remove('theme-dark');
  }
  updateThemeUI(isDark);
}

function updateThemeUI(isDark) {
  const iconEl = document.getElementById('themeToggleIcon');
  const textEl = document.getElementById('themeToggleText');
  const isEn = document.documentElement.lang === 'en';
  if (iconEl) {
    if (isDark) {
      iconEl.className = 'fa-solid fa-moon';
      iconEl.style.color = '#38bdf8';
    } else {
      iconEl.className = 'fa-solid fa-sun';
      iconEl.style.color = '#f59e0b';
    }
  }
  if (textEl) {
    if (isEn) {
      textEl.textContent = isDark ? 'Dark Mode' : 'Light Mode';
    } else {
      textEl.textContent = isDark ? 'ডার্ক মোড' : 'লাইট মোড';
    }
  }
}

// ==============================================================================
// 9. ZERO-DEPENDENCY MODAL ENGINE (openModal & closeModal)
// ==============================================================================
window.openModal = function(id) {
  const modal = document.getElementById(id);
  if (modal) {
    modal.style.display = 'flex';
    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
  }
};

window.closeModal = function(id) {
  const modal = document.getElementById(id);
  if (modal) {
    modal.classList.remove('open');
    modal.style.display = 'none';
    document.body.style.overflow = '';
  }
};

// ==============================================================================
// 10. ACCORDION SIDEBAR & TOPBAR DROPDOWN CONTROLS
// ==============================================================================
function toggleAccordion(buttonEl) {
  if (!buttonEl) return;
  const parentGroup = buttonEl.closest('.nav-accordion-group');
  if (!parentGroup) return;

  const isOpen = parentGroup.classList.contains('open');
  
  // Toggle current accordion group
  if (isOpen) {
    parentGroup.classList.remove('open');
  } else {
    parentGroup.classList.add('open');
  }
}

function toggleTopDropdown(menuId, event) {
  if (event) {
    event.stopPropagation();
  }
  const targetMenu = document.getElementById(menuId);
  if (!targetMenu) return;

  const isShown = targetMenu.classList.contains('show');

  // Close all open topbar dropdowns
  document.querySelectorAll('.topbar-dropdown-menu.show').forEach(m => {
    if (m !== targetMenu) {
      m.classList.remove('show');
      if (m.parentElement) m.parentElement.classList.remove('show');
    }
  });

  if (isShown) {
    targetMenu.classList.remove('show');
    if (targetMenu.parentElement) targetMenu.parentElement.classList.remove('show');
  } else {
    targetMenu.classList.add('show');
    if (targetMenu.parentElement) targetMenu.parentElement.classList.add('show');
  }
}

// Close topbar dropdowns when clicking anywhere outside
document.addEventListener('click', (e) => {
  if (!e.target.closest('.topbar-dropdown')) {
    document.querySelectorAll('.topbar-dropdown-menu.show').forEach(m => {
      m.classList.remove('show');
      if (m.parentElement) m.parentElement.classList.remove('show');
    });
  }
});

// ==============================================================================
// 11. DYNAMIC FONT FAMILY ENGINE & LIVE PREVIEW (Inter, Noto Sans Bengali, Arial, Roboto, Poppins, Outfit, Plus Jakarta Sans, etc.)
// ==============================================================================
// 12. MULTI-LANGUAGE TYPOGRAPHY SUITE (ENGLISH, BENGALI, ARABIC)
// ==============================================================================
const ADMIN_ENGLISH_FONT_MAP = {
  'inter': "'Inter', sans-serif",
  'plus_jakarta_sans': "'Plus Jakarta Sans', sans-serif",
  'roboto': "'Roboto', sans-serif",
  'poppins': "'Poppins', sans-serif",
  'outfit': "'Outfit', sans-serif",
  'arial': "Arial, Helvetica, sans-serif"
};

const ADMIN_BANGLA_FONT_MAP = {
  'noto_sans_bengali': "'Noto Sans Bengali', sans-serif",
  'hind_siliguri': "'Hind Siliguri', sans-serif",
  'anek_bangla': "'Anek Bangla', sans-serif",
  'noto_serif_bengali': "'Noto Serif Bengali', serif",
  'kalpurush': "'Kalpurush', 'SolaimanLipi', 'Noto Sans Bengali', sans-serif"
};

const ADMIN_ARABIC_FONT_MAP = {
  'amiri': "'Amiri', serif",
  'scheherazade': "'Scheherazade New', 'Amiri', serif"
};

window.updateAdminMainFont = function() {
  const en = document.documentElement.style.getPropertyValue('--font-english') || "'Roboto', sans-serif";
  const bn = document.documentElement.style.getPropertyValue('--font-bengali') || "'Kalpurush', sans-serif";
  const ar = document.documentElement.style.getPropertyValue('--font-arabic') || "'Amiri', serif";
  const mainFont = `${en}, ${bn}, ${ar}, -apple-system, BlinkMacSystemFont, sans-serif`;
  document.documentElement.style.setProperty('--font-main', mainFont);
  if (document.body) {
    document.body.style.fontFamily = mainFont;
  }
};

window.setAdminFont = function(fontKey, save = true) {
  if (!fontKey || !ADMIN_ENGLISH_FONT_MAP[fontKey]) return;
  document.documentElement.style.setProperty('--font-english', ADMIN_ENGLISH_FONT_MAP[fontKey]);
  window.updateAdminMainFont();
  if (save) {
    localStorage.setItem('deenone_admin_font', fontKey);
  }
  const preview = document.getElementById('previewBoxEnglish');
  if (preview) {
    preview.style.fontFamily = ADMIN_ENGLISH_FONT_MAP[fontKey];
  }
  const label = document.getElementById('labelCurrentEnglish');
  if (label) {
    label.textContent = fontKey.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
  }
};

window.setAdminBanglaFont = function(fontKey, save = true) {
  if (!fontKey || !ADMIN_BANGLA_FONT_MAP[fontKey]) return;
  document.documentElement.style.setProperty('--font-bengali', ADMIN_BANGLA_FONT_MAP[fontKey]);
  window.updateAdminMainFont();
  if (save) {
    localStorage.setItem('deenone_admin_bangla_font', fontKey);
  }
  const preview = document.getElementById('previewBoxBangla');
  if (preview) {
    preview.style.fontFamily = ADMIN_BANGLA_FONT_MAP[fontKey];
  }
  const label = document.getElementById('labelCurrentBangla');
  if (label) {
    label.textContent = fontKey.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
  }
};

window.setAdminArabicFont = function(fontKey, save = true) {
  if (!fontKey || !ADMIN_ARABIC_FONT_MAP[fontKey]) return;
  document.documentElement.style.setProperty('--font-arabic', ADMIN_ARABIC_FONT_MAP[fontKey]);
  window.updateAdminMainFont();
  if (save) {
    localStorage.setItem('deenone_admin_arabic_font', fontKey);
  }
  const preview = document.getElementById('previewBoxArabic');
  if (preview) {
    preview.style.fontFamily = ADMIN_ARABIC_FONT_MAP[fontKey];
  }
  const label = document.getElementById('labelCurrentArabic');
  if (label) {
    label.textContent = fontKey.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
  }
};

window.initAdminFont = function() {
  const savedEn = localStorage.getItem('deenone_admin_font');
  if (savedEn && ADMIN_ENGLISH_FONT_MAP[savedEn]) {
    document.documentElement.style.setProperty('--font-english', ADMIN_ENGLISH_FONT_MAP[savedEn]);
    const selEn = document.getElementById('select_admin_font_family');
    if (selEn) selEn.value = savedEn;
    const labelEn = document.getElementById('labelCurrentEnglish');
    if (labelEn) labelEn.textContent = savedEn.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
    const prevEn = document.getElementById('previewBoxEnglish');
    if (prevEn) prevEn.style.fontFamily = ADMIN_ENGLISH_FONT_MAP[savedEn];
  }
  const savedBn = localStorage.getItem('deenone_admin_bangla_font');
  if (savedBn && ADMIN_BANGLA_FONT_MAP[savedBn]) {
    document.documentElement.style.setProperty('--font-bengali', ADMIN_BANGLA_FONT_MAP[savedBn]);
    const selBn = document.getElementById('select_admin_bangla_font');
    if (selBn) selBn.value = savedBn;
    const labelBn = document.getElementById('labelCurrentBangla');
    if (labelBn) labelBn.textContent = savedBn.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
    const prevBn = document.getElementById('previewBoxBangla');
    if (prevBn) prevBn.style.fontFamily = ADMIN_BANGLA_FONT_MAP[savedBn];
  }
  const savedAr = localStorage.getItem('deenone_admin_arabic_font');
  if (savedAr && ADMIN_ARABIC_FONT_MAP[savedAr]) {
    document.documentElement.style.setProperty('--font-arabic', ADMIN_ARABIC_FONT_MAP[savedAr]);
    const selAr = document.getElementById('select_admin_arabic_font');
    if (selAr) selAr.value = savedAr;
    const labelAr = document.getElementById('labelCurrentArabic');
    if (labelAr) labelAr.textContent = savedAr.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
    const prevAr = document.getElementById('previewBoxArabic');
    if (prevAr) prevAr.style.fontFamily = ADMIN_ARABIC_FONT_MAP[savedAr];
  }
  window.updateAdminMainFont();
};




