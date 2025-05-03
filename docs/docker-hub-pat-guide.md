# Docker Hub Personal Access Token Guide

This guide explains how to create a Docker Hub Personal Access Token (PAT) for use in CI/CD pipelines.

## Why Use a Personal Access Token?

Using a Personal Access Token instead of your password offers several advantages:
- Enhanced security (tokens can be revoked individually)
- More granular access control
- Clear audit trail
- Easier management in CI/CD systems

## Creating a Personal Access Token

1. **Log in to Docker Hub**
   - Go to [https://hub.docker.com](https://hub.docker.com)
   - Sign in with your Docker Hub credentials

2. **Access Security Settings**
   - Click on your username in the top-right corner
   - Select "Account Settings" from the dropdown menu
   - In the left sidebar, click on "Security"

3. **Generate New Access Token**
   - Click the "New Access Token" button
   - Provide a description for the token (e.g., "PSN Rwanda CI/CD")
   - Select appropriate permissions:
     - For CI/CD pipelines, you typically need "Read, Write, Delete" access
   - Click "Generate" to create the token

4. **Copy the Token**
   - The token will be displayed only once
   - Copy the token immediately and store it securely
   - You won't be able to see the token again after closing the page

## Using the Token in GitHub Actions

1. Go to your GitHub repository
2. Navigate to "Settings" > "Secrets and variables" > "Actions"
3. Click "New repository secret"
4. Name: `DOCKER_HUB_PAT`
5. Value: Paste your Docker Hub personal access token
6. Click "Add secret"

## Security Best Practices

- **Token Description**: Use descriptive names for your tokens to track their usage
- **Minimal Scope**: Grant only the permissions required for the task
- **Regular Rotation**: Rotate tokens periodically (every 90 days is recommended)
- **Immediate Revocation**: If a token is compromised, revoke it immediately in Docker Hub
- **Token per Service**: Use different tokens for different services or pipelines

## Revoking a Token

If you need to revoke a token:
1. Log in to Docker Hub
2. Go to Account Settings > Security
3. Find the token in the list
4. Click the trash icon to revoke the token 