# Tarea-2-Paradigmas

Este programa fue desarrollado en Java utilizando la biblioteca Swing para crear una aplicación que permite administrar una prueba compuesta por preguntas según los niveles de la taxonomía de Bloom. Al iniciar la aplicación, el usuario puede seleccionar un archivo de texto que contiene los ítems de la evaluación. Una vez cargado el archivo, se muestra en pantalla la cantidad total de preguntas y el tiempo estimado para completar la prueba. Luego, es posible comenzar la prueba y responder cada pregunta una por una. 

El programa permite retroceder a preguntas anteriores y avanzar a las siguientes. Al llegar a la última pregunta, se puede enviar la prueba para revisar las respuestas. Al finalizar, se muestra un resumen con el porcentaje de respuestas correctas por nivel de la taxonomía y por tipo de pregunta. También se puede revisar cada respuesta para ver si fue correcta o no. 

Para ejecutar el programa, se debe compilar y correr el proyecto en un entorno con Java. Al iniciar, se abre una ventana donde se puede cargar el archivo de preguntas desde el explorador de archivos del sistema. El archivo debe tener extensión .txt y seguir el formato que se indica a continuación. 

El archivo de ítems está compuesto por una pregunta por línea, y cada línea tiene seis partes separadas por comas. Estas partes son: el tipo de pregunta, el nivel de la taxonomía, el enunciado, las opciones (en caso de ser pregunta múltiple), la respuesta correcta, y el tiempo estimado para resolverla. Las preguntas de tipo múltiple deben tener las opciones separadas por punto y coma, y las de verdadero o falso deben dejar el campo de opciones vacío. 

La respuesta correcta debe coincidir con una de las opciones si es pregunta múltiple, o ser TRUE o FALSE si es una pregunta de verdadero o falso. El tiempo debe estar indicado en segundos. 

Este es el formato que debe seguir cada línea del archivo: 
tipo_de_pregunta,nivel_taxonomico,enunciado,opciones,respuesta_correcta,tiempo_en_segundos 

El programa valida que el archivo esté bien escrito. Si hay errores en el formato o faltan datos, se muestra un mensaje y no se puede continuar hasta que el archivo sea corregido. Este archivo README incluye todos los alcances y supuestos incorporados en el desarrollo del programa, además de explicar cómo debe estar hecho el archivo que se carga al inicio.
