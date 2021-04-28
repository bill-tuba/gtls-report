# gtls-report

Report sample homework

## Usage

The project may be executed as a cli application or a 
standalone api-server

Run the project as a cli application via `clojure`:

    $ ./bin/run-cli <FILES>

Run the project as a api server via `clojure**:

    $ ./bin/run-server <PORT>

## Examples:
### CLI
    
    $ ./bin/run-cli ./resources/sample.csv ./resources/sample.space-sv ./resources/sample.pipe-sv
    
### SERVER

    $ ./bin/run-server 3000

### Running tests

    $ ./bin/kaocha

### ... tests+coverage

    $ ./bin/coverage
    
## Uberjar
### CLI
    
    $ ./bin/build-cli
    $ java -jar report-cli.jar ./resources/sample.csv  ...other files etc
    
### SERVER

    $ ./bin/build-server

    $ java -jar report-server.jar 3000

## Examples: (server)
####  POST record endpoint:

    $ \
    for i in 1 2 3 4 5; do
      curl -X POST -H "Content-Type: text/plain" -d "$foo${i},bar,email${i}@b.c,BLUE,1/1/200${i}" http://localhost:3000/records/ 
    done
 
#### POST some bad data:
 
     $ curl -X POST -H "Content-Type: text/plain" -d "foo,bar,WHUTTHAT,BLUE,1/1/2000" http://localhost:3000/records/
     ;; {"error": "request-data rejected"}

####  GET records endpoints: (assumes `jq` installed)

    $ curl http://localhost:3000/records/email     | jq
    ...
    
    $ curl http://localhost:3000/records/name      | jq
    ...
    
    $ curl http://localhost:3000/records/birthdate | jq
    ... 
    
    $ curl http://localhost:3000/OHNOES!
     ;; {"error":"Not Found"}

## Assumptions 
To keep things simple(r) these programs accept "args" vs "options".

- `Email` validation is not rigorous a 'reg-ex' is used.
- `FavoriteColor`: using an enumerations of colors (ROYGBIV) seemed 'good enough' for now.
- `DateOfBirth` : it is possible to 'fool' the pattern+parser with garbage data.
- Assumes all input fields are mandatory.
- POST (per its RFC) can return Location Header data but since individually
  POSTed data is not retrievable directly, the response Location 
  is set to `/records/` to retrieve all of the items (unsorted).
