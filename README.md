# Teapot Service
An example REST service for a mythical teapot. Now with a virtual teapot brewing api.

## Root Service
Returns a payload indicating that this is a teapot, along with the status code of 418.

## Jokes
GET /joke - will return a random joke.

GET /joke/{index} - will return the given joke, index begins at zero, ends when
the service is out of jokes.

## Netflix Conductor Branch
I recommend the [Orkes Coummunity Conductor] project for running Conductor.

### Orkes Conductor Integrations
teapot_rest_wf.json creates a workflow for requesting jokes from the teapot REST
service.

teapot_wf.json creates a workflow using the SDK implementation of the teapot joke 
interface. 

## Solace Branch
Runs with the Solace PubSub+ local server in a Docker container.