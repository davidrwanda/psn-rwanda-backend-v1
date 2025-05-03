# Git Guidelines for PSN Rwanda Backend

This document provides guidelines for working with Git in the PSN Rwanda backend project.

## Repository Structure

The repository follows a standard Spring Boot project structure:

```
├── .github/           # GitHub workflows and configuration
├── docs/              # Documentation
├── src/               # Source code
│   ├── main/          # Main application code
│   └── test/          # Test code
├── uploads/           # Upload directory (not committed to Git)
├── .gitignore         # Git ignore configuration
├── .gitattributes     # Git attributes configuration
├── docker-compose.yml # Development Docker configuration
└── pom.xml            # Maven project definition
```

## Branching Strategy

We use a simplified Git Flow approach:

1. **main**: Production-ready code. Protected branch.
2. **develop**: Integration branch for feature development.
3. **feature/xxx**: Feature branches for new development.
4. **bugfix/xxx**: Branches for bug fixes.
5. **hotfix/xxx**: Emergency fixes for production.

## Branch Naming Convention

- `feature/short-feature-description`
- `bugfix/issue-description`
- `hotfix/critical-issue-description`

Examples:
- `feature/email-notification-service`
- `bugfix/fix-booking-date-validation`
- `hotfix/security-vulnerability-fix`

## Commit Message Guidelines

Write clear, concise commit messages that explain what the change does:

```
[TYPE]: Short description (50 chars or less)

More detailed explanatory text if needed. Wrap it to about 72
characters. The blank line separating the summary from the body
is critical.
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code (formatting)
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `perf`: Code change that improves performance
- `test`: Adding missing tests or correcting existing tests
- `chore`: Changes to the build process or auxiliary tools

Examples:
- `feat: Add email notification for booking confirmation`
- `fix: Correct date validation in booking form`
- `docs: Update API documentation for new endpoints`

## Pull Request Process

1. Create a branch from `develop` for your work
2. Make your changes in small, logical commits
3. Push your branch to GitHub
4. Create a pull request to merge into `develop`
5. Request reviews from team members
6. Address any review comments
7. Once approved, merge using squash merge

## Working with Git

### Initial Setup

```bash
# Clone the repository
git clone https://github.com/your-organization/psn-rwanda.git
cd psn-rwanda

# Set up your identity
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

### Daily Workflow

```bash
# Ensure you're on develop and up to date
git checkout develop
git pull

# Create a feature branch
git checkout -b feature/my-new-feature

# Make changes and commit
git add .
git commit -m "feat: Add my new feature"

# Push changes
git push -u origin feature/my-new-feature
```

### Keeping Your Branch Up to Date

```bash
# Fetch latest changes
git fetch

# Rebase your branch on the latest develop
git rebase origin/develop

# If there are conflicts, resolve them and continue
git add .
git rebase --continue
```

## Ignored Files

The `.gitignore` file is configured to exclude:

- Build artifacts and compiled code (`target/`, `*.class`, etc.)
- IDE-specific files (`.idea/`, `.vscode/`, etc.)
- Local configuration files (`application-local.properties`)
- Logs and temporary files
- Uploads directory
- Sensitive information (certificates, keystores)

If you need to commit a file that's normally ignored, use:

```bash
git add -f path/to/ignored/file
```

## Git Attributes

The `.gitattributes` file ensures:

- Consistent line endings across operating systems
- Proper handling of binary files
- Correct language detection for GitHub statistics

## Common Issues

### Line Ending Issues

If you encounter line ending problems:

```bash
# Configure Git to handle line endings
git config --global core.autocrlf input  # For Linux/Mac
git config --global core.autocrlf true   # For Windows
```

### Large Files

Avoid committing large files to the repository. For large binary files, consider using:

1. External storage solutions
2. Git LFS (if absolutely necessary)
3. Cloud storage with links in the codebase 