#!/usr/bin/env python3
"""
DeenOne Automatic Release & README Updater
Usage:
    python scripts/update_release.py <version> "<Title / Summary>" ["<Feature 1>", "<Feature 2>", ...]

Example:
    python scripts/update_release.py 1.6.0 "Master Database & Architecture Upgrade" "হাদীস ডাটাবেজ database/ ফোল্ডারে স্থানান্তর" "৩৮,১৪৫ হাদীস ইঞ্জিন"
"""

import sys
import os
import re
from datetime import datetime

# Configure UTF-8 for console output on Windows
if sys.platform == 'win32':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
        sys.stderr.reconfigure(encoding='utf-8')
    except Exception:
        pass

ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
VERSION_FILE = os.path.join(ROOT_DIR, "VERSION")
README_FILE = os.path.join(ROOT_DIR, "README.md")
GRADLE_FILE = os.path.join(ROOT_DIR, "app", "build.gradle")

def update_version_file(new_version):
    with open(VERSION_FILE, "w", encoding="utf-8") as f:
        f.write(new_version.strip() + "\n")
    print(f"✅ Updated VERSION file to {new_version}")

def update_gradle_file(new_version):
    if not os.path.exists(GRADLE_FILE):
        print("⚠️ app/build.gradle not found, skipping gradle update.")
        return

    with open(GRADLE_FILE, "r", encoding="utf-8") as f:
        content = f.read()

    # Increment versionCode
    def inc_code(match):
        current_code = int(match.group(1))
        new_code = current_code + 1
        return f"versionCode {new_code}"

    content = re.sub(r'versionCode\s+(\d+)', inc_code, content)
    content = re.sub(r'versionName\s+"[^"]+"', f'versionName "{new_version}"', content)

    with open(GRADLE_FILE, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"✅ Updated app/build.gradle with versionName '{new_version}'")

def update_readme_file(new_version, summary, features):
    if not os.path.exists(README_FILE):
        print("⚠️ README.md not found.")
        return

    with open(README_FILE, "r", encoding="utf-8") as f:
        content = f.read()

    # 1. Update version badge and table version
    content = re.sub(r'\|\s*\*\*DeenOne Official APK\*\*\s*\|\s*`v[^`]+`', f'| **DeenOne Official APK** | `v{new_version}`', content)

    # 2. Add entry to Changelog
    changelog_header = "## 📜 রিলিজ হিস্ট্রি ও চেঞ্জলগ (Changelog)\n"
    if changelog_header in content:
        feature_lines = "\n".join([f"- **Feature:** {feat}" for feat in features]) if features else f"- {summary}"
        new_entry = f"\n### 🚀 [v{new_version}] - {summary}\n{feature_lines}\n"
        
        # Check if version already exists in changelog
        if f"[v{new_version}]" not in content:
            parts = content.split(changelog_header, 1)
            content = parts[0] + changelog_header + new_entry + parts[1]
            print(f"✅ Added v{new_version} entry to README.md Changelog")
        else:
            print(f"ℹ️ v{new_version} already present in README.md Changelog")

    with open(README_FILE, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"✅ Updated README.md successfully!")

def main():
    if len(sys.argv) < 2:
        print("Usage: python scripts/update_release.py <version> \"<Title>\" [\"<Feature 1>\" ...]")
        sys.exit(1)

    new_version = sys.argv[1].lstrip("v")
    summary = sys.argv[2] if len(sys.argv) > 2 else "New Features & Performance Improvements"
    features = sys.argv[3:] if len(sys.argv) > 3 else []

    print(f"🚀 Updating DeenOne to version v{new_version}...")
    update_version_file(new_version)
    update_gradle_file(new_version)
    update_readme_file(new_version, summary, features)
    print(f"\n🎉 DeenOne v{new_version} is ready for release and download!")

if __name__ == "__main__":
    main()
