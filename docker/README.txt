To build, run these docker commands from the terminal.

docker build -t edr10:local .

docker run -p 9090:8080/tcp edr10:local

Ignore the Errors/Exceptions at startup

From your browser, visit http://localhost:9090/teamengine

Log in with the username 'developer' and password 'developer'


Try out the EDR API ETS with these combinations of inputs.

Location of the landing page : https://ogcie.iblsoft.com/edr

Location of OpenAPI definition document : https://opengeospatial.github.io/ogcapi-environmental-data-retrieval/candidate-standard/openapi/EDR_OpenAPI.json

Conformance class:  Level 1 (Note this input is ignored)

Alternatively, try it out with:

Location of the landing page : https://data-api.mdl.nws.noaa.gov/EDR-API

Location of OpenAPI definition document : https://data-api.mdl.nws.noaa.gov/EDR-API/api?f=json

Conformance class:  Level 1 (Note this input is ignored)
