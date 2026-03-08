#!/bin/bash
ADB=/home/offz/Android/Sdk/platform-tools/adb
APK=/home/offz/AndroidStudioProjects/ContaFacil/app/build/outputs/apk/debug/app-debug.apk

echo "Verificando dispositivos conectados..."
$ADB devices

echo "Instalando aplicación..."
$ADB install -r $APK

echo "Limpiando logs anteriores..."
$ADB logcat -c

echo "Iniciando aplicación..."
$ADB shell am start -n com.example.contafacil/.MainActivity

echo "Esperando 3 segundos..."
sleep 3

echo "Capturando logs..."
$ADB logcat -d | grep -E "(contafacil|AndroidRuntime)" | tail -50

echo ""
echo "La aplicación se ha instalado y ejecutado."
echo "Revisa tu emulador para verificar que funcione correctamente."

