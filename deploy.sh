#!/bin/bash

# Deploy script for PSN Rwanda Backend

# Set default variables
DOCKER_USERNAME=${DOCKER_USERNAME:-"kevlargroup"}
DOCKER_PASSWORD=${DOCKER_PASSWORD:-""}
IMAGE_NAME=${IMAGE_NAME:-"kevlargroup/psn-rwanda-backend"}
IMAGE_TAG=${IMAGE_TAG:-"latest"}
DEPLOY_DIR=${DEPLOY_DIR:-"/opt/psn_rwanda/backend"}

# Check if Docker password is provided
if [ -z "$DOCKER_PASSWORD" ]; then
  # Check if running in GitHub Actions
  if [ -n "$GITHUB_ACTIONS" ]; then
    # In GitHub Actions, use the secret directly
    DOCKER_PASSWORD=$DOCKER_HUB_PAT
  else
    # If running locally and no password provided, prompt for it
    echo "Please provide Docker password:"
    read -s DOCKER_PASSWORD
  fi
fi

# Build the application
echo "Building application..."
./mvnw clean package -DskipTests

# Build the Docker image
echo "Building Docker image..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

# Log in to Docker Hub
echo "Logging in to Docker Hub..."
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# Push the image to Docker Hub
echo "Pushing Docker image to registry..."
docker push ${IMAGE_NAME}:${IMAGE_TAG}

# Deploy instructions
echo ""
echo "Image pushed to Docker Hub successfully!"
echo ""
echo "To deploy on the server, run the following commands:"
echo "ssh your-server"
echo "cd $DEPLOY_DIR"
echo "git pull"
echo "docker-compose -f docker-compose.prod.yml pull"
echo "docker-compose -f docker-compose.prod.yml up -d"
echo "docker image prune -f"
echo ""
echo "Or run the GitHub Actions workflow manually from the repository."

# Make the script executable
chmod +x deploy.sh 