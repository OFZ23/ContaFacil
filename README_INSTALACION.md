# ContaFacil 📱💼

Aplicación móvil de contabilidad básica para microempresarios.

**Producto Mínimo Viable (PMV)** desarrollado para facilitar el control de negocios pequeños.

---

## ✨ Características

- 💰 **Ingresos**: Registra ventas con producto, cantidad, precio y forma de pago
- 🛒 **Compras**: Gestiona compras a proveedores
- 💸 **Gastos**: Controla gastos del negocio (agua, luz, internet, arriendo, nómina, etc.)
- 📦 **Inventario**: Visualiza productos y stock actualizado automáticamente
- 📊 **Reportes**: Flujo de caja, cuentas por cobrar/pagar, estado de resultados
- 🎤 **Entrada por Voz**: Registra operaciones usando el micrófono

---

## 📲 ¿Cómo Instalar en tu Celular?

### 🎯 Método 1: USB + Android Studio (Para Desarrolladores)

1. Habilita **Opciones de Desarrollador** en tu celular (toca 7 veces en "Número de compilación")
2. Activa **Depuración USB** en Opciones de Desarrollador
3. Conecta tu celular por USB a tu PC
4. En Android Studio, selecciona tu dispositivo y haz clic en **Run** ▶️

### 📦 Método 2: Instalar APK Directamente (¡Más Fácil!)

**Este es el método recomendado si solo quieres usar la app:**

1. **Copia el APK a tu celular**
   - Archivo: `ContaFacil.apk` (está en la raíz del proyecto)
   - Puedes enviarlo por WhatsApp, email, o copiarlo por USB

2. **Habilita la instalación de fuentes desconocidas**
   - Ve a Ajustes > Seguridad
   - Activa "Instalar aplicaciones desconocidas" para tu navegador o gestor de archivos

3. **Instala la app**
   - Abre el archivo `ContaFacil.apk` desde tu celular
   - Toca **Instalar**
   - ¡Listo! Ya puedes usar ContaFacil

### 🤖 Método 3: Usar Scripts Automatizados

Si tienes tu celular conectado por USB y ADB configurado:

```bash
# Instalar en celular conectado
bash install_on_phone.sh

# O regenerar el APK primero
bash generate_apk.sh
```

**📖 [Guía Completa de Instalación →](GUIA_INSTALACION_CELULAR.md)**

---

## 🚀 Requisitos del Sistema

- **Android**: Versión 7.0 (Nougat) o superior
- **Espacio**: ~20 MB libres
- **Permisos**: Micrófono (para entrada por voz)

---

## 🛠️ Para Desarrolladores

### Compilar el Proyecto

```bash
cd /home/offz/AndroidStudioProjects/ContaFacil
./gradlew assembleDebug
```

### Ejecutar en Emulador

```bash
bash run_app.sh
```

### Ubicación del APK

- **Debug**: `app/build/outputs/apk/debug/app-debug.apk`
- **Copia directa**: `ContaFacil.apk` (raíz del proyecto)

### Estructura del Proyecto

```
app/src/main/java/com/example/contafacil/
├── data/              # Base de datos Room y repositorios
│   ├── local/
│   │   ├── entity/    # Entidades de BD
│   │   ├── dao/       # Data Access Objects
│   │   └── AppDatabase.kt
│   └── repository/    # Repositorios
├── domain/            # Lógica de negocio
│   ├── model/         # Modelos de dominio
│   └── service/       # Servicios
└── presentation/      # UI (Jetpack Compose)
    ├── income/        # Módulo de ingresos/ventas
    ├── purchases/     # Módulo de compras
    ├── expenses/      # Módulo de gastos
    ├── inventory/     # Módulo de inventario
    ├── reports/       # Módulo de reportes
    └── navigation/    # Navegación entre pantallas
```

---

## 📚 Documentación Adicional

- 📖 [Guía de Implementación](GUIA_IMPLEMENTACION.md) - Detalles técnicos del desarrollo
- ✅ [Proyecto Completado](PROYECTO_COMPLETADO.md) - Estado y features implementados
- 📱 [Guía de Instalación en Celular](GUIA_INSTALACION_CELULAR.md) - Instrucciones detalladas

---

## 🎯 Módulos Implementados

### 💰 Ingresos (Ventas)
- Registrar ventas con todos los detalles
- Formas de pago: Efectivo, Transferencia, Tarjeta, Crédito
- Entrada por voz: "Vendí 3 café volcán en efectivo"
- Lista de ventas registradas
- Actualiza inventario automáticamente

### 🛒 Compras
- Registrar compras a proveedores
- Mismas formas de pago que ventas
- Aumenta stock en inventario

### 💸 Gastos
- Categorías: Agua, Luz, Internet, Arriendo, Nómina, Otros
- Registro de todos los gastos operativos
- Historial completo

### 📦 Inventario
- Vista de todos los productos
- Cantidad disponible en tiempo real
- Alerta de stock bajo
- Actualización automática con ventas y compras

### 📊 Reportes Financieros
- **Flujo de Caja**: Ingresos - Compras - Gastos = Dinero Disponible
- **Cuentas por Cobrar**: Ventas a crédito pendientes
- **Cuentas por Pagar**: Compras a crédito pendientes
- **Estado de Resultados**: Ingresos - Costos - Gastos = Utilidad/Pérdida

---

## 🎤 Función de Entrada por Voz

Puedes registrar operaciones hablando:

1. Toca el ícono de **micrófono** 🎤 en la pantalla de Ingresos
2. Di algo como: **"Vendí 3 café volcán en efectivo"**
3. La app interpretará:
   - Producto: Café volcán
   - Cantidad: 3
   - Forma de pago: Efectivo
4. Confirma y guarda la venta

---

## 🧪 Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose (Material Design 3)
- **Base de Datos**: Room (SQLite)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Navegación**: Navigation Compose
- **Inyección de Dependencias**: Manual (repositories)
- **Reconocimiento de Voz**: Android Speech Recognition API

---

## 📝 Notas del Proyecto

Este es un **Producto Mínimo Viable (PMV)** desarrollado como proyecto universitario. Las funcionalidades están optimizadas para microempresarios que necesitan una herramienta simple y efectiva para controlar su negocio.

### Próximas Mejoras Sugeridas
- [ ] Exportar reportes a PDF
- [ ] Respaldo en la nube
- [ ] Gráficos y estadísticas
- [ ] Multi-usuario
- [ ] Modo oscuro
- [ ] Notificaciones de stock bajo

---

## 👨‍💻 Autor

Proyecto universitario - ContaFacil

---

## 📄 Licencia

Proyecto educativo para uso académico.

---

## ❓ ¿Necesitas Ayuda?

Si tienes problemas para instalar o usar la app:

1. Lee la [Guía de Instalación Completa](GUIA_INSTALACION_CELULAR.md)
2. Verifica que tu Android sea 7.0 o superior
3. Asegúrate de habilitar "Fuentes Desconocidas" al instalar el APK
4. Si la app se cierra, verifica los permisos de la app (especialmente micrófono)

---

**¡Listo para usar! 🚀**

El APK está disponible en: `ContaFacil.apk`

