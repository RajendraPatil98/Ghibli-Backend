# GitHub Setup Script for Ghibli Art AI Backend (PowerShell)
# This script helps initialize and push the project to GitHub

Write-Host "🎨 Ghibli Art AI - GitHub Setup Script" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check if git is initialized
if (-not (Test-Path ".git")) {
    Write-Host "Initializing git repository..." -ForegroundColor Blue
    git init
    Write-Host "✓ Git repository initialized" -ForegroundColor Green
} else {
    Write-Host "✓ Git repository already exists" -ForegroundColor Green
}

Write-Host ""

# Step 2: Check if .gitignore is configured
Write-Host "Verifying .gitignore configuration..." -ForegroundColor Blue
$gitignoreContent = Get-Content ".gitignore" -Raw
if ($gitignoreContent -match "\.env") {
    Write-Host "✓ .gitignore properly configured" -ForegroundColor Green
} else {
    Write-Host "⚠ Warning: .env not in .gitignore" -ForegroundColor Yellow
}

Write-Host ""

# Step 3: Add and commit files
Write-Host "Adding files to git..." -ForegroundColor Blue
git add -A
Write-Host "✓ Files staged" -ForegroundColor Green

Write-Host ""

# Step 4: Initial commit
Write-Host "Creating initial commit..." -ForegroundColor Blue
git commit -m "Initial commit: Ghibli Art AI Backend with Docker configuration" 2>$null || Write-Host "⚠ Nothing to commit" -ForegroundColor Yellow
Write-Host "✓ Initial commit created" -ForegroundColor Green

Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Create a new repository on GitHub: https://github.com/new"
Write-Host "2. Name it: Ghibli-Backend"
Write-Host "3. Copy the repository URL"
Write-Host "4. Run: git remote add origin <your-repo-url>"
Write-Host "5. Run: git branch -M main"
Write-Host "6. Run: git push -u origin main"
Write-Host ""
Write-Host "To connect to existing repository:" -ForegroundColor Blue
Write-Host "git remote add origin https://github.com/yourusername/Ghibli-Backend.git"
Write-Host "git branch -M main"
Write-Host "git push -u origin main"
Write-Host ""
Write-Host "✓ Setup complete!" -ForegroundColor Green
