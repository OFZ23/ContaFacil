# 📱 Guía: Cómo Instalar ContaFacil en tu Celular

Hay **dos formas** de instalar tu app en tu celular físico:

## Opción 1: Instalación Directa desde Android Studio (Desarrollo)

### Paso 1: Habilitar Opciones de Desarrollador en tu Celular

1. Ve a **Ajustes** > **Acerca del teléfono**
2. Busca **Número de compilación** o **Versión de MIUI/Android**
3. Toca **7 veces** sobre ese número
4. Verás un mensaje: "Ahora eres desarrollador"

### Paso 2: Habilitar Depuración USB

1. Ve a **Ajustes** > **Opciones de desarrollador**
2. Activa **Depuración USB**
3. También activa **Instalar vía USB** (si está disponible)

### Paso 3: Conectar tu Celular a la PC

1. Conecta tu celular a la PC con el cable USB
2. En tu celular, aparecerá un mensaje: **¿Permitir depuración USB?**
3. Marca "Permitir siempre desde este equipo" y toca **Aceptar**

### Paso 4: Verificar Conexión

Abre una terminal y ejecuta:
```bash
~/Android/Sdk/platform-tools/adb devices
```

Deberías ver algo como:
```
List of devices attached
ABC123DEF456    device
```

### Paso 5: Ejecutar la App desde Android Studio

1. En Android Studio, abre el proyecto ContaFacil
2. En la barra superior, selecciona tu dispositivo (debe aparecer el modelo de tu celular)
3. Haz clic en el botón **Run** (▶️) o presiona `Shift + F10`
4. La app se instalará automáticamente en tu celular

---

## Opción 2: Instalar APK Directamente (Sin Cables)

Esta es la forma **más sencilla** si solo quieres probar la app sin conectar cables.

### Paso 1: Generar el APK

Ejecuta el script que crearé para ti:
```bash
cd /home/offz/AndroidStudioProjects/ContaFacil
bash generate_release_apk.sh
```

O manualmente desde la terminal:
```bash
cd /home/offz/AndroidStudioProjects/ContaFacil
./gradlew assembleDebug
```

El APK se generará en:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Paso 2: Transferir el APK a tu Celular

Puedes usar cualquiera de estos métodos:

**a) Por Email:**
- Envíate el APK por correo electrónico
- Ábrelo desde tu celular y descárgalo

**b) Por WhatsApp/Telegram:**
- Envíate el APK a ti mismo
- Descárgalo en tu celular

**c) Por Google Drive/Dropbox:**
- Sube el APK a la nube
- Descárgalo desde tu celular

**d) Por USB:**
- Copia el archivo `app-debug.apk` a la carpeta de Descargas de tu celular

### Paso 3: Habilitar Instalación desde Fuentes Desconocidas

En tu celular:

1. Ve a **Ajustes** > **Seguridad** o **Privacidad**
2. Busca **Instalar aplicaciones desconocidas** o **Fuentes desconocidas**
3. Activa el permiso para el navegador, Gmail, o el gestor de archivos que uses

### Paso 4: Instalar el APK

1. Abre el archivo **app-debug.apk** desde tu celular
2. Toca **Instalar**
3. Espera a que termine la instalación
4. Toca **Abrir** para ejecutar la app

---

## Opción 3: Instalar Usando ADB (Sin Android Studio)

Si tienes el celular conectado por USB:

```bash
~/Android/Sdk/platform-tools/adb install -r /home/offz/AndroidStudioProjects/ContaFacil/app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔧 Solución de Problemas

### Mi celular no aparece en Android Studio

1. Verifica que la depuración USB esté habilitada
2. Prueba con otro cable USB (algunos cables solo cargan)
3. Cambia el **modo USB** en tu celular a **Transferencia de archivos** o **MTP**
4. Revoca las autorizaciones USB en Opciones de desarrollador y vuelve a conectar

### No puedo instalar el APK

1. Asegúrate de habilitar **Fuentes desconocidas** o **Instalar aplicaciones desconocidas**
2. Si dice "App no instalada", desinstala cualquier versión anterior de ContaFacil

### La app se cierra al abrirla

1. Verifica que tu Android sea **versión 7.0 o superior** (minSdk 24)
2. Revisa los logs con: `adb logcat | grep contafacil`

---

## 📦 APK ya Compilado

Tu APK de debug ya está disponible en:
```
/home/offz/AndroidStudioProjects/ContaFacil/app/build/outputs/apk/debug/app-debug.apk
```

**Tamaño aproximado:** ~15-20 MB

Puedes copiarlo a tu celular ahora mismo y probarlo. ✅

---

## 🚀 Para Producción (Opcional)

Si quieres crear una versión **firmada y optimizada** para publicar en Google Play o distribuir:

```bash
cd /home/offz/AndroidStudioProjects/ContaFacil
bash generate_release_apk.sh
```

Esto creará un APK de release en:
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

Para firmarlo, necesitarás crear un **keystore** (firma digital), pero eso es solo necesario para publicar en la Play Store.

