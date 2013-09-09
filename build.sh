#!/bin/bash
FORGE_DIR=~/forge.679
MCP_DIR=$FORGE_DIR/mcp
MINECRAFT_SRC=$MCP_DIR/src/minecraft
MINECRAFT_REOBF=$MCP_DIR/reobf/minecraft
ZIPNAME=WoodworkingTFC.zip

# Remove old code
if [ -d "$MINECRAFT_SRC/TFC" ]; then rm -rf "$MINECRAFT_SRC/TFC"; fi
if [ -d "$MINECRAFT_SRC/VegasGoatTFC" ]; then rm -rf "$MINECRAFT_SRC/VegasGoatTFC"; fi

# Copy VegasGoatTFC code
cp -r src/{TFC,VegasGoatTFC} "$MINECRAFT_SRC"

# Build and reobfuscate
pushd "$MCP_DIR"
RESULT=1
if ./recompile.sh --client; then
	if ./reobfuscate.sh --client; then
		RESULT=0
	fi
fi
popd

# Package if build OK
if (( $RESULT == 0 )); then
	if [ -f $ZIPNAME ]; then rm -f $ZIPNAME; fi
	if [ -d output ]; then rm -rf output; fi
	mkdir output
	cp -r "$MINECRAFT_REOBF/VegasGoatTFC" output
	cp -r dist/* output
	( cd output && zip -r ../$ZIPNAME * )
	rm -rf output
fi
