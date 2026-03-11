#!/bin/bash

echo "================================================"
echo "   Generando APK para Distribución"
echo "================================================"
echo ""

cd /home/offz/AndroidStudioProjects/ContaFacil

echo "🔨 Compilando APK de debug..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    APK_SIZE=$(du -h "$APK_PATH" | cut -f1)

    echo ""
    echo "✅ APK generado exitosamente"
    echo ""
    echo "📦 Ubicación: $APK_PATH"
    echo "📊 Tamaño: $APK_SIZE"
    echo ""
    echo "================================================"
    echo "   Cómo instalarlo en tu celular:"
    echo "================================================"
    echo ""
    echo "Opción 1: USB + ADB"
    echo "  bash install_on_phone.sh"
    echo ""
    echo "Opción 2: Transferir manualmente"
    echo "  1. Copia el archivo a tu celular:"
    echo "     $APK_PATH"
    echo "  2. Ábrelo desde tu celular"
    echo "  3. Toca 'Instalar'"
    echo ""
    echo "Opción 3: Por email/WhatsApp"
    echo "  Envíate el APK y descárgalo en tu celular"
    echo ""

    # Crear copia fácil de encontrar
    echo "📋 Creando copia en el directorio raíz del proyecto..."
    cp "$APK_PATH" "ContaFacil.apk"
    echo "✅ Copia creada: ContaFacil.apk"
    echo ""

else
    echo "❌ Error al compilar el APK"
    echo "Revisa los errores arriba"
    exit 1
fi

echo "================================================"
echo "✅ ¡Listo para instalar!"
echo "================================================"

