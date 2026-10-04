with open('server_backend/deenone_db.sql', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Remove duplicate hadith_books block 1 (around line 2431)
dup1_start = "-- Table structure for hadith_books\n-- --------------------------------------------------------\nCREATE TABLE IF NOT EXISTS `hadith_books`"
dup1_end = "`total_hadith_bn` = VALUES(`total_hadith_bn`);\n"

if dup1_start in content and dup1_end in content:
    idx1 = content.find(dup1_start)
    idx2 = content.find(dup1_end, idx1) + len(dup1_end)
    content = content[:idx1] + content[idx2:]
    print("Removed duplicate hadith_books block 1")
else:
    print("Could not find dup1")

# 2. Remove duplicate hadith_books block 2 (around line 2611)
dup2_start = "-- Table structure for table hadith_books\n-- --------------------------------------------------------\nCREATE TABLE IF NOT EXISTS hadith_books"
dup2_end = "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n"

if dup2_start in content:
    idx1 = content.find(dup2_start)
    idx2 = content.find(dup2_end, idx1) + len(dup2_end)
    content = content[:idx1] + content[idx2:]
    print("Removed duplicate hadith_books block 2")
else:
    print("Could not find dup2")

with open('server_backend/deenone_db.sql', 'w', encoding='utf-8') as f:
    f.write(content)

print("Saved cleaned server_backend/deenone_db.sql")
