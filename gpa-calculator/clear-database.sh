#!/bin/bash
# Script to clear the GPA Calculator database

DB_DIR="$HOME/.gpa-calculator"

if [ -d "$DB_DIR" ]; then
    rm -rf "$DB_DIR"
    echo "✓ Database cleared successfully!"
    echo "  Removed: $DB_DIR"
else
    echo "ℹ No database found at: $DB_DIR"
fi

echo ""
echo "The database will be automatically recreated when you run the application."
