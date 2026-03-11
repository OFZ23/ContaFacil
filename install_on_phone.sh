#!/bin/bash

echo "================================================"
echo "   ContaFacil - Instalador para Celular"
echo "================================================"
echo ""

ADB=/home/offz/Android/Sdk/platform-tools/adb
APK=/home/offz/AndroidStudioProjects/ContaFacil/app/build/outputs/apk/debug/app-debug.apk

# Verificar que el APK existe
if [ ! -f "$APK" ]; then
    echo "❌ Error: El APK no existe. Compilando primero..."
    cd /home/offz/AndroidStudioProjects/ContaFacil
    ./gradlew assembleDebug

    if [ ! -f "$APK" ]; then
        echo "❌ Error: No se pudo compilar el APK"
        exit 1
    fi
fi

echo "✅ APK encontrado: app-debug.apk"
echo ""

# Verificar dispositivos conectados
echo "🔍 Verificando dispositivos conectados..."
DEVICES=$($ADB devices | grep -v "List of devices" | grep "device$" | wc -l)

if [ "$DEVICES" -eq 0 ]; then
    echo "❌ No hay dispositivos conectados"
    echo ""
    echo "Por favor:"
    echo "1. Conecta tu celular por USB"
    echo "2. Habilita la Depuración USB en Opciones de Desarrollador"
    echo "3. Acepta el mensaje de depuración USB en tu celular"
    echo ""
    echo "Luego ejecuta este script nuevamente."
    echo ""
    echo "O si prefieres, copia el APK a tu celular manualmente:"
    echo "$APK"
    exit 1
fi

echo "✅ Dispositivo(s) conectado(s):"
$ADB devices
echo ""

# Instalar aplicación
echo "📦 Instalando ContaFacil en tu celular..."
INSTALL_OUTPUT=$($ADB install -r $APK 2>&1)

if [[ $INSTALL_OUTPUT == *"Success"* ]]; then
    echo "✅ Aplicación instalada correctamente"
    echo ""

    # Preguntar si quiere iniciar la app
    echo "¿Deseas iniciar la aplicación ahora? (s/n)"
    read -r response

    if [[ "$response" =~ ^[SsYy]$ ]]; then
        echo "🚀 Iniciando ContaFacil..."
        $ADB shell am start -n com.example.contafacil/.MainActivity
        echo ""
        echo "✅ Aplicación iniciada. Revisa tu celular."
    fi
else
    echo "❌ Error al instalar:"
    echo "$INSTALL_OUTPUT"
    echo ""
    echo "Posibles soluciones:"
    echo "- Desinstala la versión anterior de ContaFacil"
    echo "- Verifica que tu Android sea versión 7.0 o superior"
    echo "- Habilita 'Instalar vía USB' en Opciones de Desarrollador"
fi

echo ""
echo "================================================"
echo "¿Necesitas ayuda? Lee: GUIA_INSTALACION_CELULAR.md"
echo "================================================"

