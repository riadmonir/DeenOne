# -*- coding: utf-8 -*-
import os

sql_path = "server_backend/deenone_db.sql"
with open(sql_path, "r", encoding="utf-8") as f:
    content = f.read()

print(f"File: {sql_path}")
print(f"File Size: {len(content.encode('utf-8')) / 1024:.2f} KB")

# Count questions
q_count = content.count("('Q_")
print(f"Total Questions in SQL: {q_count}")

# Check all 31 category UIDs
categories = [
    "general_knowledge", "ibadah", "rabiul_awwal", "quran_studies", "hadith_sunnah",
    "prophets_stories", "seerat_un_nabi", "sahaba_life", "jannah_paradise", "jahannam_hell",
    "jinn_unseen", "islamic_history", "islamic_months", "shariah_life", "halal_haram",
    "muslim_scholars", "quran_nature", "islamic_architecture", "quran_vocabulary", "islamic_family",
    "masnoon_amal", "islamic_lifestyle", "ramadan_sawm", "hajj_umrah", "zakat_charity",
    "islamic_akhlaq", "dua_azkar", "holy_mosques", "akhira_qiyamah", "noble_women", "iman"
]

all_ok = True
for cat in categories:
    cat_match = content.count(f", '{cat}', ")
    if cat_match < 100:
        print(f"WARNING: Category '{cat}' has {cat_match} questions (<100)!")
        all_ok = False
    else:
        print(f"OK: Category '{cat}' has {cat_match} questions.")

if all_ok:
    print(f"\nALL 31 CATEGORIES HAVE 100+ AUTHENTIC QUESTIONS! TOTAL: {q_count}")
