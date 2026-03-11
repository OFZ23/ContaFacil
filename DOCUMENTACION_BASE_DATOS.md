# 🗄️ Base de Datos de ContaFacil

## Ubicación de la Base de Datos

La base de datos de ContaFacil se almacena en tu dispositivo (celular o emulador) en:

```
/data/data/com.example.contafacil/databases/contafacil_database
```

**Nota:** Esta carpeta solo es accesible si tienes permiso de desarrollador y acceso root.

---

## 📋 Configuración de la Base de Datos

La base de datos está configurada en el archivo:

```
app/src/main/java/com/example/contafacil/data/local/AppDatabase.kt
```

### Nombre de la Base de Datos:
```
contafacil_database
```

### Tipo:
- **SQLite** (a través de Room)
- Almacenamiento **local** en el dispositivo
- **Sin conexión a internet** requerida
- Datos **privados** de la aplicación

---

## 📊 Tablas de la Base de Datos

La base de datos contiene **3 tablas principales**:

### 1. **ProductEntity** 📦 (Tabla de Productos)

Almacena información de los productos del negocio.

**Campos:**
- `id` (Clave primaria)
- `name` (Nombre del producto)
- `quantity` (Cantidad en stock)
- `price` (Precio unitario)
- `lastUpdated` (Fecha de última actualización)

**Ubicación del código:**
```
app/src/main/java/com/example/contafacil/data/local/entity/ProductEntity.kt
```

### 2. **TransactionEntity** 💰 (Tabla de Transacciones)

Almacena todas las ventas y compras del negocio.

**Campos:**
- `id` (Clave primaria)
- `type` (Tipo: SALE o PURCHASE)
- `productName` (Nombre del producto)
- `quantity` (Cantidad)
- `unitPrice` (Precio unitario)
- `totalAmount` (Monto total)
- `paymentMethod` (Forma de pago: EFECTIVO, TRANSFERENCIA, TARJETA, CREDITO)
- `isPaid` (¿Está pagada?)
- `date` (Fecha de la transacción)
- `notes` (Notas adicionales)

**Ubicación del código:**
```
app/src/main/java/com/example/contafacil/data/local/entity/TransactionEntity.kt
```

### 3. **ExpenseEntity** 💸 (Tabla de Gastos)

Almacena todos los gastos operativos del negocio.

**Campos:**
- `id` (Clave primaria)
- `category` (Categoría: AGUA, LUZ, INTERNET, ARRIENDO, NOMINA, OTROS)
- `amount` (Monto)
- `date` (Fecha del gasto)
- `description` (Descripción)
- `notes` (Notas adicionales)

**Ubicación del código:**
```
app/src/main/java/com/example/contafacil/data/local/entity/ExpenseEntity.kt
```

---

## 🔌 Acceso a la Base de Datos

### Desde el Código:

La base de datos se accede a través de **DAOs** (Data Access Objects):

```kotlin
// Obtener la instancia de la base de datos
val database = AppDatabase.getDatabase(context)

// Acceder a cada tabla
val productDao = database.productDao()
val transactionDao = database.transactionDao()
val expenseDao = database.expenseDao()
```

### DAOs Disponibles:

1. **ProductDao** - Operaciones CRUD de productos
   ```
   app/src/main/java/com/example/contafacil/data/local/dao/ProductDao.kt
   ```

2. **TransactionDao** - Operaciones CRUD de transacciones
   ```
   app/src/main/java/com/example/contafacil/data/local/dao/TransactionDao.kt
   ```

3. **ExpenseDao** - Operaciones CRUD de gastos
   ```
   app/src/main/java/com/example/contafacil/data/local/dao/ExpenseDao.kt
   ```

---

## 📁 Estructura de Archivos de la Base de Datos

### En el Código Fuente:

```
app/src/main/java/com/example/contafacil/data/
├── local/
│   ├── AppDatabase.kt              ← Configuración principal
│   ├── Converters.kt               ← Convertidores de tipos
│   ├── entity/
│   │   ├── ProductEntity.kt
│   │   ├── TransactionEntity.kt
│   │   ├── ExpenseEntity.kt
│   │   ├── PaymentMethod.kt        ← Enum de formas de pago
│   │   ├── TransactionType.kt      ← Enum de tipos de transacciones
│   │   └── ExpenseCategory.kt      ← Enum de categorías de gastos
│   ├── dao/
│   │   ├── ProductDao.kt
│   │   ├── TransactionDao.kt
│   │   └── ExpenseDao.kt
└── repository/
    ├── ProductRepository.kt
    ├── TransactionRepository.kt
    └── ExpenseRepository.kt
```

### En el Dispositivo (En Tiempo de Ejecución):

```
/data/data/com.example.contafacil/
├── databases/
│   ├── contafacil_database         ← Archivo de BD SQLite
│   └── contafacil_database-wal     ← Write-Ahead Log (para integridad)
├── shared_prefs/                   ← Preferencias compartidas
└── files/                           ← Otros archivos de la app
```

---

## 🔍 Cómo Ver la Base de Datos

### Opción 1: Android Studio Database Inspector

1. Ejecuta la app en el emulador/celular
2. En Android Studio: **View** → **Tool Windows** → **Database Inspector**
3. Verás las tablas y datos en tiempo real

### Opción 2: Acceso por ADB (Terminal)

```bash
# Listar archivos de la app
adb shell ls -la /data/data/com.example.contafacil/databases/

# Extraer la base de datos a tu PC
adb pull /data/data/com.example.contafacil/databases/contafacil_database

# Abrir con SQLite (si tienes sqlite3 instalado)
sqlite3 contafacil_database
```

### Opción 3: Usar SQLite Browser

1. Extrae la base de datos con ADB
2. Descarga **SQLite Browser** (aplicación gratuita)
3. Abre el archivo `contafacil_database`

---

## 💾 Ciclo de Vida de los Datos

### Cuando Instalas la App:

1. La base de datos se crea automáticamente la primera vez que se ejecuta
2. Room crea el archivo `contafacil_database` en `/data/data/com.example.contafacil/databases/`

### Cuando Usas la App:

1. **Registras una venta** → Se inserta en `TransactionEntity`
2. **Se actualiza inventario** → Se modifica `ProductEntity`
3. **Registras un gasto** → Se inserta en `ExpenseEntity`

### Datos Persistentes:

- Los datos se guardan automáticamente
- **No se pierden** cuando cierras la app
- **No necesitan internet** para funcionar
- Se mantienen hasta que **desinstales la app**

---

## 🔒 Seguridad de la Base de Datos

### Protección:

- Datos almacenados **localmente** en el dispositivo
- Protegidos por el **sandbox** de Android
- Otros usuarios/apps **no pueden acceder**
- Almacenamiento **privado** de la aplicación

### Copias de Seguridad:

- Los datos se pierden si desinstales la app
- No hay copias automáticas en la nube
- Se recomienda **exportar reportes** regularmente

---

## 📊 Ejemplos de Consultas

### Ver Todos los Productos:

```kotlin
val products = productDao.getAllProducts()
products.collect { productList ->
    productList.forEach { product ->
        println("${product.name}: ${product.quantity} unidades")
    }
}
```

### Ver Todas las Ventas:

```kotlin
val sales = transactionDao.getAllTransactions()
sales.collect { transactions ->
    transactions.forEach { transaction ->
        println("${transaction.productName}: ${transaction.totalAmount}")
    }
}
```

### Ver Todos los Gastos:

```kotlin
val expenses = expenseDao.getAllExpenses()
expenses.collect { expenseList ->
    expenseList.forEach { expense ->
        println("${expense.category}: ${expense.amount}")
    }
}
```

---

## 🔄 Migración de Datos

Si en el futuro necesitas cambiar la estructura de la base de datos:

1. Aumenta el `version` en `AppDatabase.kt`
2. Define una estrategia de migración
3. Room manejará la actualización automáticamente

**Nota:** Para este MVP, estamos en `version = 1` (estructura inicial)

---

## 📈 Estadísticas de la Base de Datos

### Tamaño:

- Inicial (vacía): ~50 KB
- Con muchos datos: ~200-500 KB (depende de registros)

### Rendimiento:

- Rápida y eficiente (SQLite)
- Consultas optimizadas con Room
- Sin latencia (almacenamiento local)

---

## 🛠️ Mantenimiento de la Base de Datos

### Limpiar Datos:

Para limpiar toda la base de datos (borrar todos los datos):

```kotlin
// Esto borra TODA la base de datos
AppDatabase.getDatabase(context).clearAllTables()
```

### Respaldar Datos:

```bash
# Extraer base de datos para respaldar
adb pull /data/data/com.example.contafacil/databases/contafacil_database ~/backup/

# Restaurar desde respaldo
adb push ~/backup/contafacil_database /data/data/com.example.contafacil/databases/
```

---

## ❓ Preguntas Frecuentes

**P: ¿Dónde se guardan los datos?**
R: En `/data/data/com.example.contafacil/databases/contafacil_database` en tu dispositivo.

**P: ¿Se sincroniza con la nube?**
R: No, todo es local. No hay sincronización automática.

**P: ¿Puedo compartir datos entre dispositivos?**
R: No directamente, pero puedes exportar reportes e importarlos en otro dispositivo.

**P: ¿Qué pasa si desinstalo la app?**
R: Se borra toda la base de datos. Por eso es importante hacer respaldos.

**P: ¿Es segura la información?**
R: Sí, está protegida por el sandbox de Android. Solo tu app puede acceder.

**P: ¿Puedo ver la base de datos en tiempo real?**
R: Sí, con el Database Inspector de Android Studio mientras la app está ejecutándose.

---

## 📚 Archivos Relacionados

Para aprender más sobre la base de datos, revisa estos archivos:

1. **AppDatabase.kt** - Configuración central
2. **ProductEntity.kt** - Estructura de productos
3. **TransactionEntity.kt** - Estructura de transacciones
4. **ExpenseEntity.kt** - Estructura de gastos
5. **ProductDao.kt** - Operaciones de productos
6. **TransactionDao.kt** - Operaciones de transacciones
7. **ExpenseDao.kt** - Operaciones de gastos

Todos en:
```
app/src/main/java/com/example/contafacil/data/local/
```

---

## 🎯 Resumen

- **Ubicación:** `/data/data/com.example.contafacil/databases/contafacil_database`
- **Tipo:** SQLite (a través de Room)
- **Tablas:** 3 (Products, Transactions, Expenses)
- **Almacenamiento:** Local (sin internet)
- **Acceso:** Privado de la aplicación
- **Configuración:** `AppDatabase.kt`

¡Tu base de datos está completamente funcional y lista para guardar todos los datos de tu negocio! 🚀

