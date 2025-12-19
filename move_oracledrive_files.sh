#!/bin/bash

# Script to move OracleDrive files from app to genesis/oracledrive module
# and update package declarations

set -e

APP_SRC="/home/user/aurakai-auraos/app/src/main/java/dev/aurakai/auraframefx/oracledrive"
MODULE_SRC="/home/user/aurakai-auraos/genesis/oracledrive/src/main/java/dev/aurakai/auraframefx/genesis/oracledrive"

# Create target directory structure
mkdir -p "$MODULE_SRC"
mkdir -p "$MODULE_SRC/genesis/ai"
mkdir -p "$MODULE_SRC/genesis/cloud"
mkdir -p "$MODULE_SRC/security"
mkdir -p "$MODULE_SRC/service"

echo "Moving OracleDrive files (except GenesisHookEntry which stays in app)..."

# Move top-level files
for file in EncryptionManager.kt FileOperationUtils.kt OracleDriveAgent.kt \
            OracleDriveManager.kt OracleDriveService.kt OracleDriveServiceConnector.kt \
            OracleDriveServiceImpl.kt SecureFileManager.kt; do
  if [ -f "$APP_SRC/$file" ]; then
    echo "Moving $file..."
    mv "$APP_SRC/$file" "$MODULE_SRC/"
  fi
done

# Move genesis/* files (except ai subdirectory initially)
for file in OracleCloudApi.kt OracleDriveApi.kt OracleDriveModule.kt VertexAIClientImpl.kt; do
  if [ -f "$APP_SRC/genesis/$file" ]; then
    echo "Moving genesis/$file..."
    mv "$APP_SRC/genesis/$file" "$MODULE_SRC/genesis/"
  fi
done

# Move genesis/ai/* files EXCEPT GenesisHookEntry.kt (it must stay in app)
if [ -d "$APP_SRC/genesis/ai" ]; then
  for file in "$APP_SRC/genesis/ai"/*.kt; do
    filename=$(basename "$file")
    if [ "$filename" != "GenesisHookEntry.kt" ]; then
      echo "Moving genesis/ai/$filename..."
      mv "$file" "$MODULE_SRC/genesis/ai/"
    fi
  done
fi

# Move genesis/cloud/* files
if [ -d "$APP_SRC/genesis/cloud" ]; then
  echo "Moving genesis/cloud/*..."
  mv "$APP_SRC/genesis/cloud"/*.kt "$MODULE_SRC/genesis/cloud/" 2>/dev/null || true
fi

# Move security/* files
if [ -d "$APP_SRC/security" ]; then
  echo "Moving security/*..."
  mv "$APP_SRC/security"/*.kt "$MODULE_SRC/security/" 2>/dev/null || true
fi

# Move service/* files
if [ -d "$APP_SRC/service" ]; then
  echo "Moving service/*..."
  mv "$APP_SRC/service"/*.kt "$MODULE_SRC/service/" 2>/dev/null || true
fi

echo ""
echo "Files moved successfully!"
echo ""
echo "Next step: Update package declarations in moved files"
echo "Run: sed -i 's/package dev.aurakai.auraframefx.oracledrive/package dev.aurakai.auraframefx.genesis.oracledrive/g' \$MODULE_SRC/**/*.kt"
