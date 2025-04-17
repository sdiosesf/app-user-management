# SMARTJOB - GESTIÓN DE USUARIOS

Este proyecto es una API REST desarrollada en **Java** utilizando **Spring Boot** para la gestión de usuarios. Proporciona endpoints para crear, actualizar, listar y eliminar usuarios, así como para realizar login.

## Requisitos
- **Java 11** o superior
- **Maven** para la gestión de dependencias
- **Base de datos SQL** configurada en el archivo `application.properties`

## Tecnologías utilizadas
- **Java 11**
- **Spring Boot**
- **Hibernate/JPA**
- **Maven**
- **H2 Database**

## Swagger
- **http://localhost:8080/smartjob/swagger-ui/index.html**

## Endpoints

### 1. Login de usuario
**URL:** `POST /smartjob/login`  
**Descripción:** Permite autenticar a un usuario con su nombre de usuario y contraseña.  
**Body de ejemplo:**
```json
{
  "usuario": "admin",
  "password": "Insert1991$"
}
```

### 2. Crear usuario
**URL:** `POST /smartjob/users`  
**Descripción:** Crea un nuevo usuario con la información.   
**Body de ejemplo:**
```json
{
	"name": "Mariana Dioses",
	"email": "mariana@gmail.com",
	"password": "mariana28$",
	"phones": [
		{
			"number": "12345678",
			"cityCode": "34",
			"countryCode": "12"
		},
		{
			"number": "12345671",
			"cityCode": "23",
			"countryCode": "43"
		}
	]
}
```

### 3. Actualizar usuario
**URL:** `PUT /smartjob/users/:id`  
**Descripción:** Actualiza la información de un usuario existente.  
**Body de ejemplo:**
```json
{
	"id": "c38437a2-d4c7-4a85-8832-f22ec799c283",
	"name": "Mariana Dioses",
	"email": "mariana_updated@gmail.com",
	"password": "MarianaUpdated28$",
	"phones": [
		{
			"id": "1",
			"number": "12345678",
			"cityCode": "34",
			"countryCode": "12"
		},
		{
			"id": "2",
			"number": "12345671",
			"cityCode": "23",
			"countryCode": "43"
		}
	]
}
```

### 4. Listar todos los usuarios
**URL:** `GET /smartjob/users`  
**Descripción:** Devuelve una lista de todos los usuarios registrados.  
**Body de ejemplo:**
```json
[
	{
		"id": "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",
		"name": "Mario Dioses",
		"email": "sdiosesf@gmail.com",
		"password": "$2a$10$/DAZmyFyYCijIxCVa6oVG.yZx2o2QYcVTmwBm8tslIxWw9SIyaQja",
		"phones": [
			{
				"id": 1,
				"number": "951693606",
				"cityCode": "58",
				"countryCode": "56"
			},
			{
				"id": 2,
				"number": "961786380",
				"cityCode": "51",
				"countryCode": "01"
			}
		],
		"created": "2025-04-15T12:08:20.509-0400",
		"modified": null,
		"lastLogin": "2025-04-17T14:14:31.259-0500",
		"active": true
	},
	{
		"id": "c38437a2-d4c7-4a85-8832-f22ec799c283",
		"name": "Mariana1 Dioses1",
		"email": "mariana@gmail.com",
		"password": "$2a$10$H..dGcwWY.IQhuX3uuwn.uK90OWRMXF1t/UfdI2mUqB7hncbeNWgS",
		"phones": [
			{
				"id": 3,
				"number": "2222222",
				"cityCode": "22",
				"countryCode": "222"
			},
			{
				"id": 4,
				"number": "33333",
				"cityCode": "333",
				"countryCode": "333"
			}
		],
		"created": "2025-04-17T14:14:35.221-0500",
		"modified": "2025-04-17T14:14:53.364-0500",
		"lastLogin": "2025-04-17T14:14:35.221-0500",
		"active": true
	}
]
```

### 5. Obtener usuario por ID
**URL:** `GET /smartjob/users/:id`  
**Descripción:** Devuelve la información de un usuario específico por su ID.  
**Body de ejemplo:**
```json
{
	"id": "c38437a2-d4c7-4a85-8832-f22ec799c283",
	"name": "Mariana1 Dioses1",
	"email": "mariana@gmail.com",
	"password": "$2a$10$H..dGcwWY.IQhuX3uuwn.uK90OWRMXF1t/UfdI2mUqB7hncbeNWgS",
	"phones": [
		{
			"id": 3,
			"number": "2222222",
			"cityCode": "22",
			"countryCode": "222"
		},
		{
			"id": 4,
			"number": "33333",
			"cityCode": "333",
			"countryCode": "333"
		}
	],
	"created": "2025-04-17T14:14:35.221-0500",
	"modified": "2025-04-17T14:14:53.364-0500",
	"lastLogin": "2025-04-17T14:14:35.221-0500",
	"active": true
}
```

### 6. Eliminar usuario
**URL:** `DELETE /smartjob/users/:id`  
**Descripción:** Elimina un usuario específico por su ID.   
**Respuesta:** Código de estado 204 No Content si la eliminación es exitosa.

## Autor
- **Mario Dioses**