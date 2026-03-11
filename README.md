# ContaFacil - Sistema de Contabilidad para Microempresarios

## 📲 Descargar la App (APK)

> El APK listo para instalar está en la raíz del repositorio:
>
> **[`ContaFacil.apk`](ContaFacil.apk)** ← descarga este archivo y ábrelo en tu celular Android

### Pasos para instalar
1. Descarga el archivo `ContaFacil.apk` desde esta página (botón **Raw** o **Download**)
2. Envíatelo al celular por WhatsApp, email o USB
3. En el celular, ve a **Ajustes → Seguridad** y activa **"Fuentes desconocidas"**
4. Abre el archivo `.apk` desde el celular y toca **Instalar**
5. ¡Listo! Busca **ContaFacil** en tu menú de apps

> Requiere Android 7.0 o superior · Espacio: ~20 MB · Permiso de micrófono (para entrada por voz)

---

## Descripción
ContaFacil es una aplicación móvil Android diseñada para ayudar a microempresarios a controlar su negocio de forma sencilla. Es un Producto Mínimo Viable (PMV) desarrollado como proyecto universitario.

## Características Principales

### 1. Módulo de Ingresos (Ventas)
- Registrar ventas del negocio
- Campos: producto, cantidad, precio, forma de pago
- Formas de pago: efectivo, transferencia, tarjeta, crédito
- Actualización automática de inventario al vender

### 2. Módulo de Compras
- Registrar compras a proveedores
- Campos: producto, cantidad, precio, forma de pago
- Actualización automática de inventario al comprar

### 3. Módulo de Gastos
- Registrar gastos operativos del negocio
- Categorías: agua, luz, internet, arriendo, nómina, otros
- Seguimiento de gastos pagados y pendientes

### 4. Módulo de Inventario
- Visualización automática de productos disponibles
- Stock actualizado automáticamente con ventas y compras
- Alertas de stock bajo
- Edición de precios y límites de stock mínimo

### 5. Módulo de Reportes
- **Flujo de Caja**: Ingresos - Compras - Gastos = Dinero disponible
- **Cuentas por Cobrar**: Clientes que deben dinero (ventas a crédito)
- **Cuentas por Pagar**: Deudas con proveedores (compras a crédito)
- **Estado de Resultados**: Ingresos - Costos - Gastos = Utilidad/Pérdida

## Arquitectura Técnica

### Stack Tecnológico
- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose con Material Design 3
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Base de Datos**: Room Database
- **Navegación**: Navigation Compose
- **Gestión de Estado**: StateFlow

### Estructura del Proyecto
```
com.example.contafacil/
├── data/
│   ├── local/
│   │   ├── entity/          # Entidades de base de datos
│   │   ├── dao/             # Data Access Objects
│   │   ├── Converters.kt    # Type converters para Room
│   │   └── AppDatabase.kt   # Configuración de Room
│   └── repository/          # Repositorios de datos
├── presentation/
│   ├── income/              # Pantalla de Ingresos
│   ├── purchases/           # Pantalla de Compras
│   ├── expenses/            # Pantalla de Gastos
│   ├── inventory/           # Pantalla de Inventario
│   ├── reports/             # Pantalla de Reportes
│   └── navigation/          # Sistema de navegación
├── domain/
│   ├── model/               # Clases principales de negocio
│   └── service/             # Lógica base reusable para inventario y reportes
└── ui/
    └── theme/               # Tema de la aplicación
```

## Instalación y Configuración

### Requisitos Previos
- Android Studio Hedgehog o superior
- JDK 11 o superior
- SDK de Android 24 (Android 7.0) o superior

### Pasos de Instalación
1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Sincronizar Gradle
4. Ejecutar la aplicación en un dispositivo o emulador

## Uso de la Aplicación

### Registrar una Venta
1. Ir al módulo "Ingresos"
2. Presionar el botón flotante "+"
3. Ingresar: producto, cantidad, precio unitario, forma de pago
4. El inventario se actualizará automáticamente

### Registrar una Compra
1. Ir al módulo "Compras"
2. Presionar el botón flotante "+"
3. Ingresar: producto, cantidad, precio unitario, forma de pago
4. El inventario se actualizará automáticamente

### Registrar un Gasto
1. Ir al módulo "Gastos"
2. Presionar el botón flotante "+"
3. Seleccionar categoría, descripción, monto, forma de pago

### Ver Inventario
1. Ir al módulo "Inventario"
2. Ver lista de productos con stock actual
3. Las alertas de stock bajo se muestran en rojo
4. Editar precios o límites de stock según necesidad

### Consultar Reportes
1. Ir al módulo "Reportes"
2. Ver reportes financieros del mes actual:
   - Flujo de caja
   - Cuentas por cobrar
   - Cuentas por pagar
   - Estado de resultados

## Funcionalidades Futuras (Roadmap)

### Próximas Versiones
- [x] Entrada de datos por voz (Speech-to-Text)
- [x] Autocompletado de productos al registrar ventas
- [x] Cálculo correcto de flujo de caja y estado de resultados
- [x] Diseño visual moderno con Material Design 3
- [ ] Filtros por fecha en reportes
- [ ] Gráficos y visualizaciones
- [ ] Exportación de reportes a PDF/CSV
- [ ] Backup en la nube
- [ ] Múltiples negocios
- [ ] Notificaciones de stock bajo

## Contribuciones
Este es un proyecto universitario. Para contribuir, por favor crear un fork y enviar pull requests.

## Licencia
Proyecto académico - Universidad [Nombre de la Universidad]

## Autores
- [Tu Nombre] - Desarrollo inicial

## Agradecimientos
- Profesor guía del proyecto
- Comunidad de Android Developers
- Material Design 3 Guidelines
