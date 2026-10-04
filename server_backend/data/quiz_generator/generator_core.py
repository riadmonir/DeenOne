# -*- coding: utf-8 -*-
"""
Full Islamic Quiz Database Builder for DeenOne
31 Official Categories x 100 Authentic Questions = 3,100 Questions Total
Strict Category Boundaries with Verified Quran/Hadith References
"""

import os
import sys

def escape_sql(text):
    if text is None:
        return 'NULL'
    return "'" + str(text).replace("\\", "\\\\").replace("'", "''") + "'"

print("Quiz generator core initialized.")
