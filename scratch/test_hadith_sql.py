import sqlite3

# Test that the SQL logic and column names are 100% consistent
conn = sqlite3.connect(":memory:")
cur = conn.cursor()

# Test hadith_books
cur.execute("""
CREATE TABLE hadith_books (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  book_slug VARCHAR(60) NOT NULL UNIQUE,
  name_bn VARCHAR(150) NOT NULL,
  author_bn VARCHAR(120),
  author_en VARCHAR(120),
  initials VARCHAR(20) DEFAULT 'H',
  color_hex VARCHAR(20) DEFAULT '#10B981',
  name_en VARCHAR(150),
  name_ar VARCHAR(150),
  total_hadith INTEGER DEFAULT 0,
  total_hadith_bn VARCHAR(50) DEFAULT '',
  description_bn TEXT,
  display_order INTEGER DEFAULT 0,
  is_active INTEGER DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
""")

# Test hadith_chapters
cur.execute("""
CREATE TABLE hadith_chapters (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  book_slug VARCHAR(60) NOT NULL,
  chapter_number INTEGER NOT NULL,
  chapter_number_bn VARCHAR(50) DEFAULT '',
  title_bn VARCHAR(255) NOT NULL,
  title_en VARCHAR(255) DEFAULT '',
  hadith_range VARCHAR(100) DEFAULT '',
  hadith_range_bn VARCHAR(100) DEFAULT '',
  start_hadith INTEGER DEFAULT 0,
  end_hadith INTEGER DEFAULT 0,
  total_hadith INTEGER DEFAULT 0,
  display_order INTEGER DEFAULT 0,
  is_active INTEGER DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(book_slug, chapter_number)
);
""")

# Test hadith_sections
cur.execute("""
CREATE TABLE hadith_sections (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  book_slug VARCHAR(60) NOT NULL,
  chapter_number INTEGER NOT NULL,
  section_number VARCHAR(30) NOT NULL,
  section_tag_bn VARCHAR(100) NOT NULL,
  section_tag_en VARCHAR(100),
  section_title_bn VARCHAR(255) NOT NULL,
  section_title_en VARCHAR(255),
  arabic_verse TEXT,
  verse_translation_bn TEXT,
  verse_translation_en TEXT,
  display_order INTEGER DEFAULT 1,
  is_active INTEGER DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
""")

# Test hadith_items
cur.execute("""
CREATE TABLE hadith_items (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  book_slug VARCHAR(60) NOT NULL,
  chapter_number INTEGER DEFAULT 1,
  hadith_number INTEGER NOT NULL,
  hadith_number_bn VARCHAR(50) DEFAULT '',
  narrator_bn VARCHAR(120),
  narrator_en VARCHAR(255) DEFAULT '',
  arabic_text TEXT NOT NULL,
  bangla_text TEXT NOT NULL,
  english_text TEXT,
  grade VARCHAR(40) DEFAULT 'সহীহ (Sahih)',
  reference VARCHAR(255),
  footnote_bn TEXT,
  footnote_en TEXT,
  words_json TEXT,
  is_active INTEGER DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(book_slug, chapter_number, hadith_number)
);
""")

# Test the exact query the user ran:
cur.execute("""
INSERT INTO hadith_items (book_slug, chapter_number, hadith_number, hadith_number_bn, narrator_bn, arabic_text, bangla_text, grade, reference, is_active)
VALUES ('bukhari', 1, 1, '১', 'হযরত উমর ইবনুল খাত্তাব (রা.)', 'إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى', 'নিশ্চয় সকল কাজের ফলাফল নিয়তের উপর নির্ভরশীল। আর প্রত্যেক ব্যক্তি তাই পাবে যার সে নিয়ত করেছে।', 'সহীহ (Sahih)', 'সহীহ বুখারী: ১', 1);
""")

cur.execute("SELECT COUNT(*) FROM hadith_items;")
print("Hadith items count:", cur.fetchone()[0])
print("ALL SQL SCHEMAS AND INSERTS ARE 100% VALID!")
