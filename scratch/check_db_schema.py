import sys
import re
from collections import defaultdict

sys.stdout.reconfigure(encoding='utf-8')

with open('server_backend/deenone_db.sql', 'r', encoding='utf-8') as f:
    sql = f.read()

# 1. Extract all CREATE TABLE definitions
create_tables = {}
table_blocks = defaultdict(list)

pattern = r'CREATE\s+TABLE\s+(?:IF\s+NOT\s+EXISTS\s+)?`?([a-zA-Z0-9_]+)`?\s*\((.*?)\)\s*ENGINE'
for m in re.finditer(pattern, sql, re.DOTALL | re.IGNORECASE):
    tname = m.group(1).lower()
    cols_block = m.group(2)
    table_blocks[tname].append(cols_block)
    
    cols = set()
    for line in cols_block.split('\n'):
        line = line.strip().strip(',')
        cm = re.match(r'^[`"]?([a-zA-Z0-9_]+)[`"]?\s+', line)
        if cm:
            col = cm.group(1).lower()
            if col not in ['primary', 'key', 'unique', 'index', 'constraint', 'foreign', 'check']:
                cols.add(col)
    if tname not in create_tables:
        create_tables[tname] = cols

print(f"Total distinct tables defined: {len(create_tables)}")

# 2. Check each INSERT
mismatches = []
insert_pattern = r'INSERT\s+(?:IGNORE\s+)?INTO\s+`?([a-zA-Z0-9_]+)`?\s*\((.*?)\)\s*VALUES'
for m in re.finditer(insert_pattern, sql, re.DOTALL | re.IGNORECASE):
    tname = m.group(1).lower()
    cols_str = m.group(2)
    insert_cols = [c.strip().strip('`"').lower() for c in cols_str.split(',') if c.strip()]
    if tname in create_tables:
        known = create_tables[tname]
        missing = [c for c in insert_cols if c not in known]
        if missing:
            mismatches.append((tname, missing))

print("=== Column Mismatches Detected ===")
for tname, missing in mismatches:
    print(f"Table '{tname}': INSERT expects columns {missing} which are missing in its initial CREATE TABLE!")

if not mismatches:
    print("ALL INSERT statements have 100% valid columns matching their CREATE TABLE!")
