# Getting Started

## Hotel IAS api restfull
A continuación encontrará la documentación de como hacer
el consumo de la api rest full que se construyen para
administrar hoteles en la prueba técnica de IAS.

## Descripción técnica

### Base path
http://localhost:8085/api/hotel

### EndPoints disponibles para el consumo
#### POST:
* path **/room**: Este permite la creación de una habitación
es necesario que se envíe el siguiente body para que se
cree la habitación de forma correcta.
```
{
    "roomType": "String",
    "roomState": "String",
    "nightValue": 0,
    "numberGuests": 50
}
```
**Nota:** La habitación se puede crear con o sin state, ya
que sí no es especifica el estado, este se asigna por defecto
como disponible. Los dos estados que se deben usar son "Disponible"
y "No disponible"

Ejemplo de la respuesta del servicio:
```
{
    "data": {
        "roomId": "101",
        "roomType": "Sencilla",
        "roomState": "Disponible",
        "nightValue": 100000,
        "numberGuests": 2,
        "userId": null,
        "reservationDate": null
    },
    "status": "201 CREATED",
    "message": "Created room"
}
```

* path **/user**: Este permite reservar una habitación 
a nombre de un usuario.
A continuación se describe el body que se debe enviar para 
la creación de un usuario:
```
{
    "identification": 0,
    "documentType": "String",
    "name": "String",
    "startDate": "String format dd/mm/yyyy",
    "endDate": "String format dd/mm/yyyy",
    "roomId": "String number of room"
}
```
Los campos requeridos para registrar al usuario con la
reservación son:
* identification
* name
* startDate
* EndDate
* roomId

En caso de que alguno de estos campos no vaya, se presenta 
una excepción de negocio y no se puede crear la reservación

Ejemplo de la respuesta del servicio:
```
{
    "data": {
        "identification": 55555555,
        "documentType": "cc",
        "name": "Walther",
        "startDate": "21/04/2024",
        "endDate": "22/04/2024",
        "roomId": "101"
    },
    "status": "201 CREATED",
    "message": "Created User"
}
```

* path **/rooms**: Este permite visualizar las habitaciones
disponibles y se pueden aplicar diferentes filtros, a 
continuación se especifican los filtros que se pueden
aplicar en el body.
Se debe realizar la petición con los filtros que se desean
aplicar en el body.
```
{
    "roomType": "String",
    "roomState": "String",
    "nightValue": 0,
    "numberGuests": 50
}
```

Se debe mandar el header "hotel-id" con valor "hotel_ias_med", ya que este
es el id del hotel y a este están atadas todas la habitaciones.

#### DELETE:
* path **/room/{id}**: Este path permite eliminar una 
habitación; sin embargo, se hace un borrado lógico,
se deja la habitación en estado no disponible.

Se debe mandar el header "hotel-id" con valor "hotel_ias_med", ya que este
es el id del hotel y a este están atadas todas la habitaciones.

Ejemplo de la respuesta de la petición:

```
{
    "data": {
        "roomId": "107",
        "roomType": "Sencilla",
        "roomState": "No disponible",
        "nightValue": 100000,
        "numberGuests": 2,
        "userId": null,
        "reservationDate": null,
        "reservationStartDate": null,
        "reservationEndDate": null
    },
    "status": "200 OK",
    "message": "Deleted room"
}
```
## La aplicación cuenta con una tarea programada, la cual
al iniciar el día asigna todas las tares programadas
para ese día y al finalizar el día, va liberando las 
habitaciones que se entregan este día.

La clase se encuentra en los entry-points, se llama 
SensorTrigger y sí se desea modificar el horario de 
ejecución de dichas tareas programadas, basta
con modificar las variables 

```
private static CRON_STRING_APPLY = "0 01 01 * * *";
private static CRON_STRING_DES_APPLY = "0 59 11 * * *";
```


### Excepciones de negocio

```
    * UN_EXPECTED_ERROR("HIB0001", "Un expected error was presented"),
    * IDENTIFICATION_NULL("HIB0002", "User identification is a required field to do the reservation"),
    * NAME_NULL("HIB0003", "User name is a required field to do the reservation"),
    * START_DATE_NULL("HIB0004", "Start date is a required field to do the reservation"),
    * END_DATE_NULL("HIB0005", "End date is a required field to do the reservation"),
    * ROOM_NULL("HIB0006", "Number of room is a required field to do the reservation"),
    * ROOM_NOT_EXIST("HIB0007", "The room requested don´t exist, please validate the available rooms"),
    * ROOM_NOT_AVAILABLE("HIB0008", "The room requested don´t is available, please validate the available rooms");
    * IDENTIFICATION_LENGTH("HIB0009", "The identification must to contain less of 12 characters"),
    * NAME_LENGTH("HIB0009", "The name must to contain less of 30 characters");
    * NAME_NUMBER("HIB0010", "The name can´t contain numbers");
```
### Excepciones Técnicas
```
    * COULD_NOT_REGISTER_USER("HIT001", "Could not create user, please check the request"),
    * COULD_NOT_REGISTER_ROOM("HIT002", "Could not create room, please check the request"),
    * COULD_NOT_LIST_ROOM("HIT003", "Could not list the room, please check the request"),
    * COULD_NOT_LIST_ALL_ROOMS("HIT004", "Could not list all rooms, please check the request");
```
