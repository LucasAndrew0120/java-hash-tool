#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
TARGET_DIR="$PROJECT_DIR/target"
DIST_DIR="$PROJECT_DIR/dist"
APP_NAME="HashTool"
MAIN_CLASS="src.main.java.top.lris625.hash"

echo "==> 1. Maven 打包 fat JAR..."
cd "$PROJECT_DIR"
mvn clean package -q

FAT_JAR=$(ls "$TARGET_DIR"/*-jar-with-dependencies.jar 2>/dev/null | head -1)
if [ -z "$FAT_JAR" ]; then
    echo "错误: 未找到 jar-with-dependencies.jar"
    exit 1
fi
echo "    -> $FAT_JAR"

echo "==> 2. jpackage 生成 exe (app-image)..."
rm -rf "$DIST_DIR/$APP_NAME"

jpackage \
    --type app-image \
    --name "$APP_NAME" \
    --input "$TARGET_DIR" \
    --main-jar "$(basename "$FAT_JAR")" \
    --main-class "$MAIN_CLASS" \
    --dest "$DIST_DIR"

echo "==> 3. 打包 zip..."
ZIP_FILE="$PROJECT_DIR/${APP_NAME}.zip"
rm -f "$ZIP_FILE"

if command -v powershell.exe &>/dev/null; then
    # Windows Git Bash：将 Unix 路径转为 Windows 路径
    WIN_DIST="$(cygpath -w "$DIST_DIR/$APP_NAME")"
    WIN_ZIP="$(cygpath -w "$ZIP_FILE")"
    powershell.exe -Command "Compress-Archive -Path '$WIN_DIST' -DestinationPath '$WIN_ZIP'"
else
    # macOS / Linux: use zip
    cd "$DIST_DIR"
    zip -r "$ZIP_FILE" "$APP_NAME"
    cd "$PROJECT_DIR"
fi
echo "    -> $ZIP_FILE"

echo "==> 完成！"
echo "    exe: $DIST_DIR/$APP_NAME/$APP_NAME.exe"
echo "    zip: $ZIP_FILE"
