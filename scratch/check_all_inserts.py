import re

with open('server_backend/deenone_db.sql', 'r', encoding='utf-8') as f:
    content = f.read()

# Find all tables with INSERT statements
inserts = re.findall(r'INSERT INTO [`]?(\w+)[`]?\s*\((.*?)\)\s*VALUES', content, re.IGNORECASE)

tables_with_inserts = set()
for t, cols in inserts:
    tables_with_inserts.add(t)

print("Tables with INSERT INTO statements:")
for t in sorted(tables_with_inserts):
    print("-", t)
