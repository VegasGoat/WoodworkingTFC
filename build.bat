@ECHO OFF
SET RETDIR=%CD%
SET ZIPCMD="C:\Program Files\7-zip\7z.exe"
SET FORGE=C:\Work\SourceCode\MinecraftDev\1.5.1\forge
SET MCP=%FORGE%\mcp
SET MC_SRC=%MCP%\src\minecraft
SET MC_REOBF=%MCP%\reobf\minecraft
SET ZIPNAME=WoodworkingTFC.zip

IF EXIST %ZIPNAME% (
	ECHO Deleting old %ZIPNAME%
   DEL /Q %ZIPNAME%
   IF ERRORLEVEL 1 GOTO FAILED
)

IF EXIST %MC_SRC%\TFC (
   ECHO Deleting old DummyTFC src
   RMDIR /S /Q %MC_SRC%\TFC
   IF ERRORLEVEL 2 GOTO FAILED
)

IF EXIST %MC_SRC%\VegasGoatTFC (
   ECHO Deleting old VegasGoatTFC src
   RMDIR /S /Q %MC_SRC%\VegasGoatTFC
   IF ERRORLEVEL 2 GOTO FAILED
)

ECHO Copying DummyTFC
XCOPY /E /Q /I src\TFC %MC_SRC%\TFC
IF ERRORLEVEL 1 GOTO FAILED

ECHO Copying VegasGoatTFC
XCOPY /E /Q /I src\VegasGoatTFC %MC_SRC%\VegasGoatTFC
IF ERRORLEVEL 1 GOTO FAILED

CD %MCP%
runtime\bin\python\python_mcp runtime\recompile.py --client
IF ERRORLEVEL 1 GOTO FAILED

runtime\bin\python\python_mcp runtime\reobfuscate.py --client
IF ERRORLEVEL 1 GOTO FAILED

CD %RETDIR%

IF EXIST %MC_SRC%\TFC (
   ECHO Deleting DummyTFC src
   RMDIR /S /Q %MC_SRC%\TFC
   IF ERRORLEVEL 2 GOTO FAILED
)

IF EXIST %MC_SRC%\VegasGoatTFC (
   ECHO Deleting VegasGoatTFC src
   RMDIR /S /Q %MC_SRC%\VegasGoatTFC
   IF ERRORLEVEL 2 GOTO FAILED
)

IF EXIST output (
   RMDIR /S /Q output
   IF ERRORLEVEL 2 GOTO FAILED
)

MD output
IF ERRORLEVEL 1 GOTO FAILED

XCOPY /E /Q /I dist\* output
IF ERRORLEVEL 1 GOTO FAILED

XCOPY /E /Q /I %MC_REOBF%\VegasGoatTFC output\VegasGoatTFC
IF ERRORLEVEL 1 GOTO FAILED

CD output
%ZIPCMD% a -r -tzip ..\%ZIPNAME% *
IF ERRORLEVEL 1 GOTO FAILED

CD %RETDIR%
RMDIR /S /Q output

ECHO Build complete
EXIT /B 0

:FAILED
CD %RETDIR%
ECHO Build FAILED
EXIT /B 1

