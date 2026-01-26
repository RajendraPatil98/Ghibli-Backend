#!/bin/bash
# GitHub Setup Script for Ghibli Art AI Backend
# This script helps initialize and push the project to GitHub

set -e

echo "🎨 Ghibli Art AI - GitHub Setup Script"
echo "======================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Step 1: Check if git is initialized
if [ ! -d ".git" ]; then
    echo -e "${BLUE}Initializing git repository...${NC}"
    git init
    echo -e "${GREEN}✓ Git repository initialized${NC}"
else
    echo -e "${GREEN}✓ Git repository already exists${NC}"
fi

echo ""

# Step 2: Check if .gitignore is configured
echo -e "${BLUE}Verifying .gitignore configuration...${NC}"
if grep -q "\.env" .gitignore; then
    echo -e "${GREEN}✓ .gitignore properly configured${NC}"
else
    echo -e "${YELLOW}⚠ Warning: .env not in .gitignore${NC}"
fi

echo ""

# Step 3: Add and commit files
echo -e "${BLUE}Adding files to git...${NC}"
git add -A
echo -e "${GREEN}✓ Files staged${NC}"

echo ""

# Step 4: Initial commit
echo -e "${BLUE}Creating initial commit...${NC}"
git commit -m "Initial commit: Ghibli Art AI Backend with Docker configuration" || echo -e "${YELLOW}⚠ Nothing to commit${NC}"
echo -e "${GREEN}✓ Initial commit created${NC}"

echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Create a new repository on GitHub: https://github.com/new"
echo "2. Name it: Ghibli-Backend"
echo "3. Copy the repository URL"
echo "4. Run: git remote add origin <your-repo-url>"
echo "5. Run: git branch -M main"
echo "6. Run: git push -u origin main"
echo ""
echo -e "${BLUE}To connect to existing repository:${NC}"
echo "git remote add origin https://github.com/yourusername/Ghibli-Backend.git"
echo "git branch -M main"
echo "git push -u origin main"
echo ""
echo -e "${GREEN}✓ Setup complete!${NC}"
