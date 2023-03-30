# Teapot Service
An example REST service for a mythical teapot. Now with a Teapot Worker interface.

## Conductor Setup
I recommend the [Orkes Coummunity Conductor] project for running Conductor.

## Root Service
Returns a payload indicating that this is a teapot, along with the status code of 418.

## Jokes
GET /joke - will return a random joke.

GET /joke/{index} - will return the given joke, index begins at zero, ends when 
the service is out of jokes.


## Orkes Conductor Integrations
teapot_rest_wf.json creates a workflow for requesting jokes from the teapot REST
service. teapot_worker_wf.json supports a Conductor Java API workflow. 