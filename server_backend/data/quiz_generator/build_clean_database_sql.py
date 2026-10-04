# -*- coding: utf-8 -*-
"""
Master Builder:
Assembles all clean, authentic, distinct questions across all categories,
strictly verifies 0 duplicates, 0 prefix noise, and updates server_backend/deenone_db.sql.
"""

import os
import sys
import re

import clean_cats_1_to_6
import clean_cats_7_to_12
import clean_cats_13_to_18
import clean_cats_19_to_24
import clean_cats_25_to_31

CATEGORY_MAP = {
    1: "general_knowledge",
    2: "ibadah",
    3: "rabiul_awwal",
    4: "quran_studies",
    5: "hadith_sunnah",
    6: "prophets_stories",
    7: "seerat_un_nabi",
    8: "sahaba_life",
    9: "jannah_paradise",
    10: "jahannam_hell",
    11: "jinn_unseen",
    12: "islamic_history",
    13: "islamic_months",
    14: "shariah_life",
    15: "halal_haram",
    16: "islamic_akhlaq",
    17: "dua_azkar",
    18: "iman",
    19: "ramadan_sawm",
    20: "zakat_charity",
    21: "hajj_umrah",
    22: "masnoon_amal",
    23: "islamic_lifestyle",
    24: "islamic_family",
    25: "holy_mosques",
    26: "quran_nature",
    27: "akhira_qiyamah",
    28: "muslim_scholars",
    29: "islamic_architecture",
    30: "noble_women",
    31: "quran_vocabulary"
}

def escape_sql(val):
    if val is None:
        return "NULL"
    s = str(val).replace("\\", "\\\\").replace("'", "''")
    return f"'{s}'"

def main():
    cleaned_items = []
    duplicates = []
    seen_questions_bn = set()
    noise_count = 0
    noise_regex = re.compile(r'^(?:ইসলামিক\s+সাধারণ\s+জ্ঞান\s+)?প্রশ্ন\s+নং\s*\d+\s*:\s*', re.IGNORECASE)

    # 1. Batches 1 to 6, 7 to 12, 13 to 18
    raw_b1 = clean_cats_1_to_6.get_batch_1_to_6()
    raw_b2 = clean_cats_7_to_12.get_batch_7_to_12()
    raw_b3 = clean_cats_13_to_18.get_batch_13_to_18()

    for item in raw_b1 + raw_b2 + raw_b3:
        # Format: (uid, cat_id, q_bn, q_en, opt1_bn, opt1_en, opt2_bn, opt2_en, opt3_bn, opt3_en, opt4_bn, opt4_en, correct_opt, exp_bn, exp_en, ref_bn, ref_en, diff)
        cat_slug = item[1].strip()
        q_bn = item[2].strip()
        q_en = item[3].strip() if item[3] else ""
        
        if noise_regex.search(q_bn):
            noise_count += 1
            q_bn = noise_regex.sub('', q_bn).strip()

        opt1_bn, opt1_en = str(item[4]).strip(), str(item[5]).strip()
        opt2_bn, opt2_en = str(item[6]).strip(), str(item[7]).strip()
        opt3_bn, opt3_en = str(item[8]).strip(), str(item[9]).strip()
        opt4_bn, opt4_en = str(item[10]).strip(), str(item[11]).strip()

        correct_opt = int(item[12])  # already 0-indexed
        exp_bn = str(item[13]).strip() if item[13] else ""
        exp_en = str(item[14]).strip() if item[14] else ""
        ref_bn = str(item[15]).strip() if item[15] else ""
        ref_en = str(item[16]).strip() if item[16] else ""
        diff = str(item[17]).upper() if len(item) > 17 else "MEDIUM"

        norm_q = q_bn.lower()
        if norm_q in seen_questions_bn:
            duplicates.append(q_bn)
            continue
        seen_questions_bn.add(norm_q)

        cleaned_items.append({
            "category_id": cat_slug,
            "question_bn": q_bn,
            "question_en": q_en,
            "option_1": opt1_bn,
            "option_1_en": opt1_en,
            "option_2": opt2_bn,
            "option_2_en": opt2_en,
            "option_3": opt3_bn,
            "option_3_en": opt3_en,
            "option_4": opt4_bn,
            "option_4_en": opt4_en,
            "correct_option": correct_opt,
            "explanation": exp_bn,
            "explanation_en": exp_en,
            "reference": ref_bn,
            "reference_en": ref_en,
            "difficulty": diff
        })

    # 2. Batches 19 to 24, 25 to 31
    raw_b4 = clean_cats_19_to_24.get_questions()
    raw_b5 = clean_cats_25_to_31.get_questions()

    for item in raw_b4 + raw_b5:
        # Format: (cat_num, q_bn, q_en, opt1_bn, opt1_en, opt2_bn, opt2_en, opt3_bn, opt3_en, opt4_bn, opt4_en, correct_opt, exp_bn, exp_en, ref_bn, ref_en, diff)
        cat_num = item[0]
        cat_slug = CATEGORY_MAP.get(cat_num, "general_knowledge")

        q_bn = item[1].strip()
        q_en = item[2].strip() if item[2] else ""

        if noise_regex.search(q_bn):
            noise_count += 1
            q_bn = noise_regex.sub('', q_bn).strip()

        opt1_bn, opt1_en = str(item[3]).strip(), str(item[4]).strip()
        opt2_bn, opt2_en = str(item[5]).strip(), str(item[6]).strip()
        opt3_bn, opt3_en = str(item[7]).strip(), str(item[8]).strip()
        opt4_bn, opt4_en = str(item[9]).strip(), str(item[10]).strip()

        correct_opt = int(item[11]) - 1  # 1-indexed -> 0-indexed
        exp_bn = str(item[12]).strip() if item[12] else ""
        exp_en = str(item[13]).strip() if item[13] else ""
        ref_bn = str(item[14]).strip() if item[14] else ""
        ref_en = str(item[15]).strip() if item[15] else ""
        diff = str(item[16]).upper() if len(item) > 16 else "MEDIUM"

        norm_q = q_bn.lower()
        if norm_q in seen_questions_bn:
            duplicates.append(q_bn)
            continue
        seen_questions_bn.add(norm_q)

        cleaned_items.append({
            "category_id": cat_slug,
            "question_bn": q_bn,
            "question_en": q_en,
            "option_1": opt1_bn,
            "option_1_en": opt1_en,
            "option_2": opt2_bn,
            "option_2_en": opt2_en,
            "option_3": opt3_bn,
            "option_3_en": opt3_en,
            "option_4": opt4_bn,
            "option_4_en": opt4_en,
            "correct_option": correct_opt,
            "explanation": exp_bn,
            "explanation_en": exp_en,
            "reference": ref_bn,
            "reference_en": ref_en,
            "difficulty": diff
        })

    print(f"Cleaned unique questions: {len(cleaned_items)}")
    print(f"Duplicates found and removed: {len(duplicates)}")
    print(f"Noise prefixes cleaned: {noise_count}")

    # Build SQL INSERT statements
    insert_rows = []
    cat_counts = {}
    for idx, q in enumerate(cleaned_items, 1):
        cat_id = q["category_id"]
        cat_counts[cat_id] = cat_counts.get(cat_id, 0) + 1
        q_uid = f"Q_{cat_id.upper()}_{cat_counts[cat_id]:03d}"
        
        row = (
            f"({escape_sql(q_uid)}, "
            f"{escape_sql(q['category_id'])}, "
            f"{escape_sql(q['question_bn'])}, "
            f"{escape_sql(q['question_en'])}, "
            f"{escape_sql(q['option_1'])}, "
            f"{escape_sql(q['option_1_en'])}, "
            f"{escape_sql(q['option_2'])}, "
            f"{escape_sql(q['option_2_en'])}, "
            f"{escape_sql(q['option_3'])}, "
            f"{escape_sql(q['option_3_en'])}, "
            f"{escape_sql(q['option_4'])}, "
            f"{escape_sql(q['option_4_en'])}, "
            f"{q['correct_option']}, "
            f"{escape_sql(q['explanation'])}, "
            f"{escape_sql(q['explanation_en'])}, "
            f"{escape_sql(q['reference'])}, "
            f"{escape_sql(q['reference_en'])}, "
            f"{escape_sql(q['difficulty'])}, 1)"
        )
        insert_rows.append(row)

    # Generate batched insert statements (50 rows per batch)
    sql_header = """-- ==============================================================================
-- 36. QUIZ QUESTIONS & KNOWLEDGE BATTLE BANK (100% Genuine, Zero Noise, Zero Duplicates)
-- ==============================================================================

DROP TABLE IF EXISTS `quiz_questions`;
CREATE TABLE IF NOT EXISTS `quiz_questions` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `question_uid` VARCHAR(64) NOT NULL UNIQUE,
  `category_id` VARCHAR(100) NOT NULL,
  `question_bn` VARCHAR(500) NOT NULL,
  `question_en` VARCHAR(500) DEFAULT NULL,
  `option_1` VARCHAR(255) NOT NULL,
  `option_1_en` VARCHAR(255) DEFAULT NULL,
  `option_2` VARCHAR(255) NOT NULL,
  `option_2_en` VARCHAR(255) DEFAULT NULL,
  `option_3` VARCHAR(255) NOT NULL,
  `option_3_en` VARCHAR(255) DEFAULT NULL,
  `option_4` VARCHAR(255) NOT NULL,
  `option_4_en` VARCHAR(255) DEFAULT NULL,
  `correct_option` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-indexed: 0, 1, 2, or 3',
  `explanation` TEXT DEFAULT NULL,
  `explanation_en` TEXT DEFAULT NULL,
  `reference` VARCHAR(255) DEFAULT NULL,
  `reference_en` VARCHAR(255) DEFAULT NULL,
  `difficulty` VARCHAR(20) DEFAULT 'MEDIUM',
  `is_active` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uniq_question_bn` (`question_bn`(255)),
  INDEX `idx_qq_cat` (`category_id`, `is_active`),
  INDEX `idx_qq_uid` (`question_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

"""
    chunk_size = 50
    insert_blocks = []
    for i in range(0, len(insert_rows), chunk_size):
        chunk = insert_rows[i:i + chunk_size]
        block = "INSERT INTO `quiz_questions` (`question_uid`, `category_id`, `question_bn`, `question_en`, `option_1`, `option_1_en`, `option_2`, `option_2_en`, `option_3`, `option_3_en`, `option_4`, `option_4_en`, `correct_option`, `explanation`, `explanation_en`, `reference`, `reference_en`, `difficulty`, `is_active`) VALUES\n"
        block += ",\n".join(chunk) + ";\n"
        insert_blocks.append(block)

    questions_sql = sql_header + "\n".join(insert_blocks)

    db_file_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", "deenone_db.sql"))
    with open(db_file_path, "r", encoding="utf-8") as f:
        content = f.read()

    marker = "-- 36. QUIZ QUESTIONS"
    idx = content.find(marker)
    if idx != -1:
        prefix = content[:idx]
    else:
        marker2 = "CREATE TABLE IF NOT EXISTS `quiz_questions`"
        idx = content.find(marker2, content.find("CREATE TABLE IF NOT EXISTS `quiz_categories`"))
        prefix = content[:idx] if idx != -1 else content

    new_content = prefix.rstrip() + "\n\n" + questions_sql + "\nSET FOREIGN_KEY_CHECKS = 1;\nCOMMIT;\n"

    with open(db_file_path, "w", encoding="utf-8") as f:
        f.write(new_content)

    print(f"Successfully written {len(cleaned_items)} questions to {db_file_path}")
    print("Category breakdown:")
    for cat_id, count in sorted(cat_counts.items()):
        print(f" - {cat_id}: {count} questions")

if __name__ == "__main__":
    main()
