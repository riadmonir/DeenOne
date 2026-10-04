import re

with open('server_backend/deenone_db.sql', 'r', encoding='utf-8') as f:
    content = f.read()

# find all CREATE TABLE statements
table_matches = re.finditer(r'CREATE TABLE IF NOT EXISTS [`]?(\w+)[`]?\s*\((.*?)\)\s*ENGINE', content, re.DOTALL | re.IGNORECASE)

tables = {}
for m in table_matches:
    tbl_name = m.group(1).lower()
    tbl_body = m.group(2)
    cols = []
    for line in tbl_body.split('\n'):
        line = line.strip()
        cm = re.match(r'[`]?([a-zA-Z0-9_]+)[`]?\s+(INT|VARCHAR|TEXT|TIMESTAMP|DATETIME|TINYINT|BIGINT|DECIMAL|CHAR|MEDIUMTEXT|LONGTEXT|FLOAT|DOUBLE|BOOLEAN|DATE)', line, re.IGNORECASE)
        if cm:
            col_name = cm.group(1).lower()
            if col_name not in ('primary', 'key', 'unique', 'constraint', 'index', 'foreign', 'check'):
                cols.append(col_name)
    tables[tbl_name] = set(cols)

print(f"Parsed {len(tables)} tables.")

# Now find all INSERT INTO statements
insert_matches = re.finditer(r'INSERT INTO [`]?(\w+)[`]?\s*\((.*?)\)\s*VALUES', content, re.DOTALL | re.IGNORECASE)

errors = []
for m in insert_matches:
    tbl = m.group(1).lower()
    col_str = m.group(2)
    cols = [c.strip().strip('`').lower() for c in col_str.split(',') if c.strip()]
    if tbl in tables:
        missing = [c for c in cols if c not in tables[tbl]]
        if missing:
            errors.append((tbl, missing))
    else:
        errors.append((tbl, "TABLE NOT FOUND"))

print(f"Total insert errors: {len(errors)}")
for e in errors:
    print(e)
