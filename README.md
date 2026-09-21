# GymApp

App Android (Kotlin + Jetpack Compose + Room) para gestionar rutinas de gimnasio por cliente.

## Cómo abrirlo
1. Abrí Android Studio (versión reciente, con Kotlin 1.9.24 y AGP 8.5 disponibles).
2. `File > Open` y seleccioná la carpeta `GymApp`.
3. Dejá que Gradle sincronice (puede tardar la primera vez, descarga dependencias).
4. Corré el módulo `app` en un emulador o celular físico.

## Cómo generar el APK instalable

**Desde Android Studio:** `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`. Queda en
`app/build/outputs/apk/debug/app-debug.apk`.

**Sin instalar nada, con GitHub Actions:**
1. Creá un repositorio nuevo en GitHub y subí esta carpeta (`git init`, `git add .`, `git commit`, `git push`).
2. El workflow en `.github/workflows/build-apk.yml` se dispara solo con cada push y compila el APK.
3. Andá a la pestaña **Actions** del repo → entrá a la corrida más reciente → descargá el artifact `GymApp-debug-apk`. Ahí adentro está el `.apk` para copiar al celular e instalar (activando "instalar apps de orígenes desconocidos" si Android lo pide).

## Cómo funciona
- Al abrir la app aparece un selector: **Entrenador** (administra todo) o un **Cliente** de la lista (si tiene PIN configurado, lo pide).
- Desde "Entrenador" podés:
  - Crear/editar/eliminar clientes (nombre, apellido, edad, teléfono, sexo, PIN opcional).
  - Administrar el catálogo de ejercicios (nombre + grupo muscular), reutilizable entre clientes.
  - Tocar un cliente para ver/armar su rutina.
- Dentro de la rutina de un cliente: se agregan "días" (ej. "Día 1 Pecho + Tríceps"), y a cada día se le agregan ejercicios desde el catálogo.
- Al tocar un ejercicio, se ve el último peso/repeticiones registrado y se puede cargar una nueva serie. Todo el historial queda guardado y visible debajo.

## Datos
Todo se guarda localmente en SQLite (Room), sin conexión a internet ni servidor. La base vive en el archivo `gymapp.db` dentro del dispositivo.

## Ideas para sumar después
- Gráfico de progreso por ejercicio (ej. con MPAndroidChart o Vico).
- Plantillas de rutina para copiar de un cliente a otro.
- Exportar el historial de un cliente a PDF o CSV.
- Notificación/recordatorio de día de entrenamiento.
