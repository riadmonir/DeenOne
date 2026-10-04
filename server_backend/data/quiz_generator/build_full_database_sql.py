# -*- coding: utf-8 -*-
"""
Master Assembler: Assembles all 3,100 Islamic Quiz Questions across 31 Categories (100 per Category)
and injects them into server_backend/deenone_db.sql cleanly.
"""

import os
import sys

# Add current dir to sys.path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from cats_1_to_6 import get_batch_1_to_6
from cats_7_to_12 import get_batch_7_to_12
from cats_13_to_18 import get_batch_13_to_18
from cats_19_to_24 import get_batch_19_to_24
from cats_25_to_31 import get_batch_25_to_31

def escape_sql(val):
    if val is None:
        return "NULL"
    s = str(val).replace("\\", "\\\\").replace("'", "''")
    return f"'{s}'"

def main():
    print("Collecting all question batches...")
    b1 = get_batch_1_to_6()
    b2 = get_batch_7_to_12()
    b3 = get_batch_13_to_18()
    b4 = get_batch_19_to_24()
    b5 = get_batch_25_to_31()
    
    all_questions = b1 + b2 + b3 + b4 + b5
    print(f"Total questions loaded: {len(all_questions)}")
    
    # Category counter
    cat_counts = {}
    for q in all_questions:
        cat = q[1]
        cat_counts[cat] = cat_counts.get(cat, 0) + 1
        
    print(f"Total Categories: {len(cat_counts)}")
    for cat, count in sorted(cat_counts.items()):
        print(f" - {cat}: {count} questions")
        if count < 100:
            raise ValueError(f"Category {cat} has less than 100 questions! Found: {count}")
            
    # Generate SQL file
    sql_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", "deenone_db.sql"))
    print(f"Reading target SQL: {sql_path}")
    
    with open(sql_path, "r", encoding="utf-8") as f:
        sql_content = f.read()
        
    # Find the quiz_questions insert section
    marker = "-- Seed Authentic Verified Questions for EVERY Category"
    if marker not in sql_content:
        raise ValueError(f"Marker '{marker}' not found in deenone_db.sql")
        
    parts = sql_content.split(marker)
    header = parts[0] + marker + "\n"
    
    # Format batch inserts of 100 rows each for optimal performance and compatibility
    sql_lines = []
    
    current_cat = None
    batch_rows = []
    
    for q in all_questions:
        uid, cat, q_bn, q_en, opt1, opt1_en, opt2, opt2_en, opt3, opt3_en, opt4, opt4_en, corr, exp_bn, exp_en, ref_bn, ref_en, diff = q
        
        row_str = f"({escape_sql(uid)}, {escape_sql(cat)}, {escape_sql(q_bn)}, {escape_sql(q_en)}, {escape_sql(opt1)}, {escape_sql(opt1_en)}, {escape_sql(opt2)}, {escape_sql(opt2_en)}, {escape_sql(opt3)}, {escape_sql(opt3_en)}, {escape_sql(opt4)}, {escape_sql(opt4_en)}, {corr}, {escape_sql(exp_bn)}, {escape_sql(exp_en)}, {escape_sql(ref_bn)}, {escape_sql(ref_en)}, {escape_sql(diff)})"
        batch_rows.append(row_str)
        
    # Write INSERT statement
    insert_prefix = "INSERT INTO `quiz_questions` (`question_uid`, `category_id`, `question_bn`, `question_en`, `option_1`, `option_1_en`, `option_2`, `option_2_en`, `option_3`, `option_3_en`, `option_4`, `option_4_en`, `correct_option`, `explanation`, `explanation_en`, `reference`, `reference_en`, `difficulty`) VALUES\n"
    
    # Split into chunks of 50 for phpMyAdmin / MySQL bulk insert safety
    chunk_size = 50
    chunks = [batch_rows[i:i + chunk_size] for i in range(0, len(batch_rows), chunk_size)]
    
    full_inserts = []
    for c in chunks:
        full_inserts.append(insert_prefix + ",\n".join(c) + "\nON DUPLICATE KEY UPDATE `question_bn` = VALUES(`question_bn`), `explanation` = VALUES(`explanation`);\n")
        
    new_tail = "\n".join(full_inserts) + "\nSET FOREIGN_KEY_CHECKS = 1;\nCOMMIT;\n"
    
    final_sql = header + new_tail
    
    with open(sql_path, "w", encoding="utf-8") as f:
        f.write(final_sql)
        
    print(f"Successfully wrote {len(all_questions)} questions into {sql_path}")

if __name__ == '__main__':
    main()
