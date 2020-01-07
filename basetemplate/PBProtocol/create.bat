set dir=H:\github\h5\server\basetemplate\PBProtocol
for /f "delims=" %%i in ('dir /b/a "%dir%\proto\*.proto"') do (%dir%\protoc.exe --proto_path=%dir%\proto --java_out=%dir%\src %dir%\proto\%%i) 
pause