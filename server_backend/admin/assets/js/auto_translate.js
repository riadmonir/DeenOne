/**
 * DeenOne Admin Real-Time Auto-Translate Engine
 * 100% Free, Zero API Key, Ultra-Fast Real-Time Neural Translation (Bengali to English)
 */

// In-memory instant translation cache
const _transCache = new Map();
const _abortControllers = new Map();

/**
 * Perform ultra-fast neural translation with local caching and dual fallback
 * @param {string} text 
 * @param {string} [sl='bn'] Source language
 * @param {string} [tl='en'] Target language
 * @param {AbortSignal} [signal]
 * @returns {Promise<string>}
 */
async function autoTranslate(text, sl = 'bn', tl = 'en', signal = null) {
  if (!text || !text.trim()) return '';
  const clean = text.trim();
  const cacheKey = `${sl}_${tl}_${clean}`;

  // Instant 0ms cache check
  if (_transCache.has(cacheKey)) {
    return _transCache.get(cacheKey);
  }

  // 1. Direct Google Dict Client API (Ultra-fast, ~50-90ms client-side)
  try {
    const url = 'https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=' + encodeURIComponent(sl) + '&tl=' + encodeURIComponent(tl) + '&q=' + encodeURIComponent(clean);
    const fetchOptions = signal ? { signal } : {};
    const res = await fetch(url, fetchOptions);
    if (res.ok) {
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) {
        const fullTranslation = Array.isArray(data[0]) ? data[0].join(' ') : String(data[0]);
        if (fullTranslation.trim()) {
          const result = fullTranslation.trim();
          _transCache.set(cacheKey, result);
          return result;
        }
      }
    }
  } catch (err) {
    if (err.name === 'AbortError') return '';
  }

  // 2. Fallback to Server Proxy (Google Dict + MyMemory Multi-Tier Engine)
  try {
    const fetchOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text: clean, sl, tl })
    };
    if (signal) fetchOptions.signal = signal;

    const proxyRes = await fetch('api_translate.php', fetchOptions);
    if (proxyRes.ok) {
      const proxyData = await proxyRes.json();
      if (proxyData && proxyData.success && proxyData.translated) {
        const result = proxyData.translated.trim();
        _transCache.set(cacheKey, result);
        return result;
      }
    }
  } catch (err) {
    if (err.name === 'AbortError') return '';
    console.error('Server proxy translation error:', err);
  }

  return '';
}

/**
 * Backward compatibility wrapper for Bengali to English
 */
async function autoTranslateBnToEn(text, signal = null) {
  return autoTranslate(text, 'bn', 'en', signal);
}

/**
 * Binds live real-time debounced auto-translation from a Bengali source element to an English target element
 * @param {string|HTMLElement} source
 * @param {string|HTMLElement} target
 * @param {Object} [options]
 */
function setupLiveTranslate(source, target, options = {}) {
  const srcElem = typeof source === 'string' ? document.getElementById(source) : source;
  const tgtElem = typeof target === 'string' ? document.getElementById(target) : target;
  if (!srcElem || !tgtElem) return;

  const isTextarea = srcElem.tagName.toLowerCase() === 'textarea';
  const debounceDelay = options.debounce || (isTextarea ? 260 : 180);
  let debounceTimer = null;
  const fieldKey = srcElem.id || Math.random().toString();

  async function performLiveTranslation() {
    const text = srcElem.value.trim();
    
    // If source is empty, clear target
    if (!text) {
      if (options.clearOnEmpty !== false) {
        tgtElem.value = '';
        if (typeof options.onTranslate === 'function') {
          options.onTranslate('');
        }
      }
      return;
    }

    // Cancel any previous pending request for this specific field
    if (_abortControllers.has(fieldKey)) {
      _abortControllers.get(fieldKey).abort();
    }
    const controller = new AbortController();
    _abortControllers.set(fieldKey, controller);

    try {
      const sl = options.sl || 'bn';
      const tl = options.tl || 'en';
      const translated = await autoTranslate(text, sl, tl, controller.signal);
      if (translated) {
        tgtElem.value = translated;
        if (typeof options.onTranslate === 'function') {
          options.onTranslate(translated);
        }
      }
    } catch (e) {
      if (e.name !== 'AbortError') console.error(e);
    } finally {
      _abortControllers.delete(fieldKey);
    }
  }

  // Real-time input listener (as you type)
  srcElem.addEventListener('input', function() {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(performLiveTranslation, debounceDelay);
  });

  // Paste / Change events
  srcElem.addEventListener('paste', function() {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(performLiveTranslation, 40);
  });

  srcElem.addEventListener('change', function() {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(performLiveTranslation, 20);
  });
}

// Global aliases for compatibility
window.autoTranslate = autoTranslate;
window.autoTranslateBnToEn = autoTranslateBnToEn;
window.setupLiveTranslate = setupLiveTranslate;
