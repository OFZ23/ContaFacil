# ✅ Checklist: Instalar ContaFacil en tu Celular

## 📋 Antes de Empezar

- [ ] Tengo Android 7.0 o superior
- [ ] Tengo al menos 20 MB libres en mi celular
- [ ] Tengo el archivo ContaFacil.apk

---

## 🎯 Método Recomendado: Transferir APK

### Paso 1: Transferir el APK
- [ ] He copiado ContaFacil.apk a mi celular usando:
  - [ ] WhatsApp / Telegram
  - [ ] Email
  - [ ] USB
  - [ ] Google Drive / Dropbox

### Paso 2: Habilitar Instalación
- [ ] Voy a Ajustes → Seguridad
- [ ] Busco "Instalar aplicaciones desconocidas" o "Fuentes desconocidas"
- [ ] Activo el permiso para mi navegador/gestor de archivos

### Paso 3: Instalar
- [ ] Abro el archivo ContaFacil.apk desde mi celular
- [ ] Toco "Instalar"
- [ ] Espero a que termine la instalación
- [ ] Toco "Abrir"

### Paso 4: Configurar Permisos
- [ ] La app pide permiso de micrófono
- [ ] Acepto el permiso (necesario para entrada por voz)

### Paso 5: ¡Probar la App!
- [ ] La app abre correctamente
- [ ] Puedo ver las 5 pestañas: Ingresos, Compras, Gastos, Inventario, Reportes
- [ ] Pruebo registrar una venta
- [ ] Pruebo el botón de micrófono

---

## 🔌 Método Alternativo: USB + ADB

### Para Desarrolladores

- [ ] He habilitado Opciones de Desarrollador (tocar 7 veces en Número de compilación)
- [ ] He activado Depuración USB
- [ ] He conectado mi celular por USB
- [ ] He aceptado el mensaje de depuración USB en mi celular
- [ ] He ejecutado: `bash install_on_phone.sh`
- [ ] La app se instaló correctamente

---

## ❌ Solución de Problemas

### Si no puedo instalar el APK:
- [ ] He habilitado "Fuentes desconocidas" o "Instalar aplicaciones desconocidas"
- [ ] He desinstalado versiones anteriores de ContaFacil
- [ ] Mi Android es 7.0 o superior

### Si la app se cierra inmediatamente:
- [ ] He verificado que tengo Android 7.0 o superior
- [ ] He reinstalado la app
- [ ] He dado permisos a la app en Ajustes → Aplicaciones → ContaFacil → Permisos

### Si mi celular no aparece en Android Studio:
- [ ] He habilitado Depuración USB
- [ ] He aceptado el mensaje en mi celular
- [ ] He probado con otro cable USB
- [ ] He cambiado el modo USB a "Transferencia de archivos"

---

## 📁 Ubicación de Archivos

```
/home/offz/AndroidStudioProjects/ContaFacil/
├── ContaFacil.apk                    ← El APK listo para instalar
├── GUIA_INSTALACION_CELULAR.md       ← Guía completa
├── GUIA_RAPIDA.txt                   ← Referencia rápida
├── README_INSTALACION.md             ← Documentación
├── install_on_phone.sh               ← Script de instalación automática
├── generate_apk.sh                   ← Script para generar APK
└── app/build/outputs/apk/debug/
    └── app-debug.apk                 ← APK original
```

---

## 🎯 ¿Qué Necesito Exactamente?

**Para instalar en tu celular personal:**
→ Solo necesitas el archivo: `ContaFacil.apk`
→ Transfiérelo a tu celular y ábrelo

**Para desarrollo con Android Studio:**
→ Conecta tu celular por USB
→ Habilita Depuración USB
→ Ejecuta desde Android Studio

---

## ✅ Verificación Final

Una vez instalada, verifica que:

- [ ] La app abre sin cerrarse
- [ ] Veo la pantalla principal con 5 opciones
- [ ] Puedo navegar entre: Ingresos, Compras, Gastos, Inventario, Reportes
- [ ] El botón de micrófono funciona (pide permiso)
- [ ] Puedo registrar una venta de prueba
- [ ] La venta aparece en la lista
- [ ] El inventario se actualiza

---

## 🎉 ¡Éxito!

Si completaste todos los checks de "Verificación Final", ¡tu app está lista para usar!

**Próximos pasos:**
1. Registra algunos productos en el inventario
2. Haz ventas de prueba
3. Explora los reportes
4. Prueba la entrada por voz diciendo: "Vendí 3 café en efectivo"

---

**Proyecto:** ContaFacil - Sistema de Contabilidad para Microempresarios
**Versión:** 1.0 (MVP)
**Android mínimo:** 7.0 (API 24)

